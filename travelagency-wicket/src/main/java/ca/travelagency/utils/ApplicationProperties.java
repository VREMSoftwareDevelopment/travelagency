/**
 *    Copyright (C) 2010 - 2014 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package ca.travelagency.utils;

import java.util.Properties;

public class ApplicationProperties {
	private static final String APPLICATION = "application.";
	private static final String FILENAME = APPLICATION + "properties";
	private static final String VERSION = APPLICATION + "version";
	private static final String NAME = APPLICATION + "name";
	private static final String CODE = APPLICATION + "code";
	private static final String SYSTEM_USER_MAX = APPLICATION + "systemUserMax";

	public static ApplicationProperties make() {
		return make(FILENAME);
	}

	static ApplicationProperties make(String filename) {
		return new ApplicationProperties(filename);
	}

	private Properties properties;

	private ApplicationProperties(String filename) {
		try {
			properties = new Properties();
			properties.load(ContextClassLoaderUtils.getResourceAsStream(filename));
		} catch (Exception e) {
			throw new RuntimeException("Unable to load application properties");
		}
	}

	public String getVersion() {
		return (String) properties.get(VERSION);
	}

	public String getName() {
		return (String) properties.get(NAME);
	}

	public String getCode() {
		return (String) properties.get(CODE);
	}

	public int getSystemUserMax() {
		try {
			return Integer.parseInt((String) properties.get(SYSTEM_USER_MAX));
		} catch (Exception e) {
			return Integer.MAX_VALUE;
		}
	}
}
