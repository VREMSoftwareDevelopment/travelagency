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

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.travelagency.utils.MoneyUtils;

public class SalesAmounts implements Serializable {
	private static final long serialVersionUID = 1L;

	public static SalesAmounts make() {
		SalesAmounts salesAmounrs = new SalesAmounts();
		return salesAmounrs;
	}

	public enum Properties {
		price,
		priceAsString,
		tax,
		taxAsString,
		paid,
		paidAsString,
		commission,
		commissionAsString,
		taxOnCommission,
		taxOnCommissionAsString,

		// calculated
		verified,
		commissionReceived,
		commissionReceivedAsString,
		commissionVerified,
		commissionVerifiedAsString,
		sale,
		saleAsString,
		cost,
		costAsString,
		due,
		dueAsString,
	}

	private BigDecimal price = MoneyUtils.ZERO_VALUE;
	private BigDecimal tax = MoneyUtils.ZERO_VALUE;
	private BigDecimal paid = MoneyUtils.ZERO_VALUE;
	private BigDecimal commission = MoneyUtils.ZERO_VALUE;
	private BigDecimal taxOnCommission = MoneyUtils.ZERO_VALUE;
	private BigDecimal commissionReceived = MoneyUtils.ZERO_VALUE;
	private BigDecimal commissionVerified = MoneyUtils.ZERO_VALUE;
	private boolean verified = true;

	private SalesAmounts() {
	}

	public SalesAmounts add(SalesAmounts salesAmounts) {
		return addCommission(salesAmounts.getCommission())
			.addPaid(salesAmounts.getPaid())
			.addPrice(salesAmounts.getPrice())
			.addTax(salesAmounts.getTax())
			.addTaxOnCommission(salesAmounts.getTaxOnCommission())
			.addCommissionReceived(salesAmounts.getCommissionReceived())
			.addCommissionVerified(salesAmounts.getCommissionVerified())
			.setVerified(salesAmounts.isVerified());
	}

	public String getCommissionAsString() {
		return MoneyUtils.format(getCommission());
	}
	public BigDecimal getCommission() {
		return MoneyUtils.round(commission);
	}
	public SalesAmounts addCommission(BigDecimal commission) {
		Validate.notNull(commission);
		this.commission = this.commission.add(commission);
		return this;
	}

	public String getPaidAsString() {
		return MoneyUtils.format(getPaid());
	}
	public BigDecimal getPaid() {
		return MoneyUtils.round(paid);
	}
	public SalesAmounts addPaid(BigDecimal paid) {
		Validate.notNull(paid);
		this.paid = this.paid.add(paid);
		return this;
	}

	public String getPriceAsString() {
		return MoneyUtils.format(getPrice());
	}
	public BigDecimal getPrice() {
		return MoneyUtils.round(price);
	}
	public SalesAmounts addPrice(BigDecimal price) {
		Validate.notNull(price);
		this.price = this.price.add(price);
		return this;
	}

	public String getTaxAsString() {
		return MoneyUtils.format(getTax());
	}
	public BigDecimal getTax() {
		return MoneyUtils.round(tax);
	}
	public SalesAmounts addTax(BigDecimal tax) {
		Validate.notNull(tax);
		this.tax = this.tax.add(tax);
		return this;
	}

	public String getTaxOnCommissionAsString() {
		return MoneyUtils.format(getTaxOnCommission());
	}
	public BigDecimal getTaxOnCommission() {
		return MoneyUtils.round(taxOnCommission);
	}
	public SalesAmounts addTaxOnCommission(BigDecimal taxOnCommission) {
		Validate.notNull(taxOnCommission);
		this.taxOnCommission = this.taxOnCommission.add(taxOnCommission);
		return this;
	}

	public String getCommissionReceivedAsString() {
		return MoneyUtils.format(getCommissionReceived());
	}
	public BigDecimal getCommissionReceived() {
		return MoneyUtils.round(commissionReceived);
	}
	public SalesAmounts addCommissionReceived(BigDecimal commissionReceived) {
		Validate.notNull(commissionReceived);
		this.commissionReceived = this.commissionReceived.add(commissionReceived);
		return this;
	}

	public String getCommissionVerifiedAsString() {
		return MoneyUtils.format(getCommissionVerified());
	}
	public BigDecimal getCommissionVerified() {
		return MoneyUtils.round(commissionVerified);
	}
	public SalesAmounts addCommissionVerified(BigDecimal commissionVerified) {
		Validate.notNull(commissionVerified);
		this.commissionVerified = this.commissionVerified.add(commissionVerified);
		return this;
	}

	public boolean isVerified() {
		return verified;
	}
	public SalesAmounts setVerified(boolean verified) {
		this.verified = this.verified && verified;
		return this;
	}


	public String getSaleAsString() {
		return MoneyUtils.format(getSale());
	}
	public BigDecimal getSale() {
		return MoneyUtils.round(MoneyUtils.add(getPrice(), getTax()));
	}

	public String getCostAsString() {
		return MoneyUtils.format(getCost());
	}
	public BigDecimal getCost() {
		return MoneyUtils.round(getSale().subtract(getCommission()).subtract(getTaxOnCommission()));
	}

	public String getDueAsString() {
		return MoneyUtils.format(getDue());
	}
	public BigDecimal getDue() {
		return MoneyUtils.round(getSale().subtract(getPaid()));
	}
	public boolean isDueAmount() {
		return getDue().doubleValue() > 0;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getCommission())
			.append(getPaid())
			.append(getPrice())
			.append(getTax())
			.append(getTaxOnCommission())
			.append(getCommissionReceived())
			.append(getCommissionVerified())
			.append(isVerified())
			.toHashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof SalesAmounts)) {
			return false;
		}
		SalesAmounts other = (SalesAmounts) object;
		return new EqualsBuilder()
			.append(getCommission(), other.getCommission())
			.append(getPaid(), other.getPaid())
			.append(getPrice(), other.getPrice())
			.append(getTax(), other.getTax())
			.append(getTaxOnCommission(), other.getTaxOnCommission())
			.append(getCommissionReceived(), other.getCommissionReceived())
			.append(getCommissionVerified(), other.getCommissionVerified())
			.append(isVerified(), other.isVerified())
			.isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
