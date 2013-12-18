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
import java.util.Date;
import java.util.List;

import ca.travelagency.components.dataprovider.Filter;
import ca.travelagency.components.dataprovider.FilterHelper;
import ca.travelagency.components.formheader.DaoEntityModel;
import ca.travelagency.customer.Customer;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoicePayment;
import ca.travelagency.invoice.InvoiceTraveler;
import ca.travelagency.invoice.PaymentType;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Condition.Operator;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.persistence.query.OrderBy;
import ca.travelagency.utils.DateUtils;
import ca.travelagency.utils.StringUtils;

public class InvoicePaymentFilter implements Filter {
	private static final long serialVersionUID = 1L;

	static final String INVOICE_PATH = InvoicePayment.Properties.invoice.name()+Condition.SEPARATOR;
	static final String INVOICE_TRAVELER_PATH = INVOICE_PATH+Invoice.Properties.invoiceTravelers.name()+Condition.SEPARATOR;
	static final String INVOICE_CUSTOMER_PATH = INVOICE_PATH+Invoice.Properties.customer.name()+Condition.SEPARATOR;

	static final OrderBy DEFAULT_ORDER_BY = OrderBy.make(InvoicePayment.Properties.date.name());

	public enum Properties {
		paymentDateFrom,
		paymentDateTo,
		paymentType,
		reconciled,
		searchText,
		systemUser,
	}

	private DaoEntityModel<SystemUser> systemUserModel;
	private Date paymentDateFrom;
	private Date paymentDateTo;
	private PaymentType paymentType;
	private String searchText;
	private boolean reconciled;

	public InvoicePaymentFilter() {
	}

	public String getSearchText() {
		return searchText;
	}
	public void setSearchText(String searchText) {
		this.searchText = searchText;
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
	public void setSystemUser(SystemUser systemUser) {
		if (isValidObject(systemUser)) {
			this.systemUserModel = DaoEntityModel.make(systemUser, SystemUser.class);
		} else {
			this.systemUserModel = null;
		}
	}
	public boolean isSystemUser() {
		return getSystemUser() != null;
	}

	public Date getPaymentDateFrom() {
		return paymentDateFrom;
	}
	public void setPaymentDateFrom(Date paymentDateFrom) {
		this.paymentDateFrom = paymentDateFrom;
	}

	public Date getPaymentDateTo() {
		return paymentDateTo;
	}
	public void setPaymentDateTo(Date paymentDateTo) {
		this.paymentDateTo = paymentDateTo;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public boolean isReconciled() {
		return reconciled;
	}
	public void setReconciled(boolean reconciled) {
		this.reconciled = reconciled;
	}

	private List<Condition> getAndConditions() {
		List<Condition> results = new ArrayList<Condition>();

		results.add(Condition.equals(InvoicePayment.Properties.reconciled.name(), isReconciled()));

		if (getPaymentType() != null) {
			results.add(Condition.equals(InvoicePayment.Properties.paymentType.name(), getPaymentType()));
		}
		if (getPaymentDateFrom() != null) {
			results.add(Condition.make(InvoicePayment.Properties.date.name(), Operator.GREATEROREQUAL, getPaymentDateFrom()));
		}
		if (getPaymentDateTo() != null) {
			results.add(Condition.make(InvoicePayment.Properties.date.name(), Operator.LESS, DateUtils.addDays(getPaymentDateTo(), 1)));
		}
		if (isSystemUser()) {
			results.add(Condition.equals(INVOICE_PATH+Invoice.Properties.systemUser.name(), getSystemUser()));
		}
		return results;
	}

	private List<Condition> getOrConditions() {
		List<Condition> results = new ArrayList<Condition>();
		String value = getSearchText();
		if (StringUtils.isBlank(value)) {
			return results;
		}

		results.add(Condition.likeCapitalize(INVOICE_TRAVELER_PATH+InvoiceTraveler.Properties.firstName, value).setInnerJoin());
		results.add(Condition.likeCapitalize(INVOICE_TRAVELER_PATH+InvoiceTraveler.Properties.lastName.name(), value).setInnerJoin());
		results.add(Condition.likeCapitalize(INVOICE_TRAVELER_PATH+InvoiceTraveler.Properties.documentNumber.name(), StringUtils.upperCase(value)).setInnerJoin());

		boolean isNumeric = StringUtils.isNumeric(value);
		if (isNumeric) {
			results.add(Condition.make(INVOICE_PATH+Invoice.PROPERTY_ID, Operator.EQUAL, new Long(value)));
		}
		results.add(Condition.likeCapitalize(INVOICE_CUSTOMER_PATH+Customer.Properties.firstName.name(), value));
		results.add(Condition.likeCapitalize(INVOICE_CUSTOMER_PATH+Customer.Properties.lastName.name(), value));
		results.add(Condition.likeCapitalize(INVOICE_CUSTOMER_PATH+Customer.Properties.companyName.name(), value));
		results.add(Condition.likeCapitalize(INVOICE_CUSTOMER_PATH+Customer.Properties.address.name(), value));
		results.add(Condition.likeCapitalize(INVOICE_CUSTOMER_PATH+Customer.Properties.city.name(), value));
		results.add(Condition.likeCapitalize(INVOICE_CUSTOMER_PATH+Customer.Properties.province.name(), value));
		results.add(Condition.like(INVOICE_CUSTOMER_PATH+Customer.Properties.postalCode.name(), StringUtils.upperCase(value)));
		results.add(Condition.likeCapitalize(INVOICE_CUSTOMER_PATH+Customer.Properties.country.name(), value));
		results.add(Condition.like(INVOICE_CUSTOMER_PATH+Customer.Properties.email.name(), value));
		if (!isNumeric) {
			return results;
		}
		results.add(Condition.likePhoneNumber(INVOICE_CUSTOMER_PATH+Customer.Properties.primaryPhone.name(), value));
		results.add(Condition.likePhoneNumber(INVOICE_CUSTOMER_PATH+Customer.Properties.secondaryPhone.name(), value));
		return results;
	}

	@Override
	public String getId() {
		return InvoicePaymentFilter.class.getSimpleName();
	}

	@Override
	public Criteria getCriteria(OrderBy orderBy) {
		return Criteria.make(InvoicePayment.class)
			.addAndConditions(getAndConditions())
			.addOrConditions(getOrConditions())
			.addOrderBy(FilterHelper.getOrderBy(orderBy, DEFAULT_ORDER_BY));
	}
}
