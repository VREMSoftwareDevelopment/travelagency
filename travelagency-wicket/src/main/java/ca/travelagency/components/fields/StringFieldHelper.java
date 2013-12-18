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
package ca.travelagency.components.fields;

import org.apache.wicket.validation.validator.StringValidator;

public class StringFieldHelper {
	public static final int FIELD_MAX_LEN = 250;
	public static final int AREA_MAX_LEN = 4000;

	public static StringValidator maxLenFieldValidator() {
		return StringValidator.maximumLength(FIELD_MAX_LEN);
	}
	public static StringValidator maxLenAreaValidator() {
		return StringValidator.maximumLength(AREA_MAX_LEN);
	}
}
