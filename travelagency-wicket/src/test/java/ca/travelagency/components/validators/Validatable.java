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

import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidationError;

class Validatable<T> implements IValidatable<T> {
	private boolean isValid = true;
	private T value;
	Validatable(T value) {
		this.value = value;
	}
	@Override
	public T getValue() {
		return value;
	}
	@Override
	public void error(IValidationError error) {
		isValid = false;
	}
	@Override
	public boolean isValid() {
		return isValid;
	}
	@Override
	public IModel<T> getModel() {
		return null;
	}
}
