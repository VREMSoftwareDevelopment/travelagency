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
package ca.travelagency.components.fields;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Criteria;

import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class AutoCompleteFieldTest extends BaseWicketTester {

	private AutoCompleteField<DaoEntity> fixture;
	private Criteria criteria;

	@Before
	public void setUp() throws Exception {
		fixture = new AutoCompleteField<DaoEntity>("id", DaoEntity.class);
		criteria = Criteria.make(DaoEntity.class);
	}

	@Test
	public void testGroupByUsesCorrectCriteria() throws Exception {
		// setup
		String testName = "Test Name";
		List<String> results = Lists.newArrayList(testName);
		// stubs
		Mockito.stub(daoSupport.groupBy(criteria)).toReturn(results);
		// execute
		List<String> actual = fixture.getListOfChoices("xyz");
		// verify
		Mockito.verify(daoSupport).groupBy(criteria);
		// validate
		Assert.assertEquals(testName, actual.get(0));
	}
}
