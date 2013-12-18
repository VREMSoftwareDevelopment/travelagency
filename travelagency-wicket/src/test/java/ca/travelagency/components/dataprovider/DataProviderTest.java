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

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.formheader.DaoEntityModel;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.persistence.query.OrderBy;

import com.google.common.collect.Lists;

public class DataProviderTest extends BaseWicketTester {

	public static class TestFilter implements Filter {
		private static final long serialVersionUID = 1L;
		public TestFilter() {super();}
		@Override public String getId() {return DaoEntity.class.getName();}
		@Override public Criteria getCriteria(OrderBy orderBy) {return Criteria.make(DaoEntity.class);}
	}

	private DataProvider<DaoEntity> fixture;
	private IModel<TestFilter> model;
	private Criteria criteria;

	@Before
	public void setUp() throws Exception {
		TestFilter testFilter = new TestFilter();
		model = Model.of(testFilter);
		fixture = new DataProvider<DaoEntity>(model);
		criteria = fixture.getCriteria();
	}

	@Test
	public void testSizeRetrievesTheRowCount() throws Exception {
		// setup
		long expected = 20;
		// expected
		Mockito.when(daoSupport.count(criteria)).thenReturn((long) expected);
		// execute
		long actual = fixture.size();
		// verify
		Mockito.verify(daoSupport).count(criteria);
		// validate
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testModelConvertsToDaoEntityModel() throws Exception {
		// execute
		IModel<DaoEntity> model = fixture.model(SystemUserHelper.makeSystemUser());
		// validate
		Assert.assertTrue(model instanceof CompoundPropertyModel);
		CompoundPropertyModel<DaoEntity> actual = (CompoundPropertyModel<DaoEntity>) model;
		Assert.assertTrue(actual.getChainedModel() instanceof DaoEntityModel);
	}

	@Test
	public void testIteratorRetrievesIteratorFromResultsList() throws Exception {
		// setup
		List<DaoEntity> expected = Lists.newArrayList();
		// expected
		Mockito.when(daoSupport.find(criteria)).thenReturn(expected);
		// execute
		Iterator<DaoEntity> actual = fixture.iterator(4, 5);
		// verify
		Mockito.verify(daoSupport).find(criteria);
		// validate
		Assert.assertNotNull(actual);
	}
}
