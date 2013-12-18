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
package ca.travelagency;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;

import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;

public class TdFormPanel extends Panel {
	private static final long serialVersionUID = 1L;

	private static final String FORM_ID = "form";

	private Form<SystemUser> form;

	public TdFormPanel(String id) {
		super(id);
		form = new Form<SystemUser>(FORM_ID,
			new CompoundPropertyModel<SystemUser>(SystemUserHelper.makeSystemUser()));
		add(form);
	}

	public Form<SystemUser> getForm() {
		return form;
	}
}
