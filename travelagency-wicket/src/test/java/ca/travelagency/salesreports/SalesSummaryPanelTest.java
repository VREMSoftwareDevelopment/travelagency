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

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BasePage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.SalesAmounts;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.salesstats.InvoiceSales;
import ca.travelagency.salesstats.MonthlyDistribution;

import com.google.common.collect.Lists;

public class SalesSummaryPanelTest extends BaseWicketTester {

	private SalesSearch salesSearch;

	@Before
	public void setUp() throws Exception {
		salesSearch = SalesSearch.make(null);
	}

	@Test
	public void testWithSomeSales() throws Exception {
		List<DaoEntity> invoices = Lists.newArrayList((DaoEntity) InvoiceHelper.makeInvoice());
		InvoiceSales invoiceSales = InvoiceSales.make(Lists.newArrayList((Invoice) invoices.get(0)));

		Mockito.stub(daoSupport.find(salesSearch.getInvoiceCriteria())).toReturn(invoices);

		SalesSummaryPanel salesSummaryPanel = new SalesSummaryPanel(COMPONENT_ID, salesSearch);
		tester.startComponentInPage(salesSummaryPanel);

		tester.assertComponent(COMPONENT_PATH+InvoiceSales.Properties.monthlyDistribution.name(), ListView.class);

		List<MonthlyDistribution> monthlyDistribution = invoiceSales.getMonthlyDistribution();
		Assert.assertEquals(monthlyDistribution.size(), getDataTable().getViewSize());

		String path = COMPONENT_PATH+InvoiceSales.Properties.monthlyDistribution.name()+BasePage.PATH_SEPARATOR+"0"+BasePage.PATH_SEPARATOR;
		tester.assertModelValue(path+MonthlyDistribution.Properties.dateAsString, monthlyDistribution.get(0).getDateAsString());
		tester.assertModelValue(path+MonthlyDistribution.Properties.salesAmounts+Condition.SEPARATOR+SalesAmounts.Properties.saleAsString, monthlyDistribution.get(0).getSalesAmounts().getSaleAsString());
		tester.assertModelValue(path+MonthlyDistribution.Properties.salesAmounts+Condition.SEPARATOR+SalesAmounts.Properties.costAsString, monthlyDistribution.get(0).getSalesAmounts().getCostAsString());
		tester.assertModelValue(path+MonthlyDistribution.Properties.salesAmounts+Condition.SEPARATOR+SalesAmounts.Properties.commissionAsString, monthlyDistribution.get(0).getSalesAmounts().getCommissionAsString());
		tester.assertModelValue(path+MonthlyDistribution.Properties.salesAmounts+Condition.SEPARATOR+SalesAmounts.Properties.commissionReceivedAsString, monthlyDistribution.get(0).getSalesAmounts().getCommissionReceivedAsString());
		tester.assertModelValue(path+MonthlyDistribution.Properties.salesAmounts+Condition.SEPARATOR+SalesAmounts.Properties.commissionVerifiedAsString, monthlyDistribution.get(0).getSalesAmounts().getCommissionVerifiedAsString());
		tester.assertModelValue(path+MonthlyDistribution.Properties.salesAmounts+Condition.SEPARATOR+SalesAmounts.Properties.paidAsString, monthlyDistribution.get(0).getSalesAmounts().getPaidAsString());
		tester.assertModelValue(path+MonthlyDistribution.Properties.salesAmounts+Condition.SEPARATOR+SalesAmounts.Properties.taxOnCommissionAsString, monthlyDistribution.get(0).getSalesAmounts().getTaxOnCommissionAsString());

		tester.assertComponent(path+SalesSummaryPanel.LINK_TO_INVOICES, Link.class);
		tester.assertModelValue(path+SalesSummaryPanel.LINK_TO_INVOICES+BasePage.PATH_SEPARATOR+MonthlyDistribution.Properties.count,
			monthlyDistribution.get(0).getCount());

		tester.assertComponent(COMPONENT_PATH+SalesSummaryPanel.EXPORT_TO_CSV, Link.class);
	}

	@Test
	public void testWithNoSales() {
		Mockito.stub(daoSupport.find(salesSearch.getInvoiceCriteria())).toReturn(Lists.<DaoEntity>newArrayList());

		SalesSummaryPanel invoicesStatsPanel = new SalesSummaryPanel(COMPONENT_ID, salesSearch);
		tester.startComponentInPage(invoicesStatsPanel);

		tester.assertInvisible(COMPONENT_PATH+SalesSummaryPanel.EXPORT_TO_CSV);
	}

	@SuppressWarnings("unchecked")
	private ListView<MonthlyDistribution> getDataTable() {
		return (ListView<MonthlyDistribution>) tester.getComponentFromLastRenderedPage(COMPONENT_PATH+InvoiceSales.Properties.monthlyDistribution.name());
	}

}
