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
package ca.travelagency.identity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.text.WordUtils;

import ca.travelagency.persistence.DaoCriteria;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Condition.Operator;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.utils.StringUtils;

public class NameCriteria implements DaoCriteria {
	private static final long serialVersionUID = 1L;

	public static NameCriteria make(String name) {
		NameCriteria result = new NameCriteria();
		result.setName(name);
		return result;
	}

	private String name;

	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = WordUtils.capitalize(StringUtils.lowerCase(name));
	}

	private NameCriteria() {}

	Condition makeCondition() {
		return Condition.make(DaoEntity.PROPERTY_NAME, Operator.EQUAL, name);
	}

	@Override
	public Criteria getCriteria() {
		return Criteria.make(SystemUser.class).addAndCondition(makeCondition());
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getName()).toHashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof NameCriteria)) {
			return false;
		}
		return new EqualsBuilder().append(getName(), ((NameCriteria) object).getName()).isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
