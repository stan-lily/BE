package com.sally.api.targetnotification;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// @NoArgsConstructor
@Where(clause = "is_deleted = false")
@EqualsAndHashCode(of = "id")
@Table(name = "tb_delayed_target_manager")
@Entity
public class TargetNotification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String email;
	private LocalDateTime notifyAt;
	@Embedded
	private RequiredTarget requiredTarget;
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "startAt", column = @Column(name = "assemble_start_at")),
		@AttributeOverride(name = "endAt", column = @Column(name = "assemble_end_at"))
	})
	private TargetPeriod targetPeriod;
	private String assembleTitle;
	private boolean toTeam;
	@Enumerated(EnumType.STRING)
	private Status status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	@ColumnDefault("0")
	private boolean isDeleted = false;

	enum Status {
		SENDING, CANCEL, APPENDING
	}

	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PACKAGE)
	@Embeddable
	static class RequiredTarget {
		private String targetTitle;
		private LocalDate targetAt;
		private String LabelName;
	}

	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PACKAGE)
	@Embeddable
	static class TargetPeriod {
		private LocalDate startAt;
		private LocalDate endAt;
	}
}
