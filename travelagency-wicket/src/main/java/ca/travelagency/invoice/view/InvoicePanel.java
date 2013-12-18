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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.OddEvenListItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import ca.travelagency.components.CheckMarkPanel;
import ca.travelagency.components.NumberPropertyColumn;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.customer.CustomerPanel;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceDestination;
import ca.travelagency.invoice.InvoiceItem;
import ca.travelagency.invoice.InvoiceNote;
import ca.travelagency.invoice.InvoicePayment;
import ca.travelagency.invoice.InvoiceTraveler;
import ca.travelagency.invoice.SalesAmounts;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.utils.DateUtils;

public class InvoicePanel extends Panel {
	private static final long serialVersionUID = 1L;

	static final String CUSTOMER_PANEL = "customer";
	static final String TOTALS_PANEL = "totals";

	public InvoicePanel(String id, IModel<Invoice> model) {
		super(id, DaoEntityModelFactory.make(model.getObject()));

		add(new CustomerPanel(CUSTOMER_PANEL, model.getObject().getCustomer()));
		add(new InvoiceTotalsPanel(TOTALS_PANEL, model.getObject()));

		add(new Label(Invoice.Properties.invoiceNumber.name()));
		add(DateLabel.forDateStyle(Invoice.Properties.date.name(), DateUtils.DATE_STYLE));
		add(DateLabel.forDateStyle(Invoice.Properties.totalDueDate.name(), DateUtils.DATE_STYLE));
		add(new Label(Invoice.Properties.systemUser.name()+"."+SystemUser.PROPERTY_NAME));
		add(new Label(Invoice.Properties.status.name()));

		add(new CheckMarkPanel(Invoice.Properties.billCompany.name(), model.getObject().isBillCompany()));

    	add(addInvoiceDestinations(model));
    	add(addInvoiceTravelers(model));
    	add(addInvoiceItems(model));
    	add(addInvoiceNotes(model));
    	add(addInvoicePayments(model));
	}

	private Component addInvoiceTravelers(final IModel<Invoice> model) {
		List<IColumn<InvoiceTraveler, String>> columns = new ArrayList<IColumn<InvoiceTraveler, String>>();
		columns.add(new PropertyColumn<InvoiceTraveler, String>(new ResourceModel("invoice.panel.traveler.name"), InvoiceTraveler.PROPERTY_NAME));
		columns.add(new PropertyColumn<InvoiceTraveler, String>(new ResourceModel("invoice.panel.traveler.documentType"), InvoiceTraveler.Properties.documentType.name()));
		columns.add(new PropertyColumn<InvoiceTraveler, String>(new ResourceModel("invoice.panel.traveler.documentNumber"), InvoiceTraveler.Properties.documentNumber.name()));
		columns.add(new PropertyColumn<InvoiceTraveler, String>(new ResourceModel("invoice.panel.traveler.dateOfBirth"), InvoiceTraveler.Properties.dateOfBirth.name()){
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<InvoiceTraveler>> item, String componentId, IModel<InvoiceTraveler> rowModel) {
				item.add(DateLabel.forDateStyle(componentId, Model.of(rowModel.getObject().getDateOfBirth()), DateUtils.DATE_STYLE));
			}
		});

