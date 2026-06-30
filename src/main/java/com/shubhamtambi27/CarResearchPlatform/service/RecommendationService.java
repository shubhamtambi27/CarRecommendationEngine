package com.shubhamtambi27.CarResearchPlatform.service;

import com.shubhamtambi27.CarResearchPlatform.dto.BuyerPreferences;
import com.shubhamtambi27.CarResearchPlatform.dto.CarRecommendation;
import com.shubhamtambi27.CarResearchPlatform.dto.RecommendationResponse;
import com.shubhamtambi27.CarResearchPlatform.model.Car;
import com.shubhamtambi27.CarResearchPlatform.repository.CarRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RecommendationService {

	private final CarRepository carRepository;

	public RecommendationService(CarRepository carRepository) {
		this.carRepository = carRepository;
	}

	public RecommendationResponse recommend(BuyerPreferences prefs) {
		List<Car> candidates = carRepository.findByPriceInLakhsBetween(
				prefs.minBudgetLakhs(), prefs.maxBudgetLakhs());

		List<ScoredCar> scored = candidates.stream()
				.map(car -> score(car, prefs))
				.filter(sc -> sc.score() > 0)
				.sorted(Comparator.comparingDouble(ScoredCar::score).reversed())
				.limit(8)
				.toList();

		List<CarRecommendation> recommendations = scored.stream()
				.map(ScoredCar::recommendation)
				.toList();

		String summary = buildSummary(recommendations, candidates.size(), prefs);
		return new RecommendationResponse(recommendations, candidates.size(), summary);
	}

	public CarRecommendation toRecommendation(Car car, double score, List<String> reasons) {
		return new CarRecommendation(
				car.getId(),
				car.getDisplayName(),
				car.getMake(),
				car.getModel(),
				car.getVariant(),
				car.getBodyType(),
				car.getFuelType(),
				car.getPriceInLakhs(),
				car.getMileageKmpl(),
				car.getSafetyRating(),
				car.getHorsepower(),
				car.getSeatingCapacity(),
				car.getUserRating(),
				car.getReviewCount(),
				car.getTransmission(),
				car.getHighlights(),
				Math.round(score * 10.0) / 10.0,
				reasons
		);
	}

	private ScoredCar score(Car car, BuyerPreferences prefs) {
		List<String> reasons = new ArrayList<>();
		double score = 0;

		if (!matchesFuel(car, prefs.fuelType())) {
			return new ScoredCar(car, 0, null);
		}
		if (!matchesBody(car, prefs.bodyType())) {
			return new ScoredCar(car, 0, null);
		}
		if (car.getSeatingCapacity() < prefs.seatingNeeded()) {
			return new ScoredCar(car, 0, null);
		}

		score += 20;
		reasons.add("Fits your budget at ₹" + car.getPriceInLakhs() + "L");

		double fuelScore = normalize(car.getMileageKmpl(), 10, 25) * prefs.fuelEconomyWeight();
		double safetyScore = normalize(car.getSafetyRating(), 1, 5) * prefs.safetyWeight();
		double perfScore = normalize(car.getHorsepower(), 60, 200) * prefs.performanceWeight();
		double valueScore = (1 - normalize(car.getPriceInLakhs(), prefs.minBudgetLakhs(), prefs.maxBudgetLakhs()))
				* prefs.valueWeight();
		double reviewScore = normalize(car.getUserRating(), 3.0, 5.0) * 2;

		score += fuelScore + safetyScore + perfScore + valueScore + reviewScore;

		if (fuelScore > prefs.fuelEconomyWeight() * 0.7) {
			reasons.add("Strong fuel economy (" + car.getMileageKmpl() + " kmpl)");
		}
		if (safetyScore > prefs.safetyWeight() * 0.7) {
			reasons.add("High safety rating (" + car.getSafetyRating() + "/5)");
		}
		if (perfScore > prefs.performanceWeight() * 0.7) {
			reasons.add("Good performance (" + car.getHorsepower() + " bhp)");
		}
		if (car.getUserRating() >= 4.2) {
			reasons.add("Loved by owners (" + car.getUserRating() + "★, " + car.getReviewCount() + " reviews)");
		}

		score += useCaseBonus(car, prefs.primaryUse(), reasons);

		if (reasons.size() > 4) {
			reasons = reasons.subList(0, 4);
		}

		return new ScoredCar(car, score, toRecommendation(car, score, reasons));
	}

	private double useCaseBonus(Car car, String primaryUse, List<String> reasons) {
		return switch (primaryUse) {
			case "city" -> {
				if (car.getMileageKmpl() >= 18) {
					reasons.add("Great for daily city commutes");
					yield 8;
				}
				yield 0;
			}
			case "highway" -> {
				if (car.getHorsepower() >= 120) {
					reasons.add("Comfortable for highway cruising");
					yield 8;
				}
				yield 0;
			}
			case "family" -> {
				if (car.getSeatingCapacity() >= 7) {
					reasons.add("Spacious " + car.getSeatingCapacity() + "-seater for families");
					yield 10;
				}
				if (car.getSafetyRating() >= 4) {
					reasons.add("Family-friendly safety features");
					yield 6;
				}
				yield 0;
			}
			default -> 0;
		};
	}

	private boolean matchesFuel(Car car, String fuelType) {
		return "ANY".equalsIgnoreCase(fuelType) || car.getFuelType().equalsIgnoreCase(fuelType);
	}

	private boolean matchesBody(Car car, String bodyType) {
		return "ANY".equalsIgnoreCase(bodyType) || car.getBodyType().equalsIgnoreCase(bodyType);
	}

	private double normalize(double value, double min, double max) {
		if (max <= min) {
			return 0.5;
		}
		double clamped = Math.max(min, Math.min(max, value));
		return (clamped - min) / (max - min);
	}

	private String buildSummary(List<CarRecommendation> recs, int totalCandidates, BuyerPreferences prefs) {
		if (recs.isEmpty()) {
			return "No cars matched your filters. Try widening your budget or choosing 'Any' for fuel/body type.";
		}
		CarRecommendation top = recs.get(0);
		return String.format(
				"Scored %d cars in your ₹%d–%dL range. Top pick: %s (%.0f%% match).",
				totalCandidates,
				prefs.minBudgetLakhs(),
				prefs.maxBudgetLakhs(),
				top.displayName(),
				top.matchScore()
		);
	}

	private record ScoredCar(Car car, double score, CarRecommendation recommendation) {
	}
}
