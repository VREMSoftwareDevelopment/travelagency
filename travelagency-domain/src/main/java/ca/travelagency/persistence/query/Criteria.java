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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.travelagency.persistence.DaoCriteria;
import ca.travelagency.utils.StringUtils;

public class Criteria implements DaoCriteria {
	private static final long serialVersionUID = 1L;

	static final String ORDER_BY = " ORDER BY ";
	static final String GROUP_BY = " GROUP BY ";
	static final String SEPARATOR = ", ";

	public static Criteria make(Class<?> clazz) {
		Criteria result = new Criteria();
		result.setClazz(clazz);
		return result;
	}

	private Class<?> clazz;
	private long count = 0;
	private long offset = 0;
	private String groupBy;
	private Conditions conditions = new Conditions();
	private List<OrderBy> orderBy = new ArrayList<OrderBy>();

	private Criteria() {
	}

	public Class<?> getClazz() {
		return clazz;
	}
	public void setClazz(Class<?> clazz) {
		Validate.notNull(clazz);
		this.clazz = clazz;
	}

	public List<OrderBy> getOrderBy() {
		return Collections.unmodifiableList(orderBy);
	}
	public String getOrderByAsSql(String objectTag) {
		int count = 0;
		StringBuilder stringBuilder = new StringBuilder();
		for (OrderBy orderBy : getOrderBy()) {
			if (count == 0) {
				stringBuilder.append(ORDER_BY);
			} else {
				stringBuilder.append(SEPARATOR);
			}
			stringBuilder.append(objectTag);
			stringBuilder.append(Condition.SEPARATOR);
			stringBuilder.append(orderBy.toStringAsSql());
			count++;
		}
		return stringBuilder.toString();
	}
	public Criteria addOrderBy(OrderBy orderBy) {
		if (orderBy != null) {
			this.orderBy.add(orderBy);
		}
		return this;
	}
	public Criteria addOrderBy(Collection<OrderBy> orderBy) {
		if (orderBy != null) {
			this.orderBy.addAll(orderBy);
		}
		return this;
	}

	public String getGroupBy() {
		return groupBy;
	}
	public String getGroupByAsSql(String objectTag) {
		if (StringUtils.isBlank(this.groupBy)) {
			return StringUtils.EMPTY;
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(GROUP_BY);
		stringBuilder.append(objectTag);
		stringBuilder.append(Condition.SEPARATOR);
		stringBuilder.append(this.groupBy);
		return stringBuilder.toString();
	}
	public Criteria setGroupBy(String groupBy) {
		this.groupBy = groupBy;
		return this;
	}

	public Where where(String objectTag) {
		return conditions.where(objectTag);
	}
	public String from(final String objectTag) {
		return conditions.from(objectTag);
	}

	public List<Condition> getConditions() {
		return conditions.getConditions();
	}

	public List<Condition> getAndConditions() {
		return conditions.getAndConditions();
	}
	public Criteria addAndConditions(Collection<Condition> conditions) {
		this.conditions.addAndConditions(conditions);
		return this;
	}
	public Criteria addAndCondition(Condition condition) {
		this.conditions.addAndCondition(condition);
		return this;
	}

	public List<Condition> getOrConditions() {
		return conditions.getOrConditions();
	}
	public Criteria addOrConditions(Collection<Condition> conditions) {
		this.conditions.addOrConditions(conditions);
		return this;
	}
	public Criteria addOrCondition(Condition condition) {
		this.conditions.addOrCondition(condition);
		return this;
	}

	public long getOffset() {
		return offset;
	}
	public Criteria setOffset(long offset) {
		this.offset = offset;
		return this;
	}

	public long getCount() {
		return count;
	}
	public Criteria setCount(long count) {
		this.count = count;
		return this;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getClazz().getName())
			.toHashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Criteria)) {
			return false;
		}
		Criteria other = (Criteria) object;
		return new EqualsBuilder()
			.append(getClazz().getName(), other.getClazz().getName())
			.isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public Criteria getCriteria() {
		return this;
	}
}
