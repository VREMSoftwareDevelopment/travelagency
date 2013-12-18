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
package ca.travelagency;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.junit.Before;
import org.junit.Test;

import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;

public class HeaderPanelTest extends BaseWicketTester {
	private SystemUser systemUser;

	@Before
	public void setUp() {
		systemUser = SystemUserHelper.makeSystemUser();
	}

	@Test
	public void testComponents() {
		HeaderPanel headerPanel = new HeaderPanel(COMPONENT_ID, systemUser, true);
		tester.startComponentInPage(headerPanel);

		tester.assertComponent(COMPONENT_PATH+HeaderPanel.HOME_PAGE, BookmarkablePageLink.class);

		tester.assertInvisible(COMPONENT_PATH+HeaderPanel.HOME_PAGE+BasePage.PATH_SEPARATOR+HeaderPanel.LOGO_IMAGE);

		tester.assertLabel(COMPONENT_PATH+HeaderPanel.SIGN_IN_NAME, "Welcome, "+systemUser.getName());
		tester.assertComponent(COMPONENT_PATH+HeaderPanel.SIGN_OUT, BookmarkablePageLink.class);

	}

	@Test
	public void testSignOutComponents() {
		HeaderPanel footerPanel = new HeaderPanel(COMPONENT_ID, systemUser, false);
		tester.startComponentInPage(footerPanel);

		tester.assertInvisible(COMPONENT_PATH+HeaderPanel.SIGN_IN_NAME);
		tester.assertInvisible(COMPONENT_PATH+HeaderPanel.SIGN_OUT);
	}
}
