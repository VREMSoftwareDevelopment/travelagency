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
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ca.travelagency.components.decorators.BlockUIDecorator;
import ca.travelagency.components.javascript.JSUtils;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.DaoSupport;

public class SaveButton<T extends DaoEntity> extends IndicatingAjaxButton {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private DaoSupport<T> daoSupport;

	private boolean resetModelAfterSubmit;
	private Component componentToUpdate;
	private SaveButtonCallback<T> saveButtonCallback;

	public SaveButton(String id, Form<T> form, SaveButtonCallback<T> saveButtonCallback, Component componentToUpdate) {
		super(id, form);
		Validate.notNull(saveButtonCallback);
		this.saveButtonCallback = saveButtonCallback;
		this.componentToUpdate = componentToUpdate;
	}

	@Override
	protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
		super.updateAjaxAttributes(attributes);
		attributes.getAjaxCallListeners().add(new BlockUIDecorator());
	}

	@Override
	protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
		@SuppressWarnings("unchecked")
		Form<T> typedForm = (Form<T>) form;
		T daoEntity = typedForm.getModelObject();
		boolean newDaoEntity = !DaoEntityModelFactory.isPersisted(daoEntity);

		saveButtonCallback.preSubmit(target, daoEntity, newDaoEntity);

		daoSupport.persist(daoEntity);
		resetModelAfterSubmit(typedForm, daoEntity);

		saveButtonCallback.setMessage(daoEntity);

		target.add(typedForm);
		updateComponent(target);

		saveButtonCallback.postSubmit(target, daoEntity, newDaoEntity);

		target.appendJavaScript(JSUtils.INITIALIZE);
	}

	private void resetModelAfterSubmit(Form<T> form, T daoEntity) {
		if (isResetModelAfterSubmit()) {
			form.setDefaultModel(DaoEntityModelFactory.make(daoEntity.getTrueClass()));
		}
	}

	private void updateComponent(AjaxRequestTarget target) {
		if (getComponentToUpdate() != null) {
			target.add(getComponentToUpdate());
		}
	}

	@Override
	protected void onError(AjaxRequestTarget target, Form<?> form) {
		target.add(form);
		target.appendJavaScript(JSUtils.INITIALIZE);
	}
	public void setResetModelAfterSubmit(boolean resetModelAfterSubmit) {
		this.resetModelAfterSubmit = resetModelAfterSubmit;
	}
	public boolean isResetModelAfterSubmit() {
		return resetModelAfterSubmit;
	}

	public Component getComponentToUpdate() {
		return componentToUpdate;
	}
}
