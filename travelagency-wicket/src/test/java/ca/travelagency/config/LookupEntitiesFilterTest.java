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
package ca.travelagency.config;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.persistence.query.OrderBy;

public class LookupEntitiesFilterTest {
	private static final OrderBy ORDER_BY_ID = OrderBy.make(DaoEntity.PROPERTY_ID);
	private static final OrderBy ORDER_BY_NAME = OrderBy.make(DaoEntity.PROPERTY_NAME);

	private static final String BIG_NAME_LOWER = "big name";
	private static final String BIG_NAME_EXPECTED = "Big Name";
	private static final String BIG_NAME_UPPER = "BIG NAME";

	private LookupEntitiesFilter fixture;

	@Before
	public void setUp() {
		fixture = new LookupEntitiesFilter(City.class);
	}

	private void validateCamelCase() {
		Assert.assertEquals(BIG_NAME_EXPECTED, fixture.getName());
	}

	@Test
	public void testFromUpperToCamelCase() {
		fixture.setName(BIG_NAME_UPPER);

		validateCamelCase();
	}

	@Test
	public void testFromLowerToCamelCase() {
		fixture.setName(BIG_NAME_LOWER);

		validateCamelCase();
	}

	@Test
	public void testNoName() {
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		Assert.assertTrue(criteria.getAndConditions().isEmpty());
		Assert.assertTrue(criteria.getOrConditions().isEmpty());
	}

	@Test
	public void testWithName() {
		// setup
		String value = "My Last Name";
		fixture.setName(value);
		Condition condition = Condition.likeCapitalize(DaoEntity.PROPERTY_NAME, value);
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		List<Condition> andConditions = criteria.getAndConditions();
		Assert.assertEquals(1, andConditions.size());
		Assert.assertEquals(condition, andConditions.get(0));
		Assert.assertTrue(criteria.getOrConditions().isEmpty());
	}

	@Test
	public void testOrderBy() {
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		List<OrderBy> orderBy = criteria.getOrderBy();
		Assert.assertEquals(2, orderBy.size());
		Assert.assertEquals(ORDER_BY_NAME, orderBy.get(0));
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
