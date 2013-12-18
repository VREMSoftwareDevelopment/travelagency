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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import ca.travelagency.BasePage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.customer.CustomerHelper;
import ca.travelagency.customer.Traveler;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoiceTraveler;

public class SelectPanelTest extends BaseWicketTester {

	@Mock private ModalWindow modalWindow;
	private SelectPanel fixture;
	private Traveler traveler;
	private Form<InvoiceTraveler> form;

	@Before
	public void setUp() throws Exception {
		form = new Form<InvoiceTraveler>("form", Model.of(InvoiceHelper.makeTraveler()));
		traveler = CustomerHelper.makeTraveler();
		IModel<Traveler> model = Model.of(traveler);
		fixture = new SelectPanel(COMPONENT_ID, model, form, modalWindow);
	}

	@Test
	public void testComponents() throws Exception {
		// execute
		tester.startComponentInPage(fixture);
		// validate
		tester.assertComponent(COMPONENT_PATH+SelectPanel.LINK, AjaxLink.class);
		tester.assertComponent(COMPONENT_PATH+SelectPanel.LINK+BasePage.PATH_SEPARATOR+SelectPanel.LABEL, Label.class);
		tester.assertLabel(COMPONENT_PATH+SelectPanel.LINK+BasePage.PATH_SEPARATOR+SelectPanel.LABEL, traveler.getName());
	}

	@Test
	public void testOnClick() throws Exception {
		// setup
		tester.startComponentInPage(fixture);
		// execute
		tester.executeAjaxEvent(COMPONENT_PATH+SelectPanel.LINK, "onclick");
		// validate
		InvoiceTraveler invoiceTraveler = form.getModelObject();
		Assert.assertEquals(traveler.getLastName(), invoiceTraveler.getLastName());
		Assert.assertEquals(traveler.getFirstName(), invoiceTraveler.getFirstName());
		Assert.assertEquals(traveler.getDateOfBirth(), invoiceTraveler.getDateOfBirth());
		Assert.assertEquals(traveler.getDocumentNumber(), invoiceTraveler.getDocumentNumber());
		Assert.assertEquals(traveler.getDocumentType(), invoiceTraveler.getDocumentType());

		Mockito.verify(modalWindow).close(Mockito.any(AjaxRequestTarget.class));
	}
}
