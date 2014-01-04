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

import java.util.SortedSet;

import org.apache.commons.lang3.Validate;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ca.travelagency.components.behaviors.AjaxOnBlurBehavior;
import ca.travelagency.components.decorators.FieldDecorator;
import ca.travelagency.components.fields.CityField;
import ca.travelagency.components.fields.DateField;
import ca.travelagency.components.fields.StringFieldHelper;
import ca.travelagency.components.formdetail.CancelPanel;
import ca.travelagency.components.formdetail.SavePanelDetail;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.components.formheader.ResetPanel;
import ca.travelagency.config.ParameterRepository;
import ca.travelagency.invoice.InvoiceDestination;
import ca.travelagency.utils.DateUtils;

public class DestinationFormPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public static final String FORM = "form";
	public static final String FEEDBACK = "feedback";
	public static final String SAVE_BUTTON = "saveButton";
	public static final String RESET_BUTTON = "resetButton";
	public static final String CANCEL_BUTTON = "cancelButton";

	public static final int TRAVEL_DAY_RANGE = 7;

	@SpringBean
	private ParameterRepository parameterRepository;

	public DestinationFormPanel(String id, DestinationsPanel destinationsPanel) {
		this(id, null, destinationsPanel);
	}

	public DestinationFormPanel(final String id, InvoiceDestination invoiceDestination, final DestinationsPanel destinationsPanel) {
		super(id);
		Validate.notNull(destinationsPanel);

		setOutputMarkupId(true);

		IModel<InvoiceDestination> model = DaoEntityModelFactory.make(invoiceDestination, InvoiceDestination.class);
		initialize(destinationsPanel, model.getObject());

		final Form<InvoiceDestination> destinationForm = new Form<InvoiceDestination>(FORM, model);
		destinationForm.setOutputMarkupId(true);

		destinationForm.add(new ComponentFeedbackPanel(FEEDBACK, destinationForm));

		destinationForm.add(new CityField(InvoiceDestination.Properties.departurePlace.name())
			.setRequired(true)
			.setLabel(new ResourceModel("invoice.departurePlace"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehavior()));
		destinationForm.add(new DateField(InvoiceDestination.Properties.departureDate.name())
			.setRequired(true)
			.setLabel(new ResourceModel("invoice.departureDate"))
			.add(new FieldDecorator(), new AjaxOnBlurBehavior()));
		destinationForm.add(new CityField(InvoiceDestination.Properties.arrivalPlace.name())
			.setRequired(true)
			.setLabel(new ResourceModel("invoice.arrivalPlace"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehavior()));
		destinationForm.add(new DateField(InvoiceDestination.Properties.arrivalDate.name())
			.setLabel(new ResourceModel("invoice.arrivalDate"))
			.add(new FieldDecorator(), new AjaxOnBlurBehavior()));

		destinationForm.add(new SavePanelDetail<InvoiceDestination>(SAVE_BUTTON, destinationForm) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<InvoiceDestination> form) {
				destinationsPanel.update(target, form.getModelObject());
				IModel<InvoiceDestination> model = DaoEntityModelFactory.make(InvoiceDestination.class);
				form.setModel(model);
				initialize(destinationsPanel, model.getObject());
			}
		});

		destinationForm.add(new ResetPanel<InvoiceDestination>(RESET_BUTTON, destinationForm) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<InvoiceDestination> form) {
				initialize(destinationsPanel, form.getModelObject());
			}
		}.setResetModel(!DaoEntityModelFactory.isPersisted(model.getObject())));

        destinationForm.add(new CancelPanel(CANCEL_BUTTON) {
    		private static final long serialVersionUID = 1L;
    		@Override
    		public void onClick(AjaxRequestTarget target) {
    			DestinationRowPanel destinationRowPanel = new DestinationRowPanel(id, destinationForm.getModel(), destinationsPanel);
    			DestinationFormPanel.this.replaceWith(destinationRowPanel);
    			target.add(destinationRowPanel);
   			}
        }.setVisible(invoiceDestination != null));

		add(destinationForm);
	}

	private void initialize(DestinationsPanel destinationsPanel, InvoiceDestination invoiceDestination) {
		if (DaoEntityModelFactory.isPersisted(invoiceDestination)) {
			return;
		}
		SortedSet<InvoiceDestination> destinations = destinationsPanel.getDaoEntity().getInvoiceDestinations();
		switch (destinations.size()) {
		case 0:
			invoiceDestination.setDeparturePlace(parameterRepository.getDefaultDeparturePlace());
			break;
		case 1:
			InvoiceDestination first = destinations.first();
			invoiceDestination.setDeparturePlace(first.getArrivalPlace());
			invoiceDestination.setDepartureDate(DateUtils.addDays(first.getDepartureDate(), TRAVEL_DAY_RANGE));
			invoiceDestination.setArrivalPlace(first.getDeparturePlace());
			break;
		}
	}

}