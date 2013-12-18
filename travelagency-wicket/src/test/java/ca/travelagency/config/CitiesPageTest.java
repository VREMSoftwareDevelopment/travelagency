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

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.tester.FormTester;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BasePage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.formheader.ResetPanel;
import ca.travelagency.components.formheader.SavePanel;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Criteria;

import com.google.common.collect.Lists;

/**
 * Note: this is used as the test for LookupEntitiesPage
 */
public class CitiesPageTest extends BaseWicketTester {
	private static final String SEARCH_PATH = CitiesPage.SEARCH_FORM+BasePage.PATH_SEPARATOR;
	private static final String FORM_PATH = CitiesPage.ADD_FORM+BasePage.PATH_SEPARATOR;
	private static final String SAVE_PATH = FORM_PATH+CitiesPage.SAVE_BUTTON+BasePage.PATH_SEPARATOR+SavePanel.SAVE_BUTTON;

	private List<City> cities;
	private Criteria criteria1;
	private Criteria criteria2;

	@Before
	public void setUp() {
		stubCityDataProvider();

		tester.startPage(CitiesPage.class);
		tester.assertRenderedPage(CitiesPage.class);
	}

	private void stubCityDataProvider() {
		cities = Lists.newArrayList(ConfigHelper.makeCity(), ConfigHelper.makeCity(), ConfigHelper.makeCity(), ConfigHelper.makeCity());

		List<DaoEntity> results = Lists.newArrayList();
		results.addAll(cities);

		criteria1 = Criteria.make(City.class);
		Mockito.stub(daoSupport.count(criteria1)).toReturn((long) cities.size());

		criteria2 = Criteria.make(City.class).setOffset(0).setCount(cities.size());
		Mockito.stub(daoSupport.find(criteria2)).toReturn(results);

		Mockito.stub(daoSupport.find(City.class, cities.get(0).getId())).toReturn(cities.get(0));
		Mockito.stub(daoSupport.find(City.class, cities.get(1).getId())).toReturn(cities.get(1));
		Mockito.stub(daoSupport.find(City.class, cities.get(2).getId())).toReturn(cities.get(2));
		Mockito.stub(daoSupport.find(City.class, cities.get(3).getId())).toReturn(cities.get(3));
	}

	@After
	public void tearDown() {
		Mockito.verify(daoSupport, Mockito.atLeastOnce()).count(criteria1);
		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(criteria2);
	}


	@Test
	public void testInheritance() {
		CitiesPage citiesPage = (CitiesPage) tester.getLastRenderedPage();

		Assert.assertEquals(CitiesPage.PAGE_KEY, citiesPage.getPageTitleKey());
		Assert.assertEquals(CitiesPage.LABEL_KEY, citiesPage.getLabelNameKey());
		Assert.assertEquals(City.class, citiesPage.getTrueClass());
		Assert.assertTrue(citiesPage.makeLookupEntitiesPage(null) instanceof CitiesPage);
	}

	@Test
	public void testComponents() {
		tester.assertComponent(CitiesPage.SEARCH_FORM, Form.class);
		tester.assertComponent(SEARCH_PATH+DaoEntity.PROPERTY_NAME, TextField.class);
		tester.assertComponent(SEARCH_PATH+CitiesPage.SEARCH_BUTTON, Button.class);
		tester.assertComponent(SEARCH_PATH+CitiesPage.CLEAR_BUTTON, Button.class);

		tester.assertComponent(CitiesPage.DATA_TABLE, AjaxFallbackDefaultDataTable.class);

		tester.assertComponent(CitiesPage.ADD_FORM, Form.class);
		tester.assertComponent(CitiesPage.ADD_FORM_TITLE1, Label.class);
		tester.assertComponent(CitiesPage.ADD_FORM_TITLE2, Label.class);
		tester.assertComponent(FORM_PATH+CitiesPage.SAVE_BUTTON, SavePanel.class);
		tester.assertComponent(FORM_PATH+CitiesPage.RESET_BUTTON, ResetPanel.class);
		tester.assertComponent(FORM_PATH+DaoEntity.PROPERTY_NAME, TextField.class);
	}

	@Test
	public void testCityDataTable() {
		DataTable<City, String> dataTable = getDataTable();

		Assert.assertEquals(4, getDataTable().getItemCount());
		validateColumns(dataTable);
	}

	private void validateColumns(DataTable<City, String> dataTable) {
		List<? extends IColumn<City, String>> columns = dataTable.getColumns();
		Assert.assertEquals(2, columns.size());

		validateSortableColumn(DaoEntity.PROPERTY_NAME, columns.get(0));
		validateNotSortableColumn(DaoEntity.PROPERTY_ID, columns.get(1));
	}

	@Test
	public void testSearchForm() throws Exception {
		String name = cities.get(1).getName();
		FormTester formTester = tester.newFormTester(CitiesPage.SEARCH_FORM);
		formTester.setValue(DaoEntity.PROPERTY_NAME, name);
		formTester.submit(CitiesPage.SEARCH_BUTTON);

		tester.assertRenderedPage(CitiesPage.class);
		tester.assertModelValue(SEARCH_PATH+DaoEntity.PROPERTY_NAME, name);
	}

	@SuppressWarnings("unchecked")
	private DataTable<City, String> getDataTable() {
		return (DataTable<City, String>) tester.getComponentFromLastRenderedPage(CitiesPage.DATA_TABLE);
	}

	@Test
	public void testAddFormVerifyRequiredFields() throws Exception {
		tester.executeAjaxEvent(SAVE_PATH, "onclick");

		tester.assertErrorMessages(new String [] {
			"'City Name' is required.",
		});
	}

	@Test
	public void testAddFormWithSubmit() {
		FormTester formTester = tester.newFormTester(CitiesPage.ADD_FORM);
		formTester.setValue(DaoEntity.PROPERTY_NAME, "My New City");
		tester.executeAjaxEvent(SAVE_PATH, "onclick");

		assertSuccessMessages(new String [] {
			"My New City saved."
		});

		tester.assertRenderedPage(CitiesPage.class);

		Mockito.verify(daoSupport).persist(Mockito.any(City.class));
	}
}
