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
package ca.travelagency.invoice.destinations;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import ca.travelagency.components.formdetail.DeletePanel;
import ca.travelagency.components.formdetail.EditPanel;
import ca.travelagency.invoice.InvoiceDestination;
import ca.travelagency.utils.DateUtils;

public class DestinationRowPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public static final String DELETE_BUTTON = "deleteButton";
	public static final String EDIT_BUTTON = "editButton";

	public DestinationRowPanel(final String id, IModel<InvoiceDestination> model, final DestinationsPanel destinationsPanel) {
		super(id, model);
		setOutputMarkupId(true);

		add(new Label(InvoiceDestination.Properties.departurePlace.name()));
		add(DateLabel.forDateStyle(InvoiceDestination.Properties.departureDate.name(), DateUtils.DATE_STYLE));
		add(new Label(InvoiceDestination.Properties.arrivalPlace.name()));
		add(DateLabel.forDateStyle(InvoiceDestination.Properties.arrivalDate.name(), DateUtils.DATE_STYLE));

		add(new DeletePanel<InvoiceDestination>(DELETE_BUTTON, getInvoiceDestination(), destinationsPanel) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				destinationsPanel.delete(target, getInvoiceDestination());
			}
		}.setVisible(destinationsPanel.isEditable()));

        add(new EditPanel(EDIT_BUTTON) {
    		private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
    			DestinationFormPanel destinationFormPanel = new DestinationFormPanel(id, getInvoiceDestination(), destinationsPanel);
    			DestinationRowPanel.this.replaceWith(destinationFormPanel);
    			target.add(destinationFormPanel);
			}
        }.setVisible(destinationsPanel.isEditable()));

	}

	private InvoiceDestination getInvoiceDestination() {
		return (InvoiceDestination) getDefaultModelObject();
	}
}
