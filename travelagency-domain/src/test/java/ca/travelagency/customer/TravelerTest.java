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
package ca.travelagency.customer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TravelerTest {
	private static final String BIG_NAME_LOWER = "big name";
	private static final String BIG_NAME_EXPECTED = "Big Name";
	private static final String BIG_NAME_UPPER = "BIG NAME";
	private Traveler fixture;

	@Before
	public void setUp() throws Exception {
		fixture = Traveler.makeEmpty();
	}

	@Test
	public void testEquals() throws Exception {
		Traveler expected = CustomerHelper.makeTraveler();
		Traveler actual = CustomerHelper.makeTraveler();

		Assert.assertNotSame(expected, actual);

		Assert.assertFalse(expected.equals(actual));

		actual.setId((Long) expected.getId());
		Assert.assertTrue(expected.equals(actual));
	}

	@Test
	public void testFromUpperToCamelCase() {
		fixture.setSalutation(BIG_NAME_UPPER);
		fixture.setFirstName(BIG_NAME_UPPER);
		fixture.setLastName(BIG_NAME_UPPER);
		fixture.setDocumentType(BIG_NAME_UPPER);
		fixture.setDocumentNumber(BIG_NAME_UPPER);

		validateCamelCase();
	}

	private void validateCamelCase() {
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getSalutation());
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getFirstName());
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getLastName());
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getDocumentType());
		Assert.assertEquals(BIG_NAME_UPPER, fixture.getDocumentNumber());
	}

	@Test
	public void testFromLowerToCamelCase() {
		fixture.setSalutation(BIG_NAME_LOWER);
		fixture.setFirstName(BIG_NAME_LOWER);
		fixture.setLastName(BIG_NAME_LOWER);
		fixture.setDocumentType(BIG_NAME_LOWER);
		fixture.setDocumentNumber(BIG_NAME_LOWER);

		validateCamelCase();
	}

	@Test
	public void testName() {
		// setup
		Traveler fixture = CustomerHelper.makeTraveler();
		// execute & validate
		Assert.assertEquals(fixture.getSalutation()+" "+fixture.getFirstName()+" "+fixture.getLastName(), fixture.getName());
	}

}