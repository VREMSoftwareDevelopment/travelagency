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
package ca.travelagency.persistence.query;

import java.io.Serializable;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class OrderBy implements Serializable{
	private static final long serialVersionUID = 1L;

	private String property;
	private boolean ascending;

	public static OrderBy make(String property, boolean ascending) {
		Validate.notBlank(property);
		OrderBy result = new OrderBy();
		result.property = property;
		result.ascending = ascending;
		return result;
	}

	public static OrderBy make(String property) {
		return make(property, true);
	}

	private OrderBy() {
	}

	public String getProperty() {
		return property;
	}

	public boolean isAscending() {
		return ascending;
	}

	String toStringAsSql() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getProperty());
		stringBuilder.append(" ");
		stringBuilder.append(isAscending() ? "ASC" : "DESC");
		return stringBuilder.toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getProperty())
			.append(isAscending())
			.toHashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof OrderBy)) {
			return false;
		}
		OrderBy other = (OrderBy) object;
		return new EqualsBuilder()
			.append(getProperty(), other.getProperty())
			.append(isAscending(), other.isAscending())
			.isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
