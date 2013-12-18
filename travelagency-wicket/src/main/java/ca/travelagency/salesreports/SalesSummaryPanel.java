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

import java.util.Date;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.OddEvenListItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.resource.StringBufferResourceStream;

import ca.travelagency.authentication.AuthenticatedSession;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.invoice.InvoiceFilter;
import ca.travelagency.invoice.InvoicesPage;
import ca.travelagency.invoice.SalesAmounts;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.salesstats.InvoiceSales;
import ca.travelagency.salesstats.MonthlyDistribution;
import ca.travelagency.utils.CsvWriter;
import ca.travelagency.utils.DateUtils;

public class SalesSummaryPanel extends Panel {
	private static final long serialVersionUID = 1L;

	static final String LINK_TO_INVOICES = "link";
	static final String EXPORT_TO_CSV = "csv";

	public SalesSummaryPanel(String id, SalesSearch salesSearch) {
		super(id, new CompoundPropertyModel<InvoiceSales>(InvoiceSalesModel.make(salesSearch)));

		ListView<MonthlyDistribution> listView = new ListView<MonthlyDistribution>(InvoiceSales.Properties.monthlyDistribution.name()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected ListItem<MonthlyDistribution> newItem(int index,	IModel<MonthlyDistribution> itemModel) {
				return new OddEvenListItem<MonthlyDistribution>(index, itemModel);
			}

			@Override
			protected void populateItem(final ListItem<MonthlyDistribution> item) {
				item.setModel(new CompoundPropertyModel<MonthlyDistribution>(item.getModelObject()));

				item.add(new Label(MonthlyDistribution.Properties.dateAsString.name()));

		        Link<Void> link = new Link<Void>(LINK_TO_INVOICES) {
					private static final long serialVersionUID = 1L;
					@Override
					public void onClick() {
						getAuthenticatedSession().clearInvoiceFilter();
						InvoiceFilter invoiceFilter = getAuthenticatedSession().getInvoiceFilter();
						invoiceFilter.setSystemUser(getSystemUser());
						Date date = item.getModelObject().getDate();
						invoiceFilter.setInvoiceDateFrom(date);
						invoiceFilter.setInvoiceDateTo(DateUtils.addDays(DateUtils.addMonths(date, 1), -1));
						setResponsePage(new InvoicesPage());
					}
		        };
				link.add(new Label(MonthlyDistribution.Properties.count.name()));
				item.add(link);

				item.add(new Label(MonthlyDistribution.Properties.salesAmounts.name()+Condition.SEPARATOR+SalesAmounts.Properties.saleAsString.name()));
				item.add(new Label(MonthlyDistribution.Properties.salesAmounts.name()+Condition.SEPARATOR+SalesAmounts.Properties.costAsString.name()));
				item.add(new Label(MonthlyDistribution.Properties.salesAmounts.name()+Condition.SEPARATOR+SalesAmounts.Properties.commissionAsString.name()));
				item.add(new Label(MonthlyDistribution.Properties.salesAmounts.name()+Condition.SEPARATOR+SalesAmounts.Properties.commissionReceivedAsString.name()));
				item.add(new Label(MonthlyDistribution.Properties.salesAmounts.name()+Condition.SEPARATOR+SalesAmounts.Properties.commissionVerifiedAsString.name()));
				item.add(new Label(MonthlyDistribution.Properties.salesAmounts.name()+Condition.SEPARATOR+SalesAmounts.Properties.taxOnCommissionAsString.name()));
				item.add(new Label(MonthlyDistribution.Properties.salesAmounts.name()+Condition.SEPARATOR+SalesAmounts.Properties.paidAsString.name()));
			}
		};
		add(listView);

		Link<String> downloadLink = new Link<String>(EXPORT_TO_CSV, Model.of(makeCsvOutput())) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				StringBufferResourceStream resourceStream = new StringBufferResourceStream("text/csv");
				resourceStream.append((CharSequence) getDefaultModelObject());

				ResourceStreamRequestHandler handler =
					new ResourceStreamRequestHandler(resourceStream)
						.setFileName("export.csv")
						.setContentDisposition(ContentDisposition.ATTACHMENT);

				getRequestCycle().scheduleRequestHandlerAfterCurrent(handler);
			}
		};
		downloadLink.setVisible(!getSales().getMonthlyDistribution().isEmpty());
        add(downloadLink);
	}


	private String makeCsvOutput() {
		InvoiceSales invoiceSales = getSales();

		SystemUser systemUser = getSystemUser();

		CsvWriter csvWriter = new CsvWriter();

		// header
		if (systemUser != null) {
			csvWriter.write(systemUser.getName());
			csvWriter.endLine();
		}
		csvWriter.write(getLocalizer().getString("date", SalesSummaryPanel.this));
		csvWriter.write(getLocalizer().getString("count", SalesSummaryPanel.this));
		csvWriter.write(getLocalizer().getString("salesAmount", SalesSummaryPanel.this));
		csvWriter.write(getLocalizer().getString("costAmount", SalesSummaryPanel.this));
		csvWriter.write(getLocalizer().getString("commissionAmount", SalesSummaryPanel.this));
		csvWriter.write(getLocalizer().getString("commissionReceived", SalesSummaryPanel.this));
		csvWriter.write(getLocalizer().getString("commissionVerified", SalesSummaryPanel.this));
		csvWriter.write(getLocalizer().getString("hstOnCommission", SalesSummaryPanel.this));
		csvWriter.write(getLocalizer().getString("paidAmount", SalesSummaryPanel.this));
		csvWriter.endLine();

		for (MonthlyDistribution statistic : invoiceSales.getMonthlyDistribution()) {
			csvWriter.write(statistic.getDateAsString());
			csvWriter.write(statistic.getCount());
			csvWriter.write(statistic.getSalesAmounts().getSaleAsString());
			csvWriter.write(statistic.getSalesAmounts().getCostAsString());
			csvWriter.write(statistic.getSalesAmounts().getCommissionAsString());
			csvWriter.write(statistic.getSalesAmounts().getCommissionReceivedAsString());
			csvWriter.write(statistic.getSalesAmounts().getCommissionVerifiedAsString());
			csvWriter.write(statistic.getSalesAmounts().getTaxOnCommissionAsString());
			csvWriter.write(statistic.getSalesAmounts().getPaidAsString());
			csvWriter.endLine();
		}
		return csvWriter.toString();
	}

	InvoiceSalesModel getSalesModel() {
		@SuppressWarnings("unchecked")
		CompoundPropertyModel<InvoiceSales> compoundPropertyModel = ((CompoundPropertyModel<InvoiceSales>) getDefaultModel());
		return (InvoiceSalesModel) compoundPropertyModel.getChainedModel();
	}

	InvoiceSales getSales() {
		return (InvoiceSales) getSalesModel().getObject();
	}

	SystemUser getSystemUser() {
		return getSalesModel().getReportsSearch().getSystemUser();
	}

	AuthenticatedSession getAuthenticatedSession() {
		return ((AuthenticatedSession) getSession());
	}
}
