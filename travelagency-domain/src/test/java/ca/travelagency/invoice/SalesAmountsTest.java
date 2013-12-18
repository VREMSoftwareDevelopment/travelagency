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

public class SalesAmountsTest {
	private SalesAmounts fixture;

	@Before
	public void setUp() throws Exception {
		fixture = SalesAmounts.make();
		Assert.assertFalse(fixture.isDueAmount());
	}

	@Test
	public void testCommission() {
		// setup
		String expected = "10.00";
		BigDecimal value = new BigDecimal(expected);
		// execute
		fixture.addCommission(value);
		// validate
		Assert.assertEquals(new BigDecimal(expected), fixture.getCommission());
		Assert.assertEquals("$"+expected, fixture.getCommissionAsString());
	}

	@Test
	public void testPaid() {
		// setup
		String expected = "20.00";
		BigDecimal value = new BigDecimal(expected);
		// execute
		fixture.addPaid(value);
		// validate
		Assert.assertEquals(new BigDecimal(expected), fixture.getPaid());
		Assert.assertEquals("$"+expected, fixture.getPaidAsString());
	}

	@Test
	public void testPrice() {
		// setup
		String expected = "30.00";
		BigDecimal value = new BigDecimal(expected);
		// execute
		fixture.addPrice(value);
		// validate
		Assert.assertEquals(new BigDecimal(expected), fixture.getPrice());
		Assert.assertEquals("$"+expected, fixture.getPriceAsString());
	}

	@Test
	public void testTax() {
		// setup
		String expected = "40.00";
		BigDecimal value = new BigDecimal(expected);
		// execute
		fixture.addTax(value);
		// validate
		Assert.assertEquals(new BigDecimal(expected), fixture.getTax());
		Assert.assertEquals("$"+expected, fixture.getTaxAsString());
	}

	@Test
	public void testTaxOnCommission() {
		// setup
		String expected = "50.00";
		BigDecimal value = new BigDecimal(expected);
		// execute
		fixture.addTaxOnCommission(value);
		// validate
		Assert.assertEquals(new BigDecimal(expected), fixture.getTaxOnCommission());
		Assert.assertEquals("$"+expected, fixture.getTaxOnCommissionAsString());
	}

	@Test
	public void testCommissionReceived() {
		// setup
		String expected = "60.00";
		fixture.addCommissionReceived(new BigDecimal(expected));
		// execute & validate
		Assert.assertEquals(new BigDecimal(expected), fixture.getCommissionReceived());
		Assert.assertEquals("$"+expected, fixture.getCommissionReceivedAsString());
	}

	@Test
	public void testCommissionVerified() {
		// setup
		String expected = "70.00";
		fixture.addCommissionVerified(new BigDecimal(expected));
		// execute & validate
		Assert.assertEquals(new BigDecimal(expected), fixture.getCommissionVerified());
		Assert.assertEquals("$"+expected, fixture.getCommissionVerifiedAsString());
	}

	@Test
	public void testIsNotVerified() {
		// execute
		fixture.setVerified(true);
		fixture.setVerified(false);
		fixture.setVerified(true);
		// validate
		Assert.assertFalse(fixture.isVerified());
	}

	@Test
	public void testIsVerified() {
		// execute
		fixture.setVerified(true);
		// validate
		Assert.assertTrue(fixture.isVerified());
	}

	@Test
	public void testSale() {
		// setup
		String expected = "120.00";
		fixture.addPrice(new BigDecimal("100")).addTax(new BigDecimal("20"));
		// execute & validate
		Assert.assertEquals(new BigDecimal(expected), fixture.getSale());
		Assert.assertEquals("$"+expected, fixture.getSaleAsString());
	}

	@Test
	public void testCost() {
		// setup
		String expected = "80.00";
		fixture.addPrice(new BigDecimal("100")).addTax(new BigDecimal("20"))
			.addCommission(new BigDecimal("30")).addTaxOnCommission(new BigDecimal("10"));
		// execute & validate
		Assert.assertEquals(new BigDecimal(expected), fixture.getCost());
		Assert.assertEquals("$"+expected, fixture.getCostAsString());
	}

	@Test
	public void testDue() {
		// setup
		String expected = "60.00";
		fixture.addPrice(new BigDecimal("100")).addPaid(new BigDecimal("40"));
		// execute & validate
		Assert.assertEquals(new BigDecimal(expected), fixture.getDue());
		Assert.assertEquals("$"+expected, fixture.getDueAsString());
		Assert.assertTrue(fixture.isDueAmount());
	}

	@Test
	public void testAddSalesAmounts() {
		// setup
		SalesAmounts expected = SalesAmounts.make()
			.addPrice(new BigDecimal("100"))
			.addTax(new BigDecimal("20"))
			.addCommission(new BigDecimal("30"))
			.addTaxOnCommission(new BigDecimal("10"))
			.addCommissionReceived(new BigDecimal("5"))
			.addCommissionVerified(new BigDecimal("15"));
		// execute
		SalesAmounts actual = SalesAmounts.make().add(expected);
		// validate
		Assert.assertNotSame(expected, actual);
		Assert.assertEquals(expected, actual);
	}

}
