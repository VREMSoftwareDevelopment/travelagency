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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ca.travelagency.BasePage;
import ca.travelagency.components.behaviors.AjaxOnBlurBehavior;
import ca.travelagency.components.decorators.FieldDecorator;
import ca.travelagency.components.fields.DateField;
import ca.travelagency.components.fields.SystemUserField;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.components.formheader.ResetPanel;
import ca.travelagency.components.formheader.SavePanel;
import ca.travelagency.config.ParameterRepository;
import ca.travelagency.customer.Customer;
import ca.travelagency.customer.CustomerPanel;
import ca.travelagency.identity.Role;
import ca.travelagency.invoice.destinations.DestinationsPanel;
import ca.travelagency.invoice.items.ItemsPanel;
import ca.travelagency.invoice.notes.NotesPanel;
import ca.travelagency.invoice.payments.PaymentsPanel;
import ca.travelagency.invoice.travelers.TravelersPanel;
import ca.travelagency.invoice.view.InvoiceTotalsPanel;
import ca.travelagency.utils.DateUtils;
import ca.travelagency.utils.StringUtils;

@AuthorizeInstantiation({"AGENT"})
public class InvoicePage extends BasePage {
	private static final long serialVersionUID = 1L;

	static final String PAGE_TITLE = "invoice.title";

	static final String FORM = "form";
	static final String SAVE_BUTTON = "saveButton";
	static final String RESET_BUTTON = "resetButton";
	static final String PREVIEW_BUTTON = "previewButton";
	static final String FEEDBACK = "feedback";

	static final String CUSTOMER_PANEL = "customerPanel";
	static final String DETAILS = "details";
	static final String TOTALS_PANEL = "totalsPanel";

	private CustomerPanel customerPanel;
	private InvoiceTotalsPanel totalsPanel;

	@SpringBean
	private ParameterRepository parameterRepository;

	// testing only
	@Deprecated
	public InvoicePage() {
		this((Customer) null);
	}

	public InvoicePage(Customer customer) {
		this(customer, null);
	}

	public InvoicePage(Invoice invoice) {
		this(invoice.getCustomer(), invoice);
	}

	private InvoicePage(Customer customer, Invoice invoice) {
		customerPanel = new CustomerPanel(CUSTOMER_PANEL, customer);
		totalsPanel = new InvoiceTotalsPanel(TOTALS_PANEL, invoice);

		Form<Invoice> invoiceForm = invoiceForm(customer, invoice);
		add(invoiceForm);

		addInvoiceDetails(invoiceForm);
	}

	private void addInvoiceDetails(Form<Invoice> form) {
		final IModel<Invoice> model = form.getModel();
		boolean persisted = DaoEntityModelFactory.isPersisted(model.getObject());

		List<ITab> tabs = new ArrayList<ITab>();
		final AjaxTabbedPanel<ITab> invoiceDetails = new AjaxTabbedPanel<ITab>(DETAILS, tabs);
		invoiceDetails.setOutputMarkupId(true);
		invoiceDetails.setVisible(persisted);
		add(invoiceDetails);

		if (persisted) {
			tabs.add(new AbstractTab(new ResourceModel("invoice.destinations")) {
				private static final long serialVersionUID = 1L;
				@Override
				public Panel getPanel(String panelId) {
					return new DestinationsPanel(panelId, model);
				}
			});
			tabs.add(new AbstractTab(new ResourceModel("invoice.travelers")) {
				private static final long serialVersionUID = 1L;
				@Override
				public Panel getPanel(String panelId) {
					return new TravelersPanel(panelId, model);
				}
			});
			tabs.add(new AbstractTab(new ResourceModel("invoice.items")) {
				private static final long serialVersionUID = 1L;
				@Override
				public Panel getPanel(String panelId) {
					return new ItemsPanel(panelId, model, totalsPanel);
				}
			});
			tabs.add(new AbstractTab(new ResourceModel("invoice.notes")) {
				private static final long serialVersionUID = 1L;
				@Override
				public Panel getPanel(String panelId) {
					return new NotesPanel(panelId, model);
				}
			});
			tabs.add(new AbstractTab(new ResourceModel("invoice.payments")) {
				private static final long serialVersionUID = 1L;
				@Override
				public Panel getPanel(String panelId) {
					return new PaymentsPanel(panelId, model, totalsPanel);
				}
			});
		}
	}

