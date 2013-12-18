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
package ca.travelagency.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ca.travelagency.BasePage;
import ca.travelagency.components.behaviours.AjaxOnBlurBehaviour;
import ca.travelagency.components.dataprovider.DataProvider;
import ca.travelagency.components.decorators.FieldDecorator;
import ca.travelagency.components.fields.StringFieldHelper;
import ca.travelagency.components.formdetail.DeletePanel;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.components.formheader.ResetPanel;
import ca.travelagency.components.formheader.SavePanel;
import ca.travelagency.components.validators.DuplicateValidator;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.DaoSupport;
import ca.travelagency.utils.StringUtils;

public abstract class LookupEntitiesPage<T extends DaoEntity> extends BasePage {
	private static final long serialVersionUID = 1L;

	static final String DATA_TABLE = "dataTable";
	static final String DELETE = "deleteLink";

	static final String ADD_FORM = "addForm";
	static final String ADD_FORM_TITLE2 = "addFormTitle2";
	static final String ADD_FORM_TITLE1 = "addFormTitle1";
	static final String EDIT_FORM = "editForm";
	static final String SEARCH_FORM = "searchForm";

	static final String SAVE_BUTTON = "saveButton";
	static final String RESET_BUTTON = "resetButton";
	static final String SEARCH_BUTTON = "searchButton";
	static final String CLEAR_BUTTON = "clearButton";

	private Component dataTable;

	public LookupEntitiesPage(LookupEntitiesFilter lookupEntitiesFilter) {
		super();
		if (lookupEntitiesFilter == null) {
			lookupEntitiesFilter = new LookupEntitiesFilter(getTrueClass());
		}
		IModel<LookupEntitiesFilter> model = Model.of(lookupEntitiesFilter);

		add(makeSearchForm(model));

    	add(new Label(ADD_FORM_TITLE1, new ResourceModel("lookup.label.new")));
    	add(new Label(ADD_FORM_TITLE2, new ResourceModel(getLabelNameKey())));

    	dataTable = makeDataTable(model);
    	add(dataTable);

		add(makeAddForm(dataTable));
	}

	private Component makeDataTable(IModel<LookupEntitiesFilter> model) {
		return new AjaxFallbackDefaultDataTable<T, String>(DATA_TABLE, makeColumns(), new DataProvider<T>(model), DATA_TABLE_PER_PAGE);
	}

	private Form<LookupEntitiesFilter> makeSearchForm(IModel<LookupEntitiesFilter> model) {
		final Form<LookupEntitiesFilter> form =
			new Form<LookupEntitiesFilter>(SEARCH_FORM, new CompoundPropertyModel<LookupEntitiesFilter>(model));
		form.setOutputMarkupId(true);

		ResourceModel resourceModel = new ResourceModel(getLabelNameKey());

		form.add(new TextField<String>(DaoEntity.PROPERTY_NAME)
			.setLabel(resourceModel)
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(FieldDecorator.inlineDisplayLabel(), new AjaxOnBlurBehaviour()));

		form.add(new Button(SEARCH_BUTTON) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {
				setResponsePage(makeLookupEntitiesPage(form.getModelObject()));
    		};
    	});

		form.add(new Button(CLEAR_BUTTON) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {
				setResponsePage(makeLookupEntitiesPage(new LookupEntitiesFilter(getTrueClass())));
    		}
		}.setDefaultFormProcessing(false));

		return form;
	}

	private Form<T> makeAddForm(Component dataTable) {
		Validate.notNull(dataTable);

		@SuppressWarnings("unchecked")
		Form<T> form = makeForm(ADD_FORM, (IModel<T>) DaoEntityModelFactory.make(getTrueClass()));

		makeNameField(form).add(new FieldDecorator());

		form.add(new SavePanel<T>(SAVE_BUTTON, form, dataTable).setResetModelAfterSubmit(true));
		form.add(new ResetPanel<T>(RESET_BUTTON, form).setResetModel(true));

		return form;
	}

	private Form<T> makeForm(String id, IModel<T> model) {
		Form<T> form = new Form<T>(id, model);
		form.setOutputMarkupId(true);
		return form;
	}

	private RequiredTextField<String> makeNameField(Form<T> form) {
		ResourceModel resourceModel = new ResourceModel(getLabelNameKey());

		RequiredTextField<String> textField = new RequiredTextField<String>(DaoEntity.PROPERTY_NAME);
		textField.setLabel(resourceModel)
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new AjaxOnBlurBehaviour(), new DuplicateValidator<T>(form));
		form.add(textField);
		return textField;
	}

	private List<IColumn<T, String>> makeColumns() {
		List<IColumn<T, String>> columns = new ArrayList<IColumn<T, String>>();

		ResourceModel resourceModel = new ResourceModel(getLabelNameKey());
		columns.add(new PropertyColumn<T, String>(resourceModel, DaoEntity.PROPERTY_NAME, DaoEntity.PROPERTY_NAME) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<T>> item, String componentId, IModel<T> rowModel) {
				item.add(new EditPanel(componentId, rowModel.getObject()));
			}
		});

		columns.add(new PropertyColumn<T, String>(Model.of(StringUtils.EMPTY), DaoEntity.PROPERTY_ID) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<T>> item, String componentId, IModel<T> rowModel) {
				item.add(new DeletePanel<T>(componentId, rowModel.getObject(), getDataTable()));
			}
		});

		return columns;
	}

	private Component getDataTable() {
		return dataTable;
	}

	public abstract Class<?> getTrueClass();
	public abstract String getLabelNameKey();
	public abstract LookupEntitiesPage<T> makeLookupEntitiesPage(LookupEntitiesFilter lookupEntitiesFilter);

	public class EditPanel extends Panel {
		private static final long serialVersionUID = 1L;

		@SpringBean
		private DaoSupport<T> daoSupport;

		public EditPanel(String id, T daoEntity) {
			super(id);

	        final Form<T> form = makeForm(EDIT_FORM, DaoEntityModelFactory.make(daoEntity));
	        add(form);

			makeNameField(form).add(FieldDecorator.doNotDisplayLabel(),
				new AjaxFormComponentUpdatingBehavior("onchange") {
					private static final long serialVersionUID = 1L;
					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						T daoEntity = form.getModelObject();
						daoSupport.persist(daoEntity);
						target.add(form);
					}
					@Override
					protected void onError(AjaxRequestTarget target, RuntimeException e) {
						target.add(form);
					}
				});
		}
	}

}