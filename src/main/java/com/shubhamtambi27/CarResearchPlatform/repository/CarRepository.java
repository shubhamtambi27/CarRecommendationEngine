package com.shubhamtambi27.CarResearchPlatform.repository;

import com.shubhamtambi27.CarResearchPlatform.model.Car;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

	List<Car> findByPriceInLakhsBetween(int minPrice, int maxPrice);
}
