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
package ca.travelagency.invoice;

import org.junit.Assert;
import org.junit.Test;

import ca.travelagency.customer.Customer;
import ca.travelagency.customer.CustomerHelper;
import ca.travelagency.customer.Traveler;

public class InvoiceTravelerTest {
	private static final String BIG_NAME_LOWER = "big name";
	private static final String BIG_NAME_EXPECTED = "Big Name";
	private static final String BIG_NAME_UPPER = "BIG NAME";

	@Test
	public void testFromUpperToCamelCase() {
		InvoiceTraveler fixture = InvoiceHelper.makeTravelerWithoutId();
		fixture.setSalutation(BIG_NAME_UPPER);
		fixture.setFirstName(BIG_NAME_UPPER);
		fixture.setLastName(BIG_NAME_UPPER);
		fixture.setDocumentType(BIG_NAME_UPPER);
		fixture.setDocumentNumber(BIG_NAME_UPPER);
		validateCamelCase(fixture);
	}

	private void validateCamelCase(InvoiceTraveler fixture)  {
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getSalutation());
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getFirstName());
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getLastName());
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getDocumentType());
		Assert.assertEquals(BIG_NAME_UPPER, fixture.getDocumentNumber());
	}

	@Test
	public void testFromLowerToCamelCase() {
		InvoiceTraveler fixture = InvoiceHelper.makeTravelerWithoutId();
		fixture.setSalutation(BIG_NAME_LOWER);
		fixture.setFirstName(BIG_NAME_LOWER);
		fixture.setLastName(BIG_NAME_LOWER);
		fixture.setDocumentType(BIG_NAME_LOWER);
		fixture.setDocumentNumber(BIG_NAME_LOWER);
		validateCamelCase(fixture);
	}

	@Test
	public void testName() {
		// setup
		InvoiceTraveler fixture = InvoiceHelper.makeTravelerWithoutId();
		// execute & validate
		Assert.assertEquals(fixture.getSalutation()+" "+fixture.getFirstName()+" "+fixture.getLastName(), fixture.getName());
	}

	@Test
	public void testMakeUsingCustomer() {
		// setup
		Customer customer = CustomerHelper.makeCustomer();
		// execute
		InvoiceTraveler fixture = InvoiceTraveler.make(customer);
		// validate
		Assert.assertEquals(customer.getSalutation(), fixture.getSalutation());
		Assert.assertEquals(customer.getLastName(), fixture.getLastName());
		Assert.assertEquals(customer.getFirstName(), fixture.getFirstName());
		Assert.assertEquals(customer.getTravelDocumentNumber(), fixture.getDocumentNumber());
		Assert.assertEquals(customer.getTravelDocumentType(), fixture.getDocumentType());
		Assert.assertEquals(customer.getDateOfBirth(), fixture.getDateOfBirth());
	}

	@Test
	public void testUsingTraveler() {
		// setup
		Traveler traveler = CustomerHelper.makeTraveler();
		InvoiceTraveler fixture = InvoiceTraveler.makeEmpty();
		// execute
		fixture.copy(traveler);
		// validate
		Assert.assertEquals(traveler.getSalutation(), fixture.getSalutation());
		Assert.assertEquals(traveler.getLastName(), fixture.getLastName());
		Assert.assertEquals(traveler.getFirstName(), fixture.getFirstName());
		Assert.assertEquals(traveler.getDocumentNumber(), fixture.getDocumentNumber());
		Assert.assertEquals(traveler.getDocumentType(), fixture.getDocumentType());
		Assert.assertEquals(traveler.getDateOfBirth(), fixture.getDateOfBirth());
	}
}
