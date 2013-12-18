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
package ca.travelagency.invoice;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

import ca.travelagency.BasePrintPage;
import ca.travelagency.components.formheader.DaoEntityModelFactory;

@AuthorizeInstantiation({"AGENT"})
public class InvoicePreviewPage extends BasePrintPage {
	private static final long serialVersionUID = 1L;

	static final String VELOCITY_TEMPLATE = "invoice";
	static final String INVOICE = "Invoice";

	// testing only
	@Deprecated
	public InvoicePreviewPage() {
		super(VELOCITY_TEMPLATE, null);
	}

	public InvoicePreviewPage(Invoice invoice) {
		super(VELOCITY_TEMPLATE, DaoEntityModelFactory.make(invoice));
	}

	@Override
	protected Map<String, Object> getObjectMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("invoice", getDefaultModelObject());
		return map;
	}

	@Override
	protected String getPageTitle() {
		Invoice invoice = (Invoice) getDefaultModelObject();
		if (invoice == null) {
			return INVOICE;
		}
		return INVOICE + "-" + invoice.getInvoiceNumber() + "-" + invoice.getCustomer().getLastName();
	}
}
