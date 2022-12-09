package com.sally.api.targetnotification;

import com.sally.api.targetnotification.dto.TargetEmailDto;
import com.sally.api.targetnotification.dto.TargetEmailRequest;

import java.util.List;

public interface EmailService {
	void sendRegisterDelayedTarget(TargetEmailDto targetEmailDto);

	void sendNotificationEmail(List<TargetEmailRequest> targetEmailRequests);
}
