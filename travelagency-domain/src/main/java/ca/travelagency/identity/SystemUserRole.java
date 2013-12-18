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

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.travelagency.persistence.DaoEntity;

@Entity
public class SystemUserRole implements DaoEntity, Comparable<SystemUserRole>{
	private static final long serialVersionUID = 1L;

	public static SystemUserRole make(Role role) {
		SystemUserRole invoiceTraveler = new SystemUserRole();
		invoiceTraveler.setRole(role);
		return invoiceTraveler;
	}

	public enum Properties {
		role
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Enumerated(EnumType.STRING)
	private Role role = Role.AGENT;

	// used only for testing
	@Deprecated
	public SystemUserRole() {
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
	public String getName() {
		return getRole().name();
	}

	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		Validate.notNull(role);
		this.role = role;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getRole())
			.toHashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof SystemUserRole)) {
			return false;
		}
		SystemUserRole other = (SystemUserRole) object;
		return new EqualsBuilder()
			.append(getRole(), other.getRole())
			.isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public Class<?> getTrueClass() {
		return SystemUserRole.class;
	}

	@Override
	public int compareTo(SystemUserRole other) {
		return new CompareToBuilder()
			.append(getRole(), other.getRole())
			.toComparison();
	}

	@Override
	public void setName(String name) {}
}
