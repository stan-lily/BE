package com.sally.api.targetnotification;

import com.sally.api.targetnotification.dto.TargetEmailDto;
import com.sally.api.targetnotification.dto.TargetEmailRequest;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.List;
import java.util.Locale;

import javax.mail.internet.MimeMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DefaultEmailServiceImpl implements EmailService {
	private final JavaMailSender javaMailSender;
	private final SpringTemplateEngine springTemplateEngine;

	@Override
	public void sendRegisterDelayedTarget(TargetEmailDto targetEmailDto) {
		final MimeMessagePreparator preparator = toMimeMessagePreparator(targetEmailDto);
		javaMailSender.send(preparator);
	}

	@Override
	public void sendNotificationEmail(List<TargetEmailRequest> targetEmailRequests) {
		targetEmailRequests.stream().forEach(System.out::println);

		targetEmailRequests.stream()
			.map(targetEmail -> TargetEmailDto.builder()
				.toEmail(targetEmail.getEmail())
				.assembleTitle(targetEmail.getAssembleTitle())
				.targetTitle(targetEmail.getTargetTitle())
				.targetAt(targetEmail.getTargetAt())
				.labelName(targetEmail.getLabelName())
				.period(targetEmail.toPeriod())
				.build())
			.map(this::toMimeMessagePreparator)
			.forEach(javaMailSender::send);
	}

	private MimeMessagePreparator toMimeMessagePreparator(TargetEmailDto targetEmailDto) {
		return (MimeMessage message) -> {
			final MimeMessageHelper helper = new MimeMessageHelper(message);
			helper.setTo(targetEmailDto.getEmail());
			helper.setSubject(targetEmailDto.subject());
			helper.setText(
				springTemplateEngine.process("delayed-target-email",
					new Context(Locale.KOREA, targetEmailDto.getProperties())), true);
		};
	}
}
