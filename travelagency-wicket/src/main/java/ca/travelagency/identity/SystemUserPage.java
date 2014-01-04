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

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import ca.travelagency.BasePage;
import ca.travelagency.components.behaviors.AjaxOnBlurBehavior;
import ca.travelagency.components.decorators.FieldDecorator;
import ca.travelagency.components.fields.StringFieldHelper;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.components.formheader.ResetPanel;
import ca.travelagency.components.formheader.SavePanel;
import ca.travelagency.components.validators.DuplicateValidator;
import ca.travelagency.components.validators.PhoneNumberValidator;
import ca.travelagency.identity.roles.RolesPanel;
import ca.travelagency.persistence.DaoEntity;

@AuthorizeInstantiation({"AGENT"})
public class SystemUserPage extends BasePage {

	private static final long serialVersionUID = 1L;

	static final String FORM = "form";
	static final String SAVE_BUTTON = "saveButton";
	static final String RESET_BUTTON = "resetButton";
	static final String FEEDBACK = "feedback";
	static final String CONFIRM_PASSWORD = "confirmPassword";

	static final String DETAILS = "details";

	static final String PAGE_TITLE = "systemUser.title";

	public SystemUserPage() {
		super();
		init(null);
	}

	public SystemUserPage(SystemUser systemUser) {
		super();
		init(systemUser);
	}

	public SystemUserPage(String myProfilePage) {
		super();
		init(getSignedInSystemUser());
	}

	private void init(SystemUser systemUser) {
		Form<SystemUser> systemUserForm = makeSystemUserForm(systemUser);
		add(systemUserForm);

		addSystemUserDetails(systemUserForm);
	}

	private Form<SystemUser> makeSystemUserForm(SystemUser systemUser) {
		final Form<SystemUser> form = new Form<SystemUser>(FORM, DaoEntityModelFactory.make(systemUser, SystemUser.class));
		form.setOutputMarkupId(true);

		form.add(new ComponentFeedbackPanel(FEEDBACK, form));

		RequiredTextField<String> name = new RequiredTextField<String>(DaoEntity.PROPERTY_NAME);
		name.setLabel(new ResourceModel("systemUser.name"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehavior(), new DuplicateValidator<SystemUser>(form));
		form.add(name);
		form.add(new TextField<String>(SystemUser.Properties.lastName.name())
			.setLabel(new ResourceModel("systemUser.lastName"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehavior()));
		form.add(new TextField<String>(SystemUser.Properties.firstName.name())
			.setLabel(new ResourceModel("systemUser.firstName"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehavior()));
		form.add(new TextField<String>(SystemUser.Properties.email.name())
			.setLabel(new ResourceModel("systemUser.email"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(EmailAddressValidator.getInstance())
			.add(new FieldDecorator(), new AjaxOnBlurBehavior()));
		form.add(new TextField<String>(SystemUser.Properties.primaryPhone.name())
			.setLabel(new ResourceModel("systemUser.primaryPhone"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new PhoneNumberValidator(), new FieldDecorator(), new AjaxOnBlurBehavior()));
		form.add(new TextField<String>(SystemUser.Properties.secondaryPhone.name())
			.setLabel(new ResourceModel("systemUser.secondaryPhone"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new PhoneNumberValidator(), new FieldDecorator(), new AjaxOnBlurBehavior()));
		form.add(new CheckBox(SystemUser.Properties.active.name())
			.setLabel(new ResourceModel("systemUser.active"))
			.setVisible(hasRole(Role.ADMIN))
			.add(new FieldDecorator(), new AjaxOnBlurBehavior()));
		form.add(new NumberTextField<Double>(SystemUser.Properties.commissionRate.name())
			.setMinimum(0.00)
			.setMaximum(999.99)
			.setLabel(new ResourceModel("systemUser.commissionRate"))
			.setVisible(hasRole(Role.ADMIN))
			.add(new FieldDecorator(), new AjaxOnBlurBehavior()));

		addPasswordFields(form);

		form.add(new SavePanel<SystemUser>(SAVE_BUTTON, form) {
			private static final long serialVersionUID = 1L;
			@Override
			public void preSubmit(AjaxRequestTarget target, SystemUser daoEntity, boolean newDaoEntity) {
				if (!newDaoEntity) {
					return;
				}
				daoEntity.addSystemUserRole(SystemUserRole.make(Role.AGENT));
			}
			@Override
			public void postSubmit(AjaxRequestTarget target, SystemUser daoEntity, boolean newDaoEntity) {
				if (!newDaoEntity) {
					return;
				}
				setResponsePage(new SystemUserPage(daoEntity));
			}
    	});

		form.add(new ResetPanel<SystemUser>(RESET_BUTTON, form));

		return form;
	}


	private void addPasswordFields(Form<SystemUser> form) {
		boolean systemUserExist = DaoEntityModelFactory.isPersisted(form.getModelObject());

		PasswordTextField passwordField = new PasswordTextField(SystemUser.Properties.password.name());
		form.add(passwordField);

		PasswordTextField confirmPasswordField = new PasswordTextField(CONFIRM_PASSWORD, new Model<String>());
		form.add(confirmPasswordField);

		if (systemUserExist) {
			passwordField.setVisible(false);
			confirmPasswordField.setVisible(false);
			return;
		}

		passwordField.setLabel(new ResourceModel("password")).add(new FieldDecorator());
		confirmPasswordField.setLabel(new ResourceModel("confirmPassword")).add(new FieldDecorator());
		form.add(new EqualPasswordInputValidator(passwordField, confirmPasswordField));
	}

	private void addSystemUserDetails(Form<SystemUser> form) {
		final IModel<SystemUser> model = form.getModel();
		boolean persisted = DaoEntityModelFactory.isPersisted(model.getObject());

		List<ITab> tabs = new ArrayList<ITab>();
		final AjaxTabbedPanel<ITab> details = new AjaxTabbedPanel<ITab>(DETAILS, tabs);
		details.setOutputMarkupId(true);
		details.setVisible(persisted);
		add(details);

		if (persisted) {
			if (hasRole(Role.ADMIN)) {
				AbstractTab rolesTab = new AbstractTab(new ResourceModel("systemUser.panel.roles")) {
					private static final long serialVersionUID = 1L;
					@Override
					public Panel getPanel(String panelId) {
						return new RolesPanel(panelId, model);
					}
				};
				tabs.add(rolesTab);
			}
			AbstractTab resetPasswordTab = new AbstractTab(new ResourceModel("systemUser.panel.resetPassword")) {
				private static final long serialVersionUID = 1L;
				@Override
				public Panel getPanel(String panelId) {
					return new ResetPasswordPanel(panelId, model);
				}
			};
			tabs.add(resetPasswordTab);
		}
	}

	@Override
	public String getPageTitleKey() {
		return PAGE_TITLE;
	}
}
