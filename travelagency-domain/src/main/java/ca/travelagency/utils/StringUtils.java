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

import java.io.Serializable;

import org.apache.commons.lang3.Validate;


public abstract class StringUtils extends org.apache.commons.lang3.StringUtils implements Serializable {
	private static final long serialVersionUID = 1L;

	static final String SEPERATOR = " ";

	public static String format(String value) {
		return value == null ? StringUtils.EMPTY : value;
	}

	public static String format(String ... sources) {
		Validate.notNull(sources);
		StringBuilder stringBuilder = new StringBuilder();
		for (String source : sources) {
			if (StringUtils.isNotBlank(source)) {
				if (stringBuilder.length() > 0) {
					stringBuilder.append(SEPERATOR);
				}
				stringBuilder.append(source);
			}
		}
		return stringBuilder.toString();
	}

}
