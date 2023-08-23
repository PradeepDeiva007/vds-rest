package com.ford.vds.config;

import java.util.HashMap;
import java.util.Map;

public class ApplicationProperties {

	private static ApplicationProperties obj;
	private static Map<String, String> properties;

	private ApplicationProperties() {
	}

	public static ApplicationProperties getApplicationProperties() {
		if (obj == null) {
			obj = new ApplicationProperties();
			properties = new HashMap<>();
		}
		return obj;
	}

	public static String getProperty(String key) {
		return properties.get(key);
	}

	public static void loadProperties() {

	}

}
