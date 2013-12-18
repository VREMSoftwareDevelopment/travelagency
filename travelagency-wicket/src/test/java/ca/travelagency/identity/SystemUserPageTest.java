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

import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BasePage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.formheader.ResetPanel;
import ca.travelagency.components.formheader.SavePanel;
import ca.travelagency.identity.roles.RolesPanel;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.utils.StringUtils;

public class SystemUserPageTest extends BaseWicketTester {
	private static final String PATH = SystemUserPage.FORM + SystemUserPage.PATH_SEPARATOR;
	private static final String RESET_PATH = PATH+SystemUserPage.RESET_BUTTON+BasePage.PATH_SEPARATOR+ResetPanel.RESET_BUTTON;
	private static final String SAVE_PATH = PATH+SystemUserPage.SAVE_BUTTON+BasePage.PATH_SEPARATOR+SavePanel.SAVE_BUTTON;
	private static final String PASSWORD = "PASSWORD";

	@Test
	public void testNewSystemUserComponents() {
		tester.startPage(SystemUserPage.class);
		tester.assertRenderedPage(SystemUserPage.class);

		tester.assertComponent(SystemUserPage.FORM, Form.class);

		tester.assertComponent(PATH+SystemUserPage.SAVE_BUTTON, SavePanel.class);
		tester.assertComponent(PATH+SystemUserPage.RESET_BUTTON, ResetPanel.class);
		tester.assertComponent(PATH+SystemUserPage.FEEDBACK, ComponentFeedbackPanel.class);

		tester.assertComponent(PATH+DaoEntity.PROPERTY_NAME, RequiredTextField.class);

		tester.assertComponent(PATH+SystemUser.Properties.lastName, TextField.class);
		tester.assertComponent(PATH+SystemUser.Properties.firstName, TextField.class);
		tester.assertComponent(PATH+SystemUser.Properties.email, TextField.class);
		tester.assertComponent(PATH+SystemUser.Properties.primaryPhone, TextField.class);
		tester.assertComponent(PATH+SystemUser.Properties.secondaryPhone, TextField.class);
		tester.assertComponent(PATH+SystemUser.Properties.commissionRate, NumberTextField.class);
		tester.assertComponent(PATH+SystemUser.Properties.active, CheckBox.class);

		tester.assertComponent(PATH+SystemUser.Properties.password, PasswordTextField.class);
		tester.assertComponent(PATH+SystemUserPage.CONFIRM_PASSWORD, PasswordTextField.class);

		tester.isInvisible(SystemUserPage.DETAILS);
	}

	@Test
	public void testExistingSystemUserComponents() {
		SystemUser systemUser = SystemUserHelper.makeSystemUser();

		Mockito.when(daoSupport.find(SystemUser.class, systemUser.getId())).thenReturn(systemUser);

		tester.startPage(new SystemUserPage(systemUser));
		tester.assertRenderedPage(SystemUserPage.class);

		tester.isInvisible(PATH+SystemUser.Properties.password);
		tester.isInvisible(PATH+SystemUserPage.CONFIRM_PASSWORD);

		tester.assertComponent(SystemUserPage.DETAILS, AjaxTabbedPanel.class);

		@SuppressWarnings("unchecked")
		AjaxTabbedPanel<? extends ITab> ajaxTabbedPanel= (AjaxTabbedPanel<? extends ITab>) tester.getComponentFromLastRenderedPage(SystemUserPage.DETAILS);

		List<? extends ITab> tabs = ajaxTabbedPanel.getTabs();
		Assert.assertEquals(2, tabs.size());
		validateTabPanel(tabs.get(0), RolesPanel.class);
		validateTabPanel(tabs.get(1), ResetPasswordPanel.class);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(SystemUser.class, systemUser.getId());
	}

	private void validateTabPanel(ITab tab, Class<?> clazz) {
		Assert.assertEquals(clazz.getName(), tab.getPanel("containerId").getClass().getName());
	}

	@Test
	public void testVerifyRequiredFields() throws Exception {
		// setup
		tester.startPage(SystemUserPage.class);
		tester.assertRenderedPage(SystemUserPage.class);

		FormTester formTester = tester.newFormTester(SystemUserPage.FORM);
		formTester.setValue(SystemUser.Properties.commissionRate.name(), StringUtils.EMPTY);

		tester.executeAjaxEvent(SAVE_PATH, "onclick");

		tester.assertErrorMessages(new String [] {
			"'User Name' is required.",
			"'Commission Rate' is required.",
			"'Password' is required.",
			"'Confirm Password' is required.",
		});
	}

	@Test
	public void testVerifyValidFields() throws Exception {
		// setup
		tester.startPage(SystemUserPage.class);
		tester.assertRenderedPage(SystemUserPage.class);

		FormTester formTester = tester.newFormTester(SystemUserPage.FORM);

		formTester.setValue(DaoEntity.PROPERTY_NAME, "User Name");
		formTester.setValue(SystemUser.Properties.email.name(), "EMAIL");
		formTester.setValue(SystemUser.Properties.primaryPhone.name(), "123-1234");
		formTester.setValue(SystemUser.Properties.secondaryPhone.name(), "123-1234");
		formTester.setValue(SystemUser.Properties.password.name(), "password");
		formTester.setValue(SystemUserPage.CONFIRM_PASSWORD, "not-password");

		tester.executeAjaxEvent(SAVE_PATH, "onclick");

		tester.assertErrorMessages(new String [] {
			"The value of 'Email' is not a valid email address.",
			"'Primary Phone' is not a valid phone number.",
			"'Secondary Phone' is not a valid phone number.",
			"Password and Confirm Password must be equal."
		});
	}

