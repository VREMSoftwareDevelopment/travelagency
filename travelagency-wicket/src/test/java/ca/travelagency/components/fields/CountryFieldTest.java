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
import org.junit.Ignore;
import org.junit.Test;

import ca.travelagency.BaseWicketTester;


public class CountryFieldTest extends BaseWicketTester {

	@Test
	public void testGetChoicesWithNoInput() throws Exception {
		CountryField fixture = new CountryField("id");
		Iterator<String> choices = fixture.getChoices(null);
		Assert.assertFalse(choices.hasNext());
	}

	@Test
	public void testGetChoices() throws Exception {
		String country = Locale.CANADA.getDisplayCountry();
		CountryField fixture = new CountryField("id");
		Iterator<String> choices = fixture.getChoices(country);
		Assert.assertTrue(choices.hasNext());
		Assert.assertEquals("Canada", choices.next());
	}

	@Ignore
	@Test
	public void testDisplayAllCountries() throws Exception {
		Locale[] locales = Locale.getAvailableLocales();
		for (Locale locale : locales) {
			System.out.println(locale.getDisplayCountry()+":"+locale.getDisplayName()+":"+locale.getCountry());
		}
	}

}
