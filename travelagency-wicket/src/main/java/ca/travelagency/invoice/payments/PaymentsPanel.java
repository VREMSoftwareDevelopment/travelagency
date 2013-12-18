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
package ca.travelagency.invoice.payments;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import ca.travelagency.components.formdetail.DetailsPanel;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoicePayment;

public class PaymentsPanel extends DetailsPanel<Invoice, InvoicePayment> {
	private static final long serialVersionUID = 1L;

	private Panel totalsPanel;

	public PaymentsPanel(String id, IModel<Invoice> model, Panel totalsPanel) {
		super(id, model);
		Validate.notNull(totalsPanel);
		this.totalsPanel = totalsPanel;
	}

	@Override
	protected void update(AjaxRequestTarget target, InvoicePayment invoicePayment) {
		Invoice invoice = getDaoEntity();
		invoice.addInvoicePayment(invoicePayment);
		daoSupport.persist(invoice);
		updateDisplay(target);
	}

	@Override
	protected void delete(AjaxRequestTarget target, InvoicePayment invoicePayment) {
		Invoice invoice = getDaoEntity();
		invoice.removeInvoicePayment(invoicePayment);
		daoSupport.persist(invoice);
		updateDisplay(target);
	}

	@Override
	protected void updateDisplay(AjaxRequestTarget target) {
		super.updateDisplay(target);
		target.add(this.totalsPanel);
	}

	@Override
	public boolean isEditable() {
		return getDaoEntity().isActive();
	}

	@Override
	protected Panel makeDetailFormPanel(String id) {
		return new PaymentFormPanel(id, this);
	}

	@Override
	protected Panel makeDetailRowPanel(String id, IModel<InvoicePayment> model) {
		return new PaymentRowPanel(id, model, this);
	}

	@Override
	protected List<InvoicePayment> getDetails() {
		return new ArrayList<InvoicePayment>(getDaoEntity().getInvoicePayments());
	}

	@Override
	protected Panel makeDetailHeaderPanel(String id) {
		return new PaymentsHeaderPanel(id);
	}
}
