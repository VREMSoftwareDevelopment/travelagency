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
package ca.travelagency.invoice;

import java.util.List;
import java.util.SortedSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ca.travelagency.customer.Customer;
import ca.travelagency.customer.CustomerHelper;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.persistence.DaoSupport;
import ca.travelagency.persistence.DatabaseTester;


public class InvoicePersistenceTest extends DatabaseTester<Invoice> {

	private SystemUser systemUser;
	private Customer customer;

	@Autowired
	private DaoSupport<SystemUser> systemUserDaoSupport;

	@Autowired
	private DaoSupport<Customer> customerDaoSupport;

	@Before
	public void setUp() throws Exception {
		systemUser = SystemUserHelper.makeSystemUserWithoutId();
		systemUserDaoSupport.persist(systemUser);

		customer = CustomerHelper.makeCustomerWithoutId();
		customerDaoSupport.persist(customer);
	}

	@Test
	public void testPersistence() throws Exception {
		// setup
		Invoice expected = InvoiceHelper.makeInvoiceWithoutId(customer, systemUser);
		persist(expected);
		// execute
		Invoice actual = find(expected.getClass(), expected.getId());
		// validate
		Assert.assertNotNull(expected.getId());
		Assert.assertEquals(expected, actual);
		Assert.assertNotSame(expected, actual);
	}

	@Test
	public void testSystemUser() throws Exception {
		// setup
		Invoice invoice = InvoiceHelper.makeInvoiceWithoutId(customer, systemUser);
		persist(invoice);
		// execute
		Invoice foundInvoice = find(Invoice.class, invoice.getId());
		// validate
		Assert.assertEquals(systemUser, invoice.getSystemUser());
		Assert.assertEquals(systemUser, foundInvoice.getSystemUser());
		Assert.assertNotSame(invoice.getSystemUser(), foundInvoice.getSystemUser());
	}

	@Test
	public void testCustomer() throws Exception {
		// setup
		Invoice invoice = InvoiceHelper.makeInvoiceWithoutId(customer, systemUser);
		persist(invoice);
		// execute
		Invoice foundInvoice = find(Invoice.class, invoice.getId());
		// validate
		Assert.assertEquals(customer, invoice.getCustomer());
		Assert.assertEquals(customer, foundInvoice.getCustomer());
		Assert.assertNotSame(invoice.getCustomer(), foundInvoice.getCustomer());
	}

	@Test
	public void testAddDestination() throws Exception {
		// setup
		Invoice invoice = InvoiceHelper.makeInvoiceWithoutId(customer, systemUser);
		InvoiceDestination expected = InvoiceHelper.makeDestinationWithoutId();
		// execute
		invoice.addInvoiceDestination(expected);
		persist(invoice);
		// validate
		validateRelationship(expected);
		Invoice result = find(Invoice.class, invoice.getId());
		// validate
		SortedSet<InvoiceDestination> invoiceDestinations = result.getInvoiceDestinations();
		InvoiceDestination actual = invoiceDestinations.first();
		Assert.assertEquals(expected, actual);
		Assert.assertNotSame(expected, actual);
	}

	@Test
	public void testAddTraveler() throws Exception {
		// setup
		Invoice invoice = InvoiceHelper.makeInvoiceWithoutId(customer, systemUser);
		InvoiceTraveler expected = InvoiceHelper.makeTravelerWithoutId();
		// execute
		invoice.addInvoiceTraveler(expected);
		persist(invoice);
		// validate
		validateRelationship(expected);
		Invoice result = find(Invoice.class, invoice.getId());
		// validate
		SortedSet<InvoiceTraveler> invoiceTravelers = result.getInvoiceTravelers();
		InvoiceTraveler actual = invoiceTravelers.first();
		Assert.assertEquals(expected, actual);
		Assert.assertNotSame(expected, actual);
	}

	@Test
	public void testAddPayment() throws Exception {
		// setup
		Invoice invoice = InvoiceHelper.makeInvoiceWithoutId(customer, systemUser);
		InvoicePayment expected = InvoiceHelper.makePaymentWithoutId();
		// execute
		invoice.addInvoicePayment(expected);
		persist(invoice);
		// validate
		validateRelationship(expected);
		Invoice result = find(Invoice.class, invoice.getId());
		// validate
		SortedSet<InvoicePayment> invoicePayments = result.getInvoicePayments();
		InvoicePayment actual = invoicePayments.first();
		Assert.assertEquals(expected, actual);
		Assert.assertNotSame(expected, actual);
	}

	@Test
	public void testAddNote() throws Exception {
		// setup
		Invoice invoice = InvoiceHelper.makeInvoiceWithoutId(customer, systemUser);
		InvoiceNote expected = InvoiceHelper.makeNoteWithoutId();
		// execute
		invoice.addInvoiceNote(expected);
		persist(invoice);
		// validate
		validateRelationship(expected);
		Invoice result = find(Invoice.class, invoice.getId());
		// validate
		SortedSet<InvoiceNote> invoiceNotes = result.getInvoiceNotes();
		InvoiceNote actual = invoiceNotes.first();
		Assert.assertEquals(expected, actual);
		Assert.assertNotSame(expected, actual);
	}

	@Test
	public void testAddItem() throws Exception {
		// setup
		Invoice invoice = InvoiceHelper.makeInvoiceWithoutId(customer, systemUser);
		InvoiceItem expected = InvoiceHelper.makeItemWithoutId();
		// execute
		invoice.addInvoiceItem(expected);
		persist(invoice);
		// validate
		validateRelationship(expected);
		Invoice result = find(Invoice.class, invoice.getId());
		// validate
		List<InvoiceItem> invoiceItems = result.getInvoiceItems();
		InvoiceItem actual = invoiceItems.get(0);
		Assert.assertEquals(expected, actual);
		Assert.assertNotSame(expected, actual);
	}
}
