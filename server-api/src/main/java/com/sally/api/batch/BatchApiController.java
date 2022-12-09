package com.sally.api.batch;

import com.sally.api.targetnotification.EmailService;
import com.sally.api.targetnotification.dto.TargetEmailRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BatchApiController {
	private final EmailService emailService;

	@PostMapping("/api/batch/send/target-mail")
	public ResponseEntity<Void> sendEmail(@RequestBody List<TargetEmailRequest> request) {
		emailService.sendNotificationEmail(request);
		return ResponseEntity.ok().build();
	}
}
