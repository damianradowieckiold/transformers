package com.radowiecki.transformers.exception;

public class TooManyFleetsWithTheSameTypeException extends Exception {
	private static final long serialVersionUID = 2119763304836286927L;

	public TooManyFleetsWithTheSameTypeException() {
		super("Too many fleets with the same type");
	}
}