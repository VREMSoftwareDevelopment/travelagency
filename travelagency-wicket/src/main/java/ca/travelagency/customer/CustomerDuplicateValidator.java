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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ca.travelagency.components.fields.DateField;
import ca.travelagency.utils.DateUtils;

public class CustomerDuplicateValidator extends AbstractFormValidator {
	private static final long serialVersionUID = 1L;

	static final String KEY = "CustomerDuplicateValidator";
	static final String DATE_OF_BIRTH = "dateOfBirth";
	static final String PRIMARY_PHONE = "primaryPhone";
	static final String FIRST_NAME = "firstName";
	static final String LAST_NAME = "lastName";

	@SpringBean
	private CustomerService customerService;

	private LastNameField lastNameField;
	private FirstNameField firstNameField;
	private TextField<String> primaryPhoneNumber;
	private DateField dateOfBirth;

	public CustomerDuplicateValidator(
			LastNameField lastNameField, FirstNameField firstNameField,
			TextField<String> primaryPhoneNumber, DateField dateOfBirth) {
		super();
        Injector.get().inject(this);

		Validate.notNull(lastNameField);
		Validate.notNull(firstNameField);
		Validate.notNull(primaryPhoneNumber);
		Validate.notNull(dateOfBirth);
		this.lastNameField = lastNameField;
		this.firstNameField = firstNameField;
		this.primaryPhoneNumber = primaryPhoneNumber;
		this.dateOfBirth = dateOfBirth;
	}

	@Override
	public FormComponent<?>[] getDependentFormComponents() {
		return new FormComponent<?>[] {	lastNameField, firstNameField, primaryPhoneNumber, dateOfBirth };
	}

	@Override
	public void validate(Form<?> form) {
		Customer customer = (Customer) form.getModelObject();
		Date date = dateOfBirth.getConvertedInput();
		customer.setDateOfBirth(date);
		boolean duplicated = customerService.duplicated(customer);
		if (duplicated) {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(LAST_NAME, customer.getLastName());
			parameters.put(FIRST_NAME, customer.getFirstName());
			parameters.put(PRIMARY_PHONE, customer.getPrimaryPhone());
			parameters.put(DATE_OF_BIRTH, DateUtils.formatDate(date));
			String error = form.getLocalizer().getString(KEY, form);
			form.error(error, parameters);
		}
	}

}
