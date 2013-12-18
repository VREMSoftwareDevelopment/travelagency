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
package ca.travelagency.components;

import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.model.IModel;

import ca.travelagency.utils.StringUtils;

public class NumberPropertyColumn<T> extends PropertyColumn<T, String> {
	private static final long serialVersionUID = 1L;

	public NumberPropertyColumn(IModel<String> displayModel, String sortProperty, String propertyExpression){
		super(displayModel, sortProperty, propertyExpression);
	}

	public NumberPropertyColumn(IModel<String> displayModel, String propertyExpression) {
		this(displayModel, null, propertyExpression);
	}

	@Override
	public String getCssClass() {
		String cssClass = super.getCssClass();
		if (cssClass == null) {
			cssClass = StringUtils.EMPTY;
		}
		return cssClass + " numberAlign";
	}
}

