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


public class Base64UtilsTest {

	@Test
	public void testBase64() throws Exception {
		// setup
		String expected = "This is a test";
		// execute
		String encode = Base64Utils.encode(expected.getBytes());
		byte[] decode = Base64Utils.decode(encode);
		// validate
		Assert.assertEquals(expected, new String(decode));
	}
}
