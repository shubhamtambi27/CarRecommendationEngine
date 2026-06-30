package com.shubhamtambi27.CarResearchPlatform.controller;

import com.shubhamtambi27.CarResearchPlatform.model.Car;
import com.shubhamtambi27.CarResearchPlatform.repository.CarRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cars")
public class CarController {

	private final CarRepository carRepository;

	public CarController(CarRepository carRepository) {
		this.carRepository = carRepository;
	}

	@GetMapping
	public List<Car> getAllCars() {
		return carRepository.findAll();
	}

	@GetMapping("/{id}")
	public Car getCar(@PathVariable Long id) {
		return carRepository.findById(id)
				.orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
						org.springframework.http.HttpStatus.NOT_FOUND, "Car not found"));
	}
}
