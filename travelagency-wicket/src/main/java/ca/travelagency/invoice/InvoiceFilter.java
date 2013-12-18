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
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.Validate;

import ca.travelagency.components.dataprovider.Filter;
import ca.travelagency.components.dataprovider.FilterHelper;
import ca.travelagency.components.formheader.DaoEntityModel;
import ca.travelagency.customer.Customer;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Condition.Operator;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.persistence.query.OrderBy;
import ca.travelagency.utils.DateUtils;
import ca.travelagency.utils.MoneyUtils;
import ca.travelagency.utils.StringUtils;

public class InvoiceFilter implements Filter {
	private static final long serialVersionUID = 1L;

	static final String INVOICE_CUSTOMER = Invoice.Properties.customer.name()+Condition.SEPARATOR;
	static final String INVOICE_TRAVELER = Invoice.Properties.invoiceTravelers.name()+Condition.SEPARATOR;
	static final String INVOICE_ITEM = Invoice.Properties.invoiceItems.name()+Condition.SEPARATOR;

	public enum Properties {
		invoiceDateFrom,
		invoiceDateTo,
		searchText,
		showUnpaidInvoicesOnly,
		status,
		systemUser,
	}

	private DaoEntityModel<Customer> customerModel;
	private DaoEntityModel<SystemUser> systemUserModel;
	private Date invoiceDateFrom;
	private Date invoiceDateTo;
	private InvoiceStatus status;
	private String searchText;
	private boolean showUnpaidInvoicesOnly;
	private OrderBy defaultOrderBy = OrderBy.make(Invoice.Properties.date.name(), false);

	public InvoiceFilter() {
		super();
	}

	private DaoEntity load(DaoEntityModel<? extends DaoEntity> model) {
		return isValidModel(model) ? model.getObject() : null;
	}
	private boolean isValidModel(DaoEntityModel<? extends DaoEntity> model) {
		return model != null && model.isPersistedDaoEntity();
	}
	private boolean isValidObject(DaoEntity daoEntity) {
		return daoEntity != null && daoEntity.getId() != null;
	}

	public SystemUser getSystemUser() {
		return (SystemUser) load(systemUserModel);
	}
	public InvoiceFilter setSystemUser(SystemUser systemUser) {
		if (isValidObject(systemUser)) {
			this.systemUserModel = DaoEntityModel.make(systemUser, SystemUser.class);
		} else {
			this.systemUserModel = null;
		}
		return this;
	}

	public Customer getCustomer() {
		return (Customer) load(customerModel);
	}
	public InvoiceFilter setCustomer(Customer customer) {
		if (isValidObject(customer)) {
			this.customerModel = DaoEntityModel.make(customer, Customer.class);
		} else {
			this.customerModel = null;
		}
		return this;
	}

	public OrderBy getDefaultOrderBy() {
		return defaultOrderBy;
	}
	public InvoiceFilter setDefaultOrderBy(OrderBy defaultOrderBy) {
		Validate.notNull(defaultOrderBy);
		this.defaultOrderBy = defaultOrderBy;
		return this;
	}

	public String getSearchText() {
		return searchText;
	}
	public InvoiceFilter setSearchText(String searchText) {
		this.searchText = searchText;
		return this;
	}

	public Date getInvoiceDateFrom() {
		return invoiceDateFrom;
	}
	public InvoiceFilter setInvoiceDateFrom(Date invoiceDateFrom) {
		this.invoiceDateFrom = invoiceDateFrom;
		return this;
	}

	public Date getInvoiceDateTo() {
		return invoiceDateTo;
	}
	public InvoiceFilter setInvoiceDateTo(Date invoiceDateTo) {
		this.invoiceDateTo = invoiceDateTo;
		return this;
	}

	public boolean isShowUnpaidInvoicesOnly() {
		return showUnpaidInvoicesOnly;
	}
	public InvoiceFilter setShowUnpaidInvoicesOnly(boolean showUnpaidInvoicesOnly) {
		this.showUnpaidInvoicesOnly = showUnpaidInvoicesOnly;
		return this;
	}

	public boolean isSystemUser() {
		return getSystemUser() != null;
	}
	public boolean isCustomer() {
		return getCustomer() != null;
	}

	public InvoiceStatus getStatus() {
		return status;
	}
	public InvoiceFilter setStatus(InvoiceStatus status) {
		this.status = status;
		return this;
	}

