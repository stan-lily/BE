package com.sally.api.issue.infrastructure;

import com.sally.api.issue.domain.IssueLabel;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueLabelRepository extends JpaRepository<IssueLabel, Long> {
}
