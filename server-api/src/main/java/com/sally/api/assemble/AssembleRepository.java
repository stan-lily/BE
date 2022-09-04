package com.sally.api.assemble;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AssembleRepository extends JpaRepository<Assemble, Long> {
	Optional<Assemble> findFirstByProjectIdAndStartAtIsLessThanEqualAndEndAtIsGreaterThanEqual(
		@Param("projectId") Long projectId,
		@Param("endAt") LocalDate endAt,
		@Param("startAt") LocalDate startAt);

	boolean existsByProjectIdAndEndAtBetween(
		@Param("projectId") Long projectId,
		@Param("threeWeeksAgo") LocalDate threeWeeksAgo,
		@Param("startAt") LocalDate startAt);

	boolean existsByProjectIdAndTitleContains(@Param("projectId") Long projectId, @Param("title") String title);

	List<Assemble> findAllByProjectIdAndStartAtIsBetween(
		@Param("projectId") Long projectId,
		@Param("firstDayOfYear") LocalDate firstDayOfYear,
		@Param("lastDayOfYear") LocalDate lastDayOfYear);
}
