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
package ca.travelagency.components.fields;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.utils.StringUtils;

public class CancelDepartureFieldTest extends BaseWicketTester {

	@Test
	public void testRender() throws Exception {
		// setup
		CancelDepartureField fixture = new CancelDepartureField(COMPONENT_ID);
		// execute
		Iterator<String> choices = fixture.getChoices("xyz");
		// validate
		String first = choices.next();
		String last = StringUtils.EMPTY;
		while(choices.hasNext()) {
			last = choices.next();
		}
		Assert.assertEquals("Non-Refundable", first);
		Assert.assertEquals("$50", last);
	}
}
