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

import org.apache.wicket.markup.html.link.Link;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BasePage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.customer.Customer;
import ca.travelagency.customer.CustomerHelper;
import ca.travelagency.customer.CustomerPage;

public class InvoiceCustomerPanelTest extends BaseWicketTester {

	private static final String LINK_PATH = COMPONENT_PATH+InvoiceCustomerPanel.LINK;

	private InvoiceCustomerPanel customerPanel;
	private Customer customer;

	@Before
	public void setUp() throws Exception {
		customer = CustomerHelper.makeCustomer();
		Mockito.stub(daoSupport.find(Customer.class, customer.getId())).toReturn(customer);
		customerPanel = new InvoiceCustomerPanel(COMPONENT_ID, customer);
	}

	@Test
	public void testComponents() throws Exception {
		// execute
		tester.startComponentInPage(customerPanel);
		// validate
		tester.assertComponent(LINK_PATH, Link.class);

		tester.assertLabel(LINK_PATH + BasePage.PATH_SEPARATOR + Customer.PROPERTY_NAME, customer.getName());
		tester.assertLabel(COMPONENT_PATH+Customer.Properties.companyName.name(), customer.getCompanyName());
		tester.assertLabel(COMPONENT_PATH+Customer.Properties.primaryPhone.name(), customer.getPrimaryPhone());
	}

	@Test
	public void testLink() throws Exception {
		// setup
		tester.startComponentInPage(customerPanel);
		// execute
		tester.clickLink(LINK_PATH);
		// validate
		tester.assertRenderedPage(CustomerPage.class);
	}
}
