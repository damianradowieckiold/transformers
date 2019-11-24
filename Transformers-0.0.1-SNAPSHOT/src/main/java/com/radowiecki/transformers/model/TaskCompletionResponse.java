package com.radowiecki.transformers.model;

public class TaskCompletionResponse {
	private boolean done;
	private long secondsToWait = 0L;
	private boolean taskNotFound;

	public boolean isDone() {
		return this.done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public long getSecondsToWait() {
		return this.secondsToWait;
	}

	public void setSecondsToWait(long secondsToWait) {
		this.secondsToWait = secondsToWait;
	}

	public boolean isTaskNotFound() {
		return this.taskNotFound;
	}

	public void setTaskNotFound(boolean taskNotFound) {
		this.taskNotFound = taskNotFound;
	}
}