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

import org.junit.Assert;
import org.junit.Test;


public class PhoneNumberUtilsTest {

	@Test
	public void testPhoneNumberLength() throws Exception {
		Assert.assertTrue(PhoneNumberUtils.isValid("1234567890"));
		Assert.assertTrue(PhoneNumberUtils.isValid("1234567890123456"));

		Assert.assertTrue(PhoneNumberUtils.isValid("123--456--7890"));
		Assert.assertTrue(PhoneNumberUtils.isValid("123 4567 89 0 1 23 4 56"));

		Assert.assertFalse(PhoneNumberUtils.isValid("123456789"));
		Assert.assertFalse(PhoneNumberUtils.isValid("12345678901234567"));
	}

	@Test
	public void testPhoneNumberFormat() throws Exception {
		Assert.assertEquals("(123) 456-7890", PhoneNumberUtils.format("1234567890"));
		Assert.assertEquals("(123) 456-7890 ext:123456", PhoneNumberUtils.format("1234567890123456"));
	}
}
