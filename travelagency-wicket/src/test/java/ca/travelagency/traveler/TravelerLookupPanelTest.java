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
package ca.travelagency.traveler;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.Model;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BasePage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.customer.CustomerHelper;
import ca.travelagency.customer.Traveler;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoiceTraveler;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Criteria;

import com.google.common.collect.Lists;

public class TravelerLookupPanelTest extends BaseWicketTester {
	private Traveler traveler;
	private List<Traveler> travelers;
	private Form<InvoiceTraveler> travelerForm;

	@Before
	public void setUp() {
		traveler = CustomerHelper.makeTraveler();
		travelers = Lists.newArrayList(traveler);

		travelerForm = new Form<InvoiceTraveler>("form", Model.of(InvoiceHelper.makeTraveler()));

		List<DaoEntity> results = new ArrayList<DaoEntity>();
		results.addAll(travelers);

		Mockito.stub(daoSupport.count(Mockito.any(Criteria.class))).toReturn((long) travelers.size());
		Mockito.stub(daoSupport.find(Mockito.any(Criteria.class))).toReturn(results);
		Mockito.stub(daoSupport.find(Traveler.class, traveler.getId())).toReturn(traveler);
	}

	@Test
	public void testComponents() {
		TravelerLookupPanel travelerLookupPanel = new TravelerLookupPanel(COMPONENT_ID, travelerForm, null);
		tester.startComponentInPage(travelerLookupPanel);

		tester.assertComponent(COMPONENT_PATH+TravelerLookupPanel.FORM, Form.class);

		tester.assertComponent(COMPONENT_PATH+TravelerLookupPanel.FORM+BasePage.PATH_SEPARATOR+TravelerLookupPanel.FEEDBACK, ComponentFeedbackPanel.class);
		tester.assertComponent(COMPONENT_PATH+TravelerLookupPanel.FORM+BasePage.PATH_SEPARATOR+TravelerLookupPanel.SEARCH, IndicatingAjaxButton.class);
		tester.assertComponent(COMPONENT_PATH+TravelerLookupPanel.DATA, AjaxFallbackDefaultDataTable.class);

		tester.assertComponent(COMPONENT_PATH+TravelerLookupPanel.FORM+BasePage.PATH_SEPARATOR+TravelerFilter.Properties.searchText, TextField.class);
		tester.assertModelValue(COMPONENT_PATH+TravelerLookupPanel.FORM+BasePage.PATH_SEPARATOR+TravelerFilter.Properties.searchText, travelerForm.getModelObject().getLastName());

		Component component = tester.getComponentFromLastRenderedPage(COMPONENT_PATH+TravelerLookupPanel.DATA, false);
		@SuppressWarnings("unchecked")
		DataTable<Traveler, String> dataTable = (DataTable<Traveler, String>) component;

		Assert.assertEquals(1, dataTable.getItemCount());

		List<? extends IColumn<Traveler, String>> columns = dataTable.getColumns();
		Assert.assertEquals(4, columns.size());

		validateSortableColumn(Traveler.Properties.lastName.name(), Traveler.PROPERTY_NAME, columns.get(0));
		validateNotSortableColumn(Traveler.Properties.documentType.name(), columns.get(1));
		validateNotSortableColumn(Traveler.Properties.documentNumber.name(), columns.get(2));
		validateNotSortableColumn(Traveler.Properties.dateOfBirth.name(), columns.get(3));
	}

	@Test
	public void testSelectPanel() {
		TravelerLookupPanel travelerLookupPanel = new TravelerLookupPanel(COMPONENT_ID, travelerForm, null);
		tester.startComponentInPage(travelerLookupPanel);

		String path = COMPONENT_PATH+TravelerLookupPanel.DATA+":body:rows:1:cells:1:cell";
		tester.assertComponent(path, SelectPanel.class);
		tester.assertVisible(path);
	}

}
