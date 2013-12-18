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

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BasePage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.formdetail.CancelPanel;
import ca.travelagency.components.formdetail.SavePanelDetail;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.components.formheader.ResetPanel;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoiceNote;

public class NoteFormPanelTest extends BaseWicketTester {
	private static final String FORM_PATH = COMPONENT_PATH+NoteFormPanel.FORM+BasePage.PATH_SEPARATOR;

	private Invoice invoice;
	private InvoiceNote invoiceNote;
	private NotesPanel notesPanel;

	@Before
	public void setUp() {
		stubNoteDataProvider();

		notesPanel = new NotesPanel(COMPONENT_ID, DaoEntityModelFactory.make(invoice));
	}

	private void stubNoteDataProvider() {
		invoice = InvoiceHelper.makeInvoiceWithDetails();
		invoiceNote = invoice.getInvoiceNotes().first();

		Mockito.stub(daoSupport.find(Invoice.class, invoice.getId())).toReturn(invoice);
		Mockito.stub(daoSupport.find(InvoiceNote.class, invoiceNote.getId())).toReturn(invoiceNote);
	}

	@Test
	public void testComponents() {
		NoteFormPanel noteFormPanel = new NoteFormPanel(COMPONENT_ID, invoiceNote, notesPanel);
		tester.startComponentInPage(noteFormPanel);

		tester.assertComponent(COMPONENT_PATH+NoteFormPanel.FORM, Form.class);

		tester.assertComponent(FORM_PATH+NoteFormPanel.SAVE_BUTTON, SavePanelDetail.class);
		tester.assertComponent(FORM_PATH+NoteFormPanel.RESET_BUTTON, ResetPanel.class);
		tester.assertComponent(FORM_PATH+NoteFormPanel.CANCEL_BUTTON, CancelPanel.class);

		tester.assertComponent(FORM_PATH+InvoiceNote.Properties.text, TextArea.class);
		tester.assertComponent(FORM_PATH+InvoiceNote.Properties.privateNote, CheckBox.class);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoiceNote.class, invoiceNote.getId());
	}

	@Test
	public void testNewNoteComponents() {
		NoteFormPanel noteFormPanel = new NoteFormPanel(COMPONENT_ID, notesPanel);
		tester.startComponentInPage(noteFormPanel);

		tester.assertInvisible(FORM_PATH+NoteFormPanel.CANCEL_BUTTON);

		Mockito.verify(daoSupport, Mockito.never()).find(InvoiceNote.class, invoiceNote.getId());
	}

	@Test
	public void testValues() {
		NoteFormPanel noteFormPanel = new NoteFormPanel(COMPONENT_ID, invoiceNote, notesPanel);
		tester.startComponentInPage(noteFormPanel);

		tester.assertModelValue(FORM_PATH+InvoiceNote.Properties.text, invoiceNote.getText());
		tester.assertModelValue(FORM_PATH+InvoiceNote.Properties.privateNote, invoiceNote.isPrivateNote());

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoiceNote.class, invoiceNote.getId());
	}

	@Test
	public void testRequiredFields() {
		NoteFormPanel panel = new NoteFormPanel(COMPONENT_ID, notesPanel);
		tester.startComponentInPage(panel);

		FormTester formTester = tester.newFormTester(COMPONENT_PATH+NoteFormPanel.FORM);
		formTester.submit();

		tester.assertErrorMessages(new String [] {
			"'Note' is required."
		});
	}
}

