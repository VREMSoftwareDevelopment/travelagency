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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.CompareToBuilder;

import ca.travelagency.config.Product;
import ca.travelagency.invoice.InvoiceItem;

public class ProductSales extends InvoiceItemSales {
	private static final long serialVersionUID = 1L;

	public static ProductSales make(List<InvoiceItem> invoiceItems, List<Product> products) {
		ProductSales productSales = new ProductSales(invoiceItems);
		productSales.setProducts(products);
		return productSales;
	}

	private List<Product> products;

	private ProductSales(List<InvoiceItem> invoiceItems) {
		super(invoiceItems);
	}

	@Override
	protected String makeKey(InvoiceItem invoiceItem) {
		String result = invoiceItem.getDescription().trim();
		for (Product product : this.products) {
			if (product.getName().length() == result.length() && product.getName().equals(result)) {
				return product.getName();
			}
		}
		for (Product product : this.products) {
			if (product.getName().length() < result.length() && result.startsWith(product.getName())) {
				return product.getName();
			}
		}
		for (Product product : this.products) {
			if (product.getName().length() > result.length() && product.getName().startsWith(result)) {
				return product.getName();
			}
		}
		return result;
	}

	List<Product> getProducts() {
		return products;
	}

	void setProducts(List<Product> products) {
		Validate.notNull(products);
		this.products = new ArrayList<Product>(products);
		Collections.sort(this.products, new ProductComparator());
	}

	static class ProductComparator implements Comparator<Product>, Serializable {
		private static final long serialVersionUID = 1L;
		@Override
		public int compare(Product product1, Product product2) {
			return new CompareToBuilder()
				.append(product2.getName().length(), product1.getName().length())
				.append(product1.getName(), product2.getName())
				.toComparison();
		}

	}
}
