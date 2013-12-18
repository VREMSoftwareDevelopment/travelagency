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
package ca.travelagency.salesreports;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BasePrintPage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.VelocityTemplateHelper;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Criteria;

import com.google.common.collect.Lists;

public class InvoiceReportPageTest extends BaseWicketTester {
	private SalesSearch salesSearch;
	private Criteria criteria;
	private VelocityTemplateHelper velocityTemplateHelper;

	@Before
	public void setUp() {
		velocityTemplateHelper = new VelocityTemplateHelper();

		SystemUser systemUser = velocityTemplateHelper.getSystemUser();
		Mockito.stub(daoSupport.find(SystemUser.class, systemUser.getId())).toReturn(systemUser);

		salesSearch = velocityTemplateHelper.getSalesSearch();
		criteria = salesSearch.getInvoiceCriteria();

		List<DaoEntity> invoices = Lists.newArrayList((DaoEntity) velocityTemplateHelper.getInvoice());
		Mockito.when(daoSupport.find(criteria)).thenReturn(invoices);
	}

	@After
	public void tearDown() {
		Mockito.verify(daoSupport).find(criteria);
		velocityTemplateHelper.tearDown();
	}

	@Test
	public void testSalesPage() throws Exception {
		InvoiceSalesModel model = InvoiceSalesModel.make(salesSearch);
		tester.startPage(new InvoiceReportPage(ReportsPage.SALES_TEMPLATE, model));
		tester.assertRenderedPage(InvoiceReportPage.class);

		tester.assertModelValue(BasePrintPage.TITLE, InvoiceReportPage.REPORT_TITLE);

		String actual = getActual();
		String expected = getExpected("expected-sales-report.html");
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testMonthlyPage() throws Exception {
		InvoiceSalesModel model = InvoiceSalesModel.make(salesSearch);
		tester.startPage(new InvoiceReportPage(ReportsPage.MONTHLY_TEMPLATE, model));
		tester.assertRenderedPage(InvoiceReportPage.class);

		tester.assertModelValue(BasePrintPage.TITLE, InvoiceReportPage.REPORT_TITLE);

		String actual = getActual();
		String expected = getExpected("expected-monthly-sales-report.html");
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testDueInvoicesPage() throws Exception {
		InvoiceSalesModel model = InvoiceSalesModel.make(salesSearch);
		tester.startPage(new InvoiceReportPage(ReportsPage.DUEINVOICES_TEMPLATE, model));
		tester.assertRenderedPage(InvoiceReportPage.class);

		tester.assertModelValue(BasePrintPage.TITLE, InvoiceReportPage.REPORT_TITLE);

		String actual = getActual();
		String expected = getExpected("expected-due-invoices-report.html");
		Assert.assertEquals(expected, actual);
	}

	private String getActual() {
		String lastResponseAsString = tester.getLastResponseAsString();
//		System.out.println(lastResponseAsString);
		String actual = velocityTemplateHelper.cleanupString(lastResponseAsString);
		return actual;
	}

	private String getExpected(String filename) throws Exception {
		return velocityTemplateHelper.getExpected(InvoiceReportPageTest.class.getResourceAsStream(filename));
	}

}
