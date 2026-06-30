package com.shubhamtambi27.CarResearchPlatform.config;

import com.shubhamtambi27.CarResearchPlatform.model.Car;
import com.shubhamtambi27.CarResearchPlatform.repository.CarRepository;
import java.util.Arrays;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

	@Bean
	CommandLineRunner seedCars(CarRepository carRepository) {
		return args -> {
			if (carRepository.count() > 0) {
				return;
			}

			carRepository.saveAll(Arrays.asList(
					car("Maruti Suzuki", "Swift", "VXI", "Hatchback", "Petrol", 7, 23, 3, 82, 5, 4.3, 1240, "Manual", "Best-seller, low running cost"),
					car("Maruti Suzuki", "Brezza", "ZXI+", "SUV", "Petrol", 12, 20, 4, 103, 5, 4.4, 890, "Automatic", "Compact SUV with high resale"),
					car("Hyundai", "Creta", "SX(O)", "SUV", "Diesel", 18, 18, 5, 115, 5, 4.5, 2100, "Automatic", "Premium features, spacious cabin"),
					car("Hyundai", "i20", "Asta", "Hatchback", "Petrol", 9, 20, 4, 88, 5, 4.2, 760, "Manual", "Stylish hatch with connected tech"),
					car("Tata", "Nexon", "XZ+", "SUV", "Petrol", 11, 17, 5, 120, 5, 4.4, 1560, "Automatic", "5-star safety, punchy turbo"),
					car("Tata", "Nexon EV", "Max", "SUV", "EV", 17, 0, 5, 143, 5, 4.3, 620, "Automatic", "312km range, low running cost"),
					car("Tata", "Harrier", "XZA+", "SUV", "Diesel", 22, 16, 5, 170, 5, 4.3, 980, "Automatic", "Land Rover platform, commanding road presence"),
					car("Mahindra", "XUV700", "AX7", "SUV", "Diesel", 24, 15, 5, 185, 7, 4.5, 1120, "Automatic", "ADAS, 7 seats, feature-packed"),
					car("Mahindra", "Scorpio N", "Z8", "SUV", "Diesel", 20, 14, 4, 172, 7, 4.2, 740, "Manual", "Rugged 7-seater for adventure"),
					car("Mahindra", "Thar", "LX", "SUV", "Diesel", 16, 13, 3, 130, 4, 4.6, 1340, "Manual", "Iconic off-roader"),
					car("Kia", "Seltos", "GTX+", "SUV", "Petrol", 17, 17, 5, 160, 5, 4.4, 1450, "Automatic", "Ventilated seats, panoramic sunroof"),
					car("Kia", "Sonet", "GTX+", "SUV", "Diesel", 14, 19, 5, 115, 5, 4.3, 870, "Automatic", "Compact SUV with premium feel"),
					car("Toyota", "Innova Crysta", "ZX", "MPV", "Diesel", 26, 14, 5, 150, 7, 4.6, 1890, "Automatic", "Legendary reliability, tourer favorite"),
					car("Toyota", "Fortuner", "4x4 AT", "SUV", "Diesel", 42, 11, 5, 204, 7, 4.5, 560, "Automatic", "Full-size SUV, bulletproof build"),
					car("Honda", "City", "ZX", "Sedan", "Petrol", 16, 18, 5, 121, 5, 4.4, 1100, "Automatic", "Refined sedan, spacious rear seat"),
					car("Honda", "Amaze", "VX", "Sedan", "Petrol", 9, 19, 4, 90, 5, 4.1, 540, "Manual", "Affordable sedan with Honda reliability"),
					car("Skoda", "Kushaq", "Style", "SUV", "Petrol", 15, 17, 5, 115, 5, 4.2, 430, "Automatic", "European build quality"),
					car("Volkswagen", "Virtus", "GT", "Sedan", "Petrol", 16, 18, 5, 150, 5, 4.3, 390, "Automatic", "Sporty sedan, solid dynamics"),
					car("MG", "Hector", "Sharp Pro", "SUV", "Petrol", 18, 14, 4, 143, 5, 4.1, 510, "Automatic", "Large touchscreen, connected car"),
					car("MG", "ZS EV", "Excite", "SUV", "EV", 20, 0, 5, 176, 5, 4.2, 280, "Automatic", "461km range, zero emissions"),
					car("BMW", "X1", "sDrive20i", "SUV", "Petrol", 48, 14, 5, 190, 5, 4.4, 120, "Automatic", "Luxury compact SUV"),
					car("Mercedes-Benz", "C-Class", "C200", "Sedan", "Petrol", 58, 13, 5, 204, 5, 4.5, 85, "Automatic", "Premium sedan experience"),
					car("Maruti Suzuki", "Ertiga", "ZXI+", "MPV", "Petrol", 12, 20, 4, 103, 7, 4.3, 670, "Automatic", "Affordable 7-seater MPV"),
					car("Tata", "Punch", "Adventure", "SUV", "Petrol", 8, 20, 5, 87, 5, 4.2, 820, "Manual", "Micro SUV with 5-star safety"),
					car("Hyundai", "Venue", "SX", "SUV", "Petrol", 11, 18, 4, 120, 5, 4.3, 940, "Manual", "Feature-rich compact SUV"),
					car("Renault", "Kiger", "RXZ", "SUV", "Petrol", 10, 19, 3, 100, 5, 4.0, 310, "Automatic", "Value-for-money compact SUV"),
					car("Citroen", "C3", "Shine", "Hatchback", "Petrol", 8, 19, 4, 82, 5, 4.0, 180, "Manual", "Comfort-focused French hatch"),
					car("Jeep", "Compass", "Limited", "SUV", "Diesel", 28, 15, 5, 170, 5, 4.2, 290, "Automatic", "Capable off-road, premium cabin"),
					car("BYD", "Atto 3", "Superior", "SUV", "EV", 34, 0, 5, 204, 5, 4.4, 150, "Automatic", "Blade battery, 521km range"),
					car("Tata", "Tiago", "XZ+", "Hatchback", "Petrol", 7, 20, 4, 86, 5, 4.1, 690, "Manual", "Safest budget hatchback")
			));
		};
	}

	private static Car car(String make, String model, String variant, String bodyType, String fuelType,
			int price, int mileage, int safety, int hp, int seats, double rating, int reviews,
			String transmission, String highlights) {
		return new Car(make, model, variant, bodyType, fuelType, price, mileage, safety, hp, seats,
				rating, reviews, transmission, highlights);
	}
}
