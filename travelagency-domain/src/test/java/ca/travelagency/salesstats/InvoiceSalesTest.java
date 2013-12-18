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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.invoice.CommissionStatus;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoiceItem;
import ca.travelagency.invoice.SalesAmounts;
import ca.travelagency.utils.DateUtils;
import ca.travelagency.utils.MoneyUtils;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class InvoiceSalesTest {
	private SystemUser systemUser;
	private List<Invoice> currentMonthInvoices;
	private List<Invoice> current1MonthAgoInvoices;
	private List<Invoice> current2MonthAgoInvoices;

	@Before
	public void setUp() {
		systemUser = SystemUserHelper.makeSystemUser();

		currentMonthInvoices = Lists.newArrayList(
			InvoiceHelper.makeInvoiceWithDetails(systemUser, 0),
			InvoiceHelper.makeInvoiceWithDetails(systemUser, 0),
			InvoiceHelper.makeInvoiceWithDetails(systemUser, 0));

		current1MonthAgoInvoices = Lists.newArrayList(
			InvoiceHelper.makeInvoiceWithDetails(systemUser, -1),
			InvoiceHelper.makeInvoiceWithDetails(systemUser, -1),
			InvoiceHelper.makeInvoiceWithDetails(systemUser, -1),
			InvoiceHelper.makeInvoiceWithDetails(systemUser, -1));

		current2MonthAgoInvoices = Lists.newArrayList(
			InvoiceHelper.makeInvoiceWithDetails(systemUser, -2),
			InvoiceHelper.makeInvoiceWithDetails(systemUser, -2));
	}

	@Test
	public void testGetMonthlyDistribution() throws Exception {
		// setup
		List<Invoice> invoices = Lists.newArrayList(Iterables.concat(currentMonthInvoices, current1MonthAgoInvoices, current2MonthAgoInvoices));
		InvoiceSales fixture = InvoiceSales.make(invoices);
		// execute
		List<MonthlyDistribution> monhtlySales = fixture.getMonthlyDistribution();
		// validate
		Assert.assertEquals(3, monhtlySales.size());
		validateMonth(currentMonthInvoices, monhtlySales.get(0));
		Assert.assertEquals(getSalesAmounts(currentMonthInvoices), monhtlySales.get(0).getSalesAmounts());
		validateMonth(current1MonthAgoInvoices, monhtlySales.get(1));
		Assert.assertEquals(getSalesAmounts(current1MonthAgoInvoices), monhtlySales.get(1).getSalesAmounts());
		validateMonth(current2MonthAgoInvoices, monhtlySales.get(2));
		Assert.assertEquals(getSalesAmounts(current2MonthAgoInvoices), monhtlySales.get(2).getSalesAmounts());
	}

	@Test
	public void testInvoices() throws Exception {
		// setup
		InvoiceSales fixture = InvoiceSales.make(currentMonthInvoices);
		// execute
		Collection<Invoice> actual = fixture.getInvoices();
		// validate
		Assert.assertEquals(currentMonthInvoices, actual);
	}

	@Test
	public void testTotalSales() throws Exception {
		// setup
		InvoiceSales fixture = InvoiceSales.make(currentMonthInvoices);
		// execute
		SalesAmounts salesAmounts = fixture.getTotalSales();
		// validate
		Assert.assertEquals(getSalesAmounts(currentMonthInvoices), salesAmounts);
	}

	private void validateMonth(List<Invoice> invoices, MonthlyDistribution monthlyDistribution) {
		Date expectedKey = DateUtils.truncate(invoices.get(0).getDate(), Calendar.MONTH);
		Assert.assertEquals(expectedKey, monthlyDistribution.getDate());
		Assert.assertEquals(invoices.size(), monthlyDistribution.getCount());
	}

	private SalesAmounts getSalesAmounts(List<Invoice> invoices) {
		SalesAmounts expected = SalesAmounts.make();
		for (Invoice invoice : invoices) {
			if (invoice.isVoided()) {
				continue;
			}
			expected.add(invoice.getSalesAmounts());
		}
		return expected;
	}

	@Test
	public void testCommissionEstimatedWithSystemUser() throws Exception {
		// setup
		InvoiceSales fixture = InvoiceSales.make(currentMonthInvoices);
		fixture.setSystemUser(systemUser);
		systemUser.setCommissionRate(10.0);
		BigDecimal commission = fixture.getTotalSales().getCommission();
		BigDecimal expected = MoneyUtils.round(commission.multiply(new BigDecimal(systemUser.getCommissionRate() / 100)));
		// expected
		Assert.assertFalse(""+commission, MoneyUtils.ZERO_VALUE.equals(commission));
		// execute
		BigDecimal actual = fixture.getCommissionEstimated();
		// validate
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCommissionRPayableWithNoSystemUser() throws Exception {
		// setup
		InvoiceSales fixture = InvoiceSales.make(new ArrayList<Invoice>());
		// execute
		BigDecimal actual = fixture.getCommissionRPayable();
		// validate
		Assert.assertEquals(MoneyUtils.ZERO_VALUE, actual);
	}

	@Test
	public void testCommissionRPayableWithSystemUser() throws Exception {
		// setup
		for (Invoice invoice: currentMonthInvoices) {
			for (InvoiceItem invoiceItem : invoice.getInvoiceItems()) {
				invoiceItem.setCommissionStatus(CommissionStatus.Received);
			}
		}
		InvoiceSales fixture = InvoiceSales.make(currentMonthInvoices);
		fixture.setSystemUser(systemUser);
		systemUser.setCommissionRate(10.0);
		BigDecimal commissionReceived = fixture.getTotalSales().getCommissionReceived();
		BigDecimal expected = MoneyUtils.round(commissionReceived.multiply(new BigDecimal(systemUser.getCommissionRate() / 100)));
		// expected
		Assert.assertFalse(""+commissionReceived, MoneyUtils.ZERO_VALUE.equals(commissionReceived));
		// execute
		BigDecimal actual = fixture.getCommissionRPayable();
		// validate
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCommissionVPayableWithNoSystemUser() throws Exception {
		// setup
		InvoiceSales fixture = InvoiceSales.make(new ArrayList<Invoice>());
		// execute
		BigDecimal actual = fixture.getCommissionVPayable();
		// validate
		Assert.assertEquals(MoneyUtils.ZERO_VALUE, actual);
	}

	@Test
	public void testCommissionVPayableWithSystemUser() throws Exception {
		// setup
		for (Invoice invoice: currentMonthInvoices) {
			for (InvoiceItem invoiceItem : invoice.getInvoiceItems()) {
				invoiceItem.setCommissionStatus(CommissionStatus.Verified);
			}
		}
		InvoiceSales fixture = InvoiceSales.make(currentMonthInvoices);
		fixture.setSystemUser(systemUser);
		systemUser.setCommissionRate(10.0);
		BigDecimal commissionVerified = fixture.getTotalSales().getCommissionVerified();
		BigDecimal expected = MoneyUtils.round(commissionVerified.multiply(new BigDecimal(systemUser.getCommissionRate() / 100)));
		// expected
		Assert.assertFalse(""+commissionVerified, MoneyUtils.ZERO_VALUE.equals(commissionVerified));
		// execute
		BigDecimal actual = fixture.getCommissionVPayable();
		// validate
		Assert.assertEquals(expected, actual);
	}
}
