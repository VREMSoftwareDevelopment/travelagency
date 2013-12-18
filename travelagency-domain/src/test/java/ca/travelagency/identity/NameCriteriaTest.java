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
package ca.travelagency.identity;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Condition.Operator;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.persistence.query.OrderBy;


public class NameCriteriaTest {
	private NameCriteria fixture;
	private String name;

	@Before
	public void setUp() throws Exception {
		name = "Any Name";
		fixture = NameCriteria.make(name);
	}

	@Test
	public void testCondition() throws Exception {
		// setup
		Condition expected = Condition.make(SystemUser.PROPERTY_NAME, Operator.EQUAL, name);
		// execute
		Criteria actual = fixture.getCriteria();
		// validate
		List<Condition> orConditions = actual.getOrConditions();
		Assert.assertTrue(orConditions.isEmpty());

		List<OrderBy> orderBy = actual.getOrderBy();
		Assert.assertTrue(orderBy.isEmpty());

		List<Condition> andConditions = actual.getAndConditions();
		Assert.assertEquals(1, andConditions.size());
		Assert.assertEquals(expected, andConditions.get(0));
	}
}
