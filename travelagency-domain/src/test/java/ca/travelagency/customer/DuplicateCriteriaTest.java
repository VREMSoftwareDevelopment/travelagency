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

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Condition.Operator;
import ca.travelagency.persistence.query.Criteria;


public class DuplicateCriteriaTest {
	@Test
	public void testCriteria() throws Exception {
		// setup
		Customer customer = CustomerHelper.makeCustomer();
		customer.setDateOfBirth(new Date());
		DuplicateCriteria fixture = DuplicateCriteria.make(customer);

		Condition lastNameCondition = Condition.equals(Customer.Properties.lastName.name(), customer.getLastName());
		Condition firstNameCondition = Condition.equals(Customer.Properties.firstName.name(), customer.getFirstName());
		Condition primaryPhoneNumberCondition = Condition.equalsPhoneNumber(Customer.Properties.primaryPhone.name(), customer.getPrimaryPhone());
		Condition dateOfBirthCondition = Condition.equals(Customer.Properties.dateOfBirth.name(), customer.getDateOfBirth());
		Condition idCondition = Condition.make(DaoEntity.PROPERTY_ID, Operator.NOTEQUAL, customer.getId());
		// execute
		Criteria criteria = fixture.getCriteria();
		// validate
		Assert.assertEquals(Customer.class, criteria.getClazz());
		Assert.assertTrue(criteria.getConditions().contains(lastNameCondition));
		Assert.assertTrue(criteria.getConditions().contains(firstNameCondition));
		Assert.assertTrue(criteria.getConditions().contains(primaryPhoneNumberCondition));
		Assert.assertTrue(criteria.getConditions().contains(dateOfBirthCondition));
		Assert.assertTrue(criteria.getConditions().contains(idCondition));
	}
}
