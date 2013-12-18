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
package ca.travelagency.components;

import org.apache.wicket.markup.html.image.Image;
import org.junit.Test;

import ca.travelagency.BaseWicketTester;

public class CheckMarkPanelTest extends BaseWicketTester {
	@Test
	public void testComponents() throws Exception {
		// setup
		CheckMarkPanel fixture = new CheckMarkPanel(COMPONENT_ID, true);
		// execute
		tester.startComponentInPage(fixture);
		// expected
		tester.assertComponent(COMPONENT_PATH+CheckMarkPanel.IMAGE, Image.class);
	}

}
