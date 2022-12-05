package com.sally.api.label;

import com.sally.api.label.dto.LabelRequest;
import com.sally.api.label.dto.LabelResponse;
import com.sally.api.login.dto.AuthUser;
import com.sally.api.login.dto.FakeAuthUser;
import com.sally.api.project.ProjectService;
import com.sally.api.project.dto.ProjectInfo;
import com.sally.api.target.TargetService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LabelService {
	private final LabelRepository labelRepository;
	private final ProjectService projectService;
	private final TargetService targetService;

	@Transactional
	public void create(LabelRequest.LabelDto labelDto, String teamName, AuthUser authUser) {
		if (labelRepository.existsByName(labelDto.getName())) {
			throw new RuntimeException("Overlap : label");
		}
		Label label = labelDto.toEntity(getProjectAndVerify(teamName, authUser));
		labelRepository.save(label);
	}

	/**
	 * 라벨 조회는 팀원이 아니어도 가능하다.
	 * @param teamName
	 * @return
	 */
	@Transactional(readOnly = true)
	public LabelResponse.Lists readAll(String teamName) {
		ProjectInfo projectInfo = projectService.getFromTeamName(teamName);
		return new LabelResponse.Lists(
			labelRepository.findAllByProjectId(projectInfo.getId()).stream()
				.map(LabelResponse.LabelDto::new)
				.collect(Collectors.toList()));
	}

	@Transactional
	public void update(Long labelId, LabelRequest.LabelDto labelDto, String teamName,
		FakeAuthUser authUser) {
		ProjectInfo projectInfo = getProjectAndVerify(teamName, authUser);
		Label label = getLabelOrThrow(labelId, projectInfo.getId());
		label.update(
			labelDto.getName(),
			labelDto.getDescription(),
			labelDto.getBackgroundColor(),
			labelDto.getFontColor());
	}

	@Transactional
	public void delete(Long labelId, String teamName, AuthUser authUser) {
		ProjectInfo projectInfo = getProjectAndVerify(teamName, authUser);
		if (targetService.hasLabel(labelId)) {
			throw new NoSuchElementException("The Label associated with Target cannot be deleted.");
		}
		Label label = getLabelOrThrow(labelId, projectInfo.getId());
		label.delete();
	}

	public Label getLabelOrThrow(Long labelId, Long projectId) {
		return labelRepository.findByIdAndProjectId(labelId, projectId)
			.orElseThrow(() -> new NoSuchElementException("no existed label"));
	}

	public ProjectInfo getProjectAndVerify(String teamName, AuthUser authUser) {
		return projectService.getAndVerify(authUser.projectId(), teamName);
	}

	@Transactional(readOnly = true)
	public List<Label> readAllByIds(List<Long> labelIds) {
		List<Label> labels = labelRepository.findAllByIdIn(labelIds);
		if (Objects.isNull(labels) || labels.size() == 0) {
			throw new NoSuchElementException("no existed label");
		}
		return labels;
	}
}
