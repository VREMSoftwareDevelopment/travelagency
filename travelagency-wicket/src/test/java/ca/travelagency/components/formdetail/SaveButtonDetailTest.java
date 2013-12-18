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
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.Model;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.AjaxButtonCallback;
import ca.travelagency.components.javascript.JSUtils;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;


public class SaveButtonDetailTest extends BaseWicketTester {
	@Mock private AjaxButtonCallback<SystemUser> ajaxButtonCallback;
	@Mock private AjaxRequestTarget target;
	private Form<SystemUser> form;
	private SaveButtonDetail<SystemUser> fixture;

	@Before
	public void setUp() throws Exception {
		form = makeTestForm();
//		form.setOutputMarkupId(true);

		fixture = new SaveButtonDetail<SystemUser>("id", form, ajaxButtonCallback);

		tester.startComponentInPage(fixture);
	}

	@Test
	public void testOnSubmit() throws Exception {
		// execute
		fixture.onSubmit(target, form);
		// validate
		Mockito.verify(target).appendJavaScript(JSUtils.INITIALIZE);
		Mockito.verify(target).appendJavaScript(JSUtils.SHOW_ROW_CONTROLS);
		Mockito.verify(ajaxButtonCallback).onSubmit(target, form);
	}

	@Test
	public void testOnErrorNewObject() throws Exception {
		// setup
		form.setModelObject(SystemUserHelper.makeSystemUserWithoutId());
		// execute
		fixture.onError(target, form);
		// validate
		Mockito.verify(target).appendJavaScript(JSUtils.INITIALIZE);
		Mockito.verify(target).appendJavaScript(JSUtils.SHOW_ROW_CONTROLS);
		Mockito.verify(ajaxButtonCallback, Mockito.never()).onSubmit(target, form);
	}

	@Test
	public void testOnErrorExistingObject() throws Exception {
		// setup
		form.setModel(new Model<SystemUser>(SystemUserHelper.makeSystemUser()));
		// execute
		fixture.onError(target, form);
		// validate
		Mockito.verify(target).appendJavaScript(JSUtils.INITIALIZE);
		Mockito.verify(target).appendJavaScript(JSUtils.HIDE_ROW_CONTROLS);
		Mockito.verify(ajaxButtonCallback, Mockito.never()).onSubmit(target, form);
	}
}