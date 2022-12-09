package com.sally.api.target;

import com.sally.api.assemble.AssembleService;
import com.sally.api.project.dto.ProjectInfo;
import com.sally.api.target.dto.TargetInfo;
import com.sally.api.target.dto.TargetRequest;
import com.sally.api.target.dto.TargetResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TargetService {
	private final TargetRepository targetRepository;
	private final AssembleService assembleService;

	/**
	 * 등록시 팀의 label 별 Target title 중복되면 안 된다.
	 * 해당 assemble 기간 안에 등록되는 요청이어야 한다.
	 */
	@Transactional
	public void save(Long assembleId, ProjectInfo project, TargetRequest.CreationDto creationDto) {
		if (!assembleService.isValidTeamAndDate(assembleId, project, creationDto.getTargetAt())) {
			throw new RuntimeException("no assemble in target");
		}
		boolean duplicatedTitle = targetRepository.existsByLabelIdAndTitleContains(
			creationDto.getLabelId(),
			creationDto.getTitle());

		if (duplicatedTitle) {
			throw new RuntimeException("duplicated Title with label");
		}
		targetRepository.save(creationDto.toEntity(assembleId));
	}

	/**
	 * 해당 프로젝트의 assemble 상세조회
	 * - assemble의 기간 동안의 일별 등록된 Target 개수 목록
	 */
	@Transactional(readOnly = true)
	public TargetResponse.TargetCountsDto readTargetCountsByPeriod(Long assembleId) {
		List<TargetCountByDate> targetCountByDate = targetRepository.findCountAndEndAtByAssembleDayId(assembleId);
		return TargetResponse.TargetCountsDto.from(
			targetCountByDate.stream()
				.map(TargetResponse.TargetCountsByDays::new)
				.collect(Collectors.toUnmodifiableList()));
	}

	@Transactional(readOnly = true)
	public TargetResponse.TargetDto readTargetsByDay(Long assembleId, LocalDate targetAt) {
		List<Target> targets = targetRepository.findAllByAssembleIdAndAndEndAt(assembleId, targetAt);
		return TargetResponse.TargetDto.from(
			targets.stream()
				.map(target -> TargetResponse.TargetByDays.of(
					target.id(),
					target.day(),
					target.title(),
					target.labelId(),
					target.labelColor()))
				.collect(Collectors.toUnmodifiableList()));
	}

	@Transactional(readOnly = true)
	public boolean hasLabel(Long labelId) {
		return targetRepository.existsByLabelId(labelId);
	}

	@Transactional(readOnly = true)
	public TargetInfo readTargetInfoFrom(Long targetId) {
		Target target = getByTargetOrThrow(targetId);
		return new TargetInfo(
			target.title(),
			target.day(),
			target.assemblePeriod(),
			target.titleOfAssemble(),
			target.titleOfProject(),
			target.labelName());
	}

	private Target getByTargetOrThrow(Long targetId) {
		Target target = targetRepository.findById(targetId)
			.orElseThrow(() -> new IllegalArgumentException("not found target"));
		return target;
	}
}
