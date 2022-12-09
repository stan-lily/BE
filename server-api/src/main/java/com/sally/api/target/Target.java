package com.sally.api.target;

import com.sally.api.assemble.Assemble;
import com.sally.api.label.Label;
import com.sally.api.util.Period;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Where(clause = "is_deleted = false")
@Table(name = "tb_target")
@Entity
public class Target {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private LocalDate endAt;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "assemble_day_id")
	private Assemble assemble;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "label_id")
	private Label label;
	@ColumnDefault("0")
	private boolean isDeleted = false;
	private LocalDateTime createAt;
	private LocalDateTime updatedAt;

	@Builder(access = AccessLevel.PRIVATE)
	public Target(Long id, String title, LocalDate endAt, Assemble assembleDay, Label label,
		LocalDateTime createAt, LocalDateTime updatedAt) {
		this.id = id;
		this.title = title;
		this.endAt = endAt;
		this.assemble = assembleDay;
		this.label = label;
		this.createAt = createAt;
		this.updatedAt = updatedAt;
	}

	public static Target createOf(String title, LocalDate endAt, Assemble assemble, Label label) {
		return Target.builder()
			.title(title)
			.endAt(endAt)
			.assembleDay(assemble)
			.label(label)
			.createAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
	}

	public static Target from(Long targetId) {
		return Target.builder()
			.id(targetId)
			.build();
	}

	public Long id() {
		return id;
	}

	public LocalDate day() {
		return endAt;
	}

	public String title() {
		return this.title;
	}

	public Long labelId() {
		return label.id();
	}

	public String labelColor() {
		return this.label.backgroundColor();
	}

	public Period assemblePeriod() {
		return Period.of(this.assemble.startAt(), assemble.endAt());
	}

	public String labelName() {
		return label.name();
	}

	public String titleOfAssemble() {
		return this.assemble.title();
	}

	public String titleOfProject() {
		return this.assemble.projectOfTeam();
	}
}
