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

import org.apache.wicket.markup.html.panel.Panel;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;

public class DeletePanelTest extends BaseWicketTester {
	@Test
	public void testOnClick() throws Exception {
		// setup
		Panel component = new Panel("test") {private static final long serialVersionUID = 1L;};
		component.setOutputMarkupId(true);
		SystemUser systemUser = SystemUserHelper.makeSystemUser();
		DeletePanel<SystemUser> fixture = new DeletePanel<SystemUser>(COMPONENT_ID, systemUser, component);
		tester.startComponentInPage(fixture);
		// expected
		Mockito.stub(daoSupport.find(systemUser.getTrueClass(), systemUser.getId())).toReturn(systemUser);
		tester.assertComponent(COMPONENT_PATH+DeletePanel.LINK, DeleteLink.class);
		// execute
		tester.executeAjaxEvent(COMPONENT_PATH+DeletePanel.LINK, "onclick");
		// validate
		Mockito.verify(daoSupport).remove(systemUser);
	}
}
