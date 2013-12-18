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
package ca.travelagency.components.formheader;

import org.apache.commons.lang3.Validate;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

import ca.travelagency.components.AjaxButtonCallback;
import ca.travelagency.components.javascript.JSUtils;
import ca.travelagency.persistence.DaoEntity;

public class ResetButton<T extends DaoEntity> extends IndicatingAjaxButton {
	private static final long serialVersionUID = 1L;

	private boolean resetModel;
	private AjaxButtonCallback<T> ajaxButtonCallback;

	public ResetButton(String id, Form<T> form, AjaxButtonCallback<T> ajaxButtonCallback) {
		super(id, form);
		setDefaultFormProcessing(false);
		Validate.notNull(ajaxButtonCallback);
		this.ajaxButtonCallback = ajaxButtonCallback;
	}

	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		@SuppressWarnings("unchecked")
		Form<T> typedForm = (Form<T>) form;
		resetModel(typedForm);
		ajaxButtonCallback.onSubmit(target, typedForm);
		target.add(typedForm);
		target.appendJavaScript(JSUtils.INITIALIZE);
	}

	@Override
	protected void onError(AjaxRequestTarget target, Form<?> form) {
		target.add(form);
		target.appendJavaScript(JSUtils.INITIALIZE);
	}

	void resetModel(Form<T> form) {
		T daoEntity = form.getModelObject();
		IModel<T> model = getResetModel(daoEntity);
		form.setModel(model);
	}

	IModel<T> getResetModel(T daoEntity) {
		if (resetModel) {
			return DaoEntityModelFactory.make(daoEntity.getTrueClass());
		}
		return DaoEntityModelFactory.make(daoEntity);
	}

	public void setResetModel(boolean resetModel) {
		this.resetModel = resetModel;
	}
	public boolean isResetModel() {
		return resetModel;
	}
}
