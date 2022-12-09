package com.sally.api.targetnotification.dto;

import com.sally.api.util.TimeUnit;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TargetNotificationRequest {
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class registration {
		private LocalDateTime notifyAt;
		private RepeatInfo repeatInfo;
		private boolean toTeam;
		private boolean isNotifying;
		private Long targetId;
		private List<Long> recipients;
		private Long projectId;

		public List<LocalDateTime> repeatTimes() {
			if (Objects.isNull(repeatInfo)) {
				return Collections.singletonList(notifyAt);
			}
			return IntStream.rangeClosed(0, repeatInfo.times)
				.mapToObj(idx -> {
					int times = idx * repeatInfo.getInterval();
					LocalDateTime reservedNotifyAt = notifyAt.plus(times, repeatInfo.getTimeUnit());
					return reservedNotifyAt;
				}).collect(Collectors.toList());
		}
	}

	@Data
	public static class RepeatInfo {
		private Interval interval;
		private int times;

		int getInterval() {
			return this.interval.getIntervalValue();
		}

		ChronoUnit getTimeUnit() {
			return this.interval.toTimeUnit().toUnit();
		}
	}

	@Data
	public static class Interval {
		private int intervalValue;
		private String timeUnit;

		public TimeUnit toTimeUnit() {
			return TimeUnit.from(timeUnit);
		}
	}
}
