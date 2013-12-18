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
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;

import ca.travelagency.components.AjaxButtonCallback;
import ca.travelagency.components.decorators.BlockUIDecorator;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.components.javascript.JSUtils;
import ca.travelagency.persistence.DaoEntity;

public class SaveButtonDetail<T extends DaoEntity> extends IndicatingAjaxButton {
	private static final long serialVersionUID = 1L;
	private AjaxButtonCallback<T> ajaxButtonCallback;

	public SaveButtonDetail(String id, Form<T> form, AjaxButtonCallback<T> ajaxButtonCallback) {
		super(id, form);
		Validate.notNull(ajaxButtonCallback);
		this.ajaxButtonCallback = ajaxButtonCallback;
	}

	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		@SuppressWarnings("unchecked")
		Form<T> typedForm = (Form<T>) form;
		ajaxButtonCallback.onSubmit(target, typedForm);
		target.appendJavaScript(JSUtils.INITIALIZE);
		target.appendJavaScript(JSUtils.SHOW_ROW_CONTROLS);
	}

	@Override
	protected void onError(AjaxRequestTarget target, Form<?> form) {
		@SuppressWarnings("unchecked")
		Form<T> typedForm = (Form<T>) form;
		target.add(typedForm);
		target.appendJavaScript(JSUtils.INITIALIZE);
		target.appendJavaScript(DaoEntityModelFactory.isPersisted(typedForm.getModelObject())
				? JSUtils.HIDE_ROW_CONTROLS : JSUtils.SHOW_ROW_CONTROLS);
	}
	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		super.updateAjaxAttributes(attributes);
		attributes.getAjaxCallListeners().add(new BlockUIDecorator());
	}
}