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
import org.apache.commons.lang3.text.WordUtils;

import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.utils.MoneyUtils;
import ca.travelagency.utils.StringUtils;

@Entity
public class InvoiceItem implements DaoEntity, Comparable<InvoiceItem> {
	private static final long serialVersionUID = 1L;

	public static InvoiceItem make(String description, BigDecimal price) {
		InvoiceItem invoicePayment = new InvoiceItem();
		invoicePayment.setDescription(description);
		invoicePayment.setPrice(price);
		return invoicePayment;
	}

	public enum Properties {
		index,
		invoice,
		// item
		description,
		price,
		tax,
		qty,
		cancelBeforeDeparture,
		changeBeforeDeparture,
		changeAfterDeparture,
		commissionStatus,
		// item read-only properties
		sale,
		priceAsString,
		taxAsString,
		saleAsString,
		// supplier
		supplier,
		commission,
		taxOnCommission,
		bookingNumber,
		bookingDate,
		// supplier read-only properties
		commissionAsString,
		taxOnCommissionAsString,
		// calculated
		salesAmounts,
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Integer index;

	// item information
	private String description;
	private BigDecimal price = MoneyUtils.ZERO_VALUE;
	private BigDecimal tax = MoneyUtils.ZERO_VALUE;
	private Integer qty = 1;
	private String cancelBeforeDeparture;
	private String changeBeforeDeparture;
	private String changeAfterDeparture;

	// supplier information
	private String supplier;
	private BigDecimal commission = MoneyUtils.ZERO_VALUE;
	private BigDecimal taxOnCommission = MoneyUtils.ZERO_VALUE;
	private String bookingNumber;
	private Date bookingDate = new Date();
	@Enumerated(EnumType.STRING)
	private CommissionStatus commissionStatus = CommissionStatus.None;

	@ManyToOne
	@JoinColumn(name="invoice_id", nullable=false)
	private Invoice invoice;

	// used only for testing
	@Deprecated
	public InvoiceItem() {
	}

	@Override
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		Validate.notNull(id);
		this.id = id;
	}

	public Invoice getInvoice() {
		return invoice;
	}
	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}

	@Override
	public String getName() {
		return getDescription();
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		Validate.notNull(description);
		this.description = WordUtils.capitalize(StringUtils.lowerCase(description));
	}

	public CommissionStatus getCommissionStatus() {
		return commissionStatus;
	}
	public void setCommissionStatus(CommissionStatus commissionStatus) {
		Validate.notNull(commissionStatus);
		this.commissionStatus = commissionStatus;
	}

	public String getTaxAsString() {
		return MoneyUtils.format(getTax());
	}
	public BigDecimal getTax() {
		return tax == null ? MoneyUtils.ZERO_VALUE : tax;
	}
	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		Validate.notNull(qty);
		this.qty = qty;
	}

	public String getPriceAsString() {
		return MoneyUtils.format(getPrice());
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		Validate.notNull(price);
		this.price = price;
	}

	public BigDecimal getSale() {
		return MoneyUtils.round(MoneyUtils.add(getPrice(), getTax()));
	}
	public String getSaleAsString() {
		return MoneyUtils.format(getSale());
	}

	public String getCancelBeforeDeparture() {
		return cancelBeforeDeparture;
	}
	public void setCancelBeforeDeparture(String cancelBeforeDeparture) {
		this.cancelBeforeDeparture = WordUtils.capitalize(StringUtils.lowerCase(cancelBeforeDeparture));
	}

	public String getChangeBeforeDeparture() {
		return changeBeforeDeparture;
	}
	public void setChangeBeforeDeparture(String changeBeforeDeparture) {
		this.changeBeforeDeparture = WordUtils.capitalize(StringUtils.lowerCase(changeBeforeDeparture));
	}

	public String getChangeAfterDeparture() {
		return changeAfterDeparture;
	}
	public void setChangeAfterDeparture(String changeAfterDeparture) {
		this.changeAfterDeparture = WordUtils.capitalize(StringUtils.lowerCase(changeAfterDeparture));
	}

	// supplier
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = WordUtils.capitalize(StringUtils.lowerCase(supplier));
	}

	public String getCommissionAsString() {
		return MoneyUtils.format(getCommission());
	}
	public BigDecimal getCommission() {
		return commission == null ? MoneyUtils.ZERO_VALUE : commission;
	}
	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	public String getTaxOnCommissionAsString() {
		return MoneyUtils.format(getTaxOnCommission());
	}
	public BigDecimal getTaxOnCommission() {
		return taxOnCommission == null ? MoneyUtils.ZERO_VALUE : taxOnCommission;
	}
	public void setTaxOnCommission(BigDecimal taxOnCommission) {
		this.taxOnCommission = taxOnCommission;
	}

	public String getBookingNumber() {
		return bookingNumber;
	}
	public void setBookingNumber(String bookingNumber) {
		this.bookingNumber = bookingNumber;
	}

	public Date getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(Date bookingDate) {
		this.bookingDate = bookingDate;
	}

	public SalesAmounts getSalesAmounts() {
		BigDecimal commissionTotalAmount = MoneyUtils.multiply(getCommission(), getQty());
		SalesAmounts salesAmounts =	SalesAmounts.make()
			.addCommission(commissionTotalAmount)
			.addPrice(MoneyUtils.multiply(getPrice(), getQty()))
			.addTax(MoneyUtils.multiply(getTax(), getQty()))
			.addTaxOnCommission(MoneyUtils.multiply(getTaxOnCommission(), getQty()));
		if (isCommissionReceived()) {
			salesAmounts.addCommissionReceived(commissionTotalAmount);
		}
		if (isCommissionVerified()) {
			salesAmounts.addCommissionVerified(commissionTotalAmount);
		}
		salesAmounts.setVerified(isCommissionVerified());
		return salesAmounts;
	}

	public boolean isCommissionReceived() {
		return CommissionStatus.isReceived(getCommissionStatus());
	}
	public boolean isCommissionVerified() {
		return CommissionStatus.isVerified(getCommissionStatus());
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getIndex())
			.append(getId())
			.toHashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof InvoiceItem)) {
			return false;
		}
		InvoiceItem other = (InvoiceItem) object;
		return new EqualsBuilder()
			.append(getIndex(), other.getIndex())
			.append(getId(), other.getId())
			.isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public Class<?> getTrueClass() {
		return InvoiceItem.class;
	}

	@Override
	public int compareTo(InvoiceItem other) {
		return new CompareToBuilder()
			.append(getIndex(), other.getIndex())
			.append(getId(), other.getId())
			.toComparison();
	}

	@Override
	public void setName(String name) {}
}
