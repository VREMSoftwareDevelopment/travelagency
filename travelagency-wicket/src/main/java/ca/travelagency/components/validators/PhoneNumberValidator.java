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
package ca.travelagency.components.validators;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.StringValidator;

import ca.travelagency.utils.PhoneNumberUtils;

public class PhoneNumberValidator extends StringValidator {
	private static final long serialVersionUID = 1L;

	public PhoneNumberValidator() {
		super();
	}

	@Override
	public void validate(IValidatable<String> validatable) {
		if (!PhoneNumberUtils.isValid(validatable.getValue())) {
			ValidationError error = new ValidationError(this);
			validatable.error(decorate(error, validatable));
		}
	}

}
