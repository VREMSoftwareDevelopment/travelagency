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

import org.apache.wicket.model.IModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.formdetail.DeletePanel;
import ca.travelagency.components.formdetail.EditPanel;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoiceNote;
import ca.travelagency.invoice.InvoiceStatus;

public class NoteRowPanelTest extends BaseWicketTester {
	private Invoice invoice;
	private NotesPanel notesPanel;
	private InvoiceNote invoiceNote;

	@Before
	public void setUp() {
		stubNoteDataProvider();

		notesPanel = new NotesPanel(COMPONENT_ID, DaoEntityModelFactory.make(invoice));
	}

	private void stubNoteDataProvider() {
		invoice = InvoiceHelper.makeInvoiceWithDetails();
		invoice.addInvoiceNote(InvoiceHelper.makeNote());
		invoice.addInvoiceNote(InvoiceHelper.makeNote());

		Mockito.stub(daoSupport.find(Invoice.class, invoice.getId())).toReturn(invoice);
	}

	@Test
	public void testComponents() {
		invoiceNote = invoice.getInvoiceNotes().first();
		IModel<InvoiceNote> model = DaoEntityModelFactory.make(invoiceNote);
		Mockito.when(daoSupport.find(InvoiceNote.class, invoiceNote.getId())).thenReturn(invoiceNote);

		NoteRowPanel noteRowPanel = new NoteRowPanel(COMPONENT_ID, model, notesPanel);
		tester.startComponentInPage(noteRowPanel);

		tester.assertModelValue(COMPONENT_PATH+InvoiceNote.Properties.text.name(), invoiceNote.getText());
		tester.assertModelValue(COMPONENT_PATH+InvoiceNote.Properties.privateNote.name(), invoiceNote.isPrivateNote());

		tester.assertComponent(COMPONENT_PATH+NoteRowPanel.EDIT_BUTTON, EditPanel.class);
		tester.assertComponent(COMPONENT_PATH+NoteRowPanel.DELETE_BUTTON, DeletePanel.class);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoiceNote.class, invoiceNote.getId());
	}

	@Test
	public void testWithNotActiveInvoice() {
		invoice.setStatus(InvoiceStatus.Closed);

		invoiceNote = invoice.getInvoiceNotes().first();
		IModel<InvoiceNote> model = DaoEntityModelFactory.make(invoiceNote);
		Mockito.when(daoSupport.find(InvoiceNote.class, invoiceNote.getId())).thenReturn(invoiceNote);

		NoteRowPanel noteRowPanel = new NoteRowPanel(COMPONENT_ID, model, notesPanel);
		tester.startComponentInPage(noteRowPanel);

		tester.assertInvisible(COMPONENT_PATH+NoteRowPanel.EDIT_BUTTON);
		tester.assertInvisible(COMPONENT_PATH+NoteRowPanel.DELETE_BUTTON);

		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(InvoiceNote.class, invoiceNote.getId());
	}
}
