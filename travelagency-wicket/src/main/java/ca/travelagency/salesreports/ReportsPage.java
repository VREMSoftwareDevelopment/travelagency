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

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;

import ca.travelagency.BasePage;
import ca.travelagency.components.behaviors.AjaxOnBlurBehavior;
import ca.travelagency.components.decorators.FieldDecorator;
import ca.travelagency.components.fields.DateField;
import ca.travelagency.components.fields.SystemUserField;
import ca.travelagency.identity.Role;

@AuthorizeInstantiation({"AGENT"})
public class ReportsPage extends BasePage {
	private static final long serialVersionUID = 1L;

	static final String SALES_TEMPLATE = "salesReport";
	static final String MONTHLY_TEMPLATE = "monthlyReport";
	static final String DUEINVOICES_TEMPLATE = "dueInvoicesReport";

	static final String SEARCH_FORM = "searchForm";
	static final String FEEDBACK = "feedback";

	static final String SALES_REPORT = "salesReport";
	static final String DUEINVOICES_REPORT = "dueInvoicesReport";
	static final String MONTHLY_REPORT = "monthlyReport";
	static final String SUPPLIER_REPORT = "supplierReport";
	static final String PRODUCT_REPORT = "productReport";
	static final String PAYMENT_REPORT = "paymentReport";

	static final String PAGE_TITLE = "reports.title";

	public ReportsPage() {
		super();

		SalesSearch salesSearch =
			hasRole(Role.ADMIN)
				? SalesSearch.make()
				: SalesSearch.make(getSignedInSystemUser());

		Form<SalesSearch> form = new Form<SalesSearch>(SEARCH_FORM, new CompoundPropertyModel<SalesSearch>(salesSearch));
		add(form);

		form.add(new ComponentFeedbackPanel(FEEDBACK, form));

		form.add(new SystemUserField(SalesSearch.Properties.systemUser.name())
			.setLabel(new ResourceModel("agent"))
			.setVisible(hasRole(Role.ADMIN))
			.add(new FieldDecorator(), new AjaxOnBlurBehavior()));
		form.add(new DateField(SalesSearch.Properties.dateFrom.name())
			.setLabel(new ResourceModel("dateFrom"))
			.setRequired(true)
			.add(new FieldDecorator(), new AjaxOnBlurBehavior()));
		form.add(new DateField(SalesSearch.Properties.dateTo.name())
			.setLabel(new ResourceModel("dateTo"))
			.setRequired(true)
			.add(new FieldDecorator(), new AjaxOnBlurBehavior()));
		form.add(new CheckBox(SalesSearch.Properties.showDetails.name())
			.setLabel(new ResourceModel("showDetails"))
			.add(new FieldDecorator(), new AjaxOnBlurBehavior()));

		form.add(new Button(SALES_REPORT) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {
				SalesSearch salesSearch = ((SalesSearch) getForm().getModelObject()).resetShowUnpaidInvoicesOnly();
				InvoiceSalesModel invoiceSalesModel = InvoiceSalesModel.make(salesSearch);
				setResponsePage(new InvoiceReportPage(SALES_TEMPLATE, invoiceSalesModel));
    		}
		});
		form.add(new Button(MONTHLY_REPORT) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {
				SalesSearch salesSearch = ((SalesSearch) getForm().getModelObject()).resetShowUnpaidInvoicesOnly();
				InvoiceSalesModel invoiceSalesModel = InvoiceSalesModel.make(salesSearch);
				setResponsePage(new InvoiceReportPage(MONTHLY_TEMPLATE, invoiceSalesModel));
    		}
		});
		form.add(new Button(PAYMENT_REPORT) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {
				SalesSearch salesSearch = ((SalesSearch) getForm().getModelObject()).resetShowUnpaidInvoicesOnly();
				PaymentModel paymentModel = PaymentModel.make(salesSearch);
				setResponsePage(new PaymentReportPage(paymentModel));
    		}
		}.setVisible(hasRole(Role.ADMIN)));
		form.add(new Button(SUPPLIER_REPORT) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {
				SalesSearch salesSearch = ((SalesSearch) getForm().getModelObject()).resetShowUnpaidInvoicesOnly();
				SupplierSalesModel supplierSalesModel = SupplierSalesModel.make(salesSearch);
				setResponsePage(new SupplierReportPage(supplierSalesModel));
    		}
		});
		form.add(new Button(PRODUCT_REPORT) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {
				SalesSearch salesSearch = ((SalesSearch) getForm().getModelObject()).resetShowUnpaidInvoicesOnly();
				ProductSalesModel productSalesModel = ProductSalesModel.make(salesSearch);
				setResponsePage(new ProductReportPage(productSalesModel));
    		}
		});
		form.add(new Button(DUEINVOICES_REPORT) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {
				SalesSearch salesSearch = ((SalesSearch) getForm().getModelObject()).setShowUnpaidInvoicesOnly();
				InvoiceSalesModel invoiceSalesModel = InvoiceSalesModel.make(salesSearch);
				setResponsePage(new InvoiceReportPage(DUEINVOICES_TEMPLATE, invoiceSalesModel));
    		}
		});
	}

	@Override
	public String getPageTitleKey() {
		return PAGE_TITLE;
	}
}
