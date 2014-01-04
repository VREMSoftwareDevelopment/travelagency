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
package ca.travelagency.traveler;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import ca.travelagency.BasePage;
import ca.travelagency.components.behaviors.AjaxOnBlurBehavior;
import ca.travelagency.components.dataprovider.DataProvider;
import ca.travelagency.components.decorators.BlockUIDecorator;
import ca.travelagency.components.decorators.FieldDecorator;
import ca.travelagency.components.fields.StringFieldHelper;
import ca.travelagency.components.javascript.JSUtils;
import ca.travelagency.customer.Traveler;
import ca.travelagency.invoice.InvoiceTraveler;
import ca.travelagency.utils.DateUtils;

public class TravelerLookupPanel extends Panel {

	private static final long serialVersionUID = 1L;

	static final String FORM = "form";
	static final String FEEDBACK = "feedback";
	static final String SEARCH = "search";
	static final String DATA = "data";

	private DataTable<Traveler, String> data;
	private Form<InvoiceTraveler> travelerForm;
	private ModalWindow modalWindow;

    public TravelerLookupPanel(String id, Form<InvoiceTraveler> travelerForm, ModalWindow modalWindow) {
		super(id);
		this.travelerForm = travelerForm;
		this.modalWindow = modalWindow;

		Model<TravelerFilter> model = Model.of(new TravelerFilter(travelerForm.getModelObject().getLastName()));

		data = new AjaxFallbackDefaultDataTable<Traveler, String>
			(DATA, makeColumns(), new DataProvider<Traveler>(model), BasePage.DATA_TABLE_PER_PAGE);
		data.setOutputMarkupId(true);
		add(data);

		Form<TravelerFilter> form = new Form<TravelerFilter>(FORM, new CompoundPropertyModel<TravelerFilter>(model));
		add(form);

		form.add(new ComponentFeedbackPanel(FEEDBACK, form));

		form.add(new TextField<String>(TravelerFilter.Properties.searchText.name())
			.setLabel(new ResourceModel("travelerLookupPanel.searchText"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehavior()));

		form.add(new SearchButton(SEARCH, form));
	}

	class SearchButton extends IndicatingAjaxButton {
		private static final long serialVersionUID = 1L;
		public SearchButton(String id, Form<?> form) {
			super(id, form);
		}
		@Override
		protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			target.add(data);
			appendJavaxScript(target);
		}
		@Override
		protected void onError(AjaxRequestTarget target, Form<?> form) {
			target.add(form);
			appendJavaxScript(target);
		}
		@Override
		protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
			super.updateAjaxAttributes(attributes);
			attributes.getAjaxCallListeners().add(new BlockUIDecorator());
		}
		private void appendJavaxScript(AjaxRequestTarget target) {
			target.appendJavaScript(JSUtils.INITIALIZE);
		}
	}

	List<IColumn<Traveler, String>> makeColumns() {
		List<IColumn<Traveler, String>> columns = new ArrayList<IColumn<Traveler, String>>();

		columns.add(new PropertyColumn<Traveler, String>(new ResourceModel("travelerLookupPanel.name"), Traveler.Properties.lastName.name(), Traveler.PROPERTY_NAME) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<Traveler>> item, String componentId, IModel<Traveler> rowModel) {
				item.add(new SelectPanel(componentId, rowModel, travelerForm, modalWindow));
			}
		});
		columns.add(new PropertyColumn<Traveler, String>(new ResourceModel("travelerLookupPanel.documentType"), Traveler.Properties.documentType.name()));
		columns.add(new PropertyColumn<Traveler, String>(new ResourceModel("travelerLookupPanel.documentNumber"), Traveler.Properties.documentNumber.name()));
		columns.add(new PropertyColumn<Traveler, String>(new ResourceModel("travelerLookupPanel.dateOfBirth"), Traveler.Properties.dateOfBirth.name()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<Traveler>> item, String componentId, IModel<Traveler> rowModel) {
				item.add(DateLabel.forDateStyle(componentId, Model.of(rowModel.getObject().getDateOfBirth()), DateUtils.DATE_STYLE));
			}
		});

		return columns;
	}
}
