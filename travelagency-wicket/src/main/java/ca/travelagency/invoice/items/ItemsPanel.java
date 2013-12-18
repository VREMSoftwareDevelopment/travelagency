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
package ca.travelagency.invoice.items;

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import ca.travelagency.components.formdetail.DetailsPanel;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceItem;

public class ItemsPanel extends DetailsPanel<Invoice, InvoiceItem> {
	private static final long serialVersionUID = 1L;

	private Panel totalsPanel;

	public ItemsPanel(String id, IModel<Invoice> model, Panel totalsPanel) {
		super(id, model);
		Validate.notNull(totalsPanel);
		this.totalsPanel = totalsPanel;
	}

	@Override
	protected void update(AjaxRequestTarget target, InvoiceItem invoiceItem) {
		Invoice invoice = getDaoEntity();
		invoice.addInvoiceItem(invoiceItem);
		daoSupport.persist(invoice);
		updateDisplay(target);
	}

	@Override
	protected void delete(AjaxRequestTarget target, InvoiceItem invoiceItem) {
		Invoice invoice = getDaoEntity();
		invoice.removeInvoiceItem(invoiceItem);
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

	public void moveUp(AjaxRequestTarget target, InvoiceItem invoiceItem) {
		Invoice invoice = getDaoEntity();
		invoice.moveUpItem(invoiceItem);
		daoSupport.persist(invoice);
		updateDisplay(target);
	}

	public void moveDown(AjaxRequestTarget target, InvoiceItem invoiceItem) {
		Invoice invoice = getDaoEntity();
		invoice.moveDownItem(invoiceItem);
		daoSupport.persist(invoice);
		updateDisplay(target);
	}

	@Override
	protected Panel makeDetailFormPanel(String id) {
		return new ItemFormPanel(id, this);
	}

	@Override
	protected Panel makeDetailRowPanel(String id, IModel<InvoiceItem> model) {
		return new ItemRowPanel(id, model, this);
	}

	@Override
	protected List<InvoiceItem> getDetails() {
		return getDaoEntity().getInvoiceItems();
	}

	@Override
	protected Panel makeDetailHeaderPanel(String id) {
		return new ItemsHeaderPanel(id);
	}
}
