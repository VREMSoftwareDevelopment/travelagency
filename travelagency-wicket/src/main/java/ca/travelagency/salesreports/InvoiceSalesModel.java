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

import ca.travelagency.invoice.Invoice;
import ca.travelagency.persistence.DaoSupport;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.salesstats.InvoiceSales;

public class InvoiceSalesModel extends LoadableDetachableModel<InvoiceSales> {

	private static final long serialVersionUID = 1L;

	public static InvoiceSalesModel make(SalesSearch salesSearch) {
		return new InvoiceSalesModel(salesSearch);
	}

	@SpringBean
	protected DaoSupport<Invoice> daoSupport;

	protected SalesSearch salesSearch;

	private InvoiceSalesModel(SalesSearch salesSearch) {
		Validate.notNull(salesSearch);
		this.salesSearch = salesSearch;
	    Injector.get().inject(this);
	}

	@Override
	protected InvoiceSales load() {
		Criteria criteria = salesSearch.getInvoiceCriteria();
		List<Invoice> invoices = daoSupport.find(criteria);
		return InvoiceSales.make(invoices).setSystemUser(salesSearch.getSystemUser());
	}

	public SalesSearch getReportsSearch() {
		return salesSearch;
	}

}
