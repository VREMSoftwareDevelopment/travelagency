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
import ca.travelagency.utils.StringUtils;

import com.google.common.collect.Lists;


public class InvoiceItemDistributionTest {

	private List<InvoiceItem> invoiceItems;
	private InvoiceItemDistribution fixture;
	private SalesAmounts salesAmounts;

	@Before
	public void setUp() throws Exception {
		fixture = InvoiceItemDistribution.make(null);

		invoiceItems = Lists.newArrayList(InvoiceHelper.makeItem(), InvoiceHelper.makeItem(), InvoiceHelper.makeItem());

		salesAmounts = SalesAmounts.make();
		for (InvoiceItem invoiceItem: invoiceItems) {
			salesAmounts.add(invoiceItem.getSalesAmounts());
			fixture.add(invoiceItem);
		}
	}

	@Test
	public void testNullKey() throws Exception {
		Assert.assertEquals(InvoiceItemDistribution.UNKNOWN, fixture.getKey());
	}

	@Test
	public void testEmptyKey() throws Exception {
		// setup
		fixture = InvoiceItemDistribution.make(StringUtils.EMPTY);
		// validate
		Assert.assertEquals(InvoiceItemDistribution.UNKNOWN, fixture.getKey());
	}

	@Test
	public void testCount() throws Exception {
		Assert.assertEquals(invoiceItems.size(), fixture.getCount());
	}

	@Test
	public void testSalesAmounts() throws Exception {
		Assert.assertEquals(salesAmounts, fixture.getSalesAmounts());
	}

	@Test
	public void testInvoiceItems() throws Exception {
		Assert.assertEquals(invoiceItems, fixture.getInvoiceItems());
	}

}
