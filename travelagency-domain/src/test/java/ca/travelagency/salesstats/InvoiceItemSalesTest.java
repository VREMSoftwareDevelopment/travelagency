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

import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoiceItem;
import ca.travelagency.invoice.SalesAmounts;

import com.google.common.collect.Lists;

public class InvoiceItemSalesTest {

	private List<InvoiceItem> invoiceItems;
	private InvoiceItemSales fixture;
	private SalesAmounts expectedSalesAmounts;

	@Before
	public void setUp() {
		invoiceItems = Lists.newArrayList(
			InvoiceHelper.makeInvoiceWithDetails().getInvoiceItems().get(0),
			InvoiceHelper.makeInvoiceWithDetails().getInvoiceItems().get(0),
			InvoiceHelper.makeInvoiceWithDetails().getInvoiceItems().get(0));

		expectedSalesAmounts = SalesAmounts.make();
		for (InvoiceItem invoiceItem : invoiceItems) {
			expectedSalesAmounts.add(invoiceItem.getSalesAmounts());
		}

		fixture = new InvoiceItemSales(invoiceItems) {
			private static final long serialVersionUID = 1L;
			@Override
			protected String makeKey(InvoiceItem invoiceItem) {
				return invoiceItem.getDescription();
			}
		};
	}

	@Test
	public void testGetDistributions() throws Exception {
		// execute
		List<InvoiceItemDistribution> distributions = fixture.getDistributions();
		// validate
		Assert.assertEquals(invoiceItems.size(), distributions.size());
		validateInvoiceItem(invoiceItems.get(0), distributions);
		validateInvoiceItem(invoiceItems.get(1), distributions);
		validateInvoiceItem(invoiceItems.get(2), distributions);
	}

	private void validateInvoiceItem(InvoiceItem invoiceItem, List<InvoiceItemDistribution> distributions) {
		String expectedKey = invoiceItem.getDescription();
		for (InvoiceItemDistribution distribution : distributions) {
			if (expectedKey.equals(distribution.getKey())) {
				return;
			}
		}
		Assert.assertTrue(expectedKey, false);
	}

	@Test
	public void testGetTotalSales() throws Exception {
		Assert.assertEquals(expectedSalesAmounts, fixture.getTotalSales());
	}

	@Test
	public void testGetInvoiceItems() throws Exception {
		Assert.assertEquals(invoiceItems, fixture.getInvoiceItems());
	}

}
