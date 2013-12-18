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

import java.math.BigDecimal;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoicePayment;
import ca.travelagency.utils.MoneyUtils;


public class PaymentDistributionTest {
	private SortedSet<InvoicePayment> invoicePayments;
	private PaymentDistribution fixture;
	private BigDecimal expectedTotal;

	@Before
	public void setUp() throws Exception {
		invoicePayments = new TreeSet<InvoicePayment>();
		invoicePayments.addAll(InvoiceHelper.makeInvoiceWithDetails().getInvoicePayments());
		invoicePayments.addAll(InvoiceHelper.makeInvoiceWithDetails().getInvoicePayments());
		invoicePayments.addAll(InvoiceHelper.makeInvoiceWithDetails().getInvoicePayments());

		fixture = PaymentDistribution.make(invoicePayments);

		expectedTotal = MoneyUtils.ZERO_VALUE;
		for (InvoicePayment invoicePayment: invoicePayments) {
			expectedTotal = expectedTotal.add(invoicePayment.getAmount());
		}
	}

	@Test
	public void testInvoicePaymentsCount() throws Exception {
		Assert.assertEquals(3, fixture.getInvoicePayments().size());
	}

	@Test
	public void testDistributionsCount() throws Exception {
		Assert.assertEquals(1, fixture.getDistributions().size());
	}

}
