package com.shubhamtambi27.CarResearchPlatform.dto;

import java.util.List;

public record RecommendationResponse(
		List<CarRecommendation> recommendations,
		int totalCandidates,
		String summary
) {
}
