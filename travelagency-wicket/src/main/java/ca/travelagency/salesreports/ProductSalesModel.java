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

import org.apache.commons.lang3.Validate;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ca.travelagency.config.Product;
import ca.travelagency.invoice.InvoiceItem;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.DaoSupport;
import ca.travelagency.salesstats.ProductSales;

public class ProductSalesModel  extends LoadableDetachableModel<ProductSales> {
	private static final long serialVersionUID = 1L;

	public static ProductSalesModel make(SalesSearch salesSearch) {
		return new ProductSalesModel(salesSearch);
	}

	@SpringBean
	protected DaoSupport<? extends DaoEntity> daoSupport;

	protected SalesSearch salesSearch;

	private ProductSalesModel(SalesSearch salesSearch) {
		Validate.notNull(salesSearch);
		this.salesSearch = salesSearch;
	    Injector.get().inject(this);
	}

	@Override
	protected ProductSales load() {
		return ProductSales.make(getInvoiceItems(), getProducts());
	}

	@SuppressWarnings("unchecked")
	private List<InvoiceItem> getInvoiceItems() {
		return (List<InvoiceItem>) daoSupport.find(salesSearch.getInvoiceItemCriteria());
	}

	@SuppressWarnings("unchecked")
	private List<Product> getProducts() {
		return (List<Product>) daoSupport.find(Product.class);
	}

	public SalesSearch getReportsSearch() {
		return salesSearch;
	}
}
