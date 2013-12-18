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
package ca.travelagency.customer;

import java.util.Arrays;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import ca.travelagency.BasePage;
import ca.travelagency.components.behaviours.AjaxOnBlurBehaviour;
import ca.travelagency.components.decorators.FieldDecorator;
import ca.travelagency.components.fields.CityField;
import ca.travelagency.components.fields.CountryField;
import ca.travelagency.components.fields.DateField;
import ca.travelagency.components.fields.ProvinceField;
import ca.travelagency.components.fields.SalutationField;
import ca.travelagency.components.fields.StringFieldHelper;
import ca.travelagency.components.fields.SystemUserField;
import ca.travelagency.components.fields.TravelDocumentField;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.components.formheader.ResetPanel;
import ca.travelagency.components.formheader.SavePanel;
import ca.travelagency.components.validators.PhoneNumberValidator;
import ca.travelagency.config.ParameterRepository;
import ca.travelagency.identity.Role;
import ca.travelagency.invoice.InvoiceFilter;
import ca.travelagency.invoice.InvoicePage;
import ca.travelagency.invoice.InvoicesPanel;
import ca.travelagency.utils.StringUtils;

@AuthorizeInstantiation({"AGENT"})
public class CustomerPage extends BasePage {
	private static final long serialVersionUID = 1L;

	static final String FORM = "form";
	static final String SAVE_BUTTON = "saveButton";
	static final String RESET_BUTTON = "resetButton";
	static final String CREATE_INVOICE_BUTTON = "createInvoiceButton";
	static final String FEEDBACK = "feedback";
	static final String INVOICES = "invoices";

	static final String PAGE_TITLE = "customer.title";

	@SpringBean
	private ParameterRepository parameterRepository;

	public CustomerPage() {
		this(null);
	}

	public CustomerPage(Customer customer) {
		final Form<Customer> form = new Form<Customer>(FORM, DaoEntityModelFactory.make(customer, Customer.class));
		form.setOutputMarkupId(true);
		add(form);

		initialize(form);

		form.add(new ComponentFeedbackPanel(FEEDBACK, form));

		form.add(new DropDownChoice<CustomerStatus>(Customer.Properties.status.name(), Arrays.asList(CustomerStatus.values()))
			.setRequired(true)
			.setLabel(new ResourceModel("customer.status"))
			.setEnabled(hasRole(Role.ADMIN))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new SalutationField(Customer.Properties.salutation.name())
			.setLabel(new ResourceModel("customer.salutation"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		LastNameField lastNameField = new LastNameField(Customer.Properties.lastName.name());
		form.add(lastNameField
			.setLabel(new ResourceModel("customer.lastName"))
			.setRequired(true)
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		FirstNameField firstNameField = new FirstNameField(Customer.Properties.firstName.name());
		form.add(firstNameField
			.setLabel(new ResourceModel("customer.firstName"))
			.setRequired(true)
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new CompanyNameField(Customer.Properties.companyName.name())
			.setLabel(new ResourceModel("customer.companyName"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new TextField<String>(Customer.Properties.address.name())
			.setLabel(new ResourceModel("customer.address"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new CityField(Customer.Properties.city.name())
			.setLabel(new ResourceModel("customer.city"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new ProvinceField(Customer.Properties.province.name())
			.setLabel(new ResourceModel("customer.province"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new TextField<String>(Customer.Properties.postalCode.name())
			.setLabel(new ResourceModel("customer.postalCode"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new CountryField(Customer.Properties.country.name())
			.setLabel(new ResourceModel("customer.country"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new TextField<String>(Customer.Properties.email.name())
			.setLabel(new ResourceModel("customer.email"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(EmailAddressValidator.getInstance())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		RequiredTextField<String> primaryPhoneNumber = new RequiredTextField<String>(Customer.Properties.primaryPhone.name());
		form.add(primaryPhoneNumber
			.setLabel(new ResourceModel("customer.primaryPhone"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new PhoneNumberValidator(), new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new TextField<String>(Customer.Properties.secondaryPhone.name())
			.setLabel(new ResourceModel("customer.secondaryPhone"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new PhoneNumberValidator(), new FieldDecorator(), new AjaxOnBlurBehaviour()));
		DateField dateOfBirthField = new DateField(Customer.Properties.dateOfBirth.name());
		form.add(dateOfBirthField
			.setLabel(new ResourceModel("customer.dateOfBirth"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new TextArea<String>(Customer.Properties.notes.name())
			.setLabel(new ResourceModel("customer.notes"))
			.add(StringFieldHelper.maxLenAreaValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new SystemUserField(Customer.Properties.systemUser.name())
			.setLabel(new ResourceModel("customer.agent"))
			.setVisible(hasRole(Role.ADMIN))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new TravelDocumentField(Customer.Properties.travelDocumentType.name())
			.setLabel(new ResourceModel("customer.travelDocumentType"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new TextField<String>(Customer.Properties.travelDocumentNumber.name())
			.setLabel(new ResourceModel("customer.travelDocumentNumber"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));

		form.add(new CustomerDuplicateValidator(lastNameField, firstNameField, primaryPhoneNumber, dateOfBirthField));

		form.add(new SavePanel<Customer>(SAVE_BUTTON, form) {
			private static final long serialVersionUID = 1L;
			@Override
			public void preSubmit(AjaxRequestTarget target, Customer daoEntity, boolean newDaoEntity) {
				if (newDaoEntity && !hasRole(Role.ADMIN)) {
					daoEntity.setSystemUser(getSignedInSystemUser());
				}
			}
			@Override
			public void postSubmit(AjaxRequestTarget target, Customer daoEntity, boolean newDaoEntity) {
				if (newDaoEntity || hasRole(Role.ADMIN)) {
					setResponsePage(new CustomerPage(daoEntity));
				}
			}
		});

		form.add(new ResetPanel<Customer>(RESET_BUTTON, form));

		form.add(new Link<String>(CREATE_INVOICE_BUTTON) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(new InvoicePage(form.getModelObject()));
			}
		}.setVisible(form.getModelObject().isActive() && isVisible(form.getModelObject())));

    	add(createInvoicePanel(form));
	}

	private InvoicesPanel createInvoicePanel(Form<Customer> form) {
		InvoiceFilter invoiceFilter = new InvoiceFilter().setCustomer(form.getModelObject());
		if (!hasRole(Role.ADMIN)) {
			invoiceFilter.setSystemUser(getSignedInSystemUser());
		}
		InvoicesPanel invoicesPanel = new InvoicesPanel(INVOICES, Model.of(invoiceFilter));
		invoicesPanel.setVisible(isVisible(form.getModelObject()));
		return invoicesPanel;
	}

	private boolean isVisible(Customer customer) {
		return DaoEntityModelFactory.isPersisted(customer);
	}

	@Override
	public String getPageTitleKey() {
		return PAGE_TITLE;
	}

	private void initialize(Form<Customer> customerForm) {
		Customer customer = customerForm.getModelObject();
		if (StringUtils.isBlank(customer.getTravelDocumentType())) {
			customer.setTravelDocumentType(parameterRepository.getDefaultTravelDocumentType());
		}
	}
}
