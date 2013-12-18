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


public class StringUtilsTest {

	private static final String MR = "Mr";
	private static final String FIRST_NAME = "First-Name";
	private static final String LAST_NAME = "Last-Name";

	@Test
	public void testFormatWithNullSource() throws Exception {
		Assert.assertEquals(StringUtils.EMPTY, StringUtils.format((String) null));
	}

	@Test
	public void testFormat() throws Exception {
		Assert.assertEquals(StringUtils.EMPTY,
				StringUtils.format(null, null, null));

		Assert.assertEquals(MR + StringUtils.SEPERATOR + FIRST_NAME + StringUtils.SEPERATOR + LAST_NAME,
				StringUtils.format(MR, FIRST_NAME, LAST_NAME));

		Assert.assertEquals(FIRST_NAME + StringUtils.SEPERATOR + LAST_NAME,
				StringUtils.format(null, FIRST_NAME, LAST_NAME));

		Assert.assertEquals(MR + StringUtils.SEPERATOR + LAST_NAME,
				StringUtils.format(MR, null, LAST_NAME));

		Assert.assertEquals(LAST_NAME,
				StringUtils.format(null, null, LAST_NAME));
	}
}
