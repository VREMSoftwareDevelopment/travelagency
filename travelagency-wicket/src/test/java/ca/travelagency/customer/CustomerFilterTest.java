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
package ca.travelagency.customer;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.persistence.query.OrderBy;
import ca.travelagency.utils.StringUtils;


public class CustomerFilterTest {
	private static final OrderBy ORDER_BY_ID = OrderBy.make(DaoEntity.PROPERTY_ID);

	private CustomerFilter fixture;

	@Before
	public void setUp() {
		fixture = new CustomerFilter();
	}

	@Test
	public void testNoConditions() {
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		Assert.assertEquals(Customer.class, criteria.getClazz());
		Assert.assertTrue(criteria.getAndConditions().isEmpty());
		Assert.assertTrue(criteria.getOrConditions().isEmpty());
	}

	@Test
	public void testWithSystemUser() {
		// setup
		SystemUser systemUser = SystemUserHelper.makeSystemUser();
		fixture.setSystemUser(systemUser);
		Condition condition = Condition.equals(Customer.Properties.systemUser.name(), systemUser);
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		List<Condition> actual = criteria.getAndConditions();
		Assert.assertTrue(actual.contains(condition));
	}

	@Test
	public void testWithCustomerStatus() {
		// setup
		CustomerStatus status = CustomerStatus.Active;
		fixture.setStatus(status);
		Condition condition = Condition.equals(Customer.Properties.status.name(), status);
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		List<Condition> actual = criteria.getAndConditions();
		Assert.assertTrue(actual.contains(condition));
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
		Assert.assertEquals(9, actual.size());
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(Customer.Properties.firstName.name(), value)));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(Customer.Properties.lastName.name(), value)));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(Customer.Properties.companyName.name(), value)));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(Customer.Properties.address.name(), value)));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(Customer.Properties.city.name(), value)));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(Customer.Properties.province.name(), value)));
		Assert.assertTrue(actual.contains(Condition.like(Customer.Properties.postalCode.name(), StringUtils.upperCase(value))));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(Customer.Properties.country.name(), value)));
		Assert.assertTrue(actual.contains(Condition.like(Customer.Properties.email.name(), value)));
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
		Assert.assertEquals(11, actual.size());
		Assert.assertTrue(actual.contains(Condition.likePhoneNumber(Customer.Properties.primaryPhone.name(), value)));
		Assert.assertTrue(actual.contains(Condition.likePhoneNumber(Customer.Properties.secondaryPhone.name(), value)));
	}

	@Test
	public void testDefaultOrderBy() {
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		List<OrderBy> orderBy = criteria.getOrderBy();
		Assert.assertEquals(2, orderBy.size());
		Assert.assertEquals(CustomerFilter.DEFAULT_ORDER_BY, orderBy.get(0));
		Assert.assertEquals(ORDER_BY_ID, orderBy.get(1));
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
