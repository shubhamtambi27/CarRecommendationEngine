package com.shubhamtambi27.CarResearchPlatform.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.shubhamtambi27.CarResearchPlatform.dto.BuyerPreferences;
import com.shubhamtambi27.CarResearchPlatform.dto.RecommendationResponse;
import com.shubhamtambi27.CarResearchPlatform.model.Car;
import com.shubhamtambi27.CarResearchPlatform.repository.CarRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RecommendationServiceTest {

	@Autowired
	private RecommendationService recommendationService;

	@Autowired
	private CarRepository carRepository;

	@BeforeEach
	void seedTestCar() {
		if (carRepository.count() == 0) {
			carRepository.save(new Car("Test", "Car", "Base", "SUV", "Petrol", 10, 20, 4, 100, 5,
					4.0, 100, "Manual", "Test car"));
		}
	}

	@Test
	void recommendReturnsMatchesWithinBudget() {
		BuyerPreferences prefs = new BuyerPreferences(8, 15, "ANY", "ANY", 5, 3, 3, 2, 4, "city");
		RecommendationResponse response = recommendationService.recommend(prefs);

		assertFalse(response.recommendations().isEmpty());
		response.recommendations().forEach(rec ->
				assertTrue(rec.priceInLakhs() >= 8 && rec.priceInLakhs() <= 15));
	}
}
