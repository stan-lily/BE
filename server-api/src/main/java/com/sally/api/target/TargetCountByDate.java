package com.sally.api.target;

import java.time.LocalDate;

public interface TargetCountByDate {
	int getTargetCount();

	LocalDate getEndAt();
}
