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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import ca.travelagency.components.CheckMarkPanel;
import ca.travelagency.components.formdetail.DeletePanel;
import ca.travelagency.components.formdetail.EditPanel;
import ca.travelagency.invoice.InvoicePayment;
import ca.travelagency.utils.DateUtils;

public class PaymentRowPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public static final String DELETE_BUTTON = "deleteButton";
	public static final String EDIT_BUTTON = "editButton";

	public PaymentRowPanel(final String id, IModel<InvoicePayment> model, final PaymentsPanel paymentsPanel) {
		super(id, model);
		setOutputMarkupId(true);

		add(DateLabel.forDateStyle(InvoicePayment.Properties.date.name(), DateUtils.DATE_STYLE));
		add(new Label(InvoicePayment.Properties.amountAsString.name()));
		add(new CheckMarkPanel(InvoicePayment.Properties.reconciled.name(), model.getObject().isReconciled()));
		add(new Label(InvoicePayment.Properties.paymentType.name()));

		add(new DeletePanel<InvoicePayment>(DELETE_BUTTON, getInvoicePayment(), paymentsPanel) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				paymentsPanel.delete(target, getInvoicePayment());
			}
		}.setVisible(isVisible(model, paymentsPanel)));

        add(new EditPanel(EDIT_BUTTON) {
    		private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
    			PaymentFormPanel paymentFormPanel = new PaymentFormPanel(id, getInvoicePayment(), paymentsPanel);
    			PaymentRowPanel.this.replaceWith(paymentFormPanel);
    			target.add(paymentFormPanel);
			}
        }.setVisible(isVisible(model, paymentsPanel)));

	}

	private boolean isVisible(IModel<InvoicePayment> model, PaymentsPanel paymentsPanel) {
		return paymentsPanel.isEditable() && !model.getObject().isReconciled();
	}

	private InvoicePayment getInvoicePayment() {
		return (InvoicePayment) getDefaultModelObject();
	}
}
