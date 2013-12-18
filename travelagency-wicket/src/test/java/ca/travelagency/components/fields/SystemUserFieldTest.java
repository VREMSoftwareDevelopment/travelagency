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

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Criteria;

import com.google.common.collect.Lists;

public class SystemUserFieldTest extends BaseWicketTester {

	private Criteria criteria;
	private SystemUserField fixture;

	@Before
	public void setUp() {
		List<DaoEntity> results = Lists.newArrayList();
		results.addAll(new ArrayList<SystemUser>());

		criteria = Criteria.make(SystemUser.class);
		Mockito.stub(daoSupport.find(criteria)).toReturn(results);

		fixture = new SystemUserField("id");
	}

	@After
	public void tearDown() {
		Mockito.verify(daoSupport).find(criteria);
	}

	@Test
	public void testRender() throws Exception {
		// setup
		SystemUser systemUser = SystemUserHelper.makeSystemUser();
		// Execute
		IChoiceRenderer<SystemUser> renderer = fixture.makeRenderer();
		// validate
		Assert.assertEquals(systemUser.getName(), renderer.getDisplayValue(systemUser));
		Assert.assertEquals(""+systemUser.getId(), renderer.getIdValue(systemUser, 0));
	}
}
