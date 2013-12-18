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
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.invoice.CommissionStatus;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceItem;
import ca.travelagency.invoice.InvoiceTraveler;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Condition.Operator;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.persistence.query.OrderBy;
import ca.travelagency.utils.DateUtils;
import ca.travelagency.utils.StringUtils;


public class InvoiceItemFilterTest extends BaseWicketTester {
	private static final OrderBy ORDER_BY_ID = OrderBy.make(DaoEntity.PROPERTY_ID);

	private InvoiceItemFilter fixture;

	@Before
	public void setUp() {
		fixture = new InvoiceItemFilter();
	}

	@Test
	public void testNoConditions() {
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		Assert.assertEquals(InvoiceItem.class, criteria.getClazz());
		Assert.assertTrue(criteria.getAndConditions().isEmpty());
		Assert.assertTrue(criteria.getOrConditions().isEmpty());
	}

	@Test
	public void testWithInvoiceDateFrom() {
		// setup
		Date date = new Date();
		fixture.setInvoiceDateFrom(date);
		Condition condition = Condition.make(InvoiceItemFilter.INVOICE_PATH+Invoice.Properties.date.name(), Operator.GREATEROREQUAL, date);
		// execute
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		Assert.assertEquals(1, criteria.getAndConditions().size());
		Assert.assertEquals(condition, criteria.getAndConditions().get(0));
		Assert.assertTrue(criteria.getOrConditions().isEmpty());
	}

	@Test
	public void testWithInvoiceDateTo() {
		// setup
		Date date = new Date();
		fixture.setInvoiceDateTo(date);
		Condition condition = Condition.make(InvoiceItemFilter.INVOICE_PATH+Invoice.Properties.date.name(), Operator.LESS, DateUtils.addDays(date, 1));
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		Assert.assertEquals(1, criteria.getAndConditions().size());
		Assert.assertEquals(condition, criteria.getAndConditions().get(0));
		Assert.assertTrue(criteria.getOrConditions().isEmpty());
	}

	@Test
	public void testCommissionStatus() {
		// setup
		fixture.setCommissionStatus(CommissionStatus.Received);
		Condition condition = Condition.equals(InvoiceItem.Properties.commissionStatus.name(), CommissionStatus.Received);
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		Assert.assertEquals(1, criteria.getAndConditions().size());
		Assert.assertEquals(condition, criteria.getAndConditions().get(0));
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
		Assert.assertEquals(5, actual.size());

		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoiceItemFilter.INVOICE_TRAVELER_PATH+InvoiceTraveler.Properties.firstName.name(), value).setInnerJoin()));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoiceItemFilter.INVOICE_TRAVELER_PATH+InvoiceTraveler.Properties.lastName.name(), value).setInnerJoin()));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoiceItemFilter.INVOICE_TRAVELER_PATH+InvoiceTraveler.Properties.documentNumber.name(), StringUtils.upperCase(value)).setInnerJoin()));

		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoiceItem.Properties.supplier.name(), value)));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(InvoiceItem.Properties.description.name(), value)));
	}

	@Test
	public void testDefaultOrderBy() {
		// setup
		OrderBy expected = OrderBy.make(DaoEntity.PROPERTY_ID, InvoiceItemFilter.DEFAULT_ORDER_BY.isAscending());
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		List<OrderBy> orderBy = criteria.getOrderBy();
		Assert.assertEquals(2, orderBy.size());
		Assert.assertEquals(InvoiceItemFilter.DEFAULT_ORDER_BY, orderBy.get(0));
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
		Condition condition = Condition.equals(InvoiceItemFilter.INVOICE_PATH+Invoice.Properties.systemUser.name(), systemUser);
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
}
