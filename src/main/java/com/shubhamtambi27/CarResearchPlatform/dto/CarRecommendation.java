package com.shubhamtambi27.CarResearchPlatform.dto;

import java.util.List;

public record CarRecommendation(
		Long id,
		String displayName,
		String make,
		String model,
		String variant,
		String bodyType,
		String fuelType,
		int priceInLakhs,
		int mileageKmpl,
		int safetyRating,
		int horsepower,
		int seatingCapacity,
		double userRating,
		int reviewCount,
		String transmission,
		String highlights,
		double matchScore,
		List<String> matchReasons
) {
}
