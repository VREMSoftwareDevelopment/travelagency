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
package ca.travelagency.components.behaviors;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;

public class PlaceholderBehavior extends Behavior {
	private static final long serialVersionUID = 1L;

	private final String placeholder;

	public PlaceholderBehavior(String placeholder) {
		this.placeholder = placeholder;
	}

	@Override
	public void onComponentTag(Component component, ComponentTag tag) {
		tag.put("placeholder", placeholder);
	}

	public String getPlaceholder() {
		return placeholder;
	}
}