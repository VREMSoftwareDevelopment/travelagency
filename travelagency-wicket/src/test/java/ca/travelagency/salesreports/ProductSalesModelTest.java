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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.config.Product;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.salesstats.ProductSales;

import com.google.common.collect.Lists;


public class ProductSalesModelTest extends BaseWicketTester {

	@Test
	public void testLoad() throws Exception {
		// setup
		List<DaoEntity> invoiceItems = Lists.newArrayList((DaoEntity) InvoiceHelper.makeItem());
		List<DaoEntity> products = new ArrayList<DaoEntity>();
		SalesSearch salesSearch = SalesSearch.make(null);
		ProductSalesModel fixture = ProductSalesModel.make(salesSearch);
		// expected
		Mockito.when(daoSupport.find(salesSearch.getInvoiceItemCriteria())).thenReturn(invoiceItems);
		Mockito.when(daoSupport.find(Product.class)).thenReturn(products);
		// execute
		ProductSales productSales = fixture.load();
		// validate
		Assert.assertEquals(invoiceItems.size(), productSales.getInvoiceItems().size());
		Assert.assertEquals(invoiceItems.get(0), productSales.getInvoiceItems().iterator().next());

		Mockito.verify(daoSupport).find(salesSearch.getInvoiceItemCriteria());
		Mockito.verify(daoSupport).find(Product.class);
	}
}
