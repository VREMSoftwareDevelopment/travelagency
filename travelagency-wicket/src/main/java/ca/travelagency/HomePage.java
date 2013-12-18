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
package ca.travelagency;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import ca.travelagency.identity.Role;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceFilter;
import ca.travelagency.invoice.InvoiceStatus;
import ca.travelagency.invoice.InvoicesPanel;
import ca.travelagency.persistence.query.OrderBy;
import ca.travelagency.salesreports.SalesSearch;
import ca.travelagency.salesreports.SalesSummaryPanel;

@AuthorizeInstantiation({"AGENT"})
public class HomePage extends BasePage {
	private static final long serialVersionUID = 1L;

	static final String UNPAID_INVOICES = "unpaidInvoices";
	static final String INVOICES_STATS = "invoicesStats";

    public HomePage() {
    	super();

    	add(new InvoicesPanel(UNPAID_INVOICES, getInvoiceModel()));
    	add(new SalesSummaryPanel(INVOICES_STATS, SalesSearch.make(getSystemUser()).setDateFromBy6Month()));
    }

	IModel<InvoiceFilter> getInvoiceModel() {
		InvoiceFilter invoiceFilter = new InvoiceFilter()
			.setSystemUser(getSystemUser())
			.setStatus(InvoiceStatus.Active)
			.setDefaultOrderBy(OrderBy.make(Invoice.Properties.totalDueDate.name()))
			.setShowUnpaidInvoicesOnly(true);

    	return Model.of(invoiceFilter);
	}

	SystemUser getSystemUser() {
    	if (hasRole(Role.ADMIN)) {
    		return null;
    	}
    	return getSignedInSystemUser();
	}

	@Override
	public String getPageTitleKey() {
		return "homePage.title";
	}

}
