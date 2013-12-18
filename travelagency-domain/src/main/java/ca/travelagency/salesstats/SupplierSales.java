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
package ca.travelagency.salesstats;

import java.util.List;

import ca.travelagency.invoice.InvoiceItem;

public class SupplierSales extends InvoiceItemSales {
	private static final long serialVersionUID = 1L;

	public static SupplierSales make(List<InvoiceItem> invoiceItems) {
		return new SupplierSales(invoiceItems);
	}

	private SupplierSales(List<InvoiceItem> invoiceItems) {
		super(invoiceItems);
	}

	@Override
	protected String makeKey(InvoiceItem invoiceItem) {
		return invoiceItem.getSupplier();
	}

}
