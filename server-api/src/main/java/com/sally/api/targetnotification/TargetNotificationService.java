package com.sally.api.targetnotification;

import com.sally.api.login.dto.AuthUser;
import com.sally.api.project.dto.ProjectInfo;
import com.sally.api.target.TargetService;
import com.sally.api.target.dto.TargetInfo;
import com.sally.api.targetnotification.dto.TargetAndAssignees;
import com.sally.api.targetnotification.dto.TargetEmailDto;
import com.sally.api.targetnotification.dto.TargetNotificationRequest;
import com.sally.api.userproject.UserHasProjectService;
import com.sally.api.userproject.dto.ProjectUserInfo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TargetNotificationService {
	private final UserHasProjectService userHasProjectService;
	private final TargetService targetService;
	private final EmailService emailService;

	private final TargetNotificationRepository targetNotificationRepository;

	/**
	 * 사용자 별로 일정별로 알람시간을 지정 합니다.
	 * 이때 팀 전체 메일전송 요청이면 사용자별로 저장하지 않고, 팀원별로 저장 합니다.
	 * Target 지연 등록자에게는 지연 신청 및 알람 여부 메일을 바로 송신 합니다. - 트랜잭션 분리
	 */
	public void create(TargetNotificationRequest.registration request, ProjectInfo project, AuthUser authUser) {
		TargetAndAssignees targetAndAssignees = getTargetInfoAndCreateTargetNotification(request, project, authUser);
		emailService.sendRegisterDelayedTarget(
			TargetEmailDto.builder()
				.toEmail(authUser.email())
				.assembleTitle(targetAndAssignees.assembleTitle())
				.targetTitle(targetAndAssignees.title())
				.targetAt(targetAndAssignees.targetAt())
				.period(targetAndAssignees.period())
				.labelName(targetAndAssignees.labelName())
				.targetAssignees(targetAndAssignees.assignees())
				.build());
	}

	@Transactional
	public TargetAndAssignees getTargetInfoAndCreateTargetNotification(TargetNotificationRequest.registration request,
		ProjectInfo project, AuthUser authUser) {
		List<ProjectUserInfo> users = userHasProjectService.getUserInfo(request.getRecipients(), project.getId());
		TargetInfo targetInfo = targetService.readTargetInfoFrom(request.getTargetId());
		createDelayedTargetNotification(request, targetInfo, users, authUser);
		return new TargetAndAssignees(targetInfo, users);
	}

	public void createDelayedTargetNotification(TargetNotificationRequest.registration request, TargetInfo targetInfo,
		List<ProjectUserInfo> users, AuthUser authUser) {
		if (request.isToTeam()) {
			saveByRepeatedNotificationAndUser(request, targetInfo,
				List.of(
					new ProjectUserInfo(
						authUser.nickName(),
						authUser.email(),
						targetInfo.getProjectTitle())));
		} else {
			saveByRepeatedNotificationAndUser(request, targetInfo, users);
		}
	}

	public void saveByRepeatedNotificationAndUser(TargetNotificationRequest.registration request,
		TargetInfo targetInfo,
		List<ProjectUserInfo> users) {
		TargetNotification.RequiredTarget requiredTarget = getRequiredTarget(targetInfo);
		TargetNotification.TargetPeriod targetPeriod = getTargetPeriod(targetInfo);

		List<TargetNotification> targetNotifications = request.repeatTimes().stream()
			.map(notifyAt -> {
				return users.stream()
					.map(member -> TargetNotification.builder()
						.email(member.getEmail())
						.notifyAt(notifyAt)
						.requiredTarget(requiredTarget)
						.targetPeriod(targetPeriod)
						.assembleTitle(targetInfo.getAssembleTitle())
						.toTeam(request.isToTeam())
						.status(TargetNotification.Status.APPENDING)
						.createdAt(LocalDateTime.now())
						.updatedAt(LocalDateTime.now())
						.build())
					.collect(Collectors.toList());
			}).flatMap(Collection::stream)
			.collect(Collectors.toList());
		targetNotificationRepository.saveAll(targetNotifications);
	}

	private TargetNotification.TargetPeriod getTargetPeriod(TargetInfo targetInfo) {
		return new TargetNotification.TargetPeriod(targetInfo.startAt(), targetInfo.endAt());
	}

	private TargetNotification.RequiredTarget getRequiredTarget(TargetInfo targetInfo) {
		return new TargetNotification.RequiredTarget(
			targetInfo.getTitle(),
			targetInfo.getTargetAt(),
			targetInfo.getLabelName());
	}
}
