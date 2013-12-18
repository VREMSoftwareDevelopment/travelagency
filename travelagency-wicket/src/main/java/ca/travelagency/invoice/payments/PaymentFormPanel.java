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

import java.math.BigDecimal;
import java.util.Arrays;

import org.apache.commons.lang3.Validate;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import ca.travelagency.authentication.AuthenticatedSession;
import ca.travelagency.components.behaviours.AjaxOnBlurBehaviour;
import ca.travelagency.components.decorators.FieldDecorator;
import ca.travelagency.components.fields.DateField;
import ca.travelagency.components.formdetail.CancelPanel;
import ca.travelagency.components.formdetail.SavePanelDetail;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.components.formheader.ResetPanel;
import ca.travelagency.identity.Role;
import ca.travelagency.invoice.InvoicePayment;
import ca.travelagency.invoice.PaymentType;

public class PaymentFormPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public static final String FORM = "form";
	public static final String FEEDBACK = "feedback";
	public static final String SAVE_BUTTON = "saveButton";
	public static final String RESET_BUTTON = "resetButton";
	public static final String CANCEL_BUTTON = "cancelButton";

	public PaymentFormPanel(String id, PaymentsPanel paymentsPanel) {
		this(id, null, paymentsPanel);
	}

	public PaymentFormPanel(final String id, InvoicePayment invoicePayment, final PaymentsPanel paymentsPanel) {
		super(id);
		Validate.notNull(paymentsPanel);

		setOutputMarkupId(true);

		IModel<InvoicePayment> model = DaoEntityModelFactory.make(invoicePayment, InvoicePayment.class);
		final Form<InvoicePayment> paymentForm = new Form<InvoicePayment>(FORM, model);
		paymentForm.setOutputMarkupId(true);

		paymentForm.add(new ComponentFeedbackPanel(FEEDBACK, paymentForm));

		paymentForm.add(new RequiredTextField<BigDecimal>(InvoicePayment.Properties.amount.name())
			.setLabel(new ResourceModel("invoice.paymentAmount"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		paymentForm.add(new CheckBox(InvoicePayment.Properties.reconciled.name())
			.setLabel(new ResourceModel("invoice.paymentReconciled"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour())
			.setVisible(hasRole(Role.ADMIN)));
		paymentForm.add(new DropDownChoice<PaymentType>(InvoicePayment.Properties.paymentType.name(), Arrays.asList(PaymentType.values()))
			.setRequired(true)
			.setLabel(new ResourceModel("invoice.paymentType"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		paymentForm.add(new DateField(InvoicePayment.Properties.date.name())
			.setRequired(true)
			.setLabel(new ResourceModel("invoice.paymentDate"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));

		paymentForm.add(new SavePanelDetail<InvoicePayment>(SAVE_BUTTON, paymentForm) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<InvoicePayment> form) {
				paymentsPanel.update(target, form.getModelObject());
				IModel<InvoicePayment> model = DaoEntityModelFactory.make(InvoicePayment.class);
				form.setModel(model);
			}
		});

		paymentForm.add(new ResetPanel<InvoicePayment>(RESET_BUTTON, paymentForm)
			.setResetModel(!DaoEntityModelFactory.isPersisted(model.getObject())));

        paymentForm.add(new CancelPanel(CANCEL_BUTTON) {
    		private static final long serialVersionUID = 1L;
    		@Override
    		public void onClick(AjaxRequestTarget target) {
    			PaymentRowPanel paymentRowPanel = new PaymentRowPanel(id, paymentForm.getModel(), paymentsPanel);
    			PaymentFormPanel.this.replaceWith(paymentRowPanel);
    			target.add(paymentRowPanel);
   			}
        }.setVisible(invoicePayment != null));

		add(paymentForm);
	}

	private boolean hasRole(Role role) {
		return ((AuthenticatedSession) getSession()).hasRole(role);
	}

}