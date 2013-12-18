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

import java.math.BigDecimal;
import java.text.NumberFormat;

public abstract class MoneyUtils {
	public static BigDecimal ZERO_VALUE = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);

	public static String format(BigDecimal value) {
		if (value == null) {
			return StringUtils.EMPTY;
		}
		return NumberFormat.getCurrencyInstance().format(value);
	}

	public static String format(double value) {
		return NumberFormat.getCurrencyInstance().format(value);
	}

	public static BigDecimal round(double value) {
		return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public static BigDecimal round(BigDecimal value) {
		if (value == null) {
			return ZERO_VALUE;
		}
		return round(value.doubleValue());
	}

	public static BigDecimal multiply(BigDecimal value, Integer multiplier) {
		if (value == null || multiplier == null) {
			return ZERO_VALUE;
		}
		return round(value.multiply(new BigDecimal(multiplier)));
	}

	public static BigDecimal add(BigDecimal value1, BigDecimal value2) {
		if (value1 != null && value2 != null) {
			return value1.add(value2);
		}
		if (value1 != null) {
			return value1;
		}
		if (value2 != null) {
			return value2;
		}
		return ZERO_VALUE;
	}

}