	@Test
	public void testExistingSystemUserValues() {
		SystemUser systemUser = SystemUserHelper.makeSystemUser();

		Mockito.when(daoSupport.find(SystemUser.class, systemUser.getId())).thenReturn(systemUser);

		tester.startPage(new SystemUserPage(systemUser));
		tester.assertRenderedPage(SystemUserPage.class);

		tester.assertModelValue(PATH+DaoEntity.PROPERTY_NAME, systemUser.getName());
		tester.assertModelValue(PATH+SystemUser.Properties.lastName, systemUser.getLastName());
		tester.assertModelValue(PATH+SystemUser.Properties.firstName, systemUser.getFirstName());
		tester.assertModelValue(PATH+SystemUser.Properties.active, systemUser.isActive());
		tester.assertModelValue(PATH+SystemUser.Properties.email, systemUser.getEmail());
		tester.assertModelValue(PATH+SystemUser.Properties.primaryPhone, systemUser.getPrimaryPhone());
		tester.assertModelValue(PATH+SystemUser.Properties.secondaryPhone, systemUser.getSecondaryPhone());

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(SystemUser.class, systemUser.getId());
	}

	@Test
	public void testExistingSystemUserWithSubmit() {
		SystemUser systemUser = SystemUserHelper.makeSystemUser();

		Mockito.when(daoSupport.find(SystemUser.class, systemUser.getId())).thenReturn(systemUser);

		tester.startPage(new SystemUserPage(systemUser));
		tester.assertRenderedPage(SystemUserPage.class);

		FormTester formTester = tester.newFormTester(SystemUserPage.FORM);
		formTester.setValue(DaoEntity.PROPERTY_NAME, systemUser.getName());
		formTester.setValue(SystemUser.Properties.lastName.name(), systemUser.getLastName());
		formTester.setValue(SystemUser.Properties.firstName.name(), systemUser.getFirstName());
		formTester.setValue(SystemUser.Properties.active.name(), systemUser.isActive());
		formTester.setValue(SystemUser.Properties.email.name(), systemUser.getEmail());
		formTester.setValue(SystemUser.Properties.primaryPhone.name(), systemUser.getPrimaryPhone());
		formTester.setValue(SystemUser.Properties.secondaryPhone.name(), systemUser.getSecondaryPhone());

		tester.executeAjaxEvent(SAVE_PATH, "onclick");

		assertSuccessMessages(new String [] {systemUser.getName()+" saved."});

		tester.assertRenderedPage(SystemUserPage.class);

		Mockito.verify(daoSupport, Mockito.times(3)).find(SystemUser.class, systemUser.getId());
		Mockito.verify(daoSupport).persist(systemUser);
	}

	@Test
	public void testExistingSystemUserWithReset() {
		SystemUser systemUser = SystemUserHelper.makeSystemUser();

		Mockito.when(daoSupport.find(SystemUser.class, systemUser.getId())).thenReturn(systemUser);

		tester.startPage(new SystemUserPage(systemUser));
		tester.assertRenderedPage(SystemUserPage.class);

		tester.executeAjaxEvent(RESET_PATH, "onclick");

		tester.assertRenderedPage(SystemUserPage.class);

		Mockito.verify(daoSupport, Mockito.never()).persist(systemUser);
	}

	@Test
	public void testDetailsAreVisibleAfterNewSystemUserIsSaved() {
		SystemUser systemUser = SystemUserHelper.makeSystemUser();

		tester.startPage(SystemUserPage.class);
		tester.assertRenderedPage(SystemUserPage.class);

		tester.isInvisible(SystemUserPage.DETAILS);

		FormTester formTester = tester.newFormTester(SystemUserPage.FORM);
		formTester.setValue(DaoEntity.PROPERTY_NAME, systemUser.getName());
		formTester.setValue(SystemUser.Properties.lastName.name(), systemUser.getLastName());
		formTester.setValue(SystemUser.Properties.firstName.name(), systemUser.getFirstName());
		formTester.setValue(SystemUser.Properties.active.name(), systemUser.isActive());
		formTester.setValue(SystemUser.Properties.email.name(), systemUser.getEmail());
		formTester.setValue(SystemUser.Properties.primaryPhone.name(), systemUser.getPrimaryPhone());
		formTester.setValue(SystemUser.Properties.secondaryPhone.name(), systemUser.getSecondaryPhone());
		formTester.setValue(SystemUser.Properties.secondaryPhone.name(), systemUser.getSecondaryPhone());
		formTester.setValue(SystemUser.Properties.password.name(), PASSWORD);
		formTester.setValue(SystemUserPage.CONFIRM_PASSWORD, PASSWORD);

		tester.executeAjaxEvent(SAVE_PATH, "onclick");

		assertSuccessMessages(new String [] {systemUser.getName()+" saved."});

		tester.assertRenderedPage(SystemUserPage.class);

		tester.isVisible(SystemUserPage.DETAILS);

		Mockito.verify(daoSupport, Mockito.never()).find(SystemUser.class, systemUser.getId());
		Mockito.verify(daoSupport).persist(systemUser);
	}

}
