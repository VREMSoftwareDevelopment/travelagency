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


public class InvoiceDestinationTest {
	private static final String BIG_NAME_LOWER = "big name";
	private static final String BIG_NAME_EXPECTED = "Big Name";
	private static final String BIG_NAME_UPPER = "BIG NAME";

	@Test
	public void testFromUpperToCamelCase() {
		InvoiceDestination fixture = InvoiceHelper.makeDestinationWithoutId();
		fixture.setDeparturePlace(BIG_NAME_UPPER);
		fixture.setArrivalPlace(BIG_NAME_UPPER);
		validateCamelCase(fixture);
	}

	private void validateCamelCase(InvoiceDestination fixture)  {
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getDeparturePlace());
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getArrivalPlace());
	}

	@Test
	public void testFromLowerToCamelCase() {
		InvoiceDestination fixture = InvoiceHelper.makeDestinationWithoutId();
		fixture.setDeparturePlace(BIG_NAME_LOWER);
		fixture.setArrivalPlace(BIG_NAME_LOWER);
		validateCamelCase(fixture);
	}

	@Test
	public void testName() {
		// setup
		InvoiceDestination fixture = InvoiceHelper.makeDestinationWithoutId();
		// execute & validate
		Assert.assertEquals(fixture.getDeparturePlace()+" | "+fixture.getDepartureDateAsString()+" | "+fixture.getArrivalPlace(), fixture.getName());
	}

}
