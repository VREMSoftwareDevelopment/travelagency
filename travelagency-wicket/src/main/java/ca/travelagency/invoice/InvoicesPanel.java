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

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import ca.travelagency.BasePage;
import ca.travelagency.authentication.AuthenticatedSession;
import ca.travelagency.components.NumberPropertyColumn;
import ca.travelagency.components.dataprovider.DataProvider;
import ca.travelagency.components.decorators.ErrorAttributeModifier;
import ca.travelagency.components.decorators.InfoAttributeModifier;
import ca.travelagency.identity.Role;
import ca.travelagency.invoice.view.InvoiceViewLink;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.utils.DateUtils;
import ca.travelagency.utils.StringUtils;

public class InvoicesPanel extends Panel {
	private static final long serialVersionUID = 1L;

	static final String AGENT_PATH = Invoice.Properties.systemUser.name()+Condition.SEPARATOR;
	static final String SALES_AMOUNT_PATH = Invoice.Properties.salesAmounts.name()+Condition.SEPARATOR;

	static final String DATA_TABLE = "dataTable";

	public InvoicesPanel(String id, IModel<InvoiceFilter> model) {
		super(id, model);
		setOutputMarkupId(true);
    	add(new AjaxFallbackDefaultDataTable<Invoice, String>(DATA_TABLE,
    			makeColumns(model),
    			new DataProvider<Invoice>(model),
    			BasePage.DATA_TABLE_PER_PAGE));
	}

	private List<IColumn<Invoice, String>> makeColumns(IModel<InvoiceFilter> model) {
		List<IColumn<Invoice, String>> columns = new ArrayList<IColumn<Invoice, String>>();

		columns.add(new PropertyColumn<Invoice, String>(new ResourceModel("invoice.number"), DaoEntity.PROPERTY_ID, Invoice.Properties.invoiceNumber.name()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<Invoice>> item, String componentId, IModel<Invoice> rowModel) {
				item.add(new InvoicePageLink(componentId, rowModel));
			}
		});
		columns.add(new PropertyColumn<Invoice, String>(Model.of(StringUtils.EMPTY), DaoEntity.PROPERTY_ID) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<Invoice>> item, String componentId, IModel<Invoice> rowModel) {
				item.add(new InvoiceViewLink(componentId, rowModel));
			}
		});
		columns.add(new PropertyColumn<Invoice, String>(new ResourceModel("invoice.status"), Invoice.Properties.status.name()){
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<Invoice>> item, String componentId, IModel<Invoice> rowModel) {
				Component component;
				if (hasRole(Role.ADMIN)) {
					component = new InvoiceStatusForm(componentId, rowModel.getObject());
				} else {
					component = new Label(componentId, Model.of(rowModel.getObject().getStatus()));
				}
				item.add(component);
			}
		});
		columns.add(new PropertyColumn<Invoice, String>(new ResourceModel("invoice.date"), Invoice.Properties.date.name(), Invoice.Properties.date.name()){
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<Invoice>> item, String componentId, IModel<Invoice> rowModel) {
				item.add(DateLabel.forDateStyle(componentId, Model.of(rowModel.getObject().getDate()), DateUtils.DATE_STYLE));
			}
		});
		if (!model.getObject().isSystemUser()) {
			columns.add(new PropertyColumn<Invoice, String>(new ResourceModel("invoice.agent"), AGENT_PATH+DaoEntity.PROPERTY_NAME));
		}
		if (!model.getObject().isCustomer()) {
			columns.add(new PropertyColumn<Invoice, String>(new ResourceModel("invoice.customer"), Invoice.Properties.customer.name()) {
				private static final long serialVersionUID = 1L;
				@Override
				public void populateItem(Item<ICellPopulator<Invoice>> item, String componentId, IModel<Invoice> rowModel) {
					item.add(new InvoiceCustomerPanel(componentId, rowModel.getObject().getCustomer()));
				}
			});
		}
		columns.add(new PropertyColumn<Invoice, String>(new ResourceModel("invoice.travelers"), Invoice.Properties.invoiceTravelers.name()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<Invoice>> item, String componentId, IModel<Invoice> rowModel) {
				item.add(new InvoiceTravelersPanel(componentId, rowModel));
			}
		});
		columns.add(new PropertyColumn<Invoice, String>(new ResourceModel("invoice.totalDueDate"), Invoice.Properties.totalDueDate.name(), Invoice.Properties.totalDueDate.name()){
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<Invoice>> item, String componentId, IModel<Invoice> rowModel) {
				item.add(DateLabel.forDateStyle(componentId, Model.of(rowModel.getObject().getTotalDueDate()), DateUtils.DATE_STYLE));
			}
		});
		columns.add(new NumberPropertyColumn<Invoice>(new ResourceModel("invoice.totalAmount"), SALES_AMOUNT_PATH+SalesAmounts.Properties.saleAsString.name()));
		columns.add(new NumberPropertyColumn<Invoice>(new ResourceModel("invoice.totalPaid"), SALES_AMOUNT_PATH+SalesAmounts.Properties.paidAsString.name()));
		columns.add(new NumberPropertyColumn<Invoice>(new ResourceModel("invoice.totalDue"), SALES_AMOUNT_PATH+SalesAmounts.Properties.dueAsString.name()){
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<Invoice>> item, String componentId, IModel<Invoice> rowModel) {
				item.add(new Label(componentId, getDataModel(rowModel))
					.add(rowModel.getObject().getSalesAmounts().isDueAmount() ? new ErrorAttributeModifier() : new InfoAttributeModifier()));
			}
		});

		return columns;
	}

	private boolean hasRole(Role role) {
		return ((AuthenticatedSession) getSession()).hasRole(role);
	}
}