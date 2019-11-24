package com.radowiecki.transformers.model;

import java.util.Date;

public interface ScheduledTask {
	Date getStartDate();

	Date getExecutionDate();

	boolean isDone();
}