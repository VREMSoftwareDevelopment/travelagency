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
import java.util.List;

import ca.travelagency.components.dataprovider.Filter;
import ca.travelagency.components.dataprovider.FilterHelper;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.persistence.query.OrderBy;
import ca.travelagency.utils.StringUtils;

public class CustomerFilter implements Filter {
	private static final long serialVersionUID = 1L;

	static final OrderBy DEFAULT_ORDER_BY = OrderBy.make(Customer.Properties.lastName.name());

	public enum Properties {
		searchText,
		systemUser,
		status,
	}

	private String searchText;
	private SystemUser systemUser;
	private CustomerStatus status;

	public String getSearchText() {
		return searchText;
	}
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public SystemUser getSystemUser() {
		return systemUser;
	}
	public void setSystemUser(SystemUser systemUser) {
		this.systemUser = systemUser;
	}

	public CustomerStatus getStatus() {
		return status;
	}
	public void setStatus(CustomerStatus status) {
		this.status = status;
	}

	private List<Condition> getAndConditions() {
		List<Condition> results = new ArrayList<Condition>();
		SystemUser systemUser = getSystemUser();
		if (systemUser != null) {
			results.add(Condition.equals(Customer.Properties.systemUser.name(), systemUser));
		}
		CustomerStatus status = getStatus();
		if (status != null) {
			results.add(Condition.equals(Customer.Properties.status.name(), status));
		}
		return results;
	}

	private List<Condition> getOrConditions() {
		List<Condition> results = new ArrayList<Condition>();
		String value = getSearchText();
		if (StringUtils.isBlank(value)) {
			return results;
		}
		results.add(Condition.likeCapitalize(Customer.Properties.firstName.name(), value));
		results.add(Condition.likeCapitalize(Customer.Properties.lastName.name(), value));
		results.add(Condition.likeCapitalize(Customer.Properties.companyName.name(), value));
		results.add(Condition.likeCapitalize(Customer.Properties.address.name(), value));
		results.add(Condition.likeCapitalize(Customer.Properties.city.name(), value));
		results.add(Condition.likeCapitalize(Customer.Properties.province.name(), value));
		results.add(Condition.like(Customer.Properties.postalCode.name(), StringUtils.upperCase(value)));
		results.add(Condition.likeCapitalize(Customer.Properties.country.name(), value));
		results.add(Condition.like(Customer.Properties.email.name(), value));
		if (!StringUtils.isNumeric(value)) {
			return results;
		}
		results.add(Condition.likePhoneNumber(Customer.Properties.primaryPhone.name(), value));
		results.add(Condition.likePhoneNumber(Customer.Properties.secondaryPhone.name(), value));
		return results;
	}

	@Override
	public String getId() {
		return CustomerFilter.class.getName();
	}
	@Override
	public Criteria getCriteria(OrderBy orderBy) {
		return Criteria.make(Customer.class)
			.addAndConditions(getAndConditions())
			.addOrConditions(getOrConditions())
			.addOrderBy(FilterHelper.getOrderBy(orderBy, DEFAULT_ORDER_BY));
	}
}
