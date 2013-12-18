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
package ca.travelagency.invoice.destinations;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import ca.travelagency.components.formdetail.DetailsPanel;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceDestination;

public class DestinationsPanel extends DetailsPanel<Invoice, InvoiceDestination> {
	private static final long serialVersionUID = 1L;

	public DestinationsPanel(String id, IModel<Invoice> model) {
		super(id, model);
	}

	@Override
	protected void update(AjaxRequestTarget target, InvoiceDestination invoiceDestination) {
		Invoice invoice = getDaoEntity();
		invoice.addInvoiceDestination(invoiceDestination);
		daoSupport.persist(invoice);
		updateDisplay(target);
	}

	@Override
	protected void delete(AjaxRequestTarget target, InvoiceDestination invoiceDestination) {
		Invoice invoice = getDaoEntity();
		invoice.removeInvoiceDestination(invoiceDestination);
		daoSupport.persist(invoice);
		updateDisplay(target);
	}

	@Override
	public boolean isEditable() {
		return getDaoEntity().isActive();
	}

	@Override
	protected Panel makeDetailFormPanel(String id) {
		return new DestinationFormPanel(id, this);
	}

	@Override
	protected Panel makeDetailRowPanel(String id, IModel<InvoiceDestination> model) {
		return new DestinationRowPanel(id, model, this);
	}

	@Override
	protected List<InvoiceDestination> getDetails() {
		return new ArrayList<InvoiceDestination>(getDaoEntity().getInvoiceDestinations());
	}

	@Override
	protected Panel makeDetailHeaderPanel(String id) {
		return new DestinationsHeaderPanel(id);
	}
}
