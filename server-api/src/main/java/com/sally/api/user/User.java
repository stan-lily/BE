package com.sally.api.user;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Table(name = "tb_user")
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String nickName;
	private String email;
	private String password;
	private String picture;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@Builder(access = AccessLevel.PRIVATE)
	public User(Long id, String nickName, String email, String password, String picture, LocalDateTime createdAt,
		LocalDateTime updatedAt) {
		this.id = id;
		this.nickName = nickName;
		this.email = email;
		this.password = password;
		this.picture = picture;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	/**
	 * default of sign-up
	 */
	public static User create(String nickName, String email, String password, String picture) {
		return User.builder()
			.nickName(nickName)
			.email(email)
			.password(password)
			.picture(picture)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
	}

	public static User from(Long userId) {
		return User.builder()
			.id(userId)
			.build();
	}

	public Long id() {
		return this.id;
	}

	public String nickName() {
		return this.nickName;
	}

	public String picture() {
		return this.picture;
	}

	public String getEmail() {
		return this.email;
	}
}
