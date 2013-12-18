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
package ca.travelagency.reconciliation;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ca.travelagency.components.decorators.FieldDecorator;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.invoice.CommissionStatus;
import ca.travelagency.invoice.InvoiceItem;
import ca.travelagency.persistence.DaoSupport;

import com.google.common.collect.Lists;

@AuthorizeInstantiation({"ADMIN"})
public class CommissionStatusForm extends Panel {
	private static final long serialVersionUID = 1L;

	static final String FORM = "form";

	@SpringBean
	private DaoSupport<InvoiceItem> daoSupport;

	public CommissionStatusForm(String id, InvoiceItem invoiceItem) {
		super(id);

   		final Form<InvoiceItem> form = new Form<InvoiceItem>(FORM, DaoEntityModelFactory.make(invoiceItem));
    	form.setOutputMarkupId(true);
    	add(form);

		form.add(new DropDownChoice<CommissionStatus>(InvoiceItem.Properties.commissionStatus.name(), Lists.newArrayList(CommissionStatus.values()))
			.setLabel(new ResourceModel("commissionStatusForm.label"))
			.add(FieldDecorator.doNotDisplayLabel())
			.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					InvoiceItem invoiceItem = form.getModelObject();
					invoiceItem.getInvoice().closeInvoice();
					daoSupport.persist(invoiceItem);
					target.add(form);
				}
				@Override
				protected void onError(AjaxRequestTarget target, RuntimeException e) {
					target.add(form);
				}
			}));
	}
}
