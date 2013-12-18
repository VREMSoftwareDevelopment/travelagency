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
import ca.travelagency.identity.SystemUser;
import ca.travelagency.invoice.CommissionStatus;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceItem;
import ca.travelagency.invoice.InvoiceTraveler;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Condition.Operator;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.persistence.query.OrderBy;
import ca.travelagency.utils.DateUtils;
import ca.travelagency.utils.StringUtils;

public class InvoiceItemFilter implements Filter {
	private static final long serialVersionUID = 1L;

	static final String INVOICE_PATH = InvoiceItem.Properties.invoice.name()+Condition.SEPARATOR;
	static final String INVOICE_TRAVELER_PATH = INVOICE_PATH+Invoice.Properties.invoiceTravelers.name()+Condition.SEPARATOR;

	static final OrderBy DEFAULT_ORDER_BY = OrderBy.make(InvoiceItem.Properties.supplier.name());

	public enum Properties {
		commissionStatus,
		invoiceDateFrom,
		invoiceDateTo,
		searchText,
		systemUser,
	}

	private CommissionStatus commissionStatus;
	private DaoEntityModel<SystemUser> systemUserModel;
	private Date invoiceDateFrom;
	private Date invoiceDateTo;
	private String searchText;

	public InvoiceItemFilter() {
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

	public Date getInvoiceDateFrom() {
		return invoiceDateFrom;
	}
	public void setInvoiceDateFrom(Date invoiceDateFrom) {
		this.invoiceDateFrom = invoiceDateFrom;
	}

	public Date getInvoiceDateTo() {
		return invoiceDateTo;
	}
	public void setInvoiceDateTo(Date invoiceDateTo) {
		this.invoiceDateTo = invoiceDateTo;
	}

	public CommissionStatus getCommissionStatus() {
		return commissionStatus;
	}
	public void setCommissionStatus(CommissionStatus commissionStatus) {
		this.commissionStatus = commissionStatus;
	}

	private List<Condition> getAndConditions() {
		List<Condition> results = new ArrayList<Condition>();

		if (getCommissionStatus() != null) {
			results.add(Condition.equals(InvoiceItem.Properties.commissionStatus.name(), getCommissionStatus()));
		}
		if (isSystemUser()) {
			results.add(Condition.equals(INVOICE_PATH+Invoice.Properties.systemUser.name(), getSystemUser()));
		}
		if (getInvoiceDateFrom() != null) {
			results.add(Condition.make(INVOICE_PATH+Invoice.Properties.date, Operator.GREATEROREQUAL, getInvoiceDateFrom()));
		}
		if (getInvoiceDateTo() != null) {
			results.add(Condition.make(INVOICE_PATH+Invoice.Properties.date, Operator.LESS, DateUtils.addDays(getInvoiceDateTo(), 1)));
		}
		return results;
	}

	private List<Condition> getOrConditions() {
		List<Condition> results = new ArrayList<Condition>();
		String value = getSearchText();
		if (StringUtils.isBlank(value)) {
			return results;
		}
		results.add(Condition.likeCapitalize(InvoiceItem.Properties.supplier.name(), value));
		results.add(Condition.likeCapitalize(InvoiceItem.Properties.description.name(), value));

		results.add(Condition.likeCapitalize(INVOICE_TRAVELER_PATH+InvoiceTraveler.Properties.firstName, value).setInnerJoin());
		results.add(Condition.likeCapitalize(INVOICE_TRAVELER_PATH+InvoiceTraveler.Properties.lastName.name(), value).setInnerJoin());
		results.add(Condition.likeCapitalize(INVOICE_TRAVELER_PATH+InvoiceTraveler.Properties.documentNumber.name(), StringUtils.upperCase(value)).setInnerJoin());

		return results;
	}

	@Override
	public String getId() {
		return InvoiceItemFilter.class.getSimpleName();
	}

	@Override
	public Criteria getCriteria(OrderBy orderBy) {
		return Criteria.make(InvoiceItem.class)
			.addAndConditions(getAndConditions())
			.addOrConditions(getOrConditions())
			.addOrderBy(FilterHelper.getOrderBy(orderBy, DEFAULT_ORDER_BY));
	}
}
