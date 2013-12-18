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
package ca.travelagency.identity;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import ca.travelagency.components.decorators.FieldDecorator;
import ca.travelagency.components.fields.StringFieldHelper;
import ca.travelagency.components.formheader.SavePanel;
import ca.travelagency.components.javascript.JSUtils;

public class ResetPasswordPanel extends Panel {
	private static final long serialVersionUID = 1L;

	static final String FEEDBACK = "feedback";
	static final String FORM = "form";
	static final String NEW_PASSWORD = "newPassword";
	static final String CONFIRM_PASSWORD = "confirmPassword";
	static final String SAVE_BUTTON = "saveButton";

	public ResetPasswordPanel(String id, IModel<SystemUser> model) {
		super(id);

		Form<SystemUser> form = new Form<SystemUser>(FORM, model);
		form.setOutputMarkupId(true);
		add(form);

		form.add(new ComponentFeedbackPanel(FEEDBACK, form));

		final PasswordTextField newPasswordField = new PasswordTextField(NEW_PASSWORD, new Model<String>());
		newPasswordField.setLabel(new ResourceModel("password"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator());
		form.add(newPasswordField);

		PasswordTextField confirmPasswordField = new PasswordTextField(CONFIRM_PASSWORD, new Model<String>());
		confirmPasswordField.setLabel(new ResourceModel("confirmPassword"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator());
		form.add(confirmPasswordField);

		form.add(new EqualPasswordInputValidator(newPasswordField, confirmPasswordField));

		form.add(new SavePanel<SystemUser>(SAVE_BUTTON, form) {
			private static final long serialVersionUID = 1L;
			@Override
			public void preSubmit(AjaxRequestTarget target, SystemUser daoEntity, boolean newDaoEntity) {
				daoEntity.setPassword(newPasswordField.getConvertedInput());
			}
			@Override
			public void setMessage(SystemUser daoEntity) {
				String message = getLocalizer().getString("passwordReset.message", ResetPasswordPanel.this);
				getSession().success(message + " " + daoEntity.getName());
			}
		});
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(OnDomReadyHeaderItem.forScript(JSUtils.INITIALIZE));
	}
}
