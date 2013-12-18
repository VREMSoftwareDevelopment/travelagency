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

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.utils.StringUtils;

public class SavePanel<T extends DaoEntity> extends Panel implements SaveButtonCallback<T> {
	private static final long serialVersionUID = 1L;

	public static final String SAVE_BUTTON = "saveButton";

	private SaveButton<T> saveButton;

	public SavePanel(String id, Form<T> form) {
		this(id, form, null);
	}

	public SavePanel(String id, Form<T> form, Component componentToUpdate) {
		super(id);
		saveButton = new SaveButton<T>(SAVE_BUTTON, form, this, componentToUpdate);
		add(saveButton);
	}

	public SavePanel<T> setResetModelAfterSubmit(boolean resetModelAfterSubmit) {
		getSaveButton().setResetModelAfterSubmit(resetModelAfterSubmit);
		return this;
	}

	SaveButton<T> getSaveButton() {
		return saveButton;
	}

	@Override
	public void preSubmit(AjaxRequestTarget target, T daoEntity, boolean newDaoEntity) {}
	@Override
	public void postSubmit(AjaxRequestTarget target, T daoEntity, boolean newDaoEntity) {}
	@Override
	public void setMessage(T daoEntity) {
		if (StringUtils.isBlank(daoEntity.getName())) {
			return;
		}
		String message = daoEntity.getName() + " " + getLocalizer().getString("saved.message", this);
		getSession().success(message);
	}

}
