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
package ca.travelagency.salesstats;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.SalesAmounts;

import com.google.common.collect.Lists;


public class MonthlyDistributionTest {

	private List<Invoice> invoices;
	private MonthlyDistribution fixture;
	private SalesAmounts salesAmounts;

	@Before
	public void setUp() throws Exception {
		invoices = Lists.newArrayList(
			InvoiceHelper.makeInvoiceWithDetails(), InvoiceHelper.makeInvoiceWithDetails(), InvoiceHelper.makeInvoiceWithDetails());
		fixture = MonthlyDistribution.make(invoices.get(0).getDate());

		salesAmounts = SalesAmounts.make();
		for (Invoice invoice: invoices) {
			fixture.add(invoice);
			salesAmounts.add(invoice.getSalesAmounts());
		}
	}

	@Test
	public void testCount() throws Exception {
		Assert.assertEquals(3, fixture.getCount());
	}

	@Test
	public void testSalesAmounts() throws Exception {
		Assert.assertEquals(salesAmounts, fixture.getSalesAmounts());
	}

}
