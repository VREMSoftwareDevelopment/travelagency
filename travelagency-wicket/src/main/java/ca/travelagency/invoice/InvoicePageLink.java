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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import ca.travelagency.components.formheader.DaoEntityModelFactory;

public class InvoicePageLink extends Panel {
	private static final long serialVersionUID = 1L;

	static final String LINK = "link";
	static final String LABEL = "label";

	public InvoicePageLink(String id, Invoice invoice) {
		this(id, DaoEntityModelFactory.make(invoice));
	}

	public InvoicePageLink(String id, final IModel<Invoice> model) {
        super(id);
        Link<Invoice> link = new Link<Invoice>(LINK) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(new InvoicePage(model.getObject()));
			}
        };
        link.add(new Label(LABEL, Model.of(model.getObject().getInvoiceNumber())));
        add(link);
    }
}