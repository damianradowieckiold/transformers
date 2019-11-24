package com.radowiecki.transformers.constant;

public enum BuildingStatus {
	OK("OK"), INVALID_PARAMETER("INVALID_PARAMETER"), TO_FEW_RESOURCES("TO_FEW_RESOURCES");

	private String status;

	private BuildingStatus(String status) {
		this.status = status;
	}

	public String toString() {
		return this.status;
	}
}