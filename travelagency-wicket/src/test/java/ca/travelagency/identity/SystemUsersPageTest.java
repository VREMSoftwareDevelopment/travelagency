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

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.link.Link;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.search.SearchPanel;

import com.google.common.collect.Lists;

public class SystemUsersPageTest extends BaseWicketTester {
	private List<SystemUser> systemUsers;
	private Criteria criteria1;
	private Criteria criteria2;

	private void stubSystemUserDataProvider() {
		systemUsers = Lists.newArrayList(SystemUserHelper.makeSystemUser(), SystemUserHelper.makeSystemUser(), SystemUserHelper.makeSystemUser());

		List<DaoEntity> results = Lists.newArrayList();
		results.addAll(systemUsers);

		criteria1 = Criteria.make(SystemUser.class);
		Mockito.stub(daoSupport.count(criteria1)).toReturn((long) systemUsers.size());

		criteria2 = Criteria.make(SystemUser.class).setOffset(0).setCount(systemUsers.size());
		Mockito.stub(daoSupport.find(criteria2)).toReturn(results);

		Mockito.stub(daoSupport.find(SystemUser.class, systemUsers.get(0).getId())).toReturn(systemUsers.get(0));
		Mockito.stub(daoSupport.find(SystemUser.class, systemUsers.get(1).getId())).toReturn(systemUsers.get(1));
		Mockito.stub(daoSupport.find(SystemUser.class, systemUsers.get(2).getId())).toReturn(systemUsers.get(2));
	}

	@Test
	public void testSystemUserDataTable() {
		// setup
		stubSystemUserDataProvider();
		// execute
		tester.startPage(SystemUsersPage.class);
		// validate
		tester.assertRenderedPage(SystemUsersPage.class);

		tester.assertComponent(SystemUsersPage.SEARCH_PANEL, SearchPanel.class);

		tester.assertComponent(SystemUsersPage.DATA_TABLE, AjaxFallbackDefaultDataTable.class);

		@SuppressWarnings("unchecked")
		DataTable<SystemUser, String> dataTable = (DataTable<SystemUser, String>) tester.getComponentFromLastRenderedPage(SystemUsersPage.DATA_TABLE);

		Assert.assertEquals(3, dataTable.getItemCount());
		List<? extends IColumn<SystemUser, String>> columns = dataTable.getColumns();
		Assert.assertEquals(7, columns.size());

		validateSortableColumn(DaoEntity.PROPERTY_NAME, columns.get(0));
		validateSortableColumn(SystemUser.Properties.firstName.name(), columns.get(1));
		validateSortableColumn(SystemUser.Properties.lastName.name(), columns.get(2));
		validateNotSortableColumn(SystemUser.Properties.email.name(), columns.get(3));
		validateNotSortableColumn(SystemUser.Properties.primaryPhone.name(), columns.get(4));
		validateNotSortableColumn(SystemUser.Properties.secondaryPhone.name(), columns.get(5));
		validateNotSortableColumn(SystemUser.Properties.active.name(), columns.get(6));

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).count(criteria1);
		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(criteria2);
	}

	@Test
	public void testCreateSystemUser() {
		// execute
		tester.startPage(SystemUsersPage.class);
		// validate
		tester.assertRenderedPage(SystemUsersPage.class);

		tester.assertComponent(SystemUsersPage.CREATE, Link.class);

		tester.clickLink(SystemUsersPage.CREATE);

		tester.assertRenderedPage(SystemUserPage.class);
	}

	@Test
	public void testMaximumNumberOfSystemUser() {
		// setup
		Criteria criteria = Criteria.make(SystemUser.class);
		Mockito.when(daoSupport.count(criteria)).thenReturn((long) Integer.MAX_VALUE + 1);
		// execute
		tester.startPage(SystemUsersPage.class);
		// validate
		tester.assertInvisible(SystemUsersPage.CREATE);
		Mockito.verify(daoSupport, Mockito.atLeastOnce()).count(criteria);
	}

	@Test
	public void testModifySystemUser() {
		// setup
		stubSystemUserDataProvider();
		// execute
		tester.startPage(SystemUsersPage.class);
		// validate
		tester.assertRenderedPage(SystemUsersPage.class);

		String path = SystemUsersPage.DATA_TABLE+":body:rows:1:cells:1:cell:"+SystemUsersPage.LINK;

		tester.assertComponent(path, Link.class);
		tester.assertLabel(path + ":" + SystemUsersPage.LINK_LABEL, systemUsers.get(0).getName());

	 	tester.clickLink(path);
		tester.assertRenderedPage(SystemUserPage.class);
	}

}

