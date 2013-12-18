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

import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.text.WordUtils;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.jasypt.util.password.BasicPasswordEncryptor;

import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.utils.PhoneNumberUtils;
import ca.travelagency.utils.StringUtils;

@Entity
public class SystemUser implements DaoEntity, Comparable<SystemUser> {
	private static final long serialVersionUID = 1L;

	public static SystemUser make(String name, String password) {
		SystemUser result = new SystemUser();
		result.setName(name);
		result.setPassword(password);
		return result;
	}

	public enum Properties {
		password,
		firstName,
		lastName,
		email,
		primaryPhone,
		secondaryPhone,
		active,
		commissionRate,
		systemUserRoles
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private String primaryPhone;
	private String secondaryPhone;
	private Date created = new Date();
	private boolean active = true;
	private double commissionRate = 0.0;

	@OneToMany(cascade={CascadeType.ALL}, orphanRemoval=true, fetch=FetchType.EAGER)
	@JoinColumn(name="system_user_id", nullable=false)
	// have to use hibernate annotation
	@Sort(type = SortType.NATURAL)
	private SortedSet<SystemUserRole> systemUserRoles = new TreeSet<SystemUserRole>();

	// used only for testing
	@Deprecated
	public SystemUser() {
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
		return name;
	}
	@Override
	public void setName(String name) {
		Validate.notBlank(name);
		this.name = WordUtils.capitalize(StringUtils.lowerCase(name));
	}

	public void setPassword(String password) {
		this.password = new BasicPasswordEncryptor().encryptPassword(password);
	}

	public String getPassword() {
		return password;
	}

	public boolean doesPasswordMatch(String password) {
		return new BasicPasswordEncryptor().checkPassword(password, this.password);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = WordUtils.capitalize(StringUtils.lowerCase(firstName));
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = WordUtils.capitalize(StringUtils.lowerCase(lastName));
	}

	public double getCommissionRate() {
		return commissionRate;
	}

	public void setCommissionRate(double commissionRate) {
		Validate.isTrue(commissionRate >= 0.00 && commissionRate <= 999.99);
		this.commissionRate = commissionRate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPrimaryPhone() {
		return PhoneNumberUtils.format(primaryPhone);
	}
	String getPrimaryPhoneRaw() {
		return primaryPhone;
	}
	public void setPrimaryPhone(String primaryPhone) {
		this.primaryPhone = PhoneNumberUtils.strip(primaryPhone);
	}

	public String getSecondaryPhone() {
		return PhoneNumberUtils.format(secondaryPhone);
	}
	String getSecondaryPhoneRaw() {
		return secondaryPhone;
	}
	public void setSecondaryPhone(String secondaryPhone) {
		this.secondaryPhone = PhoneNumberUtils.strip(secondaryPhone);
	}

	public Date getCreated() {
		return created;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public SortedSet<SystemUserRole> getSystemUserRoles() {
		return systemUserRoles;
	}
	public void addSystemUserRole(SystemUserRole systemUserRole) {
		Validate.notNull(systemUserRole);
		getSystemUserRoles().add(systemUserRole);
	}
	public void removeSystemUserRole(SystemUserRole systemUserRole) {
		Validate.notNull(systemUserRole);
		getSystemUserRoles().remove(systemUserRole);
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
		if (!(object instanceof SystemUser)) {
			return false;
		}
		return new EqualsBuilder().append(getName(), ((SystemUser) object).getName()).isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public Class<?> getTrueClass() {
		return SystemUser.class;
	}

	@Override
	public int compareTo(SystemUser other) {
		return new CompareToBuilder()
			.append(getName(), other.getName())
			.toComparison();
	}
}
