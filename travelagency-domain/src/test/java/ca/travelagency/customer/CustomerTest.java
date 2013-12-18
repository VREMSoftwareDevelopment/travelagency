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


public class CustomerTest {
	private static final String BIG_NAME_LOWER = "big name";
	private static final String BIG_NAME_EXPECTED = "Big Name";
	private static final String BIG_NAME_UPPER = "BIG NAME";
	private Customer fixture;

	@Before
	public void setUp() throws Exception {
		fixture = Customer.makeEmpty();
	}

	@Test
	public void testEquals() throws Exception {
		Customer expected = CustomerHelper.makeCustomer();
		Customer actual = CustomerHelper.makeCustomer();

		Assert.assertNotSame(expected, actual);

		Assert.assertFalse(expected.equals(actual));

		actual.setId((Long) expected.getId());
		Assert.assertTrue(expected.equals(actual));
	}

	@Test
	public void testFromUpperToCamelCase() {
		fixture.setCompanyName(BIG_NAME_UPPER);
		fixture.setSalutation(BIG_NAME_UPPER);
		fixture.setFirstName(BIG_NAME_UPPER);
		fixture.setLastName(BIG_NAME_UPPER);
		fixture.setAddress(BIG_NAME_UPPER);
		fixture.setCity(BIG_NAME_UPPER);
		fixture.setProvince(BIG_NAME_UPPER);
		fixture.setCountry(BIG_NAME_UPPER);
		fixture.setTravelDocumentType(BIG_NAME_UPPER);
		fixture.setTravelDocumentNumber(BIG_NAME_UPPER);

		validateCamelCase();
	}

	private void validateCamelCase() {
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getCompanyName());
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getSalutation());
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getFirstName());
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getLastName());
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getAddress());
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getProvince());
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getCity());
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getCountry());
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getTravelDocumentType());
		Assert.assertEquals(BIG_NAME_UPPER, fixture.getTravelDocumentNumber());
	}

	@Test
	public void testFromLowerToCamelCase() {
		fixture.setCompanyName(BIG_NAME_LOWER);
		fixture.setSalutation(BIG_NAME_LOWER);
		fixture.setFirstName(BIG_NAME_LOWER);
		fixture.setLastName(BIG_NAME_LOWER);
		fixture.setAddress(BIG_NAME_LOWER);
		fixture.setCity(BIG_NAME_LOWER);
		fixture.setProvince(BIG_NAME_LOWER);
		fixture.setCountry(BIG_NAME_LOWER);
		fixture.setTravelDocumentType(BIG_NAME_LOWER);
		fixture.setTravelDocumentNumber(BIG_NAME_LOWER);

		validateCamelCase();
	}

	@Test
	public void testPostalCodeToUpperCase() {
		fixture.setPostalCode(BIG_NAME_LOWER);
		Assert.assertEquals(BIG_NAME_UPPER, fixture.getPostalCode());
	}

	@Test
	public void testPrimaryPhone() {
		fixture.setPrimaryPhone("123-3456-6675-575");
		Assert.assertEquals("(123) 345-6667 ext:5575", fixture.getPrimaryPhone());
		Assert.assertEquals("12334566675575", fixture.getPrimaryPhoneRaw());
	}

	@Test
	public void testSecondaryPhone() {
		fixture.setSecondaryPhone("123-3456-6675-575");
		Assert.assertEquals("(123) 345-6667 ext:5575", fixture.getSecondaryPhone());
		Assert.assertEquals("12334566675575", fixture.getSecondaryPhoneRaw());
	}

	@Test
	public void testIsActiveStatus() {
		Customer fixture = CustomerHelper.makeCustomer();
		Assert.assertTrue(fixture.isActive());
		fixture.setStatus(CustomerStatus.OnHold);
		Assert.assertFalse(fixture.isActive());
	}

	@Test
	public void testName() {
		// setup
		Customer fixture = CustomerHelper.makeCustomer();
		// execute & validate
		Assert.assertEquals(fixture.getSalutation()+" "+fixture.getFirstName()+" "+fixture.getLastName(), fixture.getName());
	}

}