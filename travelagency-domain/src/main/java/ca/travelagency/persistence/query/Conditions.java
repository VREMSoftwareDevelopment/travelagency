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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.travelagency.utils.StringUtils;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

class Conditions implements Serializable {

	private static final long serialVersionUID = 1L;

	static final String BRACE_START = "(";
	static final String BRACE_END = ") ";
	static final String WHERE = " WHERE ";
	static final String AND = " AND ";
	static final String OR = " OR ";
	static final String SEPARATOR = ", ";
	static final String IN = " IN";

	private List<Condition> andConditions = new ArrayList<Condition>();
	private List<Condition> orConditions = new ArrayList<Condition>();

	Conditions() {
	}

	List<Condition> getConditions() {
		return Lists.newArrayList(Iterables.concat(getAndConditions(), getOrConditions()));
	}

	Where where(String objectTag) {
		List<Object> parameters = new ArrayList<Object>();
		if (andConditions.isEmpty() && orConditions.isEmpty()) {
			return Where.make(StringUtils.EMPTY, parameters);
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(WHERE);

		int index = addAndConditions(objectTag, parameters, stringBuilder);

		if (orConditions.isEmpty()) {
			return Where.make(stringBuilder.toString(), parameters);
		}
		if (!andConditions.isEmpty()) {
			stringBuilder.append(AND);
			if (orConditions.size() > 1) {
				stringBuilder.append(BRACE_START);
			}
		}
		addOrConditions(objectTag, parameters, stringBuilder, index);
		if (!andConditions.isEmpty() && orConditions.size() > 1) {
			stringBuilder.append(BRACE_END);
		}
		return Where.make(stringBuilder.toString(), parameters);
	}

	private void addOrConditions(String objectTag, List<Object> parameters,	StringBuilder stringBuilder, int index) {
		int count = 0;
		for (Condition condition : orConditions) {
			if (count != 0) {
				stringBuilder.append(OR);
			}
			if (!condition.isInnerJoin()) {
				stringBuilder.append(objectTag);
				stringBuilder.append(Condition.SEPARATOR);
			}
			stringBuilder.append(condition.toStringAsSql(index++));
			parameters.add(condition.getValue());
			count++;
		}
	}

	private int addAndConditions(String objectTag, List<Object> parameters,	StringBuilder stringBuilder) {
		int index = 0;
		for (Condition condition : andConditions) {
			if (index != 0) {
				stringBuilder.append(AND);
			}
			if (!condition.isInnerJoin()) {
				stringBuilder.append(objectTag);
				stringBuilder.append(Condition.SEPARATOR);
			}
			stringBuilder.append(condition.toStringAsSql(index++));
			parameters.add(condition.getValue());
		}
		return index;
	}

	String from(final String objectTag) {
		Collection<Condition> filtered = Collections2.filter(getConditions(), new Filter());
		Collection<String> transformed = Collections2.transform(filtered, new Transform(objectTag));
		return resultsAsString(transformed);
	}

	private String resultsAsString(Collection<String> collection) {
		Set<String> results = new TreeSet<String>(collection);
		StringBuilder stringBuilder = new StringBuilder();
		for (String value : results) {
			stringBuilder.append(value);
		}
		return stringBuilder.toString();
	}

	private static class Filter implements Predicate<Condition> {
		@Override
		public boolean apply(Condition condition) {
			return condition.isInnerJoin();
		}
	}

	private static class Transform implements Function<Condition, String> {
		private String objectTag;
		Transform(String objectTag) {
			this.objectTag = objectTag;
		}
		@Override
		public String apply(Condition condition) {
			StringBuilder stringBuilder = new StringBuilder(SEPARATOR);
			stringBuilder.append(IN);
			stringBuilder.append(BRACE_START);
			stringBuilder.append(objectTag);
			stringBuilder.append(Condition.SEPARATOR);
			stringBuilder.append(condition.getCollectionProperty());
			stringBuilder.append(BRACE_END);
			stringBuilder.append(condition.getCollectionTag());
			return stringBuilder.toString();
		}
	};

	List<Condition> getAndConditions() {
		return Collections.unmodifiableList(andConditions);
	}
	Conditions addAndConditions(Collection<Condition> conditions) {
		if (conditions != null) {
			this.andConditions.addAll(conditions);
		}
		return this;
	}
	Conditions addAndCondition(Condition condition) {
		if (condition != null) {
			this.andConditions.add(condition);
		}
		return this;
	}

	List<Condition> getOrConditions() {
		return Collections.unmodifiableList(orConditions);
	}
	Conditions addOrConditions(Collection<Condition> conditions) {
		if (conditions != null) {
			this.orConditions.addAll(conditions);
		}
		return this;
	}
	Conditions addOrCondition(Condition condition) {
		if (condition != null) {
			this.orConditions.add(condition);
		}
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
