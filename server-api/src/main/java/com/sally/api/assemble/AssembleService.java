package com.sally.api.assemble;

import static com.sally.api.assemble.Assemble.ASSEMBLE_LIMITS_OF_NUMBER_FOR_WEEKS;

import com.sally.api.assemble.dto.AssembleRequest;
import com.sally.api.assemble.dto.AssembleResponse;
import com.sally.api.project.dto.ProjectInfo;
import com.sally.api.util.Period;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AssembleService {
	private final AssembleRepository assembleRepository;

	/**
	 * 팀별 title 중복은 허용 되지 않는다.
	 * assemble day 기간 중복 되면 안 된다.
	 * 이전 assemble day의 deadline 이후 3주 이내로 등록 가능하다.
	 * @param creationDto
	 * @param projectInfo
	 */
	@Transactional
	public void create(
		AssembleRequest.CreationDto creationDto,
		ProjectInfo projectInfo) {
		isDuplicated(projectInfo, creationDto);
		isValidPeriod(projectInfo, creationDto.toPeriod());
		assembleRepository.save(creationDto.toEntity(projectInfo));
	}

	@Transactional(readOnly = true)
	public List<AssembleResponse.AssembleDto> readAll(ProjectInfo project) {
		Period dayOfYear = Period.dayOfYear();
		List<Assemble> assembles = assembleRepository.findAllByProjectIdAndStartAtIsBetween(
			project.getId(),
			dayOfYear.getStartDate(),
			dayOfYear.getEndDate());
		return assembles.stream()
			.map(AssembleResponse.AssembleDto::new)
			.collect(Collectors.toUnmodifiableList());
	}

	private void isValidPeriod(ProjectInfo projectInfo, Period period) {
		isOverlapped(projectInfo, period);
		isWithinLimits(projectInfo, period);
	}

	private void isOverlapped(ProjectInfo projectInfo, Period period) {
		Optional<Assemble> overlappedPeriod = assembleRepository.findFirstByProjectIdAndStartAtIsLessThanEqualAndEndAtIsGreaterThanEqual(
			projectInfo.getId(),
			period.getEndDate(),
			period.getStartDate());
		if (overlappedPeriod.isPresent()) {
			throw new RuntimeException("is registered for period");
		}
	}

	private void isWithinLimits(ProjectInfo projectInfo, Period period) {
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

	private void isDuplicated(ProjectInfo projectInfo, AssembleRequest.CreationDto creationDto) {
		boolean isDuplicatedTitle = assembleRepository.existsByProjectIdAndTitleContains(projectInfo.getId(),
			creationDto.getAssembleTitle());
		if (isDuplicatedTitle) {
			throw new RuntimeException("duplicated assemble's title");
		}
	}
}
