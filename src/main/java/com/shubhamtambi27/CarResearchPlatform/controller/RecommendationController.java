package com.shubhamtambi27.CarResearchPlatform.controller;

import com.shubhamtambi27.CarResearchPlatform.dto.BuyerPreferences;
import com.shubhamtambi27.CarResearchPlatform.dto.RecommendationResponse;
import com.shubhamtambi27.CarResearchPlatform.service.RecommendationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

	private final RecommendationService recommendationService;

	public RecommendationController(RecommendationService recommendationService) {
		this.recommendationService = recommendationService;
	}

	@PostMapping
	public RecommendationResponse recommend(@RequestBody BuyerPreferences preferences) {
		return recommendationService.recommend(preferences);
	}
}