	private List<Condition> getAndConditions() {
		List<Condition> results = new ArrayList<Condition>();

		if (isSystemUser()) {
			results.add(Condition.equals(Invoice.Properties.systemUser.name(), getSystemUser()));
		}
		if (isCustomer()) {
			results.add(Condition.equals(Invoice.Properties.customer.name(), getCustomer()));
		}
		if (getInvoiceDateFrom() != null) {
			results.add(Condition.make(Invoice.Properties.date.name(), Operator.GREATEROREQUAL, getInvoiceDateFrom()));
		}
		if (getInvoiceDateTo() != null) {
			results.add(Condition.make(Invoice.Properties.date.name(), Operator.LESS, DateUtils.addDays(getInvoiceDateTo(), 1)));
		}
		if (isShowUnpaidInvoicesOnly()) {
			results.add(Condition.make(Invoice.Properties.totalDue.name(), Operator.GREATER, MoneyUtils.ZERO_VALUE));
		}
		InvoiceStatus status = getStatus();
		if (status != null) {
			results.add(Condition.equals(Invoice.Properties.status.name(), status));
		}

		return results;
	}

	private List<Condition> getOrConditions() {
		List<Condition> results = new ArrayList<Condition>();
		String value = getSearchText();
		if (StringUtils.isBlank(value)) {
			return results;
		}
		boolean isNumeric = StringUtils.isNumeric(value);
		if (isNumeric) {
			results.add(Condition.make(Invoice.PROPERTY_ID, Operator.EQUAL, new Long(value)));
		}

		results.add(Condition.likeCapitalize(INVOICE_TRAVELER+InvoiceTraveler.Properties.firstName.name(), value).setInnerJoin());
		results.add(Condition.likeCapitalize(INVOICE_TRAVELER+InvoiceTraveler.Properties.lastName.name(), value).setInnerJoin());
		results.add(Condition.likeCapitalize(INVOICE_TRAVELER+InvoiceTraveler.Properties.documentNumber.name(), StringUtils.upperCase(value)).setInnerJoin());

		results.add(Condition.likeCapitalize(INVOICE_ITEM+InvoiceItem.Properties.description.name(), value).setInnerJoin());
		results.add(Condition.likeCapitalize(INVOICE_ITEM+InvoiceItem.Properties.supplier.name(), value).setInnerJoin());

		if (isCustomer()) {
			return results;
		}
		results.add(Condition.likeCapitalize(INVOICE_CUSTOMER+Customer.Properties.firstName.name(), value));
		results.add(Condition.likeCapitalize(INVOICE_CUSTOMER+Customer.Properties.lastName.name(), value));
		results.add(Condition.likeCapitalize(INVOICE_CUSTOMER+Customer.Properties.companyName.name(), value));
		results.add(Condition.likeCapitalize(INVOICE_CUSTOMER+Customer.Properties.address.name(), value));
		results.add(Condition.likeCapitalize(INVOICE_CUSTOMER+Customer.Properties.city.name(), value));
		results.add(Condition.likeCapitalize(INVOICE_CUSTOMER+Customer.Properties.province.name(), value));
		results.add(Condition.like(INVOICE_CUSTOMER+Customer.Properties.postalCode.name(), StringUtils.upperCase(value)));
		results.add(Condition.likeCapitalize(INVOICE_CUSTOMER+Customer.Properties.country.name(), value));
		results.add(Condition.like(INVOICE_CUSTOMER+Customer.Properties.email.name(), value));
		if (!isNumeric) {
			return results;
		}
		results.add(Condition.likePhoneNumber(INVOICE_CUSTOMER+Customer.Properties.primaryPhone.name(), value));
		results.add(Condition.likePhoneNumber(INVOICE_CUSTOMER+Customer.Properties.secondaryPhone.name(), value));
		return results;
	}

	@Override
	public String getId() {
		return InvoiceFilter.class.getSimpleName();
	}

	@Override
	public Criteria getCriteria(OrderBy orderBy) {
		return Criteria.make(Invoice.class)
			.addAndConditions(getAndConditions())
			.addOrConditions(getOrConditions())
			.addOrderBy(FilterHelper.getOrderBy(orderBy, getDefaultOrderBy()));
	}
}
