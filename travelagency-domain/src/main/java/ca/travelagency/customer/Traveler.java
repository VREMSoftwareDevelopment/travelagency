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
import javax.persistence.Id;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.text.WordUtils;

import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.utils.StringUtils;

@Entity
public class Traveler implements DaoEntity {
	private static final long serialVersionUID = 1L;

	public static Traveler makeEmpty() {
		return new Traveler();
	}

	public static Traveler make(String firstName, String lastName) {
		Traveler result = makeEmpty();
		result.setFirstName(firstName);
		result.setLastName(lastName);
		return result;
	}

	public enum Properties {
		salutation,
		firstName,
		lastName,
		documentType,
		documentNumber,
		dateOfBirth,
	}

	@Id
	private Long id;
	private String salutation;
	private String firstName;
	private String lastName;
	private String documentType;
	private String documentNumber;
	private Date dateOfBirth;

	Traveler() {
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

	public String getSalutation() {
		return salutation;
	}
	public void setSalutation(String salutation) {
		this.salutation = WordUtils.capitalize(StringUtils.lowerCase(salutation));
	}

	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = WordUtils.capitalize(StringUtils.lowerCase(documentType));
	}

	public String getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = StringUtils.upperCase(documentNumber);
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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
		if (!(object instanceof Traveler)) {
			return false;
		}
		return new EqualsBuilder().append(getId(), ((Traveler) object).getId()).isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public Class<?> getTrueClass() {
		return Traveler.class;
	}

	@Override
	public String getName() {
		return StringUtils.format(getSalutation(), getFirstName(), getLastName());
	}

	@Override
	public void setName(String name) {}

}
