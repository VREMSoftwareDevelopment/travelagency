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
import org.junit.Test;

import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Criteria;

public class SystemUserFilterTest {
	@Test
	public void testWithSearchText() {
		// setup
		SystemUserFilter fixture = new SystemUserFilter();
		String value = "searchText";
		fixture.setSearchText(value);
		// execute
		Criteria criteria = fixture.getCriteria(null);
		// validate
		List<Condition> actual = criteria.getOrConditions();
		Assert.assertEquals(4, actual.size());
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(SystemUser.PROPERTY_NAME, value)));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(SystemUser.Properties.firstName.name(), value)));
		Assert.assertTrue(actual.contains(Condition.likeCapitalize(SystemUser.Properties.lastName.name(), value)));
		Assert.assertTrue(actual.contains(Condition.like(SystemUser.Properties.email.name(), value)));
	}

}
