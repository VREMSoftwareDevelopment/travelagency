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
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoicePayment;
import ca.travelagency.invoice.PaymentType;
import ca.travelagency.utils.MoneyUtils;

import com.google.common.collect.Sets;


public class InvoicePaymentDistributionTest {

	private Set<InvoicePayment> invoicePayments;
	private InvoicePaymentDistribution fixture;
	private BigDecimal expectedAmount;
	private BigDecimal expectedReconciledAmount;

	@Before
	public void setUp() throws Exception {
		fixture = InvoicePaymentDistribution.make(PaymentType.CreditCard);

		invoicePayments = Sets.newHashSet(InvoiceHelper.makePayment(), InvoiceHelper.makePayment(), InvoiceHelper.makePayment());
		InvoicePayment invoicePaymentFirst = invoicePayments.iterator().next();
		invoicePaymentFirst.setReconciled(true);
		expectedReconciledAmount = MoneyUtils.round(invoicePaymentFirst.getAmount());

		expectedAmount = MoneyUtils.ZERO_VALUE;
		for (InvoicePayment invoicePayment: invoicePayments) {
			expectedAmount = expectedAmount.add(invoicePayment.getAmount());
			fixture.add(invoicePayment);
		}
		expectedAmount = MoneyUtils.round(expectedAmount);
		Assert.assertFalse(expectedAmount.equals(MoneyUtils.ZERO_VALUE));
		Assert.assertFalse(expectedReconciledAmount.equals(MoneyUtils.ZERO_VALUE));
	}

	@Test
	public void testCount() throws Exception {
		Assert.assertEquals(invoicePayments.size(), fixture.getCount());
	}

	@Test
	public void testReconciledAmount() throws Exception {
		Assert.assertEquals(expectedReconciledAmount, fixture.getReconciledAmount());
	}

	@Test
	public void testAmount() throws Exception {
		Assert.assertEquals(expectedAmount, fixture.getAmount());
	}

	@Test
	public void testInvoicePayments() throws Exception {
		Assert.assertEquals(invoicePayments, fixture.getInvoicePayments());
	}

}
