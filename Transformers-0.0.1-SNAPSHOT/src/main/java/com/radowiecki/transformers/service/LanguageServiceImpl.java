package com.radowiecki.transformers.service;

import com.radowiecki.transformers.language.Language;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;

@Service
public class LanguageServiceImpl implements LanguageService {
	private Properties properties;
	private Language currentLanguage;
	private final Logger logger;

	public LanguageServiceImpl() {
		this.currentLanguage = Language.POLISH;
		this.logger = Logger.getLogger(LanguageServiceImpl.class);
	}

	public void changeLanguage(Language language) {
		this.currentLanguage = language;
		this.properties = null;
	}

	public String getValue(String key) {
		return this.getLanguageProperties().getProperty(key);
	}

	public Properties getLanguageProperties() {
		this.getPropertiesFromFile();
		return this.properties;
	}

	public Map<String, String> getLanguagePropertiesMap() {
		return this.getLanguagePropertiesAsMap();
	}

	private void getPropertiesFromFile() {
		try {
			this.properties = this.getPropertiesFromFile(this.currentLanguage.getPropertiesFileName());
		} catch (IOException var2) {
			this.logger.error("Couldn't get properties from file. Error message: " + var2.getMessage());
		}

	}

	private Map<String, String> getLanguagePropertiesAsMap() {
		if (this.properties == null) {
			this.getPropertiesFromFile();
		}

		Map<String, String> propertiesMap = new HashMap();
		Iterator var2 = this.properties.stringPropertyNames().iterator();

		while (var2.hasNext()) {
			String key = (String) var2.next();
			String value = this.properties.getProperty(key);
			propertiesMap.put(key, value);
		}

		return propertiesMap;
	}

	private Properties getPropertiesFromFile(String propertiesFileName) throws IOException {
		new Properties();
		Resource resource = new ClassPathResource(propertiesFileName);
		Properties properties = PropertiesLoaderUtils.loadProperties(resource);
		return properties;
	}
}