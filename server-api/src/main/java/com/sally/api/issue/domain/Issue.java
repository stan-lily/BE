package com.sally.api.issue.domain;

import com.sally.api.issue.domain.dto.IssueLabelDto;
import com.sally.api.project.Project;
import com.sally.api.user.User;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NamedEntityGraph(name = "Issue.all",
	attributeNodes = {
		@NamedAttributeNode("writer"),
		@NamedAttributeNode(value = "labels", subgraph = "labels-subgraph")},
	subgraphs = @NamedSubgraph(
		name = "labels-subgraph",
		attributeNodes = @NamedAttributeNode("label"))
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Where(clause = "is_deleted = false")
@Table(name = "tb_issue")
@Entity
public class Issue {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String content;
	private Long issueNo;
	@Enumerated(EnumType.STRING)
	private Status status;
	@ColumnDefault("0")
	private boolean isDeleted = false;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User writer;
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "project_id", referencedColumnName = "id")
	private Project project;
	@OneToMany(mappedBy = "issue")
	private List<IssueLabel> labels = new ArrayList<>();

	@Builder(access = AccessLevel.PRIVATE)
	public Issue(Long id, String title, String content, Long issueNo, Status status, boolean isDeleted,
		LocalDateTime createdAt, LocalDateTime updatedAt, User writer, Project project,
		List<IssueLabel> labels) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.issueNo = issueNo;
		this.status = status;
		this.isDeleted = isDeleted;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.writer = writer;
		this.project = project;
		this.labels = labels;
	}

	// TODO  각각 id 만으로 저장, 조회 로직 추가
	public static Issue createOf(String title, String content, Long issueNo, Status status,
		User writer,
		Project project) {
		return Issue.builder()
			.title(title)
			.content(content)
			.status(status)
			.issueNo(issueNo)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.writer(writer)
			.project(project)
			.build();
	}

	public static Issue from(Long issueId) {
		return Issue.builder()
			.id(issueId)
			.build();
	}

	public Long id() {
		return this.id;
	}

	public IssueInfo toIssueInfo() {
		return new IssueInfo(
			title,
			issueNo,
			status.getText(),
			createdAt,
			writer.nickName(),
			writer.picture(),
			getLabelInfos()
		);
	}

	private List<IssueLabelDto> getLabelInfos() {
		return this.labels.stream()
			.map(IssueLabel::label)
			.map(label -> new IssueLabelDto(label.name(), label.backgroundColor(), label.fontColorByText()))
			.collect(Collectors.toUnmodifiableList());
	}

	@Getter
	public static class IssueInfo {
		private final String issueTitle;
		private final Long issueNo;
		private final String status;
		private final LocalDateTime createdAt;
		private final String author;
		private final String picture;
		private final List<IssueLabelDto> issueLabels;

		public IssueInfo(String issueTitle, Long issueNo, String status, LocalDateTime createdAt, String author,
			String picture, List<IssueLabelDto> issueLabels) {
			this.issueTitle = issueTitle;
			this.issueNo = issueNo;
			this.status = status;
			this.createdAt = createdAt;
			this.author = author;
			this.picture = picture;
			this.issueLabels = issueLabels;
		}
	}
}
