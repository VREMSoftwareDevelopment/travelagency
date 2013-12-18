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
package ca.travelagency.salesstats;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.travelagency.invoice.InvoicePayment;
import ca.travelagency.invoice.PaymentType;
import ca.travelagency.utils.MoneyUtils;

public class InvoicePaymentDistribution implements Serializable, Comparable<InvoicePaymentDistribution> {
	private static final long serialVersionUID = 1L;

	public static InvoicePaymentDistribution make(PaymentType paymentType) {
		Validate.notNull(paymentType);
		InvoicePaymentDistribution result = new InvoicePaymentDistribution();
		result.paymentType = paymentType;
		return result;
	}

	private PaymentType paymentType;
	private SortedSet<InvoicePayment> invoicePayments = new TreeSet<InvoicePayment>();

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public long getCount() {
		return getInvoicePayments().size();
	}

	public SortedSet<InvoicePayment> getInvoicePayments() {
		return invoicePayments;
	}
	public void add(InvoicePayment invoicePayment) {
		Validate.notNull(invoicePayment);
		this.invoicePayments.add(invoicePayment);
	}

	public BigDecimal getAmount() {
		BigDecimal amount = MoneyUtils.ZERO_VALUE;
		for (InvoicePayment invoicePayment : getInvoicePayments()) {
			amount = amount.add(invoicePayment.getAmount());
		}
		return MoneyUtils.round(amount);
	}
	public String getAmountAsString() {
		return MoneyUtils.format(getAmount());
	}

	public BigDecimal getReconciledAmount() {
		BigDecimal amount = MoneyUtils.ZERO_VALUE;
		for (InvoicePayment invoicePayment : getInvoicePayments()) {
			if (invoicePayment.isReconciled()) {
				amount = amount.add(invoicePayment.getAmount());
			}
		}
		return MoneyUtils.round(amount);
	}
	public String getReconciledAmountAsString() {
		return MoneyUtils.format(getReconciledAmount());
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getPaymentType())
			.toHashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof InvoicePaymentDistribution)) {
			return false;
		}
		InvoicePaymentDistribution invoicePaymentDistribution = (InvoicePaymentDistribution) object;
		return new EqualsBuilder()
			.append(getPaymentType(), invoicePaymentDistribution.getPaymentType())
			.isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int compareTo(InvoicePaymentDistribution other) {
		return new CompareToBuilder()
			.append(getPaymentType(), other.getPaymentType())
			.toComparison();
	}
}
