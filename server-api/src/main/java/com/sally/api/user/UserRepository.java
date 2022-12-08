package com.sally.api.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByIdIn(List<Long> userIds);
}
