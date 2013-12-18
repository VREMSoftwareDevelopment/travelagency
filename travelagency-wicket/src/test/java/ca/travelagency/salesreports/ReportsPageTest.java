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

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BasePage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.fields.DateField;
import ca.travelagency.components.fields.SystemUserField;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.persistence.DaoEntity;

import com.google.common.collect.Lists;


public class ReportsPageTest extends BaseWicketTester {
	private static final String PATH = ReportsPage.SEARCH_FORM+BasePage.PATH_SEPARATOR;

	private SystemUser systemUser;

	@Before
	public void setUp() {
		systemUser = SystemUserHelper.makeSystemUser();
		stubSystemUserField(
			Lists.newArrayList(systemUser, SystemUserHelper.makeSystemUser(), SystemUserHelper.makeSystemUser())
		);

		Mockito.stub(daoSupport.find(SystemUser.class, systemUser.getId())).toReturn(systemUser);
	}

	@Test
	public void testSearchForm() throws Exception {
		tester.startPage(ReportsPage.class);
		tester.assertRenderedPage(ReportsPage.class);

		tester.assertComponent(ReportsPage.SEARCH_FORM, Form.class);

		tester.assertComponent(PATH+ReportsPage.FEEDBACK, ComponentFeedbackPanel.class);

		tester.assertComponent(PATH+SalesSearch.Properties.dateFrom, DateField.class);
		tester.assertComponent(PATH+SalesSearch.Properties.dateTo, DateField.class);
		tester.assertComponent(PATH+SalesSearch.Properties.systemUser, SystemUserField.class);
		tester.assertComponent(PATH+SalesSearch.Properties.showDetails, CheckBox.class);
	}

	@Test
	public void testSalesReport() throws Exception {
		tester.startPage(ReportsPage.class);
		tester.assertRenderedPage(ReportsPage.class);

		tester.assertComponent(PATH+ReportsPage.SALES_REPORT, Button.class);

		FormTester formTester = tester.newFormTester(ReportsPage.SEARCH_FORM);
		formTester.select(SalesSearch.Properties.systemUser.name(), 0);

		SalesSearch salesSearch = (SalesSearch) formTester.getForm().getModelObject();
		List<DaoEntity> invoices = Lists.newArrayList((DaoEntity) InvoiceHelper.makeInvoice());
		Mockito.stub(daoSupport.find(salesSearch.getInvoiceCriteria())).toReturn(invoices);

		formTester.submit(ReportsPage.SALES_REPORT);

		tester.assertRenderedPage(InvoiceReportPage.class);
	}

	@Test
	public void testMonthlyReport() throws Exception {
		tester.startPage(ReportsPage.class);
		tester.assertRenderedPage(ReportsPage.class);

		tester.assertComponent(PATH+ReportsPage.MONTHLY_REPORT, Button.class);

		FormTester formTester = tester.newFormTester(ReportsPage.SEARCH_FORM);
		formTester.select(SalesSearch.Properties.systemUser.name(), 0);

		SalesSearch salesSearch = (SalesSearch) formTester.getForm().getModelObject();
		List<DaoEntity> invoices = Lists.newArrayList((DaoEntity) InvoiceHelper.makeInvoice());
		Mockito.stub(daoSupport.find(salesSearch.getInvoiceCriteria())).toReturn(invoices);

		formTester.submit(ReportsPage.MONTHLY_REPORT);

		tester.assertRenderedPage(InvoiceReportPage.class);
	}

	@Test
	public void testSupplierReport() throws Exception {
		tester.startPage(ReportsPage.class);
		tester.assertRenderedPage(ReportsPage.class);

		tester.assertComponent(PATH+ReportsPage.SUPPLIER_REPORT, Button.class);

		FormTester formTester = tester.newFormTester(ReportsPage.SEARCH_FORM);
		formTester.select(SalesSearch.Properties.systemUser.name(), 0);

		SalesSearch salesSearch = (SalesSearch) formTester.getForm().getModelObject();
		List<DaoEntity> invoices = Lists.newArrayList((DaoEntity) InvoiceHelper.makeInvoice());
		Mockito.stub(daoSupport.find(salesSearch.getInvoiceCriteria())).toReturn(invoices);

		formTester.submit(ReportsPage.SUPPLIER_REPORT);

		tester.assertRenderedPage(SupplierReportPage.class);
	}

	@Test
	public void testProductReport() throws Exception {
		tester.startPage(ReportsPage.class);
		tester.assertRenderedPage(ReportsPage.class);

		tester.assertComponent(PATH+ReportsPage.PRODUCT_REPORT, Button.class);

		FormTester formTester = tester.newFormTester(ReportsPage.SEARCH_FORM);
		formTester.select(SalesSearch.Properties.systemUser.name(), 0);

		SalesSearch salesSearch = (SalesSearch) formTester.getForm().getModelObject();
		List<DaoEntity> invoices = Lists.newArrayList((DaoEntity) InvoiceHelper.makeInvoice());
		Mockito.stub(daoSupport.find(salesSearch.getInvoiceCriteria())).toReturn(invoices);

		formTester.submit(ReportsPage.PRODUCT_REPORT);

		tester.assertRenderedPage(ProductReportPage.class);
	}

	@Test
	public void testPaymentReport() throws Exception {
		tester.startPage(ReportsPage.class);
		tester.assertRenderedPage(ReportsPage.class);

		tester.assertComponent(PATH+ReportsPage.PAYMENT_REPORT, Button.class);

		FormTester formTester = tester.newFormTester(ReportsPage.SEARCH_FORM);
		formTester.select(SalesSearch.Properties.systemUser.name(), 0);

		SalesSearch salesSearch = (SalesSearch) formTester.getForm().getModelObject();
		List<DaoEntity> invoicePayments = Lists.newArrayList((DaoEntity) InvoiceHelper.makePayment());
		Mockito.stub(daoSupport.find(salesSearch.getInvoicePaymentCriteria())).toReturn(invoicePayments);

		formTester.submit(ReportsPage.PAYMENT_REPORT);

		tester.assertRenderedPage(PaymentReportPage.class);
	}

	@Test
	public void testPaymentReportAsAgent() throws Exception {
		usingAgentSystemUser();
		tester.startPage(ReportsPage.class);
		tester.assertRenderedPage(ReportsPage.class);
		tester.isInvisible(PATH+ReportsPage.PAYMENT_REPORT);
	}
}
