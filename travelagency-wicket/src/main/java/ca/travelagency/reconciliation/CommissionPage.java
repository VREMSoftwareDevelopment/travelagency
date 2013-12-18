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
package ca.travelagency.reconciliation;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import ca.travelagency.BasePage;
import ca.travelagency.components.NumberPropertyColumn;
import ca.travelagency.components.behaviours.AjaxOnBlurBehaviour;
import ca.travelagency.components.dataprovider.DataProvider;
import ca.travelagency.components.decorators.FieldDecorator;
import ca.travelagency.components.fields.DateField;
import ca.travelagency.components.fields.StringFieldHelper;
import ca.travelagency.components.fields.SupplierField;
import ca.travelagency.components.fields.SystemUserField;
import ca.travelagency.invoice.CommissionStatus;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceDestinationsPanel;
import ca.travelagency.invoice.InvoiceItem;
import ca.travelagency.invoice.InvoicePageLink;
import ca.travelagency.invoice.InvoiceTravelersPanel;
import ca.travelagency.invoice.SalesAmounts;
import ca.travelagency.invoice.view.InvoiceViewLink;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.utils.DateUtils;
import ca.travelagency.utils.StringUtils;

import com.google.common.collect.Lists;

@AuthorizeInstantiation({"ADMIN"})
public class CommissionPage extends BasePage {
	private static final long serialVersionUID = 1L;

	static final String INVOICE_PATH = InvoiceItem.Properties.invoice.name()+Condition.SEPARATOR;
	static final String SALES_AMOUNTS_PATH = InvoiceItem.Properties.salesAmounts.name()+Condition.SEPARATOR;

	static final String SEARCH_FORM = "searchForm";
	static final String SEARCH_BUTTON = "searchButton";
	static final String CLEAR_BUTTON = "clearButton";
	static final String FEEDBACK = "feedback";
	static final String DATA_TABLE = "dataTable";

	static final String PAGE_TITLE = "commissionPage.title";

	public CommissionPage() {
		super();
		IModel<InvoiceItemFilter> model = Model.of(getAuthenticatedSession().getInvoiceItemFilter());

    	add(makeSearchForm(model));
    	add(new AjaxFallbackDefaultDataTable<InvoiceItem, String>(DATA_TABLE,
    			makeColumns(model),
    			new DataProvider<InvoiceItem>(model),
    			BasePage.DATA_TABLE_PER_PAGE));
	}

