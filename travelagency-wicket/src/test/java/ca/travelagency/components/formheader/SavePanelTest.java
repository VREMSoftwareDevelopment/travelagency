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
package ca.travelagency.components.formheader;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;


public class SavePanelTest extends BaseWicketTester {

	private SavePanel<SystemUser> fixture;
	private Form<SystemUser> form;

	@Before
	public void setUp() throws Exception {
		form = makeTestForm();
		fixture = new SavePanel<SystemUser>(COMPONENT_ID, form);
	}

	@Test
	public void testComponents() throws Exception {
		// execute
		tester.startComponentInPage(fixture);
		// validate
		tester.assertComponent(COMPONENT_PATH+SavePanel.SAVE_BUTTON, SaveButton.class);
	}

	@Test
	public void testResetModelAfterSubmit() throws Exception {
		// expected
		Assert.assertFalse(fixture.getSaveButton().isResetModelAfterSubmit());
		// execute
		fixture.setResetModelAfterSubmit(true);
		// validate
		Assert.assertTrue(fixture.getSaveButton().isResetModelAfterSubmit());
	}

	@Test
	public void testPanelToUpdate() throws Exception {
		// setup
		Panel testPanel = new Panel("panelId") {private static final long serialVersionUID = 1L;};
		SavePanel<SystemUser> fixture = new SavePanel<SystemUser>(COMPONENT_ID, form, testPanel);
		// execute
		Component actual = fixture.getSaveButton().getComponentToUpdate();
		// validate
		Assert.assertEquals(testPanel, actual);
	}

	@Test
	public void testSavedMessage() throws Exception {
		// setup
		SystemUser systemUser = SystemUserHelper.makeSystemUser();
		String[] expected = new String [] {systemUser.getName()+" saved."};
		// execute
		fixture.setMessage(systemUser);
		// validate
		assertSuccessMessages(expected);
	}

}
