package com.sally.api.label;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LabelRepository extends JpaRepository<Label, Long> {
	boolean existsByName(String name);

	List<Label> findAllByProjectId(Long projectId);

	Optional<Label> findByIdAndProjectId(Long labelId, Long projectId);

	@Query(value = "select id, label_name, description, background_color, font_color, is_deleted, project_id from tb_label label where label.is_deleted = true and label.project_id = :projectId", nativeQuery = true)
	List<Label> findByLabelIsDeletedAAndProject(@Param("projectId") Long projectId);

	List<Label> findAllByIdIn(List<Long> labelIds);
}
