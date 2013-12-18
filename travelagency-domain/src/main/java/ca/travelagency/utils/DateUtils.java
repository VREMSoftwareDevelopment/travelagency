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
import java.util.Date;

import org.joda.time.format.DateTimeFormat;

public abstract class DateUtils extends org.apache.commons.lang3.time.DateUtils implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String DATE_STYLE = "L-";
	public static final String DATE_TIME_STYLE = "LL";
	public static final String DATE_STYLE_SHORT = "S-";
	public static final String DATE_STYLE_FULL = "F-";
	public static final String YEAR_MONTH_PATTERN = "yyyy MMM";
	public static final String DATE_CUSTOM = "MMM dd, yyyy";

	public static String formatDate(Date date) {
		if (date == null) {
			return StringUtils.EMPTY;
		}
		return DateTimeFormat.forStyle(DATE_STYLE).print(date.getTime());
	}

	public static String formatDateTime(Date date) {
		if (date == null) {
			return StringUtils.EMPTY;
		}
		return DateTimeFormat.forStyle(DATE_TIME_STYLE).print(date.getTime());
	}

	public static String formatDateCustom(Date date) {
		if (date == null) {
			return StringUtils.EMPTY;
		}
		return DateTimeFormat.forPattern(DATE_CUSTOM).print(date.getTime());
	}

	public static Date parseDateCustom(String date) {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		return DateTimeFormat.forPattern(DATE_CUSTOM).parseLocalDate(date).toDate();
	}

	public static String formatDateShort(Date date) {
		if (date == null) {
			return StringUtils.EMPTY;
		}
		return DateTimeFormat.forStyle(DATE_STYLE_SHORT).print(date.getTime());
	}

	public static String formatDateAsMonth(Date date) {
		if (date == null) {
			return StringUtils.EMPTY;
		}
		return DateTimeFormat.forPattern(YEAR_MONTH_PATTERN).print(date.getTime());
	}
}
