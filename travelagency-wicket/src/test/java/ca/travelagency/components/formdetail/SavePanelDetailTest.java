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
package ca.travelagency.components.formdetail;

import org.junit.Test;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.identity.SystemUser;


public class SavePanelDetailTest extends BaseWicketTester {
	@Test
	public void testComponents() throws Exception {
		// setup
		SavePanelDetail<SystemUser> fixture = new SavePanelDetail<SystemUser>(COMPONENT_ID, makeTestForm());
		tester.startComponentInPage(fixture);
		// expected
		tester.assertComponent(COMPONENT_PATH+SavePanelDetail.SAVE_BUTTON, SaveButtonDetail.class);
	}
}
