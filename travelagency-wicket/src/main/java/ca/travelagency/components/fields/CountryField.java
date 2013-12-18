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

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.wicket.extensions.ajax.markup.html.autocomplete.DefaultCssAutoCompleteTextField;

import ca.travelagency.utils.StringUtils;

public class CountryField extends DefaultCssAutoCompleteTextField<String> {
	private static final long serialVersionUID = 1L;
	private static final int DISPLAY_MAX_SIZE = 10;

	public CountryField(String id) {
		super(id, null);
	}

	@Override
	protected Iterator<String> getChoices(String input) {
		if (StringUtils.isEmpty(input)) {
			return Collections.<String>emptyList().iterator();
		}
		SortedSet<String> choices = new TreeSet<String>();
		Locale[] locales = Locale.getAvailableLocales();
		for (Locale locale : locales) {
			String country = locale.getDisplayCountry();
			if (country.toUpperCase().startsWith(input.toUpperCase())) {
				choices.add(country);
				if (choices.size() == DISPLAY_MAX_SIZE) {
					break;
				}
			}
		}
		return choices.iterator();
	}
}