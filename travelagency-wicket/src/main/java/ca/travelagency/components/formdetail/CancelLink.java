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

import org.apache.commons.lang3.Validate;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;

import ca.travelagency.components.AjaxLinkCallback;
import ca.travelagency.components.javascript.JSUtils;

public class CancelLink extends IndicatingAjaxLink<Void> {
	private static final long serialVersionUID = 1L;
	private AjaxLinkCallback ajaxLinkCallback;

	public CancelLink(String id, AjaxLinkCallback ajaxLinkCallback) {
		super(id);
		Validate.notNull(ajaxLinkCallback);
		this.ajaxLinkCallback = ajaxLinkCallback;
	}

	@Override
	public void onClick(AjaxRequestTarget target) {
		ajaxLinkCallback.onClick(target);
		target.appendJavaScript(JSUtils.INITIALIZE);
		target.appendJavaScript(JSUtils.SHOW_ROW_CONTROLS);
	}
}
