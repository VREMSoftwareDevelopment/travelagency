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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.AjaxLinkCallback;
import ca.travelagency.components.javascript.JSUtils;

public class DeleteLinkTest extends BaseWicketTester {
	@Mock private AjaxLinkCallback ajaxLinkCallback;
	@Mock private AjaxRequestTarget target;

	@Test
	public void testOnClick() throws Exception {
		// setup
		Panel component = new Panel("test") {private static final long serialVersionUID = 1L;};
		component.setOutputMarkupId(true);
		DeleteLink fixture = new DeleteLink("id", ajaxLinkCallback, component);
		tester.startComponentInPage(fixture);
		// execute
		fixture.onClick(target);
		// validate
		Mockito.verify(target).appendJavaScript(JSUtils.INITIALIZE);
		Mockito.verify(ajaxLinkCallback).onClick(target);
	}
}
