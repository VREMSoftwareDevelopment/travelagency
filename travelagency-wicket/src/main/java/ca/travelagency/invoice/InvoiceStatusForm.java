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

import java.util.Arrays;

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
import ca.travelagency.persistence.DaoSupport;

@AuthorizeInstantiation({"ADMIN"})
public class InvoiceStatusForm extends Panel {
	private static final long serialVersionUID = 1L;

	static final String FORM = "form";

	@SpringBean
	private DaoSupport<Invoice> daoSupport;

	public InvoiceStatusForm(String id, Invoice invoice) {
		super(id);

   		final Form<Invoice> form = new Form<Invoice>(FORM, DaoEntityModelFactory.make(invoice));
    	form.setOutputMarkupId(true);
    	add(form);

		form.add(new DropDownChoice<InvoiceStatus>(Invoice.Properties.status.name(), Arrays.asList(InvoiceStatus.values()))
			.setRequired(true)
			.setLabel(new ResourceModel("invoiceStatusForm.label"))
			.add(FieldDecorator.doNotDisplayLabel())
			.add(new AjaxFormComponentUpdatingBehavior("onchange") {
				private static final long serialVersionUID = 1L;
				@Override
				protected void onUpdate(AjaxRequestTarget target) {
					Invoice invoice = form.getModelObject();
					daoSupport.persist(invoice);
					target.add(form);
				}
				@Override
				protected void onError(AjaxRequestTarget target, RuntimeException e) {
					target.add(form);
				}
			}));
	}
}
