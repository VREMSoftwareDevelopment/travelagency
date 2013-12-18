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
package ca.travelagency.components.javascript;

import org.apache.wicket.Component;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ConfirmDialogTest {
	@Mock private Component component;

	private static final String THIS_MY_MESSAGE = "THIS MY MESSAGE";
	private ConfirmDialog fixture;

	@Before
	public void setUp() throws Exception {
		fixture = new ConfirmDialog(THIS_MY_MESSAGE);
	}

	@Test
	public void testPreconditionJS() throws Exception {
		// execute
		CharSequence actual = fixture.getPrecondition(component);
		// validate
		Assert.assertEquals(ConfirmDialog.PREFIX+THIS_MY_MESSAGE+ConfirmDialog.POSTFIX, actual.toString());
	}

	@Test
	public void testEmptyJS() throws Exception {
		// execute & validate
		Assert.assertNull(fixture.getAfterHandler(component));
		Assert.assertNull(fixture.getBeforeHandler(component));
		Assert.assertNull(fixture.getBeforeSendHandler(component));
		Assert.assertNull(fixture.getCompleteHandler(component));
		Assert.assertNull(fixture.getFailureHandler(component));
		Assert.assertNull(fixture.getSuccessHandler(component));
	}
}
