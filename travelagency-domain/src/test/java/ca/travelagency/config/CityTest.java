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
package ca.travelagency.config;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CityTest {
	private static final String BIG_NAME_LOWER = "big name";
	private static final String BIG_NAME_EXPECTED = "Big Name";
	private static final String BIG_NAME_UPPER = "BIG NAME";
	private City fixture;

	@Before
	public void setUp() throws Exception {
		fixture = new City();
	}

	@Test
	public void testEquals() throws Exception {
		City expected = new City();
		City actual = new City();

		Assert.assertNotSame(expected, actual);
		Assert.assertTrue(expected.equals(actual));
	}

	@Test
	public void testFromUpperToCamelCase() {
		fixture.setName(BIG_NAME_UPPER);
		validateCamelCase();
	}

	private void validateCamelCase() {
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getName());
	}

	@Test
	public void testFromLowerToCamelCase() {
		fixture.setName(BIG_NAME_LOWER);
		validateCamelCase();
	}
}