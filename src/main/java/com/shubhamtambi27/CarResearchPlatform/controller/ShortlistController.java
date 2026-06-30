package com.shubhamtambi27.CarResearchPlatform.controller;

import com.shubhamtambi27.CarResearchPlatform.dto.ShortlistResponse;
import com.shubhamtambi27.CarResearchPlatform.service.ShortlistService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shortlist")
public class ShortlistController {

	private final ShortlistService shortlistService;

	public ShortlistController(ShortlistService shortlistService) {
		this.shortlistService = shortlistService;
	}

	@GetMapping
	public ShortlistResponse getShortlist(
			@CookieValue(value = "sessionId", required = false) String sessionId,
			HttpServletResponse response) {
		String id = ensureSession(sessionId, response);
		return shortlistService.getShortlist(id);
	}

	@PostMapping("/{carId}")
	public ShortlistResponse addToShortlist(
			@PathVariable Long carId,
			@CookieValue(value = "sessionId", required = false) String sessionId,
			HttpServletResponse response) {
		String id = ensureSession(sessionId, response);
		return shortlistService.addToShortlist(id, carId);
	}

	@DeleteMapping("/{carId}")
	public ShortlistResponse removeFromShortlist(
			@PathVariable Long carId,
			@CookieValue(value = "sessionId", required = false) String sessionId,
			HttpServletResponse response) {
		String id = ensureSession(sessionId, response);
		return shortlistService.removeFromShortlist(id, carId);
	}

	private String ensureSession(String sessionId, HttpServletResponse response) {
		if (sessionId != null && !sessionId.isBlank()) {
			return sessionId;
		}
		String newId = UUID.randomUUID().toString();
		Cookie cookie = new Cookie("sessionId", newId);
		cookie.setPath("/");
		cookie.setMaxAge(60 * 60 * 24 * 30);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
		return newId;
	}
}
