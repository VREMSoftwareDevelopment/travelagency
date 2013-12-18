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

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.salesstats.InvoiceSales;

import com.google.common.collect.Lists;


public class InvoiceSalesModelTest extends BaseWicketTester {

	@Test
	public void testLoad() throws Exception {
		// setup
		SalesSearch salesSearch = SalesSearch.make(null);
		InvoiceSalesModel fixture = InvoiceSalesModel.make(salesSearch);
		List<DaoEntity> invoices = Lists.newArrayList((DaoEntity) InvoiceHelper.makeInvoice());
		// expected
		Mockito.when(daoSupport.find(salesSearch.getInvoiceCriteria())).thenReturn(invoices);
		// execute
		InvoiceSales invoiceSales = fixture.load();
		// validate
		Assert.assertEquals(invoices.size(), invoiceSales.getInvoices().size());
		Assert.assertEquals(invoices.get(0), invoiceSales.getInvoices().iterator().next());
		Mockito.verify(daoSupport).find(salesSearch.getInvoiceCriteria());
	}
}
