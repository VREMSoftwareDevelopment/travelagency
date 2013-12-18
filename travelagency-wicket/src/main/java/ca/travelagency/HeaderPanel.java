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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import ca.travelagency.authentication.SignOutPage;
import ca.travelagency.identity.SystemUser;

public class HeaderPanel extends Panel {
	private static final long serialVersionUID = 1L;

	static final String SIGN_IN_NAME = "signInName";
	static final String SIGN_OUT = "signOut";
	static final String HOME_PAGE = "homePage";
	static final String LOGO_IMAGE = "logoImage";

	static final String WELCOME_GREETINGS = "welcome.greetings";

	public HeaderPanel(String id, SystemUser systemUser, boolean isSignedIn) {
		super(id);

		BookmarkablePageLink<String> homePage = new BookmarkablePageLink<String>(HOME_PAGE, getApplication().getHomePage());
		add(homePage);

		homePage.add(new LogoImage(LOGO_IMAGE));

		StringResourceModel model = new StringResourceModel(WELCOME_GREETINGS, this, Model.of(systemUser));
		add(new Label(SIGN_IN_NAME, model).setVisible(isSignedIn));

		add(new BookmarkablePageLink<String>(SIGN_OUT, SignOutPage.class).setVisible(isSignedIn));
	}
}
