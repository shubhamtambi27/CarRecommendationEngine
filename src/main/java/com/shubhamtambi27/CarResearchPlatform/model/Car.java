package com.shubhamtambi27.CarResearchPlatform.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cars")
public class Car {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String make;

	@Column(nullable = false)
	private String model;

	@Column(nullable = false)
	private String variant;

	@Column(nullable = false)
	private String bodyType;

	@Column(nullable = false)
	private String fuelType;

	@Column(nullable = false)
	private int priceInLakhs;

	private int mileageKmpl;
	private int safetyRating;
	private int horsepower;
	private int seatingCapacity;
	private double userRating;
	private int reviewCount;
	private String transmission;
	private String highlights;

	public Car() {
	}

	public Car(String make, String model, String variant, String bodyType, String fuelType,
			int priceInLakhs, int mileageKmpl, int safetyRating, int horsepower, int seatingCapacity,
			double userRating, int reviewCount, String transmission, String highlights) {
		this.make = make;
		this.model = model;
		this.variant = variant;
		this.bodyType = bodyType;
		this.fuelType = fuelType;
		this.priceInLakhs = priceInLakhs;
		this.mileageKmpl = mileageKmpl;
		this.safetyRating = safetyRating;
		this.horsepower = horsepower;
		this.seatingCapacity = seatingCapacity;
		this.userRating = userRating;
		this.reviewCount = reviewCount;
		this.transmission = transmission;
		this.highlights = highlights;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getVariant() {
		return variant;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	public String getBodyType() {
		return bodyType;
	}

	public void setBodyType(String bodyType) {
		this.bodyType = bodyType;
	}

	public String getFuelType() {
		return fuelType;
	}

	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}

	public int getPriceInLakhs() {
		return priceInLakhs;
	}

	public void setPriceInLakhs(int priceInLakhs) {
		this.priceInLakhs = priceInLakhs;
	}

	public int getMileageKmpl() {
		return mileageKmpl;
	}

	public void setMileageKmpl(int mileageKmpl) {
		this.mileageKmpl = mileageKmpl;
	}

	public int getSafetyRating() {
		return safetyRating;
	}

	public void setSafetyRating(int safetyRating) {
		this.safetyRating = safetyRating;
	}

	public int getHorsepower() {
		return horsepower;
	}

	public void setHorsepower(int horsepower) {
		this.horsepower = horsepower;
	}

	public int getSeatingCapacity() {
		return seatingCapacity;
	}

	public void setSeatingCapacity(int seatingCapacity) {
		this.seatingCapacity = seatingCapacity;
	}

	public double getUserRating() {
		return userRating;
	}

	public void setUserRating(double userRating) {
		this.userRating = userRating;
	}

	public int getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(int reviewCount) {
		this.reviewCount = reviewCount;
	}

	public String getTransmission() {
		return transmission;
	}

	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}

	public String getHighlights() {
		return highlights;
	}

	public void setHighlights(String highlights) {
		this.highlights = highlights;
	}

	public String getDisplayName() {
		return make + " " + model + " " + variant;
	}
}
