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

import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.junit.Before;
import org.junit.Test;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoiceTraveler;

public class TravelerLookupLinkTest extends BaseWicketTester {
	private InvoiceTraveler traveler;
	private TravelerLookupLink fixture;
	private Form<InvoiceTraveler> travelerForm;

	@Before
	public void setUp() throws Exception {
		traveler = InvoiceHelper.makeTraveler();
		travelerForm = new Form<InvoiceTraveler>("form", Model.of(traveler));

		fixture = new TravelerLookupLink(COMPONENT_ID, travelerForm);
	}

	@Test
	public void testComponents() throws Exception {
		// execute
		tester.startComponentInPage(fixture);
		// validate
		tester.assertComponent(COMPONENT_PATH+TravelerLookupLink.LINK, AjaxLink.class);
		tester.assertComponent(COMPONENT_PATH+TravelerLookupLink.LINK_MODALWINDOW, TravelerLookupModalWindow.class);
		tester.isInvisible(COMPONENT_PATH+TravelerLookupLink.LINK_MODALWINDOW);
	}

	@Test
	public void testRedirectToTravelerLookup() {
		// setup
		tester.startComponentInPage(fixture);
		// execute
	 	tester.clickLink(COMPONENT_PATH+TravelerLookupLink.LINK);
		// validate
		tester.isVisible(COMPONENT_PATH+TravelerLookupLink.LINK_MODALWINDOW);
	}

}
