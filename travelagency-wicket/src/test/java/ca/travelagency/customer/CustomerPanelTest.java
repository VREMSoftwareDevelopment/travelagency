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
package ca.travelagency.customer;

import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;


public class CustomerPanelTest extends BaseWicketTester {
	@Test
	public void testCustomerPanel() throws Exception {
		Customer customer = CustomerHelper.makeCustomer();

		Mockito.stub(daoSupport.find(Customer.class, customer.getId())).toReturn(customer);

		CustomerPanel customerPanel = new CustomerPanel(COMPONENT_ID, customer);
		tester.startComponentInPage(customerPanel);

		tester.assertLabel(COMPONENT_PATH+Customer.PROPERTY_NAME, customer.getName());
		tester.assertLabel(COMPONENT_PATH+Customer.Properties.companyName.name(), customer.getCompanyName());
		tester.assertLabel(COMPONENT_PATH+Customer.Properties.address.name(), customer.getAddress());
		tester.assertLabel(COMPONENT_PATH+Customer.Properties.city.name(), customer.getCity());
		tester.assertLabel(COMPONENT_PATH+Customer.Properties.province.name(), customer.getProvince());
		tester.assertLabel(COMPONENT_PATH+Customer.Properties.postalCode.name(), customer.getPostalCode());
		tester.assertLabel(COMPONENT_PATH+Customer.Properties.country.name(), customer.getCountry());
		tester.assertLabel(COMPONENT_PATH+Customer.Properties.email.name(), customer.getEmail());
		tester.assertLabel(COMPONENT_PATH+Customer.Properties.primaryPhone.name(), customer.getPrimaryPhone());
		tester.assertLabel(COMPONENT_PATH+Customer.Properties.secondaryPhone.name(), customer.getSecondaryPhone());
	}

}
