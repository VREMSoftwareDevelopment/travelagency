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

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import ca.travelagency.BasePage;
import ca.travelagency.components.behaviours.AjaxOnBlurBehaviour;
import ca.travelagency.components.decorators.FieldDecorator;
import ca.travelagency.components.fields.DateField;
import ca.travelagency.components.fields.StringFieldHelper;
import ca.travelagency.components.fields.SystemUserField;
import ca.travelagency.identity.Role;

@AuthorizeInstantiation({"AGENT"})
public class InvoicesPage extends BasePage {
	private static final long serialVersionUID = 1L;

	static final String CREATE = "create";
	static final String INVOICES = "invoices";
	static final String SEARCH_FORM = "searchForm";
	static final String SEARCH_BUTTON = "searchButton";
	static final String CLEAR_BUTTON = "clearButton";
	static final String FEEDBACK = "feedback";

	static final String PAGE_TITLE = "invoices.title";

	public InvoicesPage() {
		super();
		IModel<InvoiceFilter> model = Model.of(getAuthenticatedSession().getInvoiceFilter());
		if (!hasRole(Role.ADMIN)) {
    		model.getObject().setSystemUser(getSignedInSystemUser());
    	}
    	add(makeSearchForm(model));
    	add(new InvoicesPanel(INVOICES, model));
	}

	private Form<InvoiceFilter> makeSearchForm(IModel<InvoiceFilter> model) {
		final Form<InvoiceFilter> form = new Form<InvoiceFilter>(SEARCH_FORM,
				new CompoundPropertyModel<InvoiceFilter>(model));

		form.add(new ComponentFeedbackPanel(FEEDBACK, form));

		form.add(new TextField<String>(InvoiceFilter.Properties.searchText.name())
			.setLabel(new ResourceModel("invoice.searchText"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new CheckBox(InvoiceFilter.Properties.showUnpaidInvoicesOnly.name())
			.setLabel(new ResourceModel("invoice.showUnpaidInvoicesOnly"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new DropDownChoice<InvoiceStatus>(InvoiceFilter.Properties.status.name(), Arrays.asList(InvoiceStatus.values()))
			.setLabel(new ResourceModel("invoice.status"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new SystemUserField(InvoiceFilter.Properties.systemUser.name())
			.setLabel(new ResourceModel("invoice.agent"))
			.setVisible(hasRole(Role.ADMIN))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new DateField(InvoiceFilter.Properties.invoiceDateFrom.name())
			.setLabel(new ResourceModel("invoice.dateFrom"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new DateField(InvoiceFilter.Properties.invoiceDateTo.name())
			.setLabel(new ResourceModel("invoice.dateTo"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));

		form.add(new Button(SEARCH_BUTTON) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {
				setResponsePage(new InvoicesPage());
    		}
    	});

		form.add(new Button(CLEAR_BUTTON) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {
				getAuthenticatedSession().clearInvoiceFilter();
				setResponsePage(new InvoicesPage());
    		}
		}.setDefaultFormProcessing(false));

		return form;
	}

	@Override
	public String getPageTitleKey() {
		return PAGE_TITLE;
	}
}
