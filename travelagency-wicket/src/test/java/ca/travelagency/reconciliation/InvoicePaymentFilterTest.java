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
package ca.travelagency.reconciliation;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.customer.Customer;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoicePayment;
import ca.travelagency.invoice.InvoiceTraveler;
import ca.travelagency.invoice.PaymentType;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Condition.Operator;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.persistence.query.OrderBy;
import ca.travelagency.utils.DateUtils;
import ca.travelagency.utils.StringUtils;


public class InvoicePaymentFilterTest extends BaseWicketTester {
	private static final OrderBy ORDER_BY_ID = OrderBy.make(DaoEntity.PROPERTY_ID);

	private InvoicePaymentFilter fixture;

	@Before
	public void setUp() {
		fixture = new InvoicePaymentFilter();
	}

	@Test
	public void testNoConditions() {
		// setup
		Condition condition = Condition.equals(InvoicePayment.Properties.reconciled.name(), Boolean.FALSE);
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		Assert.assertEquals(InvoicePayment.class, criteria.getClazz());
		Assert.assertEquals(1, criteria.getAndConditions().size());
		Assert.assertEquals(condition, criteria.getAndConditions().get(0));
		Assert.assertTrue(criteria.getOrConditions().isEmpty());
	}

	@Test
	public void testWithPaymentDateFrom() {
		// setup
		Date date = new Date();
		fixture.setPaymentDateFrom(date);
		Condition condition = Condition.make(InvoicePayment.Properties.date.name(), Operator.GREATEROREQUAL, date);
		// execute
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		Assert.assertEquals(2, criteria.getAndConditions().size());
		Assert.assertEquals(condition, criteria.getAndConditions().get(1));
		Assert.assertTrue(criteria.getOrConditions().isEmpty());
	}

	@Test
	public void testWithPaymentDateTo() {
		// setup
		Date date = new Date();
		fixture.setPaymentDateTo(date);
		Condition condition = Condition.make(InvoicePayment.Properties.date.name(), Operator.LESS, DateUtils.addDays(date, 1));
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		Assert.assertEquals(2, criteria.getAndConditions().size());
		Assert.assertEquals(condition, criteria.getAndConditions().get(1));
		Assert.assertTrue(criteria.getOrConditions().isEmpty());
	}

	@Test
	public void testPaymentType() {
		// setup
		fixture.setPaymentType(PaymentType.Cash);
		Condition condition = Condition.equals(InvoicePayment.Properties.paymentType.name(), PaymentType.Cash);
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		Assert.assertEquals(2, criteria.getAndConditions().size());
		Assert.assertEquals(condition, criteria.getAndConditions().get(1));
		Assert.assertTrue(criteria.getOrConditions().isEmpty());
	}

	@Test
	public void testWithSearchText() {
		// setup
		String value = "searchText";
		fixture.setSearchText(value);
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		List<Condition> actual = criteria.getOrConditions();
		Assert.assertEquals(12, actual.size());

		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoicePaymentFilter.INVOICE_TRAVELER_PATH+InvoiceTraveler.Properties.firstName.name(), value).setInnerJoin()));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoicePaymentFilter.INVOICE_TRAVELER_PATH+InvoiceTraveler.Properties.lastName.name(), value).setInnerJoin()));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoicePaymentFilter.INVOICE_TRAVELER_PATH+InvoiceTraveler.Properties.documentNumber.name(), StringUtils.upperCase(value)).setInnerJoin()));

		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoicePaymentFilter.INVOICE_CUSTOMER_PATH+Customer.Properties.firstName, value)));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoicePaymentFilter.INVOICE_CUSTOMER_PATH+Customer.Properties.lastName, value)));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoicePaymentFilter.INVOICE_CUSTOMER_PATH+Customer.Properties.companyName, value)));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoicePaymentFilter.INVOICE_CUSTOMER_PATH+Customer.Properties.address, value)));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoicePaymentFilter.INVOICE_CUSTOMER_PATH+Customer.Properties.city, value)));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoicePaymentFilter.INVOICE_CUSTOMER_PATH+Customer.Properties.province, value)));
		Assert.assertTrue(actual.contains(Condition.like(InvoicePaymentFilter.INVOICE_CUSTOMER_PATH+Customer.Properties.postalCode, StringUtils.upperCase(value))));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoicePaymentFilter.INVOICE_CUSTOMER_PATH+Customer.Properties.country, value)));
		Assert.assertTrue(actual.contains(Condition.like(InvoicePaymentFilter.INVOICE_CUSTOMER_PATH+Customer.Properties.email, value)));
	}

	@Test
	public void testWithSearchTextNumeric() {
		// setup
		String value = "123";
		fixture.setSearchText(value);
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		List<Condition> actual = criteria.getOrConditions();
		Assert.assertEquals(15, actual.size());

		Assert.assertTrue(actual.contains(Condition.equals(InvoicePaymentFilter.INVOICE_PATH+Invoice.PROPERTY_ID, new Long(value))));
		Assert.assertTrue(actual.contains(Condition.likePhoneNumber(InvoicePaymentFilter.INVOICE_CUSTOMER_PATH+Customer.Properties.primaryPhone, value)));
		Assert.assertTrue(actual.contains(Condition.likePhoneNumber(InvoicePaymentFilter.INVOICE_CUSTOMER_PATH+Customer.Properties.secondaryPhone, value)));
	}

	@Test
	public void testDefaultOrderBy() {
		// setup
		OrderBy expected = OrderBy.make(DaoEntity.PROPERTY_ID, InvoicePaymentFilter.DEFAULT_ORDER_BY.isAscending());
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		List<OrderBy> orderBy = criteria.getOrderBy();
		Assert.assertEquals(2, orderBy.size());
		Assert.assertEquals(InvoicePaymentFilter.DEFAULT_ORDER_BY, orderBy.get(0));
		Assert.assertEquals(expected, orderBy.get(1));
	}

	@Test
	public void testGivenOrderBy() {
		// setup
		OrderBy expected = OrderBy.make("MY FIELD");
		// execute
		Criteria criteria = fixture.getCriteria(expected);
		// validate
		List<OrderBy> orderBy = criteria.getOrderBy();
		Assert.assertEquals(2, orderBy.size());
		Assert.assertEquals(expected, orderBy.get(0));
		Assert.assertEquals(ORDER_BY_ID, orderBy.get(1));
	}

	@Test
	public void testWithSystemUser() {
		// setup
		SystemUser systemUser = SystemUserHelper.makeSystemUser();
		fixture.setSystemUser(systemUser);
		Condition condition = Condition.equals(InvoicePaymentFilter.INVOICE_PATH+Invoice.Properties.systemUser.name(), systemUser);
		// expected
		Mockito.when(daoSupport.find(SystemUser.class, systemUser.getId())).thenReturn(systemUser);
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		List<Condition> actual = criteria.getAndConditions();
		Assert.assertEquals(2, actual.size());
		Assert.assertEquals(condition, actual.get(1));
		Assert.assertTrue(criteria.getOrConditions().isEmpty());
		Mockito.verify(daoSupport).find(SystemUser.class, systemUser.getId());
	}
}
