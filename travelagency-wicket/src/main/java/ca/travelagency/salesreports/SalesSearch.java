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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.Validate;

import ca.travelagency.components.formheader.DaoEntityModel;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceItem;
import ca.travelagency.invoice.InvoicePayment;
import ca.travelagency.invoice.InvoiceStatus;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Condition.Operator;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.persistence.query.OrderBy;
import ca.travelagency.utils.DateUtils;
import ca.travelagency.utils.MoneyUtils;
import ca.travelagency.utils.StringUtils;

import com.google.common.collect.Lists;

public class SalesSearch implements Serializable {
	private static final long serialVersionUID = 1L;

	public static SalesSearch make() {
		return new SalesSearch();
	}
	public static SalesSearch make(SystemUser systemUser) {
		SalesSearch salesSearch = make();
		salesSearch.setSystemUser(systemUser);
		return salesSearch;
	}

	public enum Properties {
		dateFrom,
		dateTo,
		systemUser,
		showDetails,
		showUnpaidInvoicesOnly,
	}

	private DaoEntityModel<SystemUser> systemUserModel;
	private Date dateFrom;
	private Date dateTo;
	private boolean showDetails = true;
	private boolean showUnpaidInvoicesOnly;

	private SalesSearch() {
		setDateFrom(DateUtils.truncate(new Date(), Calendar.MONTH));
		setDateTo(DateUtils.addDays(DateUtils.addMonths(getDateFrom(), 1), -1));
	}

	public SystemUser getSystemUser() {
		return systemUserModel == null ? null : systemUserModel.getObject();
	}
	public SalesSearch setSystemUser(SystemUser systemUser) {
		this.systemUserModel = systemUser == null ? null : DaoEntityModel.make(systemUser, SystemUser.class);
		return this;
	}

	public Date getDateFrom() {
		return dateFrom;
	}
	public SalesSearch setDateFrom(Date dateFrom) {
		Validate.notNull(dateFrom);
		this.dateFrom = dateFrom;
		return this;
	}
	public SalesSearch setDateFromBy6Month() {
		return setDateFrom(DateUtils.addDays(DateUtils.addMonths(getDateTo(), -6), 1));
	}

	public Date getDateTo() {
		return dateTo;
	}
	public SalesSearch setDateTo(Date dateTo) {
		Validate.notNull(dateTo);
		this.dateTo = dateTo;
		return this;
	}

	public boolean isShowDetails() {
		return showDetails;
	}
	public SalesSearch setShowDetails(boolean showDetails) {
		this.showDetails = showDetails;
		return this;
	}

	public boolean isShowUnpaidInvoicesOnly() {
		return showUnpaidInvoicesOnly;
	}
	public SalesSearch setShowUnpaidInvoicesOnly() {
		this.showUnpaidInvoicesOnly = true;
		return this;
	}
	public SalesSearch resetShowUnpaidInvoicesOnly() {
		this.showUnpaidInvoicesOnly = false;
		return this;
	}

	List<OrderBy> getOrderBy() {
		return Lists.newArrayList(
			OrderBy.make(Invoice.Properties.date.name()),
			OrderBy.make(DaoEntity.PROPERTY_ID));
	}

	private List<Condition> getConditions(String prefix) {
		List<Condition> results = new ArrayList<Condition>();
		results.add(Condition.make(prefix+Invoice.Properties.status.name(), Operator.NOTEQUAL, InvoiceStatus.Voided));
		SystemUser systemUser = getSystemUser();
		if (systemUser != null) {
			results.add(Condition.equals(prefix+Invoice.Properties.systemUser.name(), getSystemUser()));
		}
		if (isShowUnpaidInvoicesOnly()) {
			results.add(Condition.make(prefix+Invoice.Properties.totalDue.name(), Operator.GREATER, MoneyUtils.ZERO_VALUE));
			return results;
		}
		return results;
	}

	List<Condition> getInvoiceConditions(String prefix) {
		List<Condition> results = getConditions(prefix);
		results.add(Condition.make(prefix+Invoice.Properties.date.name(), Operator.GREATEROREQUAL, getDateFrom()));
		results.add(Condition.make(prefix+Invoice.Properties.date.name(), Operator.LESS, DateUtils.addDays(getDateTo(), 1)));
		return results;
	}

	List<Condition> getPaymentConditions(String prefix) {
		List<Condition> results = getConditions(prefix);
		results.add(Condition.make(InvoicePayment.Properties.date.name(), Operator.GREATEROREQUAL, getDateFrom()));
		results.add(Condition.make(InvoicePayment.Properties.date.name(), Operator.LESS, DateUtils.addDays(getDateTo(), 1)));
		return results;
	}

	public Criteria getInvoiceCriteria() {
		return Criteria.make(Invoice.class)
			.addOrderBy(getOrderBy())
			.addAndConditions(getInvoiceConditions(StringUtils.EMPTY));
	}

	public Criteria getInvoiceItemCriteria() {
		String prefix = InvoiceItem.Properties.invoice.name()+Condition.SEPARATOR;
		return Criteria.make(InvoiceItem.class)
			.addAndConditions(getInvoiceConditions(prefix));
	}

	public Criteria getInvoicePaymentCriteria() {
		String prefix = InvoiceItem.Properties.invoice.name()+Condition.SEPARATOR;
		return Criteria.make(InvoicePayment.class)
			.addAndConditions(getPaymentConditions(prefix));
	}
}
