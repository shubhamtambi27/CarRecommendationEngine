package com.shubhamtambi27.CarResearchPlatform.dto;

import java.util.List;

public record ShortlistResponse(
		String sessionId,
		List<CarRecommendation> cars
) {
}
