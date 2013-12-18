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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.persistence.DaoEntity;

public class CustomerPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public CustomerPanel(String id, Customer customer) {
		super(id, DaoEntityModelFactory.make(customer, Customer.class));

		add(new Label(DaoEntity.PROPERTY_NAME));
		add(new Label(Customer.Properties.companyName.name()));
		add(new Label(Customer.Properties.address.name()));
		add(new Label(Customer.Properties.city.name()));
		add(new Label(Customer.Properties.province.name()));
		add(new Label(Customer.Properties.postalCode.name()));
		add(new Label(Customer.Properties.country.name()));
		add(new Label(Customer.Properties.email.name()));
		add(new Label(Customer.Properties.primaryPhone.name()));
		add(new Label(Customer.Properties.secondaryPhone.name()));
	}

	public Customer getModelObject() {
		return (Customer) getDefaultModelObject();
	}

}
