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

import org.junit.Assert;
import org.junit.Test;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.identity.SystemUser;


public class ResetPanelTest extends BaseWicketTester {
	@Test
	public void testComponents() throws Exception {
		// setup
		ResetPanel<SystemUser> fixture = new ResetPanel<SystemUser>(COMPONENT_ID, makeTestForm());
		// execute
		tester.startComponentInPage(fixture);
		// validate
		tester.assertComponent(COMPONENT_PATH+ResetPanel.RESET_BUTTON, ResetButton.class);
	}

	@Test
	public void testResetModel() throws Exception {
		// setup
		ResetPanel<SystemUser> fixture = new ResetPanel<SystemUser>(COMPONENT_ID, makeTestForm());
		// expected
		Assert.assertFalse(fixture.getResetButton().isResetModel());
		// execute
		fixture.setResetModel(true);
		// validate
		Assert.assertTrue(fixture.getResetButton().isResetModel());
	}
}
