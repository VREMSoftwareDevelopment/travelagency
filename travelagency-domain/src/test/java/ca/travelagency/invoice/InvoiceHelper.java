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

import java.math.BigDecimal;
import java.util.Date;

import ca.travelagency.customer.Customer;
import ca.travelagency.customer.CustomerHelper;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.utils.DateUtils;
public class InvoiceHelper {
	public static final String BOOKING_NUMBER = "BOOKINGNUMBER";
	public static final String SUPPLIER_NAME = "SUPPLIER";
	public static final String ITEM_NAME = "ITEM";
	public static final int COMMISSION_AMOUNT = 30;
	public static final int TAX_AMOUNT = 50;
	public static final int PRICE_AMOUNT = 100;
	public static final int TAX_ON_COMMISSION_AMOUNT = 1;

	private static long count = 0;

	public static Invoice makeInvoiceWithDetails() {
		return makeInvoiceWithDetails(null, 0);
	}

	public static Invoice makeInvoiceWithDetails(SystemUser systemUser, int monthAdd) {
		Invoice invoice = makeInvoice(systemUser, monthAdd);
		invoice.addInvoiceTraveler(makeTraveler());
		invoice.addInvoiceDestination(makeDestination());
		invoice.addInvoiceItem(makeItem());
		invoice.addInvoicePayment(makePayment());
		invoice.addInvoiceNote(makeNote());
		return invoice;
	}

	public static Invoice makeInvoice() {
		return makeInvoice(null, 0);
	}

	public static Invoice makeInvoice(SystemUser systemUser, int monthAdd) {
		if (systemUser == null) {
			systemUser = SystemUserHelper.makeSystemUser();
		}
		return makeInvoice(CustomerHelper.makeCustomer(), systemUser, monthAdd);
	}

	public static Invoice makeInvoice(Customer customer, SystemUser systemUser, int monthAdd) {
		Invoice invoice = makeInvoiceWithoutId(customer, systemUser, monthAdd);
		invoice.setId(count);
		for (InvoiceTraveler invoiceTraveler : invoice.getInvoiceTravelers()) {
			invoiceTraveler.setId(count--);
		}
		for (InvoiceNote invoiceNote: invoice.getInvoiceNotes()) {
			invoiceNote.setId(count--);
		}
		return invoice;
	}

	public static Invoice makeInvoiceWithoutId(Customer customer, SystemUser systemUser) {
		return makeInvoiceWithoutId(customer, systemUser, 0);
	}

	public static Invoice makeInvoiceWithoutId(Customer customer, SystemUser systemUser, int monthAdd) {
		count--;
		Invoice invoice = Invoice.make(customer, systemUser);
		invoice.setDate(DateUtils.addMonths(invoice.getDate(), monthAdd));
		invoice.setTotalDueDate(DateUtils.addDays(invoice.getDate(), 10));
		return invoice;
	}

	public static InvoiceDestination makeDestination() {
		InvoiceDestination invoiceDestination = makeDestinationWithoutId();
		invoiceDestination.setId(count);
		return invoiceDestination;
	}

	public static InvoiceDestination makeDestinationWithoutId() {
		count--;
		Date departureDate = new Date();
		return InvoiceDestination.make("departureCity"+count, departureDate, "arrivalCity"+count);
	}

	public static InvoiceTraveler makeTraveler() {
		InvoiceTraveler invoiceTraveler = makeTravelerWithoutId();
		invoiceTraveler.setId(count);
		return invoiceTraveler;
	}

	public static InvoiceTraveler makeTravelerWithoutId() {
		count--;
		return InvoiceTraveler.make(CustomerHelper.makeCustomer());
	}

	public static InvoicePayment makePayment() {
		InvoicePayment invoicePayment = makePaymentWithoutId();
		invoicePayment.setId(count);
		return invoicePayment;
	}

	public static InvoicePayment makePaymentWithoutId() {
		count--;
		return InvoicePayment.make(new BigDecimal(Math.abs(count) * PRICE_AMOUNT), new Date());
	}

	public static InvoiceItem makeItem() {
		InvoiceItem invoiceItem = makeItemWithoutId();
		invoiceItem.setId(count);
		return invoiceItem;
	}

	public static InvoiceItem makeItemWithoutId() {
		count--;
		InvoiceItem invoiceItem = InvoiceItem.make(ITEM_NAME+count, new BigDecimal(Math.abs(count) * PRICE_AMOUNT));
		invoiceItem.setCommission(new BigDecimal(Math.abs(count) * COMMISSION_AMOUNT));
		invoiceItem.setTax(new BigDecimal(Math.abs(count) * TAX_AMOUNT));
		invoiceItem.setTaxOnCommission(new BigDecimal(Math.abs(count) * TAX_ON_COMMISSION_AMOUNT));
		invoiceItem.setBookingDate(new Date());
		invoiceItem.setBookingNumber(BOOKING_NUMBER+count);
		invoiceItem.setQty((int) count);
		invoiceItem.setSupplier(SUPPLIER_NAME+count);
		return invoiceItem;
	}

	public static InvoiceNote makeNote() {
		InvoiceNote invoiceNote = makeNoteWithoutId();
		invoiceNote.setId(count);
		return invoiceNote;
	}

	public static InvoiceNote makeNoteWithoutId() {
		count--;
		return InvoiceNote.make("this is a long note"+count);
	}

	public static long getCount() {
		return count;
	}

	public static void setCount(long count) {
		InvoiceHelper.count = count;
	}



}
