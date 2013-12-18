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
package ca.travelagency.persistence.query;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.travelagency.utils.StringUtils;

import com.google.common.collect.Lists;

public class CriteriaTest {

	private Criteria fixture;

	@Before
	public void setUp() {
		fixture = Criteria.make(Criteria.class);
	}

	@Test
	public void testOrderByWithNone() {
		String actual = fixture.getOrderByAsSql("mytag");
		Assert.assertEquals(StringUtils.EMPTY, actual);
	}

	@Test
	public void testOrderBySqlWithOne() {
		fixture.addOrderBy(OrderBy.make("property"));
		String actual = fixture.getOrderByAsSql("mytag");
		Assert.assertEquals(" ORDER BY mytag.property ASC", actual);
	}

	@Test
	public void testOrderByWithMany() {
		fixture.addOrderBy(OrderBy.make("propertyC"));
		fixture.addOrderBy(OrderBy.make("propertyB", false));
		fixture.addOrderBy(OrderBy.make("propertyA"));
		String actual = fixture.getOrderByAsSql("mytag");
		Assert.assertEquals(
				" ORDER BY mytag.propertyC ASC, mytag.propertyB DESC, mytag.propertyA ASC",
				actual);
	}

	@Test
	public void testGroupByWithNone() {
		Assert.assertEquals(StringUtils.EMPTY, fixture.getGroupByAsSql("mytag"));
	}

	@Test
	public void testGroupBySqlWithOne() {
		fixture.setGroupBy("property");
		Assert.assertEquals(" GROUP BY mytag.property", fixture.getGroupByAsSql("mytag"));
	}

	@Test
	public void testConditionsWhere() {
		fixture.addAndConditions(Lists.newArrayList(Condition.like("property", "value1")));
		fixture.addOrConditions(Lists.newArrayList(Condition.like("property", "value2")));
		Where where = fixture.where("mytag");
		Assert.assertEquals(
				" WHERE mytag.property LIKE ?0 AND mytag.property LIKE ?1",
				where.getWhereAsSql());
		Assert.assertEquals("value1%", where.getParameters().get(0));
		Assert.assertEquals("value2%", where.getParameters().get(1));
	}

	@Test
	public void testConditionsFrom() {
		fixture.addAndConditions(Lists.newArrayList(Condition.equals("property.next", true).setInnerJoin()));
		String actual = fixture.from("mytag");
		Assert.assertEquals(",  IN(mytag.property) property", actual);
	}

	@Test
	public void testAddConditions() {
		List<Condition> andConditions = Lists.newArrayList(Condition.like("property", "value1"));
		fixture.addAndConditions(andConditions);
		List<Condition> orConditions = Lists.newArrayList(Condition.like("property", "value2"));
		fixture.addOrConditions(orConditions);
		Assert.assertEquals(andConditions, fixture.getAndConditions());
		Assert.assertEquals(orConditions, fixture.getOrConditions());
	}
	@Test
	public void testAddCondition() {
		Condition andCondition = Condition.like("property", "value1");
		fixture.addAndCondition(andCondition);
		Condition orCondition = Condition.like("property", "value2");
		fixture.addOrCondition(orCondition);
		Assert.assertEquals(andCondition, fixture.getAndConditions().get(0));
		Assert.assertEquals(orCondition, fixture.getOrConditions().get(0));
	}
}
