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

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InvoiceItemTest {
	private static final String BIG_NAME_LOWER = "big name";
	private static final String BIG_NAME_EXPECTED = "Big Name";
	private static final String BIG_NAME_UPPER = "BIG NAME";

	private InvoiceItem fixture;

	@Before
	public void setUp() throws Exception {
		fixture = InvoiceHelper.makeItem();
	}

	@Test
	public void testTotalPrice() {
		// setup
		fixture.setQty(5);
		fixture.setPrice(new BigDecimal("100.00"));
		String expected = "500.00";
		// execute & verified
		Assert.assertEquals(new BigDecimal(expected), fixture.getSalesAmounts().getPrice());
	}

	@Test
	public void testTotalTax() {
		// setup
		fixture.setQty(5);
		fixture.setTax(new BigDecimal("50.00"));
		String expected = "250.00";
		// execute & verified
		Assert.assertEquals(new BigDecimal(expected), fixture.getSalesAmounts().getTax());
	}

	@Test
	public void testTotalSale() {
		// setup
		fixture.setQty(2);
		fixture.setPrice(new BigDecimal("100.00"));
		fixture.setTax(new BigDecimal("50.00"));
		String expected = "300.00";
		// execute & verified
		Assert.assertEquals(new BigDecimal(expected), fixture.getSalesAmounts().getSale());
	}

	@Test
	public void testTotalCommission() {
		// setup
		fixture.setQty(2);
		fixture.setCommission(new BigDecimal("20.00"));
		String expected = "40.00";
		// execute & verified
		Assert.assertEquals(new BigDecimal(expected), fixture.getSalesAmounts().getCommission());
	}

	@Test
	public void testTotalTaxOnCommission() {
		// setup
		fixture.setQty(2);
		fixture.setTaxOnCommission(new BigDecimal("1.00"));
		String expected = "2.00";
		// execute & verified
		Assert.assertEquals(new BigDecimal(expected), fixture.getSalesAmounts().getTaxOnCommission());
	}

	@Test
	public void testTotalCost() {
		// setup
		fixture.setQty(2);
		fixture.setPrice(new BigDecimal("100.00"));
		fixture.setTax(new BigDecimal("50.00"));
		fixture.setCommission(new BigDecimal("20.00"));
		fixture.setTaxOnCommission(new BigDecimal("2.00"));
		String expected = "256.00";
		// execute & verified
		Assert.assertEquals(new BigDecimal(expected), fixture.getSalesAmounts().getCost());
	}

	@Test
	public void testIsVerfied() {
		// execute
		fixture.setCommissionStatus(CommissionStatus.Verified);
		// verified
		Assert.assertTrue(fixture.isCommissionVerified());
		Assert.assertTrue(fixture.getSalesAmounts().isVerified());
	}

	@Test
	public void testIsNotVerfiedByDefault() {
		// execute & verified
		Assert.assertFalse(fixture.getSalesAmounts().isVerified());
		Assert.assertFalse(fixture.isCommissionReceived());
		Assert.assertFalse(fixture.isCommissionVerified());
	}

	@Test
	public void testIsNotVerfiedWhenReceived() {
		// setup
		fixture.setCommissionStatus(CommissionStatus.Received);
		// execute & verified
		Assert.assertFalse(fixture.getSalesAmounts().isVerified());
		Assert.assertTrue(fixture.isCommissionReceived());
		Assert.assertFalse(fixture.isCommissionVerified());
	}

	@Test
	public void testFromUpperToCamelCase() {
		fixture.setDescription(BIG_NAME_UPPER);
		fixture.setSupplier(BIG_NAME_UPPER);
		fixture.setCancelBeforeDeparture(BIG_NAME_UPPER);
		fixture.setChangeAfterDeparture(BIG_NAME_UPPER);
		fixture.setChangeBeforeDeparture(BIG_NAME_UPPER);
		validateCamelCase();
	}

	private void validateCamelCase() {
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getDescription());
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getSupplier());
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getCancelBeforeDeparture());
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getChangeAfterDeparture());
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getChangeBeforeDeparture());
	}

	@Test
	public void testFromLowerToCamelCase() {
		fixture.setDescription(BIG_NAME_LOWER);
		fixture.setSupplier(BIG_NAME_LOWER);
		fixture.setCancelBeforeDeparture(BIG_NAME_LOWER);
		fixture.setChangeAfterDeparture(BIG_NAME_LOWER);
		fixture.setChangeBeforeDeparture(BIG_NAME_LOWER);
		validateCamelCase();
	}
}
