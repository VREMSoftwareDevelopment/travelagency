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
package ca.travelagency.invoice;

import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.junit.Test;

import ca.travelagency.BasePage;
import ca.travelagency.BaseWicketTester;

public class InvoiceDestinationsPanelTest extends BaseWicketTester {
	@Test
	public void testDisplayLabels() throws Exception {
		Invoice invoice = InvoiceHelper.makeInvoiceWithDetails();

		InvoiceDestinationsPanel destinationsPanel = new InvoiceDestinationsPanel(COMPONENT_ID, Model.of(invoice));
		tester.startComponentInPage(destinationsPanel);

		tester.assertComponent(COMPONENT_PATH+Invoice.Properties.invoiceDestinations.name(), ListView.class);
		tester.assertLabel(
			COMPONENT_PATH+Invoice.Properties.invoiceDestinations.name()+BasePage.PATH_SEPARATOR+"0"+BasePage.PATH_SEPARATOR+InvoiceDestination.PROPERTY_NAME,
			invoice.getInvoiceDestinations().first().getName());
	}

}
