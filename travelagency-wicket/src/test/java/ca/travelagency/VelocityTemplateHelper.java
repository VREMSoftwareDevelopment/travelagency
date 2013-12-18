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
package ca.travelagency;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import org.apache.wicket.util.io.IOUtils;

import ca.travelagency.customer.Customer;
import ca.travelagency.customer.CustomerHelper;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoiceNote;
import ca.travelagency.salesreports.SalesSearch;
import ca.travelagency.utils.DateUtils;

public class VelocityTemplateHelper {
	private long systemUserCount;
	private long customerCount;
	private long invoiceCount;
	private Invoice invoice;
	private SalesSearch salesSearch;

	public VelocityTemplateHelper() {
		systemUserCount = SystemUserHelper.getCount(); SystemUserHelper.setCount(1L);
		customerCount = CustomerHelper.getCount(); CustomerHelper.setCount(1L);
		invoiceCount = InvoiceHelper.getCount(); InvoiceHelper.setCount(1L);

		invoice = InvoiceHelper.makeInvoiceWithDetails();
		invoice.setDate(makeDate());
		invoice.setTotalDueDate(makeTotalDueDate());
		invoice.getInvoiceDestinations().first().setDepartureDate(makeDepartureDate());
		invoice.getInvoiceItems().get(0).setBookingDate(makeBookingDate());
		invoice.getInvoicePayments().first().setDate(makePaymentDate());

		InvoiceNote privateInvoiceNote = InvoiceHelper.makeNote();
		privateInvoiceNote.setPrivateNote(true);
		invoice.addInvoiceNote(privateInvoiceNote);

		salesSearch = SalesSearch.make(invoice.getSystemUser());
		salesSearch.setDateFrom(makeDateFrom());
		salesSearch.setDateTo(makeDateTo());
	}

	private Date makeDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(2011, 8, 10, 10, 25, 5);
		return calendar.getTime();
	}
	private Date makeDepartureDate() {
		return DateUtils.setDays(makeDate(), 30);
	}
	private Date makeBookingDate() {
		return DateUtils.setDays(makeDate(), 15);
	}
	private Date makePaymentDate() {
		return DateUtils.setDays(makeDate(), 20);
	}
	private Date makeTotalDueDate() {
		return DateUtils.setDays(makeDate(), 25);
	}
	private Date makeDateFrom() {
		return DateUtils.setDays(makeDate(), 1);
	}
	private Date makeDateTo() {
		return DateUtils.setDays(makeDate(), 30);
	}

	public void tearDown() {
		SystemUserHelper.getCount(); SystemUserHelper.setCount(systemUserCount);
		CustomerHelper.getCount(); CustomerHelper.setCount(customerCount);
		InvoiceHelper.getCount(); InvoiceHelper.setCount(invoiceCount);
	}

	public String getExpected(InputStream inputStream) throws Exception {
		return cleanupString(IOUtils.toString(inputStream));
	}


	public String cleanupString(String source) {
		return source.replace("\n", "").replace("\r", "").replace("\t", "").replace(" ", "").trim();
	}

	public Invoice getInvoice() {
		return invoice;
	}
	public Customer getCustomer() {
		return invoice.getCustomer();
	}
	public SystemUser getSystemUser() {
		return invoice.getSystemUser();
	}
	public SalesSearch getSalesSearch() {
		return salesSearch;
	}
}
