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
package ca.travelagency.invoice.travelers;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import ca.travelagency.components.formdetail.DeletePanel;
import ca.travelagency.components.formdetail.EditPanel;
import ca.travelagency.invoice.InvoiceTraveler;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.utils.DateUtils;

public class TravelerRowPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public static final String DELETE_BUTTON = "deleteButton";
	public static final String EDIT_BUTTON = "editButton";

	public TravelerRowPanel(final String id, IModel<InvoiceTraveler> model, final TravelersPanel travelersPanel) {
		super(id, model);
		setOutputMarkupId(true);

		add(new Label(DaoEntity.PROPERTY_NAME));
		add(new Label(InvoiceTraveler.Properties.documentType.name()));
		add(new Label(InvoiceTraveler.Properties.documentNumber.name()));
		add(DateLabel.forDateStyle(InvoiceTraveler.Properties.dateOfBirth.name(), DateUtils.DATE_STYLE));

		add(new DeletePanel<InvoiceTraveler>(DELETE_BUTTON, getInvoiceTraveler(), travelersPanel) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				travelersPanel.delete(target, getInvoiceTraveler());
			}
		}.setVisible(travelersPanel.isEditable()));

        add(new EditPanel(EDIT_BUTTON) {
    		private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
    			TravelerFormPanel travelerFormPanel = new TravelerFormPanel(id, getInvoiceTraveler(), travelersPanel);
    			TravelerRowPanel.this.replaceWith(travelerFormPanel);
    			target.add(travelerFormPanel);
			}
        }.setVisible(travelersPanel.isEditable()));

	}

	private InvoiceTraveler getInvoiceTraveler() {
		return (InvoiceTraveler) getDefaultModelObject();
	}
}
