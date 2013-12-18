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

import java.util.Arrays;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import ca.travelagency.components.behaviours.AjaxOnBlurBehaviour;
import ca.travelagency.components.decorators.FieldDecorator;
import ca.travelagency.components.formdetail.SavePanelDetail;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.components.formheader.ResetPanel;
import ca.travelagency.identity.Role;
import ca.travelagency.identity.SystemUserRole;

@AuthorizeInstantiation({"ADMIN"})
public class RoleFormPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public static final String FORM = "form";
	public static final String FEEDBACK = "feedback";
	public static final String SAVE_BUTTON = "saveButton";
	public static final String RESET_BUTTON = "resetButton";

	public RoleFormPanel(String id, final RolesPanel rolesPanel) {
		super(id);
		IModel<SystemUserRole> model = DaoEntityModelFactory.make(SystemUserRole.class);
		Form<SystemUserRole> roleForm = new Form<SystemUserRole>(FORM, model);
		roleForm.setOutputMarkupId(true);

		roleForm.add(new ComponentFeedbackPanel(FEEDBACK, roleForm));

		roleForm.add(new DropDownChoice<Role>(SystemUserRole.Properties.role.name(), Arrays.asList(Role.values()))
			.setLabel(new ResourceModel("systemUser.role"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));

		roleForm.add(new SavePanelDetail<SystemUserRole>(SAVE_BUTTON, roleForm) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<SystemUserRole> form) {
				SystemUserRole systemUserRole = form.getModelObject();
				rolesPanel.update(target, systemUserRole);
				form.setDefaultModel(DaoEntityModelFactory.make(SystemUserRole.class));
			}
		});

		roleForm.add(new ResetPanel<SystemUserRole>(RESET_BUTTON, roleForm));

		add(roleForm);
	}

}
