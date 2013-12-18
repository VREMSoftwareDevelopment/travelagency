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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import ca.travelagency.authentication.AuthenticatedSession;
import ca.travelagency.identity.Role;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.navigation.NavigationMenu;
import ca.travelagency.utils.StringUtils;

public class BasePage extends WebPage {
	private static final long serialVersionUID = 1L;

	public static final int DATA_TABLE_PER_PAGE = 10;

	public static final String TITLE = "title";
	public static final String PAGE_TITLE = "pageTitle";
	public static final String PAGE_FEEDBACK = "pageFeedback";
	public static final String NAVIGATION_MENU = "navigationMenu";
	public static final String HEADER_PANEL = "headerPanel";
	public static final String FOOTER_PANEL = "footerPanel";
	public static final String WINDOWS_TITLE = "Travel Agency - ";

	protected FeedbackPanel feedbackPanel;

	public BasePage() {
		this(null);
	}

	public BasePage(IModel<?> model) {
		super(model);

		add(new HeaderPanel(HEADER_PANEL, getSignedInSystemUser(), isSignedIn()));
		add(new FooterPanel(FOOTER_PANEL));

		add(new NavigationMenu(NAVIGATION_MENU, this.getClass()).setVisible(isSignedIn()));

		add(new Label(TITLE, WINDOWS_TITLE + this.getClass().getSimpleName()));

		String pageTitleKey = getPageTitleKey();
		if (StringUtils.isBlank(pageTitleKey)) {
			add(new Label(PAGE_TITLE, StringUtils.EMPTY));
		} else {
			add(new Label(PAGE_TITLE, new ResourceModel(pageTitleKey)));
		}
		feedbackPanel = new FeedbackPanel(PAGE_FEEDBACK);
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);

		add(new DebugBar("debug").setVisible(getApplication().usesDevelopmentConfig()));
	}

	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);

		Object payload = event.getPayload();
		if (payload instanceof AjaxRequestTarget) {
			AjaxRequestTarget ajaxRequestTarget = (AjaxRequestTarget) payload;
			ajaxRequestTarget.add(feedbackPanel);
         }
	}

	public String getPageTitleKey() {
		return StringUtils.EMPTY;
	}

	protected boolean isSignedIn() {
		return getAuthenticatedSession().isSignedIn();
	}

	protected void signOut() {
		getAuthenticatedSession().signOut();
	}

	protected SystemUser getSignedInSystemUser() {
		return getAuthenticatedSession().getSystemUser();
	}

	protected boolean hasRole(Role role) {
		return getAuthenticatedSession().hasRole(role);
	}

	protected AuthenticatedSession getAuthenticatedSession() {
		return ((AuthenticatedSession) getSession());
	}

	protected FeedbackPanel getFeedbackPanel() {
		return feedbackPanel;
	}
}
