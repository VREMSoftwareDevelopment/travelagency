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
package ca.travelagency.components.validators;

import org.junit.Assert;
import org.junit.Test;

public class PhoneNumberValidatorTest {
	@Test
	public void testValidPhoneNumberPattern() throws Exception {
		// setup
		PhoneNumberValidator fixture = new PhoneNumberValidator();
		Validatable<String> validatable = new Validatable<String>("(123)-234-3454");
		// execute
		fixture.validate(validatable);
		// validate
		Assert.assertTrue(validatable.isValid());
	}

	@Test
	public void testInValidPhoneNumberPattern() throws Exception {
		// setup
		PhoneNumberValidator fixture = new PhoneNumberValidator();
		Validatable<String> validatable = new Validatable<String>("(123)-2A4-3454");
		// execute
		fixture.validate(validatable);
		// validate
		Assert.assertFalse(validatable.isValid());
	}

}
