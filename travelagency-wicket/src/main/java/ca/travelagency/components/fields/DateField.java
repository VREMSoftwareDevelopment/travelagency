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
package ca.travelagency.components.fields;

import java.util.Date;
import java.util.Map;

import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.IModel;

public class DateField extends org.apache.wicket.extensions.yui.calendar.DateField {
	private static final long serialVersionUID = 1L;

	public static final String DATE_COMPONENT_ID  = org.apache.wicket.extensions.yui.calendar.DateField.DATE;

	public DateField(String id) {
		this(id, null);
	}

	public DateField(String id, IModel<Date> model) {
		super(id, model);
	}

	@Override
	protected void configure(Map<String, Object> widgetProperties) {
		IModel<String> label = this.getLabel();
		if (label != null) {
			widgetProperties.put("title", label.getObject());
		}
		widgetProperties.put("navigator", true);
		super.configure(widgetProperties);
	}

	@Override
	public FormComponent<Date> setLabel(IModel<String> labelModel) {
		getDateTextField().setLabel(labelModel);
		return super.setLabel(labelModel);
	}
}
