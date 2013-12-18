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
package ca.travelagency.invoice.view;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import ca.travelagency.components.decorators.ErrorAttributeModifier;
import ca.travelagency.components.decorators.InfoAttributeModifier;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.SalesAmounts;
import ca.travelagency.persistence.query.Condition;

public class InvoiceTotalsPanel extends Panel {
	private static final long serialVersionUID = 1L;

	static final String PATH = Invoice.Properties.salesAmounts.name()+Condition.SEPARATOR;

	public InvoiceTotalsPanel(String id, Invoice invoice) {
		this(id, DaoEntityModelFactory.make(invoice, Invoice.class));
	}

	public InvoiceTotalsPanel(String id, IModel<Invoice> model) {
        super(id, model);
        setOutputMarkupId(true);

		add(new Label(PATH+SalesAmounts.Properties.priceAsString.name()));
		add(new Label(PATH+SalesAmounts.Properties.taxAsString.name()));
		add(new Label(PATH+SalesAmounts.Properties.saleAsString.name()));
		add(new Label(PATH+SalesAmounts.Properties.paidAsString.name()));
		add(new Label(PATH+SalesAmounts.Properties.dueAsString.name())
			.add(model.getObject().getSalesAmounts().isDueAmount() ? new ErrorAttributeModifier() : new InfoAttributeModifier()));
		add(new Label(PATH+SalesAmounts.Properties.costAsString.name()));
		add(new Label(PATH+SalesAmounts.Properties.taxOnCommissionAsString.name()));
		add(new Label(PATH+SalesAmounts.Properties.commissionAsString.name()));
		add(new Label(PATH+SalesAmounts.Properties.commissionReceivedAsString.name()));
		add(new Label(PATH+SalesAmounts.Properties.commissionVerifiedAsString.name()));
    }
}