	private Form<InvoiceItemFilter> makeSearchForm(IModel<InvoiceItemFilter> model) {
		final Form<InvoiceItemFilter> form = new Form<InvoiceItemFilter>(SEARCH_FORM,
				new CompoundPropertyModel<InvoiceItemFilter>(model));

		form.add(new ComponentFeedbackPanel(FEEDBACK, form));

		form.add(new SupplierField(InvoiceItemFilter.Properties.searchText.name())
			.setLabel(new ResourceModel("commissionPage.filter.searchText"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new SystemUserField(InvoiceItemFilter.Properties.systemUser.name())
			.setLabel(new ResourceModel("commissionPage.filter.systemUser"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new DropDownChoice<CommissionStatus>(InvoiceItemFilter.Properties.commissionStatus.name(),	Lists.newArrayList(CommissionStatus.values()))
			.setLabel(new ResourceModel("commissionPage.filter.commissionStatus"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new DateField(InvoiceItemFilter.Properties.invoiceDateFrom.name())
			.setLabel(new ResourceModel("commissionPage.filter.dateFrom"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new DateField(InvoiceItemFilter.Properties.invoiceDateTo.name())
			.setLabel(new ResourceModel("commissionPage.filter.dateTo"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));

		form.add(new Button(SEARCH_BUTTON) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {
				setResponsePage(new CommissionPage());
    		}
    	});

		form.add(new Button(CLEAR_BUTTON) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {
				getAuthenticatedSession().clearInvoiceItemFilter();
				setResponsePage(new CommissionPage());
    		}
		}.setDefaultFormProcessing(false));

		return form;
	}

	private List<IColumn<InvoiceItem, String>> makeColumns(IModel<InvoiceItemFilter> model) {
		List<IColumn<InvoiceItem, String>> columns = new ArrayList<IColumn<InvoiceItem, String>>();

		columns.add(new PropertyColumn<InvoiceItem, String>(new ResourceModel("commissionPage.invoiceNumber"), INVOICE_PATH+Invoice.Properties.invoiceNumber.name()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<InvoiceItem>> item, String componentId, IModel<InvoiceItem> rowModel) {
				item.add(new InvoicePageLink(componentId, rowModel.getObject().getInvoice()));
			}
		});
		columns.add(new PropertyColumn<InvoiceItem, String>(Model.of(StringUtils.EMPTY), InvoiceItem.PROPERTY_ID) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<InvoiceItem>> item, String componentId, IModel<InvoiceItem> rowModel) {
				item.add(new InvoiceViewLink(componentId, rowModel.getObject().getInvoice()));
			}
		});
		columns.add(new PropertyColumn<InvoiceItem, String>(new ResourceModel("commissionPage.supplier"), InvoiceItem.Properties.supplier.name(), InvoiceItem.Properties.supplier.name()));
		columns.add(new PropertyColumn<InvoiceItem, String>(new ResourceModel("commissionPage.description"), InvoiceItem.Properties.description.name(), InvoiceItem.Properties.description.name()));
		columns.add(new PropertyColumn<InvoiceItem, String>(new ResourceModel("commissionPage.invoiceTravelers"), INVOICE_PATH+Invoice.Properties.invoiceTravelers.name()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<InvoiceItem>> item, String componentId, IModel<InvoiceItem> rowModel) {
				item.add(new InvoiceTravelersPanel(componentId, rowModel.getObject().getInvoice()));
			}
		});
		columns.add(new NumberPropertyColumn<InvoiceItem>(new ResourceModel("commissionPage.price"), InvoiceItem.Properties.saleAsString.name()));
		columns.add(new NumberPropertyColumn<InvoiceItem>(new ResourceModel("commissionPage.qty"), InvoiceItem.Properties.qty.name()));
		columns.add(new NumberPropertyColumn<InvoiceItem>(new ResourceModel("commissionPage.totalAmount"), SALES_AMOUNTS_PATH+SalesAmounts.Properties.saleAsString.name()));
		columns.add(new NumberPropertyColumn<InvoiceItem>(new ResourceModel("commissionPage.totalTaxOnCommission"), SALES_AMOUNTS_PATH+SalesAmounts.Properties.taxOnCommissionAsString.name()));
		columns.add(new NumberPropertyColumn<InvoiceItem>(new ResourceModel("commissionPage.totalCommission"), SALES_AMOUNTS_PATH+SalesAmounts.Properties.commissionAsString.name()));
		columns.add(new PropertyColumn<InvoiceItem, String>(new ResourceModel("commissionPage.commissionStatus"), InvoiceItem.Properties.commissionStatus.name()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<InvoiceItem>> item, String componentId, IModel<InvoiceItem> rowModel) {
				item.add(new CommissionStatusForm(componentId, rowModel.getObject()));
			}
		});
		columns.add(new PropertyColumn<InvoiceItem, String>(new ResourceModel("commissionPage.bookingDate"), InvoiceItem.Properties.bookingDate.name()){
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<InvoiceItem>> item, String componentId, IModel<InvoiceItem> rowModel) {
				item.add(DateLabel.forDateStyle(componentId, Model.of(rowModel.getObject().getBookingDate()), DateUtils.DATE_STYLE));
			}
		});
		columns.add(new PropertyColumn<InvoiceItem, String>(new ResourceModel("commissionPage.invoiceDestinations"), INVOICE_PATH+Invoice.Properties.invoiceDestinations.name()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<InvoiceItem>> item, String componentId, IModel<InvoiceItem> rowModel) {
				item.add(new InvoiceDestinationsPanel(componentId, rowModel.getObject().getInvoice()));
			}
		});

		return columns;
	}

	@Override
	public String getPageTitleKey() {
		return PAGE_TITLE;
	}
}
