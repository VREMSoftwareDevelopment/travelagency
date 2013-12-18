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

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.persistence.DaoEntity;

import com.google.common.collect.Lists;

public class InvoiceTravelersPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public InvoiceTravelersPanel(String id, Invoice invoice) {
		this(id, DaoEntityModelFactory.make(invoice));
	}
	public InvoiceTravelersPanel(String id, IModel<Invoice> model) {
        super(id);
        List<InvoiceTraveler> travellers = Lists.newArrayList(model.getObject().getInvoiceTravelers());
		ListView<InvoiceTraveler> listView = new ListView<InvoiceTraveler>(Invoice.Properties.invoiceTravelers.name(), travellers) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void populateItem(ListItem<InvoiceTraveler> item) {
				item.add(new Label(DaoEntity.PROPERTY_NAME, item.getModelObject().getName()));
			}
		};
		add(listView);
    }
}