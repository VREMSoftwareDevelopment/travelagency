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

import java.math.BigDecimal;
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
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.utils.MoneyUtils;
import ca.travelagency.utils.StringUtils;

@Entity
public class InvoicePayment implements DaoEntity, Comparable<InvoicePayment> {
	private static final long serialVersionUID = 1L;

	public static InvoicePayment make(BigDecimal amount, Date date) {
		InvoicePayment invoicePayment = new InvoicePayment();
		invoicePayment.setAmount(amount);
		invoicePayment.setDate(date);
		return invoicePayment;
	}

	public enum Properties {
		invoice,
		amount,
		paymentType,
		date,
		reconciled,

		// read only
		amountAsString
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private BigDecimal amount;
	@Enumerated(EnumType.STRING)
	private PaymentType paymentType = PaymentType.CreditCard;
	private Date date = new Date();
	private boolean reconciled;

	@ManyToOne
	@JoinColumn(name="invoice_id", nullable=false)
	private Invoice invoice;

	// used only for testing
	@Deprecated
	public InvoicePayment() {
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
		return StringUtils.EMPTY;
	}

	public String getAmountAsString() {
		return MoneyUtils.format(getAmount());
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		Validate.notNull(amount);
		this.amount = amount;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		Validate.notNull(date);
		this.date = date;
	}

	public boolean isReconciled() {
		return reconciled;
	}
	public void setReconciled(boolean reconciled) {
		this.reconciled = reconciled;
	}

	public Invoice getInvoice() {
		return invoice;
	}
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getId())
			.toHashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof InvoicePayment)) {
			return false;
		}
		InvoicePayment other = (InvoicePayment) object;
		return new EqualsBuilder()
			.append(getId(), other.getId())
			.isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public Class<?> getTrueClass() {
		return InvoicePayment.class;
	}

	@Override
	public int compareTo(InvoicePayment other) {
		return new CompareToBuilder()
			.append(getDate(), other.getDate())
			.append(getId(), other.getId())
			.toComparison();
	}

	@Override
	public void setName(String name) {}
}
