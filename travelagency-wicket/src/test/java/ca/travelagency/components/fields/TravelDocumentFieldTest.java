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
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

import ca.travelagency.BaseWicketTester;

public class TravelDocumentFieldTest extends BaseWicketTester {

	@Test
	public void testGetChoicesWithNoInput() throws Exception {
		TravelDocumentField fixture = new TravelDocumentField("id");
		Iterator<String> choices = fixture.getChoices(null);
		Assert.assertFalse(choices.hasNext());
	}

	@Test
	public void testRender() throws Exception {
		// setup
		TravelDocumentField fixture = new TravelDocumentField("id");
		// execute & validate
	    Assert.assertEquals("Canadian Passport", fixture.getChoices(Locale.CANADA.getDisplayCountry()).next());
	    Assert.assertEquals("Chinese Passport", fixture.getChoices(Locale.CHINA.getDisplayCountry()).next());
	    Assert.assertEquals("French Passport", fixture.getChoices(Locale.FRANCE.getDisplayCountry()).next());
	    Assert.assertEquals("German Passport", fixture.getChoices(Locale.GERMANY.getDisplayCountry()).next());
	    Assert.assertEquals("Italian Passport", fixture.getChoices(Locale.ITALY.getDisplayCountry()).next());
	    Assert.assertEquals("Japanese Passport", fixture.getChoices(Locale.JAPAN.getDisplayCountry()).next());
	    Assert.assertEquals("South Korea Passport", fixture.getChoices(Locale.KOREA.getDisplayCountry()).next());
	    Assert.assertEquals("United Kingdom Passport", fixture.getChoices(Locale.UK.getDisplayCountry()).next());
	    Assert.assertEquals("United States Passport", fixture.getChoices(Locale.US.getDisplayCountry()).next());

	}
}
