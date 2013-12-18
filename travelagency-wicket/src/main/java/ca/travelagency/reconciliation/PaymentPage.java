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
import org.apache.wicket.markup.html.form.CheckBox;
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
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceCustomerPanel;
import ca.travelagency.invoice.InvoicePageLink;
import ca.travelagency.invoice.InvoicePayment;
import ca.travelagency.invoice.InvoiceTravelersPanel;
import ca.travelagency.invoice.PaymentType;
import ca.travelagency.invoice.view.InvoiceViewLink;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.utils.DateUtils;
import ca.travelagency.utils.StringUtils;

import com.google.common.collect.Lists;

@AuthorizeInstantiation({"ADMIN"})
public class PaymentPage extends BasePage {
	private static final long serialVersionUID = 1L;

	static final String INVOICE_PATH = InvoicePayment.Properties.invoice.name()+Condition.SEPARATOR;

	static final String SEARCH_FORM = "searchForm";
	static final String SEARCH_BUTTON = "searchButton";
	static final String CLEAR_BUTTON = "clearButton";
	static final String FEEDBACK = "feedback";
	static final String DATA_TABLE = "dataTable";

	static final String PAGE_TITLE = "paymentPage.title";

	public PaymentPage() {
		super();
		IModel<InvoicePaymentFilter> model = Model.of(getAuthenticatedSession().getInvoicePaymentFilter());

    	add(makeSearchForm(model));
    	add(new AjaxFallbackDefaultDataTable<InvoicePayment, String>(DATA_TABLE,
    			makeColumns(model),
    			new DataProvider<InvoicePayment>(model),
    			BasePage.DATA_TABLE_PER_PAGE));
	}

	private Form<InvoicePaymentFilter> makeSearchForm(IModel<InvoicePaymentFilter> model) {
		final Form<InvoicePaymentFilter> form = new Form<InvoicePaymentFilter>(SEARCH_FORM,
				new CompoundPropertyModel<InvoicePaymentFilter>(model));

		form.add(new ComponentFeedbackPanel(FEEDBACK, form));

		form.add(new SupplierField(InvoicePaymentFilter.Properties.searchText.name())
			.setLabel(new ResourceModel("paymentPage.filter.searchText"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new SystemUserField(InvoicePaymentFilter.Properties.systemUser.name())
			.setLabel(new ResourceModel("paymentPage.filter.systemUser"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new DropDownChoice<PaymentType>(InvoicePaymentFilter.Properties.paymentType.name(), Lists.newArrayList(PaymentType.values()))
			.setLabel(new ResourceModel("paymentPage.filter.paymentType"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new CheckBox(InvoicePaymentFilter.Properties.reconciled.name())
			.setLabel(new ResourceModel("paymentPage.filter.reconciled"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new DateField(InvoicePaymentFilter.Properties.paymentDateFrom.name())
			.setLabel(new ResourceModel("paymentPage.filter.dateFrom"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new DateField(InvoicePaymentFilter.Properties.paymentDateTo.name())
			.setLabel(new ResourceModel("paymentPage.filter.dateTo"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));

		form.add(new Button(SEARCH_BUTTON) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {
				setResponsePage(new PaymentPage());
    		}
    	});

		form.add(new Button(CLEAR_BUTTON) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {
				getAuthenticatedSession().clearInvoicePaymentFilter();
				setResponsePage(new PaymentPage());
    		}
		}.setDefaultFormProcessing(false));

		return form;
	}

	private List<IColumn<InvoicePayment, String>> makeColumns(IModel<InvoicePaymentFilter> model) {
		List<IColumn<InvoicePayment, String>> columns = new ArrayList<IColumn<InvoicePayment, String>>();

		columns.add(new PropertyColumn<InvoicePayment, String>(new ResourceModel("paymentPage.paymentType"), InvoicePayment.Properties.paymentType.name()));
		columns.add(new PropertyColumn<InvoicePayment, String>(new ResourceModel("paymentPage.paymentDate"), InvoicePayment.Properties.date.name()){
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<InvoicePayment>> item, String componentId, IModel<InvoicePayment> rowModel) {
				item.add(DateLabel.forDateStyle(componentId, Model.of(rowModel.getObject().getDate()), DateUtils.DATE_STYLE));
			}
		});
		columns.add(new NumberPropertyColumn<InvoicePayment>(new ResourceModel("paymentPage.amount"), InvoicePayment.Properties.amountAsString.name()));
		columns.add(new PropertyColumn<InvoicePayment, String>(new ResourceModel("paymentPage.reconciled"), InvoicePayment.Properties.reconciled.name()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<InvoicePayment>> item, String componentId, IModel<InvoicePayment> rowModel) {
				item.add(new ReconciledForm(componentId, rowModel.getObject()));
			}
		});
		columns.add(new PropertyColumn<InvoicePayment, String>(new ResourceModel("paymentPage.invoiceNumber"), INVOICE_PATH+Invoice.Properties.invoiceNumber.name()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<InvoicePayment>> item, String componentId, IModel<InvoicePayment> rowModel) {
				item.add(new InvoicePageLink(componentId, rowModel.getObject().getInvoice()));
			}
		});
		columns.add(new PropertyColumn<InvoicePayment, String>(Model.of(StringUtils.EMPTY), INVOICE_PATH+Invoice.PROPERTY_ID) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<InvoicePayment>> item, String componentId, IModel<InvoicePayment> rowModel) {
				item.add(new InvoiceViewLink(componentId, rowModel.getObject().getInvoice()));
			}
		});
		columns.add(new PropertyColumn<InvoicePayment, String>(new ResourceModel("paymentPage.invoiceDate"), INVOICE_PATH+Invoice.Properties.date.name()){
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<InvoicePayment>> item, String componentId, IModel<InvoicePayment> rowModel) {
				item.add(DateLabel.forDateStyle(componentId, Model.of(rowModel.getObject().getInvoice().getDate()), DateUtils.DATE_STYLE));
			}
		});
		columns.add(new PropertyColumn<InvoicePayment, String>(new ResourceModel("paymentPage.customer"), INVOICE_PATH+Invoice.Properties.customer.name()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<InvoicePayment>> item, String componentId, IModel<InvoicePayment> rowModel) {
				item.add(new InvoiceCustomerPanel(componentId, rowModel.getObject().getInvoice().getCustomer()));
			}
		});
		columns.add(new PropertyColumn<InvoicePayment, String>(new ResourceModel("paymentPage.invoiceTravelers"), INVOICE_PATH+Invoice.Properties.invoiceTravelers.name()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<InvoicePayment>> item, String componentId, IModel<InvoicePayment> rowModel) {
				item.add(new InvoiceTravelersPanel(componentId, rowModel.getObject().getInvoice()));
			}
		});

		return columns;
	}

	@Override
	public String getPageTitleKey() {
		return PAGE_TITLE;
	}
}
