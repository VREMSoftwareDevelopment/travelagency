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

import ca.travelagency.invoice.InvoiceItem;
import ca.travelagency.persistence.DaoSupport;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.salesstats.SupplierSales;


public class SupplierSalesModel extends LoadableDetachableModel<SupplierSales> {
	private static final long serialVersionUID = 1L;

	public static SupplierSalesModel make(SalesSearch salesSearch) {
		return new SupplierSalesModel(salesSearch);
	}

	@SpringBean
	protected DaoSupport<InvoiceItem> daoSupport;

	protected SalesSearch salesSearch;

	private SupplierSalesModel(SalesSearch salesSearch) {
		Validate.notNull(salesSearch);
		this.salesSearch = salesSearch;
	    Injector.get().inject(this);
	}

	@Override
	protected SupplierSales load() {
		Criteria criteria = salesSearch.getInvoiceItemCriteria();
		List<InvoiceItem> invoiceItems = daoSupport.find(criteria);
		return SupplierSales.make(invoiceItems);
	}

	public SalesSearch getReportsSearch() {
		return salesSearch;
	}
}
