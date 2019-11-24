package com.radowiecki.transformers.language;

public enum Language {
	POLISH("polish_language.properties"), ENGLISH("english_language.properties"), SPANISH(
			"spanish_language.properties");

	private String propertiesFileName;

	private Language(String propertiesFileName) {
		this.propertiesFileName = propertiesFileName;
	}

	public String getPropertiesFileName() {
		return this.propertiesFileName;
	}
}