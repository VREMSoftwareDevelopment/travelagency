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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BasePrintPage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.VelocityTemplateHelper;

public class InvoicePreviewPageTest extends BaseWicketTester {
	private VelocityTemplateHelper velocityTemplateHelper;
	private Invoice invoice;

	@Before
	public void setUp() {
		velocityTemplateHelper = new VelocityTemplateHelper();
		invoice = velocityTemplateHelper.getInvoice();
		Mockito.when(daoSupport.find(Invoice.class, invoice.getId())).thenReturn(invoice);
	}

	@After
	public void tearDown() {
		Mockito.verify(daoSupport).find(Invoice.class, invoice.getId());
		velocityTemplateHelper.tearDown();
	}

	@Test
	public void testPageComponents() throws Exception {
		tester.startPage(new InvoicePreviewPage(invoice));
		tester.assertRenderedPage(InvoicePreviewPage.class);
		tester.assertModelValue(BasePrintPage.TITLE, InvoicePreviewPage.INVOICE + "-" + invoice.getInvoiceNumber() + "-" + invoice.getCustomer().getLastName());

		String lastResponseAsString = tester.getLastResponseAsString();
//		System.out.println(lastResponseAsString);
		String actual = velocityTemplateHelper.cleanupString(lastResponseAsString);
		String expected = velocityTemplateHelper.getExpected(InvoicePreviewPageTest.class.getResourceAsStream("expected-invoice.html"));
		Assert.assertEquals(expected, actual);
	}

}
