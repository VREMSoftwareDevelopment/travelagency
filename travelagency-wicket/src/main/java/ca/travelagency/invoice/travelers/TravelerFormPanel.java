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

import org.apache.commons.lang3.Validate;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ca.travelagency.components.behaviours.AjaxOnBlurBehaviour;
import ca.travelagency.components.decorators.FieldDecorator;
import ca.travelagency.components.fields.DateField;
import ca.travelagency.components.fields.SalutationField;
import ca.travelagency.components.fields.StringFieldHelper;
import ca.travelagency.components.fields.TravelDocumentField;
import ca.travelagency.components.formdetail.CancelPanel;
import ca.travelagency.components.formdetail.SavePanelDetail;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.components.formheader.ResetPanel;
import ca.travelagency.config.ParameterRepository;
import ca.travelagency.customer.FirstNameField;
import ca.travelagency.customer.LastNameField;
import ca.travelagency.invoice.InvoiceTraveler;
import ca.travelagency.traveler.TravelerLookupLink;
import ca.travelagency.utils.StringUtils;

public class TravelerFormPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public static final String FORM = "form";
	public static final String FEEDBACK = "feedback";
	public static final String SAVE_BUTTON = "saveButton";
	public static final String RESET_BUTTON = "resetButton";
	public static final String CANCEL_BUTTON = "cancelButton";

	static final String LOOKUP_TRAVELERS = "lookupTravelers";

	@SpringBean
	private ParameterRepository parameterRepository;

	public TravelerFormPanel(String id, TravelersPanel travelersPanel) {
		this(id, null, travelersPanel);
	}

	public TravelerFormPanel(final String id, InvoiceTraveler invoiceTraveler, final TravelersPanel travelersPanel) {
		super(id);
		Validate.notNull(travelersPanel);

		setOutputMarkupId(true);

		IModel<InvoiceTraveler> model = DaoEntityModelFactory.make(invoiceTraveler, InvoiceTraveler.class);
		initialize(model.getObject());

		final Form<InvoiceTraveler> travelerForm = new Form<InvoiceTraveler>(FORM, model);
		travelerForm.setOutputMarkupId(true);

		travelerForm.add(new ComponentFeedbackPanel(FEEDBACK, travelerForm));

		travelerForm.add(new SalutationField(InvoiceTraveler.Properties.salutation.name())
			.setLabel(new ResourceModel("travelerFormPanel.salutation"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		travelerForm.add(new LastNameField(InvoiceTraveler.Properties.lastName.name())
			.setLabel(new ResourceModel("travelerFormPanel.lastName"))
			.setRequired(true)
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		travelerForm.add(new FirstNameField(InvoiceTraveler.Properties.firstName.name())
			.setLabel(new ResourceModel("travelerFormPanel.firstName"))
			.setRequired(true)
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		travelerForm.add(new TravelDocumentField(InvoiceTraveler.Properties.documentType.name())
			.setLabel(new ResourceModel("travelerFormPanel.document"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		travelerForm.add(new TextField<String>(InvoiceTraveler.Properties.documentNumber.name())
			.setLabel(new ResourceModel("travelerFormPanel.documentNumber"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		travelerForm.add(new DateField(InvoiceTraveler.Properties.dateOfBirth.name())
			.setLabel(new ResourceModel("travelerFormPanel.dateOfBirth"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));

		travelerForm.add(new SavePanelDetail<InvoiceTraveler>(SAVE_BUTTON, travelerForm) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<InvoiceTraveler> form) {
				InvoiceTraveler travelerItem = form.getModelObject();
				travelersPanel.update(target, travelerItem);
				IModel<InvoiceTraveler> model = DaoEntityModelFactory.make(InvoiceTraveler.class);
				form.setModel(model);
				initialize(model.getObject());
			}
		});

		travelerForm.add(new ResetPanel<InvoiceTraveler>(RESET_BUTTON, travelerForm) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<InvoiceTraveler> form) {
				initialize(form.getModelObject());
			}
		}.setResetModel(!DaoEntityModelFactory.isPersisted(model.getObject())));


		travelerForm.add(new CancelPanel(CANCEL_BUTTON) {
    		private static final long serialVersionUID = 1L;
    		@Override
    		public void onClick(AjaxRequestTarget target) {
    			TravelerRowPanel travelerRowPanel = new TravelerRowPanel(id, travelerForm.getModel(), travelersPanel);
    			TravelerFormPanel.this.replaceWith(travelerRowPanel);
    			target.add(travelerRowPanel);
   			}
        }.setVisible(invoiceTraveler != null));

		travelerForm.add(new TravelerLookupLink(LOOKUP_TRAVELERS, travelerForm));

		add(travelerForm);
	}

	private void initialize(InvoiceTraveler invoiceTraveler) {
		if (StringUtils.isBlank(invoiceTraveler.getDocumentType())) {
			invoiceTraveler.setDocumentType(parameterRepository.getDefaultTravelDocumentType());
		}
	}
}