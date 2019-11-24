package com.radowiecki.transformers.exception;

public class TooFewFleetException extends Exception {
	private static final long serialVersionUID = -7462936865824024582L;

	public TooFewFleetException() {
		super("Too few fleet");
	}
}