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
package ca.travelagency.config;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.text.WordUtils;

import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.utils.StringUtils;

@Entity
public class Parameter implements DaoEntity, Comparable<Parameter> {
	private static final long serialVersionUID = 1L;

	public static Parameter make(String name, String value) {
		Parameter parameter = new Parameter();
		parameter.setName(name);
		parameter.setValue(value);
		return parameter;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String value;

	public Parameter() {
	}

	@Override
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		Validate.notNull(id);
		this.id = id;
	}

	@Override
	public void setName(String name) {
		Validate.notBlank(name);
		this.name = name;
	}
	@Override
	public String getName() {
		return this.name;
	}

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		Validate.notBlank(value);
		this.value = WordUtils.capitalize(StringUtils.lowerCase(value));
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
		if (!(object instanceof Parameter)) {
			return false;
		}
		return new EqualsBuilder().append(getName(), ((Parameter) object).getName()).isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public Class<?> getTrueClass() {
		return Parameter.class;
	}

	@Override
	public int compareTo(Parameter other) {
		return new CompareToBuilder().append(getName(), other.getName()).toComparison();
	}
}
