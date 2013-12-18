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

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;

import ca.travelagency.BasePage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.components.formheader.SavePanel;

public class ResetPasswordPanelTest extends BaseWicketTester {
	public static final String PATH = COMPONENT_PATH+ResetPasswordPanel.FORM+BasePage.PATH_SEPARATOR;
	public static final String SAVE_PATH = PATH+ResetPasswordPanel.SAVE_BUTTON+BasePage.PATH_SEPARATOR+SavePanel.SAVE_BUTTON;

	private SystemUser systemUser;

	@Before
	public void setUp() {
		systemUser = SystemUserHelper.makeSystemUser();

		ResetPasswordPanel resetPasswordPanel = new ResetPasswordPanel(COMPONENT_ID, DaoEntityModelFactory.make(systemUser));
		tester.startComponentInPage(resetPasswordPanel);
	}

	@Test
	public void testResetPasswordFormComponents() {
		tester.assertComponent(COMPONENT_PATH+ResetPasswordPanel.FORM, Form.class);
		tester.assertComponent(PATH+ResetPasswordPanel.SAVE_BUTTON, SavePanel.class);

		tester.assertComponent(PATH+ResetPasswordPanel.NEW_PASSWORD, PasswordTextField.class);
		tester.assertComponent(PATH+ResetPasswordPanel.CONFIRM_PASSWORD, PasswordTextField.class);
	}

	@Test
	public void testVerifyRequiredFields() throws Exception {
		tester.executeAjaxEvent(SAVE_PATH, "onclick");

		tester.assertErrorMessages(new String [] {
			"'Password' is required.",
			"'Confirm Password' is required.",
		});
	}

	@Test
	public void testVerifyValidFields() throws Exception {
		FormTester formTester = tester.newFormTester(COMPONENT_PATH+ResetPasswordPanel.FORM);

		formTester.setValue(ResetPasswordPanel.NEW_PASSWORD, "password");
		formTester.setValue(ResetPasswordPanel.CONFIRM_PASSWORD, "not-password");

		tester.executeAjaxEvent(SAVE_PATH, "onclick");

		tester.assertErrorMessages(new String [] {
			"Password and Confirm Password must be equal."
		});
	}
}
