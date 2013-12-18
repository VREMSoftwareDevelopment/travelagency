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
package ca.travelagency.invoice.notes;

import org.apache.commons.lang3.Validate;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import ca.travelagency.components.behaviours.AjaxOnBlurBehaviour;
import ca.travelagency.components.decorators.FieldDecorator;
import ca.travelagency.components.fields.StringFieldHelper;
import ca.travelagency.components.formdetail.CancelPanel;
import ca.travelagency.components.formdetail.SavePanelDetail;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.components.formheader.ResetPanel;
import ca.travelagency.invoice.InvoiceNote;

public class NoteFormPanel  extends Panel {
	private static final long serialVersionUID = 1L;

	public static final String FORM = "form";
	public static final String FEEDBACK = "feedback";
	public static final String SAVE_BUTTON = "saveButton";
	public static final String RESET_BUTTON = "resetButton";
	public static final String CANCEL_BUTTON = "cancelButton";

	public NoteFormPanel(String id, NotesPanel notesPanel) {
		this(id, null, notesPanel);
	}

	public NoteFormPanel(final String id, InvoiceNote invoiceNote, final NotesPanel notesPanel) {
		super(id);
		Validate.notNull(notesPanel);

		setOutputMarkupId(true);

		IModel<InvoiceNote> model = DaoEntityModelFactory.make(invoiceNote, InvoiceNote.class);

		final Form<InvoiceNote> noteForm = new Form<InvoiceNote>(FORM, model);
		noteForm.setOutputMarkupId(true);

		noteForm.add(new ComponentFeedbackPanel(FEEDBACK, noteForm));

		noteForm.add(new TextArea<String>(InvoiceNote.Properties.text.name())
			.setRequired(true)
			.setLabel(new ResourceModel("invoiceNote.note"))
			.add(StringFieldHelper.maxLenAreaValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		noteForm.add(new CheckBox(InvoiceNote.Properties.privateNote.name())
			.setLabel(new ResourceModel("invoiceNote.private"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));

		noteForm.add(new SavePanelDetail<InvoiceNote>(SAVE_BUTTON, noteForm) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit(AjaxRequestTarget target, Form<InvoiceNote> form) {
				notesPanel.update(target, form.getModelObject());
				IModel<InvoiceNote> model = DaoEntityModelFactory.make(InvoiceNote.class);
				form.setModel(model);
			}
		});

		noteForm.add(new ResetPanel<InvoiceNote>(RESET_BUTTON, noteForm)
				.setResetModel(!DaoEntityModelFactory.isPersisted(model.getObject())));

        noteForm.add(new CancelPanel(CANCEL_BUTTON) {
    		private static final long serialVersionUID = 1L;
    		@Override
    		public void onClick(AjaxRequestTarget target) {
    			NoteRowPanel noteRowPanel = new NoteRowPanel(id, noteForm.getModel(), notesPanel);
    			NoteFormPanel.this.replaceWith(noteRowPanel);
    			target.add(noteRowPanel);
   			}
        }.setVisible(invoiceNote != null));

		add(noteForm);
	}
}