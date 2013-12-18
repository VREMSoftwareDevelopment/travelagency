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
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;

import ca.travelagency.components.AjaxLinkCallback;
import ca.travelagency.components.javascript.ConfirmDialog;
import ca.travelagency.components.javascript.JSUtils;

public class DeleteLink extends AjaxLink<Void> {
	private static final long serialVersionUID = 1L;
	private AjaxLinkCallback ajaxLinkCallback;
	private Component component;

	public DeleteLink(String id, AjaxLinkCallback ajaxLinkCallback, Component component) {
		super(id);
		Validate.notNull(ajaxLinkCallback);
		this.ajaxLinkCallback = ajaxLinkCallback;
		Validate.notNull(component);
		this.component = component;
	}

	@Override
	public void onClick(AjaxRequestTarget target) {
		ajaxLinkCallback.onClick(target);
		target.appendJavaScript(JSUtils.INITIALIZE);
		target.add(component);
	}
	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		super.updateAjaxAttributes(attributes);
		String message = getLocalizer().getString("deleteConfirm.message", DeleteLink.this);
		ConfirmDialog confirmDialog = new ConfirmDialog(message);
		attributes.getAjaxCallListeners().add(confirmDialog);
	}
}
