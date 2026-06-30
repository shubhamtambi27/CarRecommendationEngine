package com.shubhamtambi27.CarResearchPlatform.service;

import com.shubhamtambi27.CarResearchPlatform.dto.CarRecommendation;
import com.shubhamtambi27.CarResearchPlatform.dto.ShortlistResponse;
import com.shubhamtambi27.CarResearchPlatform.model.Car;
import com.shubhamtambi27.CarResearchPlatform.model.ShortlistItem;
import com.shubhamtambi27.CarResearchPlatform.repository.CarRepository;
import com.shubhamtambi27.CarResearchPlatform.repository.ShortlistItemRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ShortlistService {

	private static final int MAX_SHORTLIST_SIZE = 5;

	private final ShortlistItemRepository shortlistRepository;
	private final CarRepository carRepository;
	private final RecommendationService recommendationService;

	public ShortlistService(ShortlistItemRepository shortlistRepository, CarRepository carRepository,
			RecommendationService recommendationService) {
		this.shortlistRepository = shortlistRepository;
		this.carRepository = carRepository;
		this.recommendationService = recommendationService;
	}

	public ShortlistResponse getShortlist(String sessionId) {
		List<CarRecommendation> cars = shortlistRepository.findBySessionIdOrderByAddedAtAsc(sessionId).stream()
				.map(item -> carRepository.findById(item.getCarId()))
				.flatMap(Optional::stream)
				.map(car -> recommendationService.toRecommendation(car, 0, List.of("In your shortlist")))
				.toList();
		return new ShortlistResponse(sessionId, cars);
	}

	public ShortlistResponse addToShortlist(String sessionId, Long carId) {
		Car car = carRepository.findById(carId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));

		if (shortlistRepository.existsBySessionIdAndCarId(sessionId, carId)) {
			return getShortlist(sessionId);
		}

		if (shortlistRepository.countBySessionId(sessionId) >= MAX_SHORTLIST_SIZE) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Shortlist is full (max " + MAX_SHORTLIST_SIZE + " cars). Remove one to add another.");
		}

		shortlistRepository.save(new ShortlistItem(sessionId, car.getId()));
		return getShortlist(sessionId);
	}

	public ShortlistResponse removeFromShortlist(String sessionId, Long carId) {
		shortlistRepository.deleteBySessionIdAndCarId(sessionId, carId);
		return getShortlist(sessionId);
	}
}