	private Form<Invoice> invoiceForm(Customer customer, Invoice invoice) {
		final IModel<Invoice> invoiceModel = DaoEntityModelFactory.make(invoice, Invoice.class);
		boolean isPersisted = DaoEntityModelFactory.isPersisted(invoiceModel.getObject());

		Form<Invoice> form = new Form<Invoice>(FORM, invoiceModel);
		form.setOutputMarkupId(true);

		form.add(customerPanel);
		form.add(totalsPanel);

		form.add(new ComponentFeedbackPanel(FEEDBACK, form));

		form.add(new RequiredTextField<String>(Invoice.Properties.invoiceNumber.name())
			.setLabel(new ResourceModel("invoice.number"))
			.setVisible(isPersisted)
			.setEnabled(false)
			.add(new FieldDecorator()));
		form.add(new DateField(Invoice.Properties.date.name())
			.setRequired(true)
			.setLabel(new ResourceModel("invoice.date"))
			.setEnabled(invoiceModel.getObject().isActive())
			.add(new FieldDecorator(), new AjaxOnBlurBehavior()));
		form.add(new DateField(Invoice.Properties.totalDueDate.name())
			.setRequired(true)
			.setLabel(new ResourceModel("invoice.totalDueDate"))
			.setEnabled(invoiceModel.getObject().isActive())
			.add(new FieldDecorator(), new AjaxOnBlurBehavior()));
		form.add(new SystemUserField(Invoice.Properties.systemUser.name())
			.setRequired(true)
			.setLabel(new ResourceModel("invoice.agent"))
			.setEnabled(invoiceModel.getObject().isActive())
			.setVisible(hasRole(Role.ADMIN))
			.add(new FieldDecorator(), new AjaxOnBlurBehavior()));
		form.add(new DropDownChoice<InvoiceStatus>(Invoice.Properties.status.name(), Arrays.asList(InvoiceStatus.values()))
			.setRequired(true)
			.setLabel(new ResourceModel("invoice.status"))
			.setEnabled(hasRole(Role.ADMIN))
			.add(new FieldDecorator(), new AjaxOnBlurBehavior()));
		form.add(new CheckBox(Invoice.Properties.billCompany.name())
			.setLabel(new ResourceModel("invoice.billCompany"))
			.setVisible(StringUtils.isNotEmpty(customerPanel.getModelObject().getCompanyName()))
			.setEnabled(invoiceModel.getObject().isActive())
			.add(new FieldDecorator(), new AjaxOnBlurBehavior()));

		form.add(new SavePanel<Invoice>(SAVE_BUTTON, form) {
			private static final long serialVersionUID = 1L;
			@Override
			public void preSubmit(AjaxRequestTarget target, Invoice daoEntity, boolean newDaoEntity) {
				if (!newDaoEntity) {
					return;
				}
				initialize(daoEntity);
				if (!hasRole(Role.ADMIN)) {
					daoEntity.setSystemUser(getSignedInSystemUser());
				}
			}
			@Override
			public void postSubmit(AjaxRequestTarget target, Invoice daoEntity, boolean newDaoEntity) {
				if (newDaoEntity || hasRole(Role.ADMIN)) {
					setResponsePage(new InvoicePage(daoEntity));
				}
			}
			private void initialize(Invoice daoEntity) {
				Customer customer = customerPanel.getModelObject();
				daoEntity.initialize(customer);
				daoEntity.addInvoiceTraveler(InvoiceTraveler.make(customer));

				List<String> defaultInvoiceNotes = parameterRepository.getDefaultInvoiceNotes();
				for (int i = 0; i < defaultInvoiceNotes.size(); i++) {
					InvoiceNote invoiceNote = InvoiceNote.make(defaultInvoiceNotes.get(i));
					invoiceNote.setDate(DateUtils.addSeconds(invoiceNote.getDate(), i));
					daoEntity.addInvoiceNote(invoiceNote);
				}
			}
    	});

		form.add(new ResetPanel<Invoice>(RESET_BUTTON, form));

		form.add(new Link<String>(PREVIEW_BUTTON) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(new InvoicePreviewPage(invoiceModel.getObject()));
			}
		}.setVisible(isPersisted));

		return form;
	}

	@Override
	public String getPageTitleKey() {
		return PAGE_TITLE;
	}
}
