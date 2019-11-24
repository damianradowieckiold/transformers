package com.radowiecki.transformers.constant;

public enum Resource {
	KRYPTON("krypton"), ASTAT("astat"), PROKATYN("prokatyn"), EUROPIUM("europium");

	private String resource;

	private Resource(String resource) {
		this.resource = resource;
	}

	public String toString() {
		return this.resource;
	}
}