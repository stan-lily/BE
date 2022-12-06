package com.sally.api.assemble;

import static com.sally.api.assemble.Assemble.ASSEMBLE_LIMITS_OF_NUMBER_FOR_WEEKS;

import com.sally.api.assemble.dto.AssembleRequest;
import com.sally.api.assemble.dto.AssembleResponse;
import com.sally.api.project.dto.ProjectInfo;
import com.sally.api.util.Period;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AssembleService {
	private final AssembleRepository assembleRepository;

	/**
	 * 처음 등록시, 검증 철자를 진행하지 않는다.
	 * 시작일은 종료일보다 먼저여야 한다.
	 * 팀별 title 중복은 허용 되지 않는다.
	 * assemble day 기간 중복 되면 안 된다.
	 * 이전 assemble day의 deadline 이후 3주 이내로 등록 가능하다.
	 */
	@Transactional
	public void create(
		AssembleRequest.CreationDto creationDto,
		ProjectInfo projectInfo) {
		if (assembleRepository.existsByProjectId(projectInfo.getId())) {
			isDuplicated(projectInfo, creationDto);
			isValidPeriod(projectInfo, creationDto.toPeriod());
		}
		assembleRepository.save(creationDto.toEntity(projectInfo));
	}

	@Transactional(readOnly = true)
	public AssembleResponse.Lists readAll(ProjectInfo project) {
		Period dayOfYear = Period.dayOfYear();
		List<Assemble> assembles = assembleRepository.findAllByProjectIdAndStartAtIsBetween(
			project.getId(),
			dayOfYear.getStartDate(),
			dayOfYear.getEndDate());
		return new AssembleResponse.Lists(assembles.stream()
			.map(AssembleResponse.AssembleDto::new)
			.collect(Collectors.toUnmodifiableList()));
	}

	/**
	 * Assemble 기간 중복 및 3주 범위 검증, 제목 중복 검증
	 */
	public void isValidPeriod(ProjectInfo projectInfo, Period period) {
		isOverlapped(projectInfo, period);
		isWithinLimits(projectInfo, period);
	}

	public void isOverlapped(ProjectInfo projectInfo, Period period) {
		Optional<Assemble> overlappedPeriod = assembleRepository.findFirstByProjectIdAndStartAtIsLessThanEqualAndEndAtIsGreaterThanEqual(
			projectInfo.getId(),
			period.getEndDate(),
			period.getStartDate());
		if (overlappedPeriod.isPresent()) {
			throw new RuntimeException("is already registered for period");
		}
	}

	public void isWithinLimits(ProjectInfo projectInfo, Period period) {
		if (!period.isWithin(ASSEMBLE_LIMITS_OF_NUMBER_FOR_WEEKS)) {
			throw new RuntimeException("A gap of more than three weeks is not allowed");
		}
		boolean isWithinThreeWeeks = assembleRepository.existsByProjectIdAndEndAtBetween(
			projectInfo.getId(),
			period.startAtWeeksAgo(ASSEMBLE_LIMITS_OF_NUMBER_FOR_WEEKS),
			period.getStartDate());
		if (!isWithinThreeWeeks) {
			throw new RuntimeException("A gap of more than three weeks is not allowed");
		}
	}

	public void isDuplicated(ProjectInfo projectInfo, AssembleRequest.CreationDto creationDto) {
		boolean isDuplicatedTitle = assembleRepository.existsByProjectIdAndTitleContains(projectInfo.getId(),
			creationDto.getAssembleTitle());
		if (isDuplicatedTitle) {
			throw new RuntimeException("duplicated assemble's title");
		}
	}

	public boolean isValidTeamAndDate(Long assembleId, ProjectInfo project, LocalDate targetAt) {
		Optional<Assemble> assemble = assembleRepository.findByIdAndProjectId(assembleId, project.getId());
		if (assemble.isEmpty()) {
			return false;
		}
		return assemble.stream()
			.anyMatch(a -> a.startAt().isBefore(targetAt) && a.endAt().isAfter(targetAt));
	}
}
