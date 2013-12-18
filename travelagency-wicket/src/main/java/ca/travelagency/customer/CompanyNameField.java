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

import ca.travelagency.components.fields.AutoCompleteField;

public class CompanyNameField extends AutoCompleteField<Customer> {
	private static final long serialVersionUID = 1L;

	public CompanyNameField(String id) {
		super(id, Customer.class);
	}

	@Override
	protected String getPropertyName() {
		return Customer.Properties.companyName.name();
	}
}
