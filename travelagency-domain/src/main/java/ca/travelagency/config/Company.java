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
import javax.persistence.Id;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.text.WordUtils;

import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.utils.PhoneNumberUtils;
import ca.travelagency.utils.StringUtils;

@Entity
public class Company implements DaoEntity {
	private static final long serialVersionUID = 1L;

	public static final long COMPANY_ID = 1L;

	public enum Properties {
		address,
		city,
		province,
		postalCode,
		country,
		email,
		phoneNumber,
		faxNumber,
		taxNumber,
		ticoNumber
	}

	@Id
	private Long id;
	private String name;
	private String address;
	private String city;
	private String province;
	private String postalCode;
	private String country;
	private String email;
	private String phoneNumber;
	private String faxNumber;
	private String taxNumber;
	private String ticoNumber;

	public Company() {
		setId(COMPANY_ID);
	}

	@Override
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		Validate.notNull(id);
		this.id = id;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		Validate.notBlank(address);
		this.address = WordUtils.capitalize(StringUtils.lowerCase(address));
	}

	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		Validate.notBlank(city);
		this.city = WordUtils.capitalize(StringUtils.lowerCase(city));
	}

	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		Validate.notBlank(province);
		this.province = WordUtils.capitalize(StringUtils.lowerCase(province));
	}

	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		Validate.notBlank(postalCode);
		this.postalCode = StringUtils.upperCase(postalCode);
	}

	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		Validate.notBlank(country);
		this.country = WordUtils.capitalize(StringUtils.lowerCase(country));
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		Validate.notBlank(email);
		this.email = email;
	}

	public String getPhoneNumber() {
		return PhoneNumberUtils.format(phoneNumber);
	}
	String getPhoneNumberRaw() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		Validate.isTrue(PhoneNumberUtils.isValid(phoneNumber));
		this.phoneNumber = PhoneNumberUtils.strip(phoneNumber);
	}

	public String getFaxNumber() {
		return PhoneNumberUtils.format(faxNumber);
	}
	String getFaxNumberRaw() {
		return faxNumber;
	}
	public void setFaxNumber(String faxNumber) {
		Validate.isTrue(PhoneNumberUtils.isValid(faxNumber));
		this.faxNumber = PhoneNumberUtils.strip(faxNumber);
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

	public String getTaxNumber() {
		return taxNumber;
	}
	public void setTaxNumber(String taxNumber) {
		Validate.notBlank(taxNumber);
		this.taxNumber = StringUtils.upperCase(taxNumber);
	}

	public String getTicoNumber() {
		return ticoNumber;
	}
	public void setTicoNumber(String ticoNumber) {
		Validate.notBlank(ticoNumber);
		this.ticoNumber = StringUtils.upperCase(ticoNumber);
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
		if (!(object instanceof Company)) {
			return false;
		}
		return new EqualsBuilder().append(getId(), ((Company) object).getId()).isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public Class<?> getTrueClass() {
		return Company.class;
	}
}
