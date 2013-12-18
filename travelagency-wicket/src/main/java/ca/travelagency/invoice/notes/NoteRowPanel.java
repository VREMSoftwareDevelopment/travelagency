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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import ca.travelagency.components.CheckMarkPanel;
import ca.travelagency.components.formdetail.DeletePanel;
import ca.travelagency.components.formdetail.EditPanel;
import ca.travelagency.invoice.InvoiceNote;
import ca.travelagency.utils.DateUtils;

public class NoteRowPanel extends Panel {
	private static final long serialVersionUID = 1L;

	public static final String DELETE_BUTTON = "deleteButton";
	public static final String EDIT_BUTTON = "editButton";

	public NoteRowPanel(final String id, IModel<InvoiceNote> model, final NotesPanel notesPanel) {
		super(id, model);
		setOutputMarkupId(true);

		add(new Label(InvoiceNote.Properties.text.name()).setEscapeModelStrings(false));
		add(new CheckMarkPanel(InvoiceNote.Properties.privateNote.name(), model.getObject().isPrivateNote()));
		add(DateLabel.forDateStyle(InvoiceNote.Properties.date.name(), DateUtils.DATE_STYLE));

		add(new DeletePanel<InvoiceNote>(DELETE_BUTTON, getInvoiceNote(), notesPanel) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				notesPanel.delete(target, getInvoiceNote());
			}
		}.setVisible(notesPanel.isEditable()));

        add(new EditPanel(EDIT_BUTTON) {
    		private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
    			NoteFormPanel noteFormPanel = new NoteFormPanel(id, getInvoiceNote(), notesPanel);
    			NoteRowPanel.this.replaceWith(noteFormPanel);
    			target.add(noteFormPanel);
			}
        }.setVisible(notesPanel.isEditable()));

	}

	private InvoiceNote getInvoiceNote() {
		return (InvoiceNote) getDefaultModelObject();
	}
}
