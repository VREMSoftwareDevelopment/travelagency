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
package ca.travelagency.components.decorators;

import org.apache.wicket.Component;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BlockUIDecoratorTest {
	@Mock private Component component;

	private BlockUIDecorator fixture;

	@Before
	public void setUp() throws Exception {
		fixture = new BlockUIDecorator();
	}

	@Test
	public void testCompleteJS() throws Exception {
		// execute
		CharSequence actual = fixture.getCompleteHandler(component);
		// validate
		Assert.assertEquals(BlockUIDecorator.UI_UNBLOCK, actual.toString());
	}

	@Test
	public void testBeforeSendHandlerJS() throws Exception {
		// execute
		CharSequence actual = fixture.getBeforeSendHandler(component);
		// validate
		Assert.assertEquals(BlockUIDecorator.UI_BLOCK, actual.toString());
	}

	@Test
	public void testEmptyJS() throws Exception {
		// execute & validate
		Assert.assertNull(fixture.getAfterHandler(component));
		Assert.assertNull(fixture.getBeforeHandler(component));
		Assert.assertNull(fixture.getFailureHandler(component));
		Assert.assertNull(fixture.getPrecondition(component));
		Assert.assertNull(fixture.getSuccessHandler(component));
	}
}
