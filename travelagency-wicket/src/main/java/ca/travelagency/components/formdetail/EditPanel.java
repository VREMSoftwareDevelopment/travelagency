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
import org.apache.wicket.markup.html.panel.Panel;

import ca.travelagency.components.AjaxLinkCallback;

public abstract class EditPanel extends Panel implements AjaxLinkCallback {
	private static final long serialVersionUID = 1L;

	static final String LINK = "link";

	public EditPanel(String id) {
		super(id);
        add(new EditLink(LINK, this));
	}

	@Override
	public abstract void onClick(AjaxRequestTarget target);
}
