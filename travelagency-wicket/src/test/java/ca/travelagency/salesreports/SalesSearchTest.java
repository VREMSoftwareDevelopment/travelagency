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
package ca.travelagency.salesreports;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceItem;
import ca.travelagency.invoice.InvoicePayment;
import ca.travelagency.invoice.InvoiceStatus;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Condition.Operator;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.persistence.query.OrderBy;
import ca.travelagency.utils.DateUtils;
import ca.travelagency.utils.MoneyUtils;
import ca.travelagency.utils.StringUtils;

public class SalesSearchTest extends BaseWicketTester {
	private static final String PREFIX = "prefix";
	private SalesSearch fixture;
	private SystemUser systemUser;

	@Before
	public void setUp() {
		fixture = SalesSearch.make(null);
		systemUser = SystemUserHelper.makeSystemUser();
	}

	@Test
	public void testDefaultsDates() {
		// setup
		Date expectedDateFrom = DateUtils.truncate(new Date(), Calendar.MONTH);
		Date expectedDateTo = DateUtils.addDays(DateUtils.addMonths(expectedDateFrom, 1), -1);
		// validate
		Assert.assertEquals(expectedDateFrom, fixture.getDateFrom());
		Assert.assertEquals(expectedDateTo, fixture.getDateTo());
	}

	@Test
		public void testSetDateFromBy6Month() {
			// setup
			Date expectedDateFrom = DateUtils.addDays(DateUtils.addMonths(fixture.getDateTo(), -6), 1);
			// execute
			fixture.setDateFromBy6Month();
			// validate
			Assert.assertEquals(expectedDateFrom, fixture.getDateFrom());
		}

	@Test
	public void testInvoiceDatesConditions() {
		// setup
		Condition expectedDateFrom = Condition.make(PREFIX+Invoice.Properties.date.name(), Operator.GREATEROREQUAL, fixture.getDateFrom());
		Condition expectedDateTo = Condition.make(PREFIX+Invoice.Properties.date.name(), Operator.LESS, DateUtils.addDays(fixture.getDateTo(), 1));
		// execute
		List<Condition> actual = fixture.getInvoiceConditions(PREFIX);
		// validate
		Assert.assertTrue(actual.contains(expectedDateFrom));
		Assert.assertTrue(actual.contains(expectedDateTo));
	}

	@Test
	public void testInvoiceStatusCondition() {
		// setup
		Condition expected = Condition.make(PREFIX+Invoice.Properties.status.name(), Operator.NOTEQUAL, InvoiceStatus.Voided);
		// execute
		List<Condition> actual = fixture.getInvoiceConditions(PREFIX);
		// validate
		Assert.assertTrue(actual.contains(expected));
	}

	@Test
	public void testOrderBy() {
		// setup
		OrderBy expectedOrderByDate = OrderBy.make(Invoice.Properties.date.name());
		OrderBy expectedOrderById = OrderBy.make(DaoEntity.PROPERTY_ID);
		// execute
		List<OrderBy> actual = fixture.getOrderBy();
		// validate
		Assert.assertEquals(2, actual.size());
		Assert.assertEquals(expectedOrderByDate, actual.get(0));
		Assert.assertEquals(expectedOrderById, actual.get(1));
	}

	@Test
	public void testSystemUserCondition() {
		// setup
		fixture = SalesSearch.make(systemUser);

		Condition expectedSystemUserCondition = Condition.equals(PREFIX+Invoice.Properties.systemUser.name(), systemUser);
		// expected
		Mockito.when(daoSupport.find(SystemUser.class, systemUser.getId())).thenReturn(systemUser);
		// execute
		List<Condition> actual = fixture.getInvoiceConditions(PREFIX);
		// validate
		Assert.assertTrue(actual.contains(expectedSystemUserCondition));
		Mockito.verify(daoSupport).find(SystemUser.class, systemUser.getId());
	}

	@Test
	public void testSetShowUnpaidInvoicesOnly() {
		// setup
		fixture.setShowUnpaidInvoicesOnly();
		Condition expected = Condition.make(PREFIX+Invoice.Properties.totalDue.name(), Operator.GREATER, MoneyUtils.ZERO_VALUE);
		// execute
		List<Condition> actual = fixture.getInvoiceConditions(PREFIX);
		// validate
		Assert.assertTrue(actual.contains(expected));
	}

	@Test
	public void testNoSystemUserCondition() {
		// setup
		Condition systemUserCondition = Condition.equals(PREFIX+Invoice.Properties.systemUser.name(), systemUser);
		// execute
		List<Condition> actual = fixture.getInvoiceConditions(PREFIX);
		// validate
		Assert.assertFalse(actual.contains(systemUserCondition));
		Mockito.verify(daoSupport, Mockito.never()).find(SystemUser.class, systemUser.getId());
	}

	@Test
	public void testInvoiceCriteria() throws Exception {
		// execute
		Criteria criteria = fixture.getInvoiceCriteria();
		// validate
		Assert.assertEquals(Invoice.class, criteria.getClazz());

		Assert.assertEquals(2, criteria.getOrderBy().size());
		Assert.assertEquals(fixture.getOrderBy(), criteria.getOrderBy());

		Assert.assertEquals(3, criteria.getConditions().size());
		Assert.assertEquals(fixture.getInvoiceConditions(StringUtils.EMPTY), criteria.getConditions());
	}

	@Test
	public void testInvoiceItemCriteriaClass() throws Exception {
		// execute
		String prefix = InvoiceItem.Properties.invoice.name()+Condition.SEPARATOR;
		Criteria criteria = fixture.getInvoiceItemCriteria();
		// validate
		Assert.assertEquals(InvoiceItem.class, criteria.getClazz());

		Assert.assertTrue(criteria.getOrderBy().isEmpty());

		Assert.assertEquals(3, criteria.getConditions().size());
		Assert.assertEquals(fixture.getInvoiceConditions(prefix), criteria.getConditions());
	}

	@Test
	public void testInvoicePaymentDatesConditions() {
		// setup
		Condition expectedDateFrom = Condition.make(InvoicePayment.Properties.date.name(), Operator.GREATEROREQUAL, fixture.getDateFrom());
		Condition expectedDateTo = Condition.make(InvoicePayment.Properties.date.name(), Operator.LESS, DateUtils.addDays(fixture.getDateTo(), 1));
		// execute
		List<Condition> actual = fixture.getPaymentConditions(PREFIX);
		// validate
		Assert.assertTrue(actual.contains(expectedDateFrom));
		Assert.assertTrue(actual.contains(expectedDateTo));
	}

	@Test
	public void testInvoicePaymentCriteriaClass() throws Exception {
		// execute
		String prefix = InvoicePayment.Properties.invoice.name()+Condition.SEPARATOR;
		Criteria criteria = fixture.getInvoicePaymentCriteria();
		// validate
		Assert.assertEquals(InvoicePayment.class, criteria.getClazz());

		Assert.assertTrue(criteria.getOrderBy().isEmpty());

		Assert.assertEquals(3, criteria.getConditions().size());
		Assert.assertEquals(fixture.getPaymentConditions(prefix), criteria.getConditions());
	}
}
