package com.sally.api.target;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TargetRepository extends JpaRepository<Target, Long> {
	boolean existsByLabelIdAndTitleContains(
		@Param("labelId") Long labelId,
		@Param("title") String title);

	@Query(
		"select count(t.id) as targetCount, t.endAt as endAt"
			+ " from Target as t where t.assemble.id = :assembleId"
			+ " group by t.endAt"
	)
	List<TargetCountByDate> findCountAndEndAtByAssembleDayId(@Param("assembleId") Long assembleId);

	List<Target> findAllByAssembleIdAndAndEndAt(
		@Param("assembleId") Long assembleId,
		@Param("endAt") LocalDate endAt);
}
