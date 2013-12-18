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
package ca.travelagency.customer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import ca.travelagency.BasePage;
import ca.travelagency.components.behaviours.AjaxOnBlurBehaviour;
import ca.travelagency.components.dataprovider.DataProvider;
import ca.travelagency.components.decorators.FieldDecorator;
import ca.travelagency.components.fields.StringFieldHelper;
import ca.travelagency.components.fields.SystemUserField;
import ca.travelagency.identity.Role;
import ca.travelagency.invoice.InvoicePage;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.utils.DateUtils;

@AuthorizeInstantiation({"AGENT"})
public class CustomersPage extends BasePage {
	private static final long serialVersionUID = 1L;

	static final String CREATE = "create";
	static final String DATA_TABLE = "dataTable";
	static final String GO_TO_CUSTOMER = "goToCustomer";
	static final String GO_TO_CUSTOMER_LABEL = "goToCustomerLabel";
	static final String ADD_INVOICE = "addInvoice";
	static final String SEARCH_FORM = "searchForm";
	static final String SEARCH_BUTTON = "searchButton";
	static final String CLEAR_BUTTON = "clearButton";
	static final String FEEDBACK = "feedback";

	static final String PAGE_TITLE = "customers.title";

	public CustomersPage() {
		super();

		IModel<CustomerFilter> model = Model.of(getAuthenticatedSession().getCustomerFilter());

		add(makeSearchForm(model));

		add(new Link<String>(CREATE) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick() {
				setResponsePage(new CustomerPage());
			}
		});

    	add(new AjaxFallbackDefaultDataTable<Customer, String>(DATA_TABLE,
    			makeColumns(),
    			new DataProvider<Customer>(model),
    			DATA_TABLE_PER_PAGE));
	}

	private List<IColumn<Customer, String>> makeColumns() {
		List<IColumn<Customer, String>> columns = new ArrayList<IColumn<Customer, String>>();

		columns.add(new PropertyColumn<Customer, String>(new ResourceModel("customer.name"), Customer.Properties.lastName.name(), DaoEntity.PROPERTY_NAME) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<Customer>> item, String componentId, IModel<Customer> rowModel) {
				item.add(new GoToCustomer(componentId, rowModel));
			}
		});
		columns.add(new PropertyColumn<Customer, String>(new ResourceModel("customer.status"), Customer.Properties.status.name()));
		columns.add(new PropertyColumn<Customer, String>(new ResourceModel("customer.companyName"), Customer.Properties.companyName.name(), Customer.Properties.companyName.name()));
		columns.add(new PropertyColumn<Customer, String>(new ResourceModel("customer.dateOfBirth"), Customer.Properties.dateOfBirth.name()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<Customer>> item, String componentId, IModel<Customer> rowModel) {
				item.add(DateLabel.forDateStyle(componentId, Model.of(rowModel.getObject().getDateOfBirth()), DateUtils.DATE_STYLE));
			}
		});
		columns.add(new PropertyColumn<Customer, String>(new ResourceModel("customer.address"), Customer.Properties.address.name()));
		columns.add(new PropertyColumn<Customer, String>(new ResourceModel("customer.email"), Customer.Properties.email.name()));
		columns.add(new PropertyColumn<Customer, String>(new ResourceModel("customer.primaryPhone"), Customer.Properties.primaryPhone.name()));
		columns.add(new PropertyColumn<Customer, String>(new ResourceModel("customer.secondaryPhone"), Customer.Properties.secondaryPhone.name()));
		columns.add(new PropertyColumn<Customer, String>(new ResourceModel("addInvoice"), DaoEntity.PROPERTY_ID) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<Customer>> item, String componentId, IModel<Customer> rowModel) {
				item.add(new AddInvoice(componentId, rowModel).setVisible(rowModel.getObject().isActive()));
			}
		});

		return columns;
	}

	private static class GoToCustomer extends Panel {
		private static final long serialVersionUID = 1L;

		public GoToCustomer(String id, final IModel<Customer> model) {
	        super(id);
	        Link<Customer> link = new Link<Customer>(GO_TO_CUSTOMER) {
				private static final long serialVersionUID = 1L;
				@Override
				public void onClick() {
					setResponsePage(new CustomerPage(model.getObject()));
				}
	        };
	        link.add(new Label(GO_TO_CUSTOMER_LABEL, Model.of(model.getObject().getName())));
	        add(link);
	    }
	}

	private static class AddInvoice extends Panel {
		private static final long serialVersionUID = 1L;

		public AddInvoice(String id, final IModel<Customer> model) {
	        super(id);
	        Link<Customer> link = new Link<Customer>(ADD_INVOICE) {
				private static final long serialVersionUID = 1L;
				@Override
				public void onClick() {
					setResponsePage(new InvoicePage(model.getObject()));
				}
	        };
	        add(link);
	    }
	}

	private Form<CustomerFilter> makeSearchForm(IModel<CustomerFilter> model) {
		final Form<CustomerFilter> form = new Form<CustomerFilter>(SEARCH_FORM,
				new CompoundPropertyModel<CustomerFilter>(model));

		form.add(new ComponentFeedbackPanel(FEEDBACK, form));

		form.add(new TextField<String>(CustomerFilter.Properties.searchText.name())
			.setLabel(new ResourceModel("customer.searchText"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new DropDownChoice<CustomerStatus>(CustomerFilter.Properties.status.name(), Arrays.asList(CustomerStatus.values()))
			.setLabel(new ResourceModel("customer.status"))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new SystemUserField(CustomerFilter.Properties.systemUser.name())
			.setLabel(new ResourceModel("customer.agent"))
			.setVisible(hasRole(Role.ADMIN))
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));

		form.add(new Button(SEARCH_BUTTON) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {
				setResponsePage(new CustomersPage());
    		}
    	});

		form.add(new Button(CLEAR_BUTTON) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onSubmit() {
				getAuthenticatedSession().clearCustomerFilter();
				setResponsePage(new CustomersPage());
    		}
		}.setDefaultFormProcessing(false));

		return form;
	}

	@Override
	public String getPageTitleKey() {
		return PAGE_TITLE;
	}
}
