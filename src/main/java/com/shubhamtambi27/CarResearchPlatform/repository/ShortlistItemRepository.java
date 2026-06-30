package com.shubhamtambi27.CarResearchPlatform.repository;

import com.shubhamtambi27.CarResearchPlatform.model.ShortlistItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortlistItemRepository extends JpaRepository<ShortlistItem, Long> {

	List<ShortlistItem> findBySessionIdOrderByAddedAtAsc(String sessionId);

	boolean existsBySessionIdAndCarId(String sessionId, Long carId);

	void deleteBySessionIdAndCarId(String sessionId, Long carId);

	int countBySessionId(String sessionId);
}
