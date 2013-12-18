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
import org.springframework.beans.factory.annotation.Autowired;

import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.persistence.DaoSupport;
import ca.travelagency.persistence.DatabaseTester;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Criteria;

import com.google.common.collect.Lists;

public class CustomerPersistenceTest extends DatabaseTester<Customer> {
	private SystemUser systemUser;

	@Autowired
	private DaoSupport<SystemUser> systemUserDaoSupport;
	@Autowired
	private CustomerService customerService;

	@Before
	public void setUp() throws Exception {
		systemUser = SystemUserHelper.makeSystemUserWithoutId();
		systemUserDaoSupport.persist(systemUser);
	}

	@Test
	public void testPersistence() throws Exception {
		// setup
		Customer expected = CustomerHelper.makeCustomerWithoutId();
		persist(expected);
		// execute
		Customer actual = find(expected.getClass(), expected.getId());
		// validate
		Assert.assertEquals(expected, actual);
		Assert.assertNotSame(expected, actual);
	}

	@Test
	public void testSystemUserRelationship() throws Exception {
		// setup
		Customer customer = CustomerHelper.makeCustomerWithoutId();
		customer.setSystemUser(systemUser);
		persist(customer);

		// execute
		Customer foundCustomer = find(customer.getClass(), customer.getId());

		// validate
		Assert.assertEquals(systemUser, customer.getSystemUser());
		Assert.assertEquals(systemUser, foundCustomer.getSystemUser());
		Assert.assertNotSame(customer.getSystemUser(), foundCustomer.getSystemUser());
	}


	@Test
	public void testSystemUserRelationshipUsingCriteria() throws Exception {
		// setup
		Customer customer = CustomerHelper.makeCustomerWithoutId();
		customer.setSystemUser(systemUser);
		persist(customer);

		Condition condition = Condition.equals(Customer.Properties.systemUser.name(), systemUser);
		Criteria criteria = Criteria.make(Customer.class);
		criteria.addAndConditions(Lists.newArrayList(condition));

		// execute
		List<Customer> customers = find(criteria);

		// validate
		Assert.assertEquals(1, customers.size());
		Assert.assertEquals(customer, customers.get(0));
	}

	@Test
	public void testDuplicatedCustomer() throws Exception {
		// setup
		Customer customer = CustomerHelper.makeCustomerWithoutId();
		customer.setSystemUser(systemUser);
		persist(customer);

		Customer differntCustomerWithSameInfo = CustomerHelper.makeCustomerWithoutId();
		differntCustomerWithSameInfo.setFirstName(customer.getFirstName());
		differntCustomerWithSameInfo.setLastName(customer.getLastName());
		differntCustomerWithSameInfo.setPrimaryPhone(customer.getPrimaryPhone());
		differntCustomerWithSameInfo.setDateOfBirth(customer.getDateOfBirth());
		// execute & validate
		Assert.assertFalse(customerService.duplicated(customer));
		Assert.assertTrue(customerService.duplicated(differntCustomerWithSameInfo));
	}
}
