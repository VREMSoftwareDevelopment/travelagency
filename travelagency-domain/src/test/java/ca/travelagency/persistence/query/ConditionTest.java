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
import org.junit.Test;

import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.persistence.query.Condition.Operator;


public class ConditionTest {

	@Test
	public void testEqualsTrue() {
		Condition condition1 = Condition.make("property", Operator.EQUAL, "value").setInnerJoin();
		Condition condition2 = Condition.make("property", Operator.EQUAL, "value").setInnerJoin();
		Assert.assertNotSame(condition1, condition2);
		Assert.assertEquals(condition1, condition2);
	}

	@Test
	public void testNoSeperator() {
		// setup
		String property = "property";
		// execute
		Condition condition = Condition.make(property, Operator.EQUAL, "value");
		// validate
		Assert.assertEquals(property, condition.getProperty());
		Assert.assertEquals(property, condition.getCollectionTag());
		Assert.assertEquals(property, condition.getCollectionProperty());
	}

	@Test
	public void testOneSeperator() {
		// setup
		String property = "property.next";
		// execute
		Condition condition = Condition.make(property, Operator.EQUAL, "value");
		// validate
		Assert.assertEquals(property, condition.getProperty());
		Assert.assertEquals(property, condition.getCollectionTag());
		Assert.assertEquals(property, condition.getCollectionProperty());
	}

	@Test
	public void testThreeSeperator() {
		// setup
		String property = "property.next.prev";
		// execute
		Condition condition = Condition.make(property, Operator.EQUAL, "value");
		// validate
		Assert.assertEquals(property, condition.getProperty());
		Assert.assertEquals(property, condition.getCollectionTag());
		Assert.assertEquals(property, condition.getCollectionProperty());
	}

	@Test
	public void testManySeperator() {
		// setup
		String property = "property.next.prev.current.final";
		// execute
		Condition condition = Condition.make(property, Operator.EQUAL, "value");
		// validate
		Assert.assertEquals(property, condition.getProperty());
		Assert.assertEquals(property, condition.getCollectionTag());
		Assert.assertEquals(property, condition.getCollectionProperty());
	}

	@Test
	public void testNoSeperatorWithInnerJoin() {
		// setup
		String property = "property";
		// execute
		Condition condition = Condition.make(property, Operator.EQUAL, "value").setInnerJoin();
		// validate
		Assert.assertEquals(property, condition.getCollectionProperty());
		Assert.assertEquals(property, condition.getCollectionTag());
		Assert.assertEquals(property, condition.getProperty());
	}

	@Test
	public void testOneSeperatorWithInnerJoin() {
		// execute
		Condition condition = Condition.make("property.next", Operator.EQUAL, "value").setInnerJoin();
		// validate
		Assert.assertEquals("property", condition.getCollectionProperty());
		Assert.assertEquals("property", condition.getCollectionTag());
		Assert.assertEquals("property.next", condition.getProperty());
	}

	@Test
	public void testThreeSeperatorWithInnerJoin() {
		// execute
		Condition condition = Condition.make("property.next.prev", Operator.EQUAL, "value").setInnerJoin();
		// validate
		Assert.assertEquals("property.next", condition.getCollectionProperty());
		Assert.assertEquals("next", condition.getCollectionTag());
		Assert.assertEquals("next.prev", condition.getProperty());
	}

	@Test
	public void testManySeperatorWithInnerJoin() {
		// execute
		Condition condition = Condition.make("property.next.prev.current.final", Operator.EQUAL, "value").setInnerJoin();
		// validate
		Assert.assertEquals("property.next.prev.current", condition.getCollectionProperty());
		Assert.assertEquals("current", condition.getCollectionTag());
		Assert.assertEquals("current.final", condition.getProperty());
	}

	@Test
	public void testConditionForString() {
		String stringValue = "value1";
		Condition condition = Condition.like("property", stringValue);
		Assert.assertEquals("property LIKE ?0", condition.toStringAsSql(0));
	}

	@Test
	public void testLikeValueConversionForString() {
		Assert.assertEquals("value1%", Condition.like("property", "value1").getValue());
		Assert.assertEquals("value1%", Condition.like("property", "value1*").getValue());
		Assert.assertEquals("%value1%", Condition.like("property", "*value1").getValue());
		Assert.assertEquals("val%ue1%", Condition.like("property", "val*ue1").getValue());
	}

	@Test
	public void testConditionForLong() {
		long longValue = 101L;
		Condition condition = Condition.equals("property", longValue);
		Assert.assertEquals("property = ?0", condition.toStringAsSql(0));
		Assert.assertEquals(longValue, condition.getValue());
	}

	@Test
	public void testConditionForSystemUser() {
		SystemUser systemUser = SystemUserHelper.makeSystemUser();
		Condition condition = Condition.equals("property", systemUser);
		Assert.assertEquals("property = ?0", condition.toStringAsSql(0));
		Assert.assertEquals(systemUser, condition.getValue());
	}

	@Test
	public void testLikeCapitalizeConversionForString() {
		Assert.assertEquals("Value1%", Condition.likeCapitalize("property", "value1").getValue());
		Assert.assertEquals("Value1%", Condition.likeCapitalize("property", "value1*").getValue());
		Assert.assertEquals("%Value1%", Condition.likeCapitalize("property", "%value1").getValue());
		Assert.assertEquals("%Value1%", Condition.likeCapitalize("property", "*value1").getValue());
		Assert.assertEquals("Val%ue1%", Condition.likeCapitalize("property", "val*ue1").getValue());
	}

}
