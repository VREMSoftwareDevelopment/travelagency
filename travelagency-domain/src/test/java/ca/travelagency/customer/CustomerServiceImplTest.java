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

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ca.travelagency.persistence.DaoSupport;

import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceImplTest {
	@Mock
	private DaoSupport<Customer> daoSupport;

	private CustomerServiceImpl fixture;
	private Customer customer;

	private DuplicateCriteria duplicateCriteria;

	@Before
	public void setUp() {
		fixture = new CustomerServiceImpl();
		fixture.setDaoSupport(daoSupport);

		customer = CustomerHelper.makeCustomer();
		duplicateCriteria = DuplicateCriteria.make(customer);
	}

	@Test
	public void testCustomerDuplicated() throws Exception {
		Mockito.when(daoSupport.find(duplicateCriteria)).thenReturn(Lists.newArrayList(customer));
		Assert.assertTrue(fixture.duplicated(customer));
		Mockito.verify(daoSupport).find(duplicateCriteria);
	}

	@Test
	public void testCustomerNotDuplicated() throws Exception {
		Mockito.when(daoSupport.find(duplicateCriteria)).thenReturn(new ArrayList<Customer>());
		Assert.assertFalse(fixture.duplicated(customer));
		Mockito.verify(daoSupport).find(duplicateCriteria);
	}

}
