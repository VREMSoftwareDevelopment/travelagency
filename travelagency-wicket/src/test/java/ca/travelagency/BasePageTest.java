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
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import ca.travelagency.navigation.NavigationMenu;
import ca.travelagency.utils.StringUtils;

public class BasePageTest extends BaseWicketTester {
	@Mock private IEvent<AjaxRequestTarget> event;
	@Mock private AjaxRequestTarget ajaxRequestTarget;

	private BasePage fixture;

	@Before
	public void setUp() {
		fixture = new BasePage();
	}

	@Test
	public void testBasePageRendersSuccessfully() {
		tester.startPage(fixture);
		tester.assertRenderedPage(BasePage.class);

		tester.assertComponent(BasePage.NAVIGATION_MENU, NavigationMenu.class);
		tester.assertComponent(BasePage.HEADER_PANEL, HeaderPanel.class);
		tester.assertComponent(BasePage.FOOTER_PANEL, FooterPanel.class);

		tester.assertLabel(BasePage.TITLE, BasePage.WINDOWS_TITLE + BasePage.class.getSimpleName());
		tester.assertLabel(BasePage.PAGE_TITLE, StringUtils.EMPTY);
		tester.assertComponent(BasePage.PAGE_FEEDBACK, FeedbackPanel.class);
	}

	@Test
	public void testPageFeedbackIsUpdateOnEvent() throws Exception {
		// setup
		FeedbackPanel feedbackPanel = fixture.getFeedbackPanel();
		// expected
		Mockito.when(event.getPayload()).thenReturn(ajaxRequestTarget);
		// execute
		fixture.onEvent(event);
		// validate
		Mockito.verify(event).getPayload();
		Mockito.verify(ajaxRequestTarget).add(feedbackPanel);
	}
}