		ISortableDataProvider<InvoiceTraveler, String> dataProvider = new ISortableDataProvider<InvoiceTraveler, String>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Iterator<? extends InvoiceTraveler> iterator(long first, long count) {
				return model.getObject().getInvoiceTravelers().iterator();
			}
			@Override
			public long size() {
				return model.getObject().getInvoiceTravelers().size();
			}
			@Override
			public IModel<InvoiceTraveler> model(InvoiceTraveler invoiceTraveler) {
				return DaoEntityModelFactory.make(invoiceTraveler);
			}
			@Override
			public void detach() {}
			@Override
			public ISortState<String> getSortState() {return null;}
		};
		DefaultDataTable<InvoiceTraveler, String> dataTable = new DefaultDataTable<InvoiceTraveler, String>(
				Invoice.Properties.invoiceTravelers.name(),
				columns,
				dataProvider,
				Math.max(model.getObject().getInvoiceTravelers().size(), 1));

		return dataTable;
	}

	private Component addInvoiceDestinations(final IModel<Invoice> model) {
		List<IColumn<InvoiceDestination, String>> columns = new ArrayList<IColumn<InvoiceDestination, String>>();
		columns.add(new PropertyColumn<InvoiceDestination, String>(new ResourceModel("invoice.panel.destination.departurePlace"), InvoiceDestination.Properties.departurePlace.name()));
		columns.add(new PropertyColumn<InvoiceDestination, String>(new ResourceModel("invoice.panel.destination.departureDate"), InvoiceDestination.Properties.departureDate.name()){
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<InvoiceDestination>> item, String componentId, IModel<InvoiceDestination> rowModel) {
				item.add(DateLabel.forDateStyle(componentId, Model.of(rowModel.getObject().getDepartureDate()), DateUtils.DATE_STYLE));
			}
		});
		columns.add(new PropertyColumn<InvoiceDestination, String>(new ResourceModel("invoice.panel.destination.arrivalPlace"), InvoiceDestination.Properties.arrivalPlace.name()));
		columns.add(new PropertyColumn<InvoiceDestination, String>(new ResourceModel("invoice.panel.destination.arrivalDate"), InvoiceDestination.Properties.arrivalDate.name()){
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<InvoiceDestination>> item, String componentId, IModel<InvoiceDestination> rowModel) {
				item.add(DateLabel.forDateStyle(componentId, Model.of(rowModel.getObject().getArrivalDate()), DateUtils.DATE_STYLE));
			}
		});

		ISortableDataProvider<InvoiceDestination, String> dataProvider = new ISortableDataProvider<InvoiceDestination, String>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Iterator<? extends InvoiceDestination> iterator(long first, long count) {
				return model.getObject().getInvoiceDestinations().iterator();
			}
			@Override
			public long size() {
				return model.getObject().getInvoiceDestinations().size();
			}
			@Override
			public IModel<InvoiceDestination> model(InvoiceDestination invoiceDestination) {
				return DaoEntityModelFactory.make(invoiceDestination);
			}
			@Override
			public void detach() {}
			@Override
			public ISortState<String> getSortState() {return null;}
		};
		DefaultDataTable<InvoiceDestination, String> dataTable = new DefaultDataTable<InvoiceDestination, String>(
				Invoice.Properties.invoiceDestinations.name(),
				columns,
				dataProvider,
				Math.max(model.getObject().getInvoiceDestinations().size(), 1));

		return dataTable;
	}

	private Component addInvoiceNotes(final IModel<Invoice> model) {
		List<IColumn<InvoiceNote, String>> columns = new ArrayList<IColumn<InvoiceNote, String>>();
		columns.add(new PropertyColumn<InvoiceNote, String>(new ResourceModel("invoice.panel.note.text"), InvoiceNote.Properties.text.name()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<InvoiceNote>> item, String componentId, IModel<InvoiceNote> rowModel) {
				item.add(new Label(componentId, rowModel.getObject().getText().replaceAll("\\<.*?>","")));
			}
		});
		columns.add(new PropertyColumn<InvoiceNote, String>(new ResourceModel("invoice.panel.note.private"), InvoiceNote.Properties.privateNote.name()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<InvoiceNote>> item, String componentId, IModel<InvoiceNote> rowModel) {
				item.add(new CheckMarkPanel(componentId, rowModel.getObject().isPrivateNote()));
			}
		});
		columns.add(new PropertyColumn<InvoiceNote, String>(new ResourceModel("invoice.panel.note.date"), InvoiceNote.Properties.date.name()){
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<InvoiceNote>> item, String componentId, IModel<InvoiceNote> rowModel) {
				item.add(DateLabel.forDateStyle(componentId, Model.of(rowModel.getObject().getDate()), DateUtils.DATE_STYLE));
			}
		});

		ISortableDataProvider<InvoiceNote, String> dataProvider = new ISortableDataProvider<InvoiceNote, String>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Iterator<? extends InvoiceNote> iterator(long first, long count) {
				return model.getObject().getInvoiceNotes().iterator();
			}
			@Override
			public long size() {
				return model.getObject().getInvoiceNotes().size();
			}
			@Override
			public IModel<InvoiceNote> model(InvoiceNote invoiceNote) {
				return DaoEntityModelFactory.make(invoiceNote);
			}
			@Override
			public void detach() {}
			@Override
			public ISortState<String> getSortState() {return null;}
		};
		DefaultDataTable<InvoiceNote, String> dataTable = new DefaultDataTable<InvoiceNote, String>(
				Invoice.Properties.invoiceNotes.name(),
				columns,
				dataProvider,
				Math.max(model.getObject().getInvoiceNotes().size(), 1));

		return dataTable;
	}

	private Component addInvoicePayments(final IModel<Invoice> model) {
		List<IColumn<InvoicePayment, String>> columns = new ArrayList<IColumn<InvoicePayment, String>>();
		columns.add(new PropertyColumn<InvoicePayment, String>(new ResourceModel("invoice.panel.payment.date"), InvoicePayment.Properties.date.name()){
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<InvoicePayment>> item, String componentId, IModel<InvoicePayment> rowModel) {
				item.add(DateLabel.forDateStyle(componentId, Model.of(rowModel.getObject().getDate()), DateUtils.DATE_STYLE));
			}
		});
		columns.add(new NumberPropertyColumn<InvoicePayment>(new ResourceModel("invoice.panel.payment.amount"), InvoicePayment.Properties.amountAsString.name()));
		columns.add(new NumberPropertyColumn<InvoicePayment>(new ResourceModel("invoice.panel.payment.reconciled"), InvoicePayment.Properties.reconciled.name()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<InvoicePayment>> item, String componentId, IModel<InvoicePayment> rowModel) {
				item.add(new CheckMarkPanel(componentId, rowModel.getObject().isReconciled()));
			}
		});
		columns.add(new PropertyColumn<InvoicePayment, String>(new ResourceModel("invoice.panel.payment.type"), InvoicePayment.Properties.paymentType.name()));

		ISortableDataProvider<InvoicePayment, String> dataProvider = new ISortableDataProvider<InvoicePayment, String>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Iterator<? extends InvoicePayment> iterator(long first, long count) {
				return model.getObject().getInvoicePayments().iterator();
			}
			@Override
			public long size() {
				return model.getObject().getInvoicePayments().size();
			}
			@Override
			public IModel<InvoicePayment> model(InvoicePayment invoicePayment) {
				return DaoEntityModelFactory.make(invoicePayment);
			}
			@Override
			public void detach() {}
			@Override
			public ISortState<String> getSortState() {return null;}
		};
		DefaultDataTable<InvoicePayment, String> dataTable = new DefaultDataTable<InvoicePayment, String>(
				Invoice.Properties.invoicePayments.name(),
				columns,
				dataProvider,
				Math.max(model.getObject().getInvoicePayments().size(), 1));

		return dataTable;
	}

	private Component addInvoiceItems(IModel<Invoice> model) {
		ListView<InvoiceItem> listView = new ListView<InvoiceItem>(Invoice.Properties.invoiceItems.name(), model.getObject().getInvoiceItems()) {
			private static final long serialVersionUID = 1L;
			@Override
			protected ListItem<InvoiceItem> newItem(int index, IModel<InvoiceItem> itemModel) {
				return new OddEvenListItem<InvoiceItem>(index, itemModel);
			}
			@Override
			protected void populateItem(ListItem<InvoiceItem> item) {
				item.setModel(DaoEntityModelFactory.make(item.getModelObject()));

				item.add(new Label(InvoiceItem.Properties.description.name()));
				item.add(new Label(InvoiceItem.Properties.supplier.name()));
				item.add(new Label(InvoiceItem.Properties.commissionAsString.name()));
				item.add(new Label(InvoiceItem.Properties.salesAmounts.name() + Condition.SEPARATOR + SalesAmounts.Properties.commissionAsString));
				item.add(new Label(InvoiceItem.Properties.taxOnCommissionAsString.name()));
				item.add(new Label(InvoiceItem.Properties.priceAsString.name()));
				item.add(new Label(InvoiceItem.Properties.taxAsString.name()));
				item.add(new Label(InvoiceItem.Properties.cancelBeforeDeparture.name()));
				item.add(new Label(InvoiceItem.Properties.changeBeforeDeparture.name()));
				item.add(new Label(InvoiceItem.Properties.changeAfterDeparture.name()));
				item.add(new Label(InvoiceItem.Properties.qty.name()));
				item.add(new Label(InvoiceItem.Properties.salesAmounts.name() + Condition.SEPARATOR + SalesAmounts.Properties.saleAsString.name()));
				item.add(new Label(InvoiceItem.Properties.bookingNumber.name()));
				item.add(DateLabel.forDateStyle(InvoiceItem.Properties.bookingDate.name(), DateUtils.DATE_STYLE));
				item.add(new Label(InvoiceItem.Properties.commissionStatus.name()));
			}
		};

		return listView;
	}
}
