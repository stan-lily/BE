package com.sally.api.issue.infrastructure;

import com.sally.api.issue.domain.Assignee;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssigneeRepository extends JpaRepository<Assignee, Long> {
}
