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

import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.utils.StringUtils;

@Entity
public class InvoiceNote implements DaoEntity, Comparable<InvoiceNote> {
	private static final long serialVersionUID = 1L;

	public static InvoiceNote make(String text) {
		InvoiceNote invoiceNote = new InvoiceNote();
		invoiceNote.setText(text);
		return invoiceNote;
	}

	public enum Properties {
		text,
		date,
		privateNote,
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String text;
	private Date date = new Date();
	private boolean privateNote;

	// used only for testing
	@Deprecated
	public InvoiceNote() {
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		Validate.notNull(id);
		this.id = id;
	}

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		Validate.notNull(date);
		this.date = date;
	}

	@Override
	public String getName() {
		return StringUtils.EMPTY;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		Validate.notBlank(text);
		this.text = text;
	}

	public boolean isPrivateNote() {
		return privateNote;
	}
	public void setPrivateNote(boolean privateNote) {
		this.privateNote = privateNote;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getDate())
			.append(getText())
			.append(getId())
			.toHashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof InvoiceNote)) {
			return false;
		}
		InvoiceNote other = (InvoiceNote) object;
		return new EqualsBuilder()
			.append(getDate(), other.getDate())
			.append(getText(), other.getText())
			.append(getId(), other.getId())
			.isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public Class<?> getTrueClass() {
		return InvoiceNote.class;
	}

	@Override
	public int compareTo(InvoiceNote other) {
		return new CompareToBuilder()
			.append(getDate(), other.getDate())
			.append(getText(), other.getText())
			.append(getId(), other.getId())
			.toComparison();
	}

	@Override
	public void setName(String name) {}
}
