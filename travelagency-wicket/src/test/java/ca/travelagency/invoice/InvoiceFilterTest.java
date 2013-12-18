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

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.customer.Customer;
import ca.travelagency.customer.CustomerHelper;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Condition.Operator;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.persistence.query.OrderBy;
import ca.travelagency.utils.DateUtils;
import ca.travelagency.utils.MoneyUtils;
import ca.travelagency.utils.StringUtils;

public class InvoiceFilterTest extends BaseWicketTester {
	private static final OrderBy ORDER_BY_ID = OrderBy.make(DaoEntity.PROPERTY_ID);

	private InvoiceFilter fixture;

	@Before
	public void setUp() {
		fixture = new InvoiceFilter();
	}

	@Test
	public void testNoConditions() {
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		Assert.assertEquals(Invoice.class, criteria.getClazz());
		Assert.assertTrue(criteria.getAndConditions().isEmpty());
		Assert.assertTrue(criteria.getOrConditions().isEmpty());
	}

	@Test
	public void testWithSystemUser() {
		// setup
		SystemUser systemUser = SystemUserHelper.makeSystemUser();
		fixture.setSystemUser(systemUser);
		Condition condition = Condition.equals(Invoice.Properties.systemUser.name(), systemUser);
		// expected
		Mockito.when(daoSupport.find(SystemUser.class, systemUser.getId())).thenReturn(systemUser);
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		List<Condition> actual = criteria.getAndConditions();
		Assert.assertEquals(1, actual.size());
		Assert.assertEquals(condition, actual.get(0));
		Assert.assertTrue(criteria.getOrConditions().isEmpty());
		Mockito.verify(daoSupport).find(SystemUser.class, systemUser.getId());
	}

	@Test
	public void testWithCustomer() {
		// setup
		Customer customer = CustomerHelper.makeCustomer();
		fixture.setCustomer(customer);
		Condition condition = Condition.equals(Invoice.Properties.customer.name(), customer);
		// expected
		Mockito.when(daoSupport.find(Customer.class, customer.getId())).thenReturn(customer);
		// execute
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		List<Condition> actual = criteria.getAndConditions();
		Assert.assertEquals(1, actual.size());
		Assert.assertEquals(condition, actual.get(0));
		Assert.assertTrue(criteria.getOrConditions().isEmpty());
		Mockito.verify(daoSupport).find(Customer.class, customer.getId());
	}

	@Test
	public void testWithInvoiceDateFrom() {
		// setup
		Date date = new Date();
		fixture.setInvoiceDateFrom(date);
		Condition condition = Condition.make(Invoice.Properties.date.name(), Operator.GREATEROREQUAL, date);
		// execute
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		List<Condition> actual = criteria.getAndConditions();
		Assert.assertEquals(1, actual.size());
		Assert.assertEquals(condition, actual.get(0));
		Assert.assertTrue(criteria.getOrConditions().isEmpty());
	}

	@Test
	public void testWithInvoiceDateTo() {
		// setup
		Date date = new Date();
		fixture.setInvoiceDateTo(date);
		Condition condition = Condition.make(Invoice.Properties.date.name(), Operator.LESS, DateUtils.addDays(date, 1));
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		List<Condition> actual = criteria.getAndConditions();
		Assert.assertEquals(1, actual.size());
		Assert.assertEquals(condition, actual.get(0));
		Assert.assertTrue(criteria.getOrConditions().isEmpty());
	}

	@Test
	public void testShowUnpaidInvoicesOnly() {
		// setup
		fixture.setShowUnpaidInvoicesOnly(true);
		Condition condition = Condition.make(Invoice.Properties.totalDue.name(), Operator.GREATER, MoneyUtils.ZERO_VALUE);
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		List<Condition> actual = criteria.getAndConditions();
		Assert.assertEquals(1, actual.size());
		Assert.assertEquals(condition, actual.get(0));
		Assert.assertTrue(criteria.getOrConditions().isEmpty());
	}

	@Test
	public void testWithInvoiceStatus() {
		// setup
		InvoiceStatus status = InvoiceStatus.Active;
		fixture.setStatus(status);
		Condition condition = Condition.equals(Invoice.Properties.status.name(), status);
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		List<Condition> actual = criteria.getAndConditions();
		Assert.assertEquals(1, actual.size());
		Assert.assertEquals(condition, actual.get(0));
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
		Assert.assertEquals(14, actual.size());

		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoiceFilter.INVOICE_TRAVELER+InvoiceTraveler.Properties.firstName, value).setInnerJoin()));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoiceFilter.INVOICE_TRAVELER+InvoiceTraveler.Properties.lastName, value).setInnerJoin()));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoiceFilter.INVOICE_TRAVELER+InvoiceTraveler.Properties.documentNumber, StringUtils.upperCase(value)).setInnerJoin()));

		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoiceFilter.INVOICE_ITEM+InvoiceItem.Properties.description, value).setInnerJoin()));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoiceFilter.INVOICE_ITEM+InvoiceItem.Properties.supplier, value).setInnerJoin()));

		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoiceFilter.INVOICE_CUSTOMER+Customer.Properties.firstName, value)));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoiceFilter.INVOICE_CUSTOMER+Customer.Properties.lastName, value)));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoiceFilter.INVOICE_CUSTOMER+Customer.Properties.companyName, value)));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoiceFilter.INVOICE_CUSTOMER+Customer.Properties.address, value)));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoiceFilter.INVOICE_CUSTOMER+Customer.Properties.city, value)));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoiceFilter.INVOICE_CUSTOMER+Customer.Properties.province, value)));
		Assert.assertTrue(actual.contains(Condition.like(InvoiceFilter.INVOICE_CUSTOMER+Customer.Properties.postalCode, StringUtils.upperCase(value))));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoiceFilter.INVOICE_CUSTOMER+Customer.Properties.country, value)));
		Assert.assertTrue(actual.contains(Condition.like(InvoiceFilter.INVOICE_CUSTOMER+Customer.Properties.email, value)));

		Assert.assertTrue(criteria.getAndConditions().isEmpty());
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
		Assert.assertEquals(17, actual.size());
		Assert.assertTrue(actual.contains(Condition.equals(Invoice.PROPERTY_ID, new Long(value))));
		Assert.assertTrue(actual.contains(Condition.likePhoneNumber(InvoiceFilter.INVOICE_CUSTOMER+Customer.Properties.primaryPhone, value)));
		Assert.assertTrue(actual.contains(Condition.likePhoneNumber(InvoiceFilter.INVOICE_CUSTOMER+Customer.Properties.secondaryPhone, value)));

		Assert.assertTrue(criteria.getAndConditions().isEmpty());
	}

	@Test
	public void testDefaultOrderBy() {
		// setup
		OrderBy expected = OrderBy.make(DaoEntity.PROPERTY_ID, fixture.getDefaultOrderBy().isAscending());
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		List<OrderBy> orderBy = criteria.getOrderBy();
		Assert.assertEquals(2, orderBy.size());
		Assert.assertEquals(fixture.getDefaultOrderBy(), orderBy.get(0));
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
}
