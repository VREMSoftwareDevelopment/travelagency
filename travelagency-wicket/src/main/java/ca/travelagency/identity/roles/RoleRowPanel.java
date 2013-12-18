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
package ca.travelagency.identity.roles;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import ca.travelagency.components.formdetail.DeletePanel;
import ca.travelagency.identity.SystemUserRole;

public class RoleRowPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public static final String DELETE_BUTTON = "deleteButton";

	public RoleRowPanel(final String id, IModel<SystemUserRole> model, final RolesPanel rolesPanel) {
		super(id, model);
		setOutputMarkupId(true);

		add(new Label(SystemUserRole.Properties.role.name()));
		add(new DeletePanel<SystemUserRole>(DELETE_BUTTON, getSystemUserRole(), rolesPanel) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				rolesPanel.delete(target, getSystemUserRole());
			}
		}.setVisible(rolesPanel.isEditable()));
	}

	private SystemUserRole getSystemUserRole() {
		return (SystemUserRole) getDefaultModelObject();
	}
}
