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

import java.math.BigDecimal;

import org.apache.commons.lang3.Validate;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import ca.travelagency.components.behaviours.AjaxOnBlurBehaviour;
import ca.travelagency.components.decorators.FieldDecorator;
import ca.travelagency.components.fields.CancelDepartureField;
import ca.travelagency.components.fields.ChangeDepartureField;
import ca.travelagency.components.fields.DateField;
import ca.travelagency.components.fields.ProductField;
import ca.travelagency.components.fields.StringFieldHelper;
import ca.travelagency.components.fields.SupplierField;
import ca.travelagency.components.formdetail.CancelPanel;
import ca.travelagency.components.formdetail.SavePanelDetail;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.components.formheader.ResetPanel;
import ca.travelagency.invoice.CommissionStatus;
import ca.travelagency.invoice.InvoiceItem;

import com.google.common.collect.Lists;

public class ItemFormPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public static final String FORM = "form";
	public static final String FEEDBACK = "feedback";
	public static final String SAVE_BUTTON = "saveButton";
	public static final String RESET_BUTTON = "resetButton";
	public static final String CANCEL_BUTTON = "cancelButton";

	public ItemFormPanel(String id, ItemsPanel itemsPanel) {
		this(id, null, itemsPanel);
	}

	public ItemFormPanel(final String id, InvoiceItem invoiceItem, final ItemsPanel itemsPanel) {
		super(id);
		Validate.notNull(itemsPanel);

		setOutputMarkupId(true);

		IModel<InvoiceItem> model = DaoEntityModelFactory.make(invoiceItem, InvoiceItem.class);
		initialize(model.getObject(), itemsPanel);

		final Form<InvoiceItem> itemForm = new Form<InvoiceItem>(FORM, model);
		itemForm.setOutputMarkupId(true);

		itemForm.add(new ComponentFeedbackPanel(FEEDBACK, itemForm));

		itemForm.add(new ProductField(InvoiceItem.Properties.description.name())
			.setRequired(true)
			.setLabel(new ResourceModel("itemProduct"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		itemForm.add(new SupplierField(InvoiceItem.Properties.supplier.name())
			.setRequired(true)
			.setLabel(new ResourceModel("itemSupplier"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		itemForm.add(new TextField<BigDecimal>(InvoiceItem.Properties.commission.name())
			.setLabel(new ResourceModel("itemCommission"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		itemForm.add(new TextField<BigDecimal>(InvoiceItem.Properties.taxOnCommission.name())
			.setLabel(new ResourceModel("itemHSTOnCommission"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		itemForm.add(new TextField<BigDecimal>(InvoiceItem.Properties.price.name())
			.setRequired(true)
			.setLabel(new ResourceModel("itemPrice"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		itemForm.add(new TextField<BigDecimal>(InvoiceItem.Properties.tax.name())
			.setLabel(new ResourceModel("itemTax"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		itemForm.add(new CancelDepartureField(InvoiceItem.Properties.cancelBeforeDeparture.name())
			.setLabel(new ResourceModel("itemCancelBeforeDeparture"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		itemForm.add(new ChangeDepartureField(InvoiceItem.Properties.changeBeforeDeparture.name())
			.setLabel(new ResourceModel("itemChangeBeforeDeparture"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		itemForm.add(new ChangeDepartureField(InvoiceItem.Properties.changeAfterDeparture.name())
			.setLabel(new ResourceModel("itemChangeAfterDeparture"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		itemForm.add(new TextField<Integer>(InvoiceItem.Properties.qty.name())
			.setRequired(true)
			.setLabel(new ResourceModel("itemQty"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		itemForm.add(new TextField<String>(InvoiceItem.Properties.bookingNumber.name())
			.setLabel(new ResourceModel("itemBookingNumber"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		itemForm.add(new DateField(InvoiceItem.Properties.bookingDate.name())
			.setLabel(new ResourceModel("itemBookingDate"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		itemForm.add(commissionStatusField(model));

		itemForm.add(new SavePanelDetail<InvoiceItem>(SAVE_BUTTON, itemForm) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<InvoiceItem> form) {
				InvoiceItem invoiceItem = form.getModelObject();
				itemsPanel.update(target, invoiceItem);
				IModel<InvoiceItem> model = DaoEntityModelFactory.make(InvoiceItem.class);
				form.setModel(model);
				initialize(model.getObject(), itemsPanel);
			}
		});

		itemForm.add(new ResetPanel<InvoiceItem>(RESET_BUTTON, itemForm) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<InvoiceItem> form) {
				initialize(form.getModelObject(), itemsPanel);
			}
		}.setResetModel(!DaoEntityModelFactory.isPersisted(model.getObject())));

        itemForm.add(new CancelPanel(CANCEL_BUTTON) {
    		private static final long serialVersionUID = 1L;
    		@Override
    		public void onClick(AjaxRequestTarget target) {
    			ItemRowPanel itemRowPanel = new ItemRowPanel(id, itemForm.getModel(), itemsPanel);
    			ItemFormPanel.this.replaceWith(itemRowPanel);
    			target.add(itemRowPanel);
   			}
        }.setVisible(invoiceItem != null));

		add(itemForm);
	}

	private DropDownChoice<CommissionStatus> commissionStatusField(IModel<InvoiceItem> model) {
		DropDownChoice<CommissionStatus> commissionStatusField = new DropDownChoice<CommissionStatus>(InvoiceItem.Properties.commissionStatus.name());
		if (model.getObject().isCommissionVerified()) {
			commissionStatusField.setEnabled(false);
			commissionStatusField.setChoices(Lists.newArrayList(CommissionStatus.values()));
		} else {
			commissionStatusField.setChoices(Lists.newArrayList(CommissionStatus.NotVerified));
		}
		commissionStatusField.setLabel(new ResourceModel("itemCommissionStatus"));
		commissionStatusField.add(new FieldDecorator(), new AjaxOnBlurBehaviour());
		return commissionStatusField;
	}

	private void initialize(InvoiceItem invoiceItem, ItemsPanel itemsPanel) {
		if (DaoEntityModelFactory.isPersisted(invoiceItem)) {
			return;
		}
		invoiceItem.setCancelBeforeDeparture(getLocalizer().getString("cancel.before.departure", this));
		invoiceItem.setChangeBeforeDeparture(getLocalizer().getString("change.before.departure", this));
		invoiceItem.setChangeAfterDeparture(getLocalizer().getString("change.after.departure", this));
		invoiceItem.setBookingDate(itemsPanel.getDaoEntity().getDate());
	}
}
