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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.travelagency.config.Product;
import ca.travelagency.invoice.InvoiceHelper;
import ca.travelagency.invoice.InvoiceItem;
import ca.travelagency.utils.StringUtils;

import com.google.common.collect.Lists;

public class ProductSalesTest {
	private ProductSales fixture;
	private InvoiceItem invoiceItem;
	private Product product1;
	private Product product2;
	private Product product3;

	@Before
	public void setUp() {
		invoiceItem = InvoiceHelper.makeItem();

		product1 = Product.make(InvoiceHelper.ITEM_NAME+"-");
		product2 = Product.make(InvoiceHelper.ITEM_NAME);
		product3 = Product.make(StringUtils.reverse(InvoiceHelper.ITEM_NAME));

		List<Product> products = Lists.newArrayList(product3, product1, product2);

		fixture = ProductSales.make(Lists.newArrayList(invoiceItem), products);
	}

	@Test
	public void testProductsAreSorted() throws Exception {
		List<Product> products = fixture.getProducts();
		Assert.assertEquals(product1.getName(), products.get(0).getName());
		Assert.assertEquals(product2.getName(), products.get(1).getName());
		Assert.assertEquals(product3.getName(), products.get(2).getName());
	}

	@Test
	public void testNoMatch() throws Exception {
		fixture.setProducts(new ArrayList<Product>());
		Assert.assertEquals(invoiceItem.getDescription(), fixture.makeKey(invoiceItem));
	}

	@Test
	public void testMatchFirstProduct() throws Exception {
		invoiceItem.setDescription(product1.getName());
		Assert.assertEquals(product1.getName(), fixture.makeKey(invoiceItem));
	}

	@Test
	public void testMatchMiddleProduct() throws Exception {
		invoiceItem.setDescription(product2.getName());
		Assert.assertEquals(product2.getName(), fixture.makeKey(invoiceItem));
	}

	@Test
	public void testMatchLastProduct() throws Exception {
		invoiceItem.setDescription(product3.getName());
		Assert.assertEquals(product3.getName(), fixture.makeKey(invoiceItem));
	}
}
