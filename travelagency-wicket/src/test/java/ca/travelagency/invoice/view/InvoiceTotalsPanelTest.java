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
package ca.travelagency.invoice.view;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.SalesAmounts;

public class InvoiceTotalsPanelTest extends BaseWicketTester {
	private Invoice invoice;

	@Before
	public void setUp() {
		invoice = InvoiceHelper.makeInvoice();

		Mockito.when(daoSupport.find(Invoice.class, invoice.getId())).thenReturn(invoice);
	}

	@After
	public void tearDown() {
		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(Invoice.class, invoice.getId());
	}

	@Test
	public void testPageComponents() {
		InvoiceTotalsPanel invoiceTotalsPanel = new InvoiceTotalsPanel(COMPONENT_ID, invoice);
		tester.startComponentInPage(invoiceTotalsPanel);

		String path = COMPONENT_PATH+InvoiceTotalsPanel.PATH;
		tester.assertModelValue(path+SalesAmounts.Properties.priceAsString, invoice.getSalesAmounts().getPriceAsString());
		tester.assertModelValue(path+SalesAmounts.Properties.taxAsString, invoice.getSalesAmounts().getTaxAsString());
		tester.assertModelValue(path+SalesAmounts.Properties.saleAsString, invoice.getSalesAmounts().getSaleAsString());
		tester.assertModelValue(path+SalesAmounts.Properties.paidAsString, invoice.getSalesAmounts().getPaidAsString());
		tester.assertModelValue(path+SalesAmounts.Properties.dueAsString, invoice.getSalesAmounts().getDueAsString());
		tester.assertModelValue(path+SalesAmounts.Properties.costAsString, invoice.getSalesAmounts().getCostAsString());
		tester.assertModelValue(path+SalesAmounts.Properties.taxOnCommissionAsString, invoice.getSalesAmounts().getTaxOnCommissionAsString());
		tester.assertModelValue(path+SalesAmounts.Properties.commissionAsString, invoice.getSalesAmounts().getCommissionAsString());
		tester.assertModelValue(path+SalesAmounts.Properties.commissionReceivedAsString, invoice.getSalesAmounts().getCommissionReceivedAsString());
		tester.assertModelValue(path+SalesAmounts.Properties.commissionVerifiedAsString, invoice.getSalesAmounts().getCommissionVerifiedAsString());
	}
}
