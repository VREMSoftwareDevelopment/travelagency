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

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.text.WordUtils;

import ca.travelagency.identity.SystemUser;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.utils.PhoneNumberUtils;
import ca.travelagency.utils.StringUtils;

@Entity
public class Customer implements DaoEntity {
	private static final long serialVersionUID = 1L;

	public static Customer makeEmpty() {
		return new Customer();
	}

	public static Customer make(String firstName, String lastName, String primaryPhone) {
		Customer result = makeEmpty();
		result.setFirstName(firstName);
		result.setLastName(lastName);
		result.setPrimaryPhone(primaryPhone);
		return result;
	}

	public enum Properties {
		status,
		salutation,
		firstName,
		lastName,
		companyName,
		address,
		city,
		province,
		postalCode,
		country,
		email,
		primaryPhone,
		secondaryPhone,
		travelDocumentType,
		travelDocumentNumber,
		notes,
		systemUser,
		dateOfBirth,
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Enumerated(EnumType.STRING)
	private CustomerStatus status = CustomerStatus.Active;
	private String salutation;
	private String firstName;
	private String lastName;
	private String companyName;
	private String address;
	private String city;
	private String province;
	private String postalCode;
	private String country;
	private String email;
	private String primaryPhone;
	private String secondaryPhone;
	private String travelDocumentType;
	private String travelDocumentNumber;
	private String notes;
	@ManyToOne()
	@JoinColumn(name="system_user_id")
	private SystemUser systemUser;
	private Date created = new Date();
	private Date dateOfBirth;

	// used only for testing
	@Deprecated
	public Customer() {
	}

	@Override
	public Long getId() {
		return id;
	}

	void setId(Long id) {
		Validate.notNull(id);
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		Validate.notBlank(firstName);
		this.firstName = WordUtils.capitalize(StringUtils.lowerCase(firstName));
	}

	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		Validate.notBlank(lastName);
		this.lastName = WordUtils.capitalize(StringUtils.lowerCase(lastName));
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = WordUtils.capitalize(StringUtils.lowerCase(address));
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = WordUtils.capitalize(StringUtils.lowerCase(city));
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = WordUtils.capitalize(StringUtils.lowerCase(province));
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = StringUtils.upperCase(postalCode);
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = WordUtils.capitalize(StringUtils.lowerCase(country));
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
		Validate.isTrue(PhoneNumberUtils.isValid(primaryPhone));
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

	public String getSalutation() {
		return salutation;
	}
	public void setSalutation(String salutation) {
		this.salutation = WordUtils.capitalize(StringUtils.lowerCase(salutation));
	}

	public CustomerStatus getStatus() {
		return status;
	}
	public void setStatus(CustomerStatus status) {
		Validate.notNull(status);
		this.status = status;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = WordUtils.capitalize(StringUtils.lowerCase(companyName));
	}

	public String getTravelDocumentType() {
		return travelDocumentType;
	}
	public void setTravelDocumentType(String travelDocumentType) {
		this.travelDocumentType = WordUtils.capitalize(StringUtils.lowerCase(travelDocumentType));
	}

	public String getTravelDocumentNumber() {
		return travelDocumentNumber;
	}
	public void setTravelDocumentNumber(String travelDocumentNumber) {
		this.travelDocumentNumber = StringUtils.upperCase(travelDocumentNumber);
	}

	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}

	public SystemUser getSystemUser() {
		return systemUser;
	}
	public void setSystemUser(SystemUser systemUser) {
		this.systemUser = systemUser;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Date getCreated() {
		return created;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Customer)) {
			return false;
		}
		return new EqualsBuilder().append(getId(), ((Customer) object).getId()).isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public Class<?> getTrueClass() {
		return Customer.class;
	}

	@Override
	public String getName() {
		return StringUtils.format(getSalutation(), getFirstName(), getLastName());
	}

	public boolean isActive() {
		return CustomerStatus.Active.equals(getStatus());
	}

	@Override
	public void setName(String name) {}
}
