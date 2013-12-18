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
package ca.travelagency.navigation;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;

public abstract class BaseNavigationMenu extends Panel {
	private static final long serialVersionUID = 1L;

	public BaseNavigationMenu(String id) {
		super(id);
	}

	protected Component bookmarkablePageLink(String id, Class<? extends Page> page, Class<? extends Page> currentPage) {
		Component component = new BookmarkablePageLink<String>(id, page);
		if (page.getSimpleName().equals(currentPage.getSimpleName())) {
			component.add(new AttributeAppender("class", "selected"));
		}
		return component;
	}
}
