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
package ca.travelagency.search;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import ca.travelagency.components.behaviours.AjaxOnBlurBehaviour;
import ca.travelagency.components.decorators.FieldDecorator;
import ca.travelagency.components.fields.StringFieldHelper;
import ca.travelagency.persistence.DaoEntity;

public class SearchPanel<T extends DaoEntity, U extends SearchFilter<T>> extends Panel {
	private static final long serialVersionUID = 1L;

	static final String SEARCH_FORM = "searchForm";
	static final String SEARCH_BUTTON = "searchButton";
	static final String CLEAR_BUTTON = "clearButton";
	static final String FEEDBACK = "feedback";

	public SearchPanel(String id, IModel<U> model, final SearchButtons searchButtons) {
		super(id);

		Form<U> form = new Form<U>(SEARCH_FORM, new CompoundPropertyModel<U>(model));

		form.add(new ComponentFeedbackPanel(FEEDBACK, form));

		form.add(new TextField<String>(SearchFilter.Properties.searchText.name())
			.setLabel(new ResourceModel(SearchFilter.Properties.searchText.name()))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));

		form.add(new Button(SEARCH_BUTTON) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {
				searchButtons.search();
    		}
    	});

		form.add(new Button(CLEAR_BUTTON) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {
				searchButtons.clear();
    		}
		}.setDefaultFormProcessing(false));

		add(form);
	}

}
