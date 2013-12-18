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
package ca.travelagency.components.dataprovider;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.OrderBy;

public class FilterHelperTest {
	private static final OrderBy ORDER_BY_ID = OrderBy.make(DaoEntity.PROPERTY_ID);
	private static final OrderBy ORDER_BY_NAME = OrderBy.make(DaoEntity.PROPERTY_NAME);


	@Test
	public void testWhenOrderByIsNullHelperDefaultOrderByIsUsed() {
		// execute
		List<OrderBy> orderBy = FilterHelper.getOrderBy(null);
		// validate
		Assert.assertEquals(2, orderBy.size());
		Assert.assertEquals(ORDER_BY_NAME, orderBy.get(0));
		Assert.assertEquals(ORDER_BY_ID, orderBy.get(1));
	}

	@Test
	public void testWhenOrderByIsNullGivenDefaultOrderByIsUsed() {
		// execute
		List<OrderBy> orderBy = FilterHelper.getOrderBy(null, ORDER_BY_NAME);
		// validate
		Assert.assertEquals(2, orderBy.size());
		Assert.assertEquals(ORDER_BY_NAME, orderBy.get(0));
		Assert.assertEquals(ORDER_BY_ID, orderBy.get(1));
	}

	@Test
	public void testGivenOrderByIsUsed() {
		// setup
		OrderBy expected1 = OrderBy.make("MY FIELD", false);
		OrderBy expected2 = OrderBy.make(DaoEntity.PROPERTY_ID, false);
		// execute
		List<OrderBy> orderBy = FilterHelper.getOrderBy(expected1, ORDER_BY_NAME);
		// validate
		Assert.assertEquals(2, orderBy.size());
		Assert.assertEquals(expected1, orderBy.get(0));
		Assert.assertEquals(expected2, orderBy.get(1));
	}

	@Test
	public void testGivenOrderByIdAscIsNotDuplicated() {
		// execute
		List<OrderBy> orderBy = FilterHelper.getOrderBy(ORDER_BY_ID);
		// validate
		Assert.assertEquals(1, orderBy.size());
		Assert.assertEquals(ORDER_BY_ID, orderBy.get(0));
	}

	@Test
	public void testGivenOrderByIdDescIsNotDuplicated() {
		// setup
		OrderBy expected = OrderBy.make(DaoEntity.PROPERTY_ID, false);
		// execute
		List<OrderBy> orderBy = FilterHelper.getOrderBy(expected);
		// validate
		Assert.assertEquals(1, orderBy.size());
		Assert.assertEquals(expected, orderBy.get(0));
	}
}
