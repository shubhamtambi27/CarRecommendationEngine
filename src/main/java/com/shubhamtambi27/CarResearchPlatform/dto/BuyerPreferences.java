package com.shubhamtambi27.CarResearchPlatform.dto;

public record BuyerPreferences(
		int minBudgetLakhs,
		int maxBudgetLakhs,
		String fuelType,
		String bodyType,
		int seatingNeeded,
		int fuelEconomyWeight,
		int safetyWeight,
		int performanceWeight,
		int valueWeight,
		String primaryUse
) {
}
