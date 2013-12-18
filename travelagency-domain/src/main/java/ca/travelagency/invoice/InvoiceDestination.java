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

import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.utils.DateUtils;
import ca.travelagency.utils.StringUtils;

import com.google.common.base.Joiner;

@Entity
public class InvoiceDestination implements DaoEntity, Comparable<InvoiceDestination> {
	private static final long serialVersionUID = 1L;

	private static final String SEPARATOR = " | ";

	public static InvoiceDestination make(String departurePlace, Date departureDate, String arrivalPlace) {
		InvoiceDestination invoiceDestination = new InvoiceDestination();
		invoiceDestination.setDeparturePlace(departurePlace);
		invoiceDestination.setDepartureDate(departureDate);
		invoiceDestination.setArrivalPlace(arrivalPlace);
		return invoiceDestination;
	}

	public enum Properties {
		departurePlace,
		departureDate,
		arrivalPlace,
		arrivalDate,
		// read-only
		departureDateAsString,
		arrivalDateAsString
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String departurePlace;
	private Date departureDate;
	private String arrivalPlace;
	private Date arrivalDate;


	// used only for testing
	@Deprecated
	public InvoiceDestination() {
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		Validate.notNull(id);
		this.id = id;
	}

	public String getDeparturePlace() {
		return departurePlace;
	}
	public void setDeparturePlace(String departurePlace) {
		Validate.notBlank(departurePlace);
		this.departurePlace = WordUtils.capitalize(StringUtils.lowerCase(departurePlace));
	}

	public String getDepartureDateAsString() {
		return DateUtils.formatDateCustom(getDepartureDate());
	}
	public Date getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(Date departureDate) {
		Validate.notNull(departureDate);
		this.departureDate = departureDate;
	}

	public String getArrivalPlace() {
		return arrivalPlace;
	}
	public void setArrivalPlace(String arrivalPlace) {
		Validate.notBlank(arrivalPlace);
		this.arrivalPlace = WordUtils.capitalize(StringUtils.lowerCase(arrivalPlace));
	}

	public String getArrivalDateAsString() {
		return DateUtils.formatDateCustom(getArrivalDate());
	}
	public Date getArrivalDate() {
		return arrivalDate;
	}
	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getDepartureDate())
			.append(getDeparturePlace())
			.append(getId())
			.toHashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof InvoiceDestination)) {
			return false;
		}
		InvoiceDestination other = (InvoiceDestination) object;
		return new EqualsBuilder()
			.append(getDepartureDate(), other.getDepartureDate())
			.append(getDeparturePlace(), other.getDeparturePlace())
			.append(getId(), other.getId())
			.isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public Class<?> getTrueClass() {
		return InvoiceDestination.class;
	}

	@Override
	public int compareTo(InvoiceDestination other) {
		return new CompareToBuilder()
			.append(getDepartureDate(), other.getDepartureDate())
			.append(getDeparturePlace(), other.getDeparturePlace())
			.append(getId(), other.getId())
			.toComparison();
	}

	@Override
	public String getName() {
		return Joiner.on(SEPARATOR).useForNull(StringUtils.EMPTY)
				.join(getDeparturePlace(), getDepartureDateAsString(), getArrivalPlace());
	}

	@Override
	public void setName(String name) {}
}
