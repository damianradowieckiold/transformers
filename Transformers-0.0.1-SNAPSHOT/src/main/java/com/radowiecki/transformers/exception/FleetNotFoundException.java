package com.radowiecki.transformers.exception;

public class FleetNotFoundException extends Exception {
	private static final long serialVersionUID = 3119742043173941606L;

	public FleetNotFoundException() {
		super("Fleet not found");
	}
}