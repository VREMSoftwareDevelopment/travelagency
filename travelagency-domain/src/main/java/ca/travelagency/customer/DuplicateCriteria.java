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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.travelagency.persistence.DaoCriteria;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Condition.Operator;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.utils.StringUtils;

public class DuplicateCriteria implements DaoCriteria {
	private static final long serialVersionUID = 1L;

	private Customer customer;

	public static DuplicateCriteria make(Customer customer) {
		Validate.notNull(customer);
		DuplicateCriteria result = new DuplicateCriteria();
		result.customer = customer;
		return result;
	}

	public Customer getCustomer() {
		return customer;
	}

	private DuplicateCriteria() {};

	List<Condition> makeConditions() {
		List<Condition> results = new ArrayList<Condition>();
		String lastName = customer.getLastName();
		if (StringUtils.isNotEmpty(lastName)) {
			results.add(Condition.equals(Customer.Properties.lastName.name(), lastName));
		}
		String firstName = customer.getFirstName();
		if (StringUtils.isNotEmpty(firstName)) {
			results.add(Condition.equals(Customer.Properties.firstName.name(), firstName));
		}
		String primaryPhone = customer.getPrimaryPhone();
		if (StringUtils.isNotEmpty(primaryPhone)) {
			results.add(Condition.equalsPhoneNumber(Customer.Properties.primaryPhone.name(), primaryPhone));
		}
		Date dateOfBirth = customer.getDateOfBirth();
		if (dateOfBirth != null) {
			results.add(Condition.equals(Customer.Properties.dateOfBirth.name(), dateOfBirth));
		}
		Serializable id = customer.getId();
		if (id != null) {
			results.add(Condition.make(DaoEntity.PROPERTY_ID, Operator.NOTEQUAL, id));
		}
		return results;
	}

	@Override
	public Criteria getCriteria() {
		return Criteria.make(Customer.class).addAndConditions(makeConditions());
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getCustomer()).toHashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof DuplicateCriteria)) {
			return false;
		}
		return new EqualsBuilder().append(getCustomer(), ((DuplicateCriteria) object).getCustomer()).isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
