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
package ca.travelagency.invoice;

import java.util.Date;

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

import ca.travelagency.customer.Customer;
import ca.travelagency.customer.Traveler;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.utils.StringUtils;

@Entity
public class InvoiceTraveler implements DaoEntity, Comparable<InvoiceTraveler> {
	private static final long serialVersionUID = 1L;

	public static InvoiceTraveler makeEmpty() {
		return new InvoiceTraveler();
	}

	public static InvoiceTraveler make(Customer customer) {
		InvoiceTraveler invoiceTraveler = makeEmpty();
		invoiceTraveler.setSalutation(customer.getSalutation());
		invoiceTraveler.setFirstName(customer.getFirstName());
		invoiceTraveler.setLastName(customer.getLastName());
		invoiceTraveler.setDateOfBirth(customer.getDateOfBirth());
		invoiceTraveler.setDocumentType(customer.getTravelDocumentType());
		invoiceTraveler.setDocumentNumber(customer.getTravelDocumentNumber());
		return invoiceTraveler;
	}

	public enum Properties {
		salutation,
		firstName,
		lastName,
		documentType,
		documentNumber,
		dateOfBirth
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String salutation;
	private String firstName;
	private String lastName;
	private String documentType;
	private String documentNumber;
	private Date dateOfBirth;

	// used only for testing
	@Deprecated
	public InvoiceTraveler() {
	}

	@Override
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		Validate.notNull(id);
		this.id = id;
	}

	public String getSalutation() {
		return salutation;
	}
	public void setSalutation(String salutation) {
		this.salutation = WordUtils.capitalize(StringUtils.lowerCase(salutation));
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
		return new HashCodeBuilder()
			.append(getLastName())
			.append(getFirstName())
			.append(getId())
			.toHashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof InvoiceTraveler)) {
			return false;
		}
		InvoiceTraveler other = (InvoiceTraveler) object;
		return new EqualsBuilder()
			.append(getLastName(), other.getLastName())
			.append(getFirstName(), other.getFirstName())
			.append(getId(), other.getId())
			.isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public Class<?> getTrueClass() {
		return InvoiceTraveler.class;
	}

	@Override
	public int compareTo(InvoiceTraveler other) {
		return new CompareToBuilder()
			.append(getLastName(), other.getLastName())
			.append(getFirstName(), other.getFirstName())
			.append(getId(), other.getId())
			.toComparison();
	}

	@Override
	public String getName() {
		return StringUtils.format(getSalutation(), getFirstName(), getLastName());
	}

	@Override
	public void setName(String name) {}

	public void copy(Traveler traveler) {
		setLastName(traveler.getLastName());
		setFirstName(traveler.getFirstName());
		setSalutation(traveler.getSalutation());
		setDateOfBirth(traveler.getDateOfBirth());
		setDocumentNumber(traveler.getDocumentNumber());
		setDocumentType(traveler.getDocumentType());
	}
}
