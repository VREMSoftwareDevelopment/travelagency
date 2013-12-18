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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.javascript.JSUtils;
import ca.travelagency.identity.SystemUser;


public class SaveButtonTest extends BaseWicketTester {
	@Mock private SaveButtonCallback<SystemUser> callback;
	@Mock private AjaxRequestTarget target;

	private Form<SystemUser> form;
	private SaveButton<SystemUser> fixture;
	private SystemUser systemUser;

	@Before
	public void setUp() throws Exception {
		form = makeTestForm();
		systemUser = form.getModelObject();
		fixture = new SaveButton<SystemUser>("id", form, callback, null);
		tester.startComponentInPage(fixture);
	}

	@Test
	public void testOnSubmit() throws Exception {
		// execute
		fixture.onSubmit(target, form);
		// validate
		Mockito.verify(target).appendJavaScript(JSUtils.INITIALIZE);
		Mockito.verify(callback).preSubmit(target, systemUser, false);
		Mockito.verify(callback).postSubmit(target, systemUser, false);
	}

	@Test
	public void testOnError() throws Exception {
		// execute
		fixture.onError(target, form);
		// validate
		Mockito.verify(target).appendJavaScript(JSUtils.INITIALIZE);
		Mockito.verify(callback, Mockito.never()).preSubmit(target, systemUser, false);
		Mockito.verify(callback, Mockito.never()).postSubmit(target, systemUser, false);
	}

	@Test
	public void testOnSubmitResetsModel() throws Exception {
		// setup
		fixture.setResetModelAfterSubmit(true);
		// execute
		fixture.onSubmit(target, form);
		// validate
		Assert.assertNull(form.getModelObject().getId());
	}

	@Test
	public void testOnSubmitDoesNotResetModel() throws Exception {
		// setup
		fixture.setResetModelAfterSubmit(false);
		// execute
		fixture.onSubmit(target, form);
		// validate
		Assert.assertEquals(systemUser, form.getModelObject());
	}
}