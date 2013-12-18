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
import org.apache.commons.lang3.text.WordUtils;

import ca.travelagency.utils.PhoneNumberUtils;
import ca.travelagency.utils.StringUtils;

/**
 * NOTES:
 *  - Single Inner Join currently is supports per condition
 *  - One property before last is considered as collection property
 */
public class Condition implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final String SEPARATOR = ".";
	public static final String WILD_CHAR2 = "*";
	public static final String WILD_CHAR1 = "%";

	public enum Operator {
		LIKE(" LIKE "),
		EQUAL(" = "),
		NOTEQUAL(" != "),
		LESS(" < "),
		LESSORTEQUAL(" <= "),
		GREATER(" > "),
		GREATEROREQUAL(" >= ");

		private String asString;
		private Operator(String asString) {
			this.asString = asString;
		}
		public String getAsString() {
			return asString;
		}
	};

	private String property;
	private Object value;
	private Operator operator;
	private boolean innerJoin;

	public static Condition equals(String property, Object value) {
		return make(property, Operator.EQUAL, value);
	}
	public static Condition like(String property, String value) {
		return make(property, Operator.LIKE, value);
	}
	public static Condition likeCapitalize(String property, String value) {
		String lowerCase = StringUtils.lowerCase(value);
		if (lowerCase.startsWith(WILD_CHAR2) || lowerCase.startsWith(WILD_CHAR1)) {
			return like(property, lowerCase.substring(0, 1) + WordUtils.capitalize(lowerCase.substring(1)));
		}
		return like(property, WordUtils.capitalize(lowerCase));
	}
	public static Condition equalsPhoneNumber(String property, String phoneNumber) {
		return equals(property, PhoneNumberUtils.strip(phoneNumber));
	}
	public static Condition likePhoneNumber(String property, String phoneNumber) {
		return like(property, PhoneNumberUtils.strip(phoneNumber));
	}
	public static Condition make(String property, Operator operator, Object value) {
		Validate.notBlank(property);
		Validate.notNull(operator);
		Validate.notNull(value);
		Condition result = new Condition();
		result.property = property;
		result.operator = operator;
		result.value = value;
		return result;
	}

	private Condition() {
	}

	public boolean isInnerJoin() {
		return innerJoin;
	}
	public Condition setInnerJoin() {
		this.innerJoin = true;
		return this;
	}
	public Condition resetInnerJoin() {
		this.innerJoin = false;
		return this;
	}

	public String getCollectionProperty() {
		if (!isInnerJoin()) {
			return property;
		}
		String[] split = StringUtils.split(property, SEPARATOR);
		if (split.length < 2) {
			return property;
		}
		return StringUtils.join(split, SEPARATOR, 0, split.length - 1);
	}

	public String getCollectionTag() {
		if (!isInnerJoin()) {
			return property;
		}
		String[] split = StringUtils.split(property, SEPARATOR);
		if (split.length < 2) {
			return property;
		}
		return StringUtils.join(split, SEPARATOR, split.length - 2, split.length-1);
	}

	public String getProperty() {
		if (!isInnerJoin()) {
			return property;
		}
		String[] split = StringUtils.split(property, SEPARATOR);
		if (split.length < 3) {
			return property;
		}
		return StringUtils.join(split, SEPARATOR, split.length - 2, split.length);
	}

	public Operator getOperator() {
		return operator;
	}

	public Object getValue() {
		if (!(value instanceof String) || !Operator.LIKE.equals(getOperator())) {
			return value;
		}
		String result = ((String) value).replace('*', '%');
		if (!result.endsWith(WILD_CHAR1)) {
			result += WILD_CHAR1;
		}
		return result;
	}

	String toStringAsSql(int index) {
		Validate.isTrue(index >= 0);
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getProperty());
		stringBuilder.append(getOperator().getAsString());
		stringBuilder.append("?"+index);
		return stringBuilder.toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getProperty())
			.append(getOperator())
			.append(getValue())
			.append(isInnerJoin())
			.toHashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Condition)) {
			return false;
		}
		Condition other = (Condition) object;
		return new EqualsBuilder()
			.append(getProperty(), other.getProperty())
			.append(getOperator(), other.getOperator())
			.append(getValue(), other.getValue())
			.append(isInnerJoin(), other.isInnerJoin())
			.isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
