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


public abstract class PhoneNumberUtils {
	public static final int MIN_LENGTH = 10;
	public static final int MAX_LENGTH = 16;

	public static String strip(String phoneNumber) {
		return phoneNumber == null ? StringUtils.EMPTY : phoneNumber.replaceAll("[^0-9]", "");
	}

	public static boolean isValid(String phoneNumber) {
		int length = strip(phoneNumber).length();
		return length >= MIN_LENGTH && length <= MAX_LENGTH;
	}

	public static String format(String phoneNumber) {
		if (!isValid(phoneNumber)) {
			return StringUtils.EMPTY;
		}
		String stripPhoneNumber = strip(phoneNumber);
		if (stripPhoneNumber.length() == 10) {
			return String.format("(%3.3s) %3.3s-%4.4s",
					stripPhoneNumber, stripPhoneNumber.substring(3), stripPhoneNumber.substring(6));
		} else {
			return String.format("(%3.3s) %3.3s-%4.4s ext:%s",
				stripPhoneNumber, stripPhoneNumber.substring(3), stripPhoneNumber.substring(6), stripPhoneNumber.substring(10));
		}
	}

}
