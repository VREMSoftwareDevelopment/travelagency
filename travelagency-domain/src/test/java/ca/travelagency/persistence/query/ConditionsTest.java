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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.travelagency.utils.StringUtils;

import com.google.common.collect.Lists;

public class ConditionsTest {

	private Conditions fixture;

	@Before
	public void setUp() {
		fixture = new Conditions();
	}

	@Test
	public void testWhereWithNoCondition() {
		Where where = fixture.where("mytag");
		Assert.assertEquals(StringUtils.EMPTY, where.getWhereAsSql());
		Assert.assertTrue(where.getParameters().isEmpty());
	}

	@Test
	public void testWhereWithOneAndCondition() {
		fixture.addAndConditions(Lists.newArrayList(Condition.like("property", "value")));
		Where where = fixture.where("mytag");
		Assert.assertEquals(" WHERE mytag.property LIKE ?0", where.getWhereAsSql());
		Assert.assertEquals("value%", where.getParameters().get(0));
	}

	@Test
	public void testWhereWithOneOrCondition() {
		fixture.addOrConditions(Lists.newArrayList(Condition.like("property", "value")));
		Where where = fixture.where("mytag");
		Assert.assertEquals(" WHERE mytag.property LIKE ?0", where.getWhereAsSql());
		Assert.assertEquals("value%", where.getParameters().get(0));
	}

	@Test
	public void testWhereWithManyAndConditions() {
		fixture.addAndConditions(Lists.newArrayList(
				Condition.like("property", "value"),
				Condition.equals("property", true),
				Condition.equals("property", 101)));
		Where where = fixture.where("mytag");
		Assert.assertEquals(
				" WHERE mytag.property LIKE ?0 AND mytag.property = ?1 AND mytag.property = ?2",
				where.getWhereAsSql());
		Assert.assertEquals("value%", where.getParameters().get(0));
		Assert.assertEquals(true, where.getParameters().get(1));
		Assert.assertEquals(101, where.getParameters().get(2));
	}

	@Test
	public void testWhereWithManyOrConditions() {
		fixture.addOrConditions(Lists.newArrayList(
				Condition.like("property", "value"),
				Condition.equals("property", true),
				Condition.equals("property", 101)));
		Where where = fixture.where("mytag");
		Assert.assertEquals(
				" WHERE mytag.property LIKE ?0 OR mytag.property = ?1 OR mytag.property = ?2",
				where.getWhereAsSql());
		Assert.assertEquals("value%", where.getParameters().get(0));
		Assert.assertEquals(true, where.getParameters().get(1));
		Assert.assertEquals(101, where.getParameters().get(2));
	}

	@Test
	public void testWhereWithOneAndOrCondition() {
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
	public void testWhereWithAndOrConditions() {
		fixture.addAndConditions(Lists.newArrayList(
				Condition.like("property", "value1"),
				Condition.equals("property", true),
				Condition.equals("property", 101)));
		fixture.addOrConditions(Lists.newArrayList(
				Condition.like("property", "value2"),
				Condition.equals("property", false),
				Condition.equals("property", 102)));
		Where where = fixture.where("mytag");
		Assert.assertEquals(
				" WHERE mytag.property LIKE ?0 AND mytag.property = ?1 AND mytag.property = ?2" +
				" AND (mytag.property LIKE ?3 OR mytag.property = ?4 OR mytag.property = ?5) ",
				where.getWhereAsSql());
		Assert.assertEquals("value1%", where.getParameters().get(0));
		Assert.assertEquals(true, where.getParameters().get(1));
		Assert.assertEquals(101, where.getParameters().get(2));
		Assert.assertEquals("value2%", where.getParameters().get(3));
		Assert.assertEquals(false, where.getParameters().get(4));
		Assert.assertEquals(102, where.getParameters().get(5));
	}

	@Test
	public void testWhereWithCollectionCondition() {
		fixture.addAndConditions(Lists.newArrayList(Condition.equals("number.property.next", true).setInnerJoin()));
		Where where = fixture.where("mytag");
		Assert.assertEquals(" WHERE property.next = ?0", where.getWhereAsSql());
	}

	@Test
	public void testFromWithSameCollectionCondition() {
		fixture.addAndConditions(
			Lists.newArrayList(
				Condition.equals("number.property.next", true).setInnerJoin(),
				Condition.equals("number.property.prev", false).setInnerJoin(),
				Condition.equals("number.property.other", false).setInnerJoin()
			)
		);
		String actual = fixture.from("mytag");
		Assert.assertEquals(",  IN(mytag.number.property) property", actual);
	}

	@Test
	public void testFromWithDifferentCollectionCondition() {
		fixture.addAndConditions(
				Lists.newArrayList(
					Condition.equals("number.property1.next", true).setInnerJoin(),
					Condition.equals("number.property1.prev", true).setInnerJoin(),
					Condition.equals("number.property3.next", false).setInnerJoin()
				)
			);
		fixture.addOrConditions(
				Lists.newArrayList(
					Condition.equals("number.property2.next", false).setInnerJoin(),
					Condition.equals("number.property2.prev", true).setInnerJoin()
				)
			);
		String actual = fixture.from("mytag");
		Assert.assertEquals(",  IN(mytag.number.property1) property1,  IN(mytag.number.property2) property2,  IN(mytag.number.property3) property3", actual);
	}
}
