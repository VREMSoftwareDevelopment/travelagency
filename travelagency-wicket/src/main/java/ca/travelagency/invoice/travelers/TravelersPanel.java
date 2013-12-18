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
package ca.travelagency.invoice.travelers;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import ca.travelagency.components.formdetail.DetailsPanel;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceTraveler;

public class TravelersPanel extends DetailsPanel<Invoice, InvoiceTraveler> {
	private static final long serialVersionUID = 1L;

	public TravelersPanel(String id, IModel<Invoice> model) {
		super(id, model);
	}

	@Override
	protected void update(AjaxRequestTarget target, InvoiceTraveler invoiceTraveler) {
		Invoice invoice = getDaoEntity();
		invoice.addInvoiceTraveler(invoiceTraveler);
		daoSupport.persist(invoice);
		updateDisplay(target);
	}

	@Override
	protected void delete(AjaxRequestTarget target, InvoiceTraveler invoiceTraveler) {
		Invoice invoice = getDaoEntity();
		invoice.removeInvoiceTraveler(invoiceTraveler);
		daoSupport.persist(invoice);
		updateDisplay(target);
	}

	@Override
	public boolean isEditable() {
		return getDaoEntity().isActive();
	}

	@Override
	protected Panel makeDetailFormPanel(String id) {
		return new TravelerFormPanel(id, this);
	}

	@Override
	protected Panel makeDetailRowPanel(String id, IModel<InvoiceTraveler> model) {
		return new TravelerRowPanel(id, model, this);
	}

	@Override
	protected List<InvoiceTraveler> getDetails() {
		return new ArrayList<InvoiceTraveler>(getDaoEntity().getInvoiceTravelers());
	}

	@Override
	protected Panel makeDetailHeaderPanel(String id) {
		return new TravelersHeaderPanel(id);
	}
}
