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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;

import ca.travelagency.customer.Customer;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.utils.MoneyUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

@Entity
public class Invoice implements DaoEntity {
	private static final long serialVersionUID = 1L;

	public static Invoice make(Customer customer, SystemUser systemUser) {
		Invoice invoice = new Invoice();
		invoice.setSystemUser(systemUser);
		invoice.initialize(customer);
		return invoice;
	}

	public enum Properties {
		status,
		customer,
		systemUser,
		date,
		billCompany,
		totalDueDate,

		invoiceDestinations,
		invoiceItems,
		invoiceNotes,
		invoicePayments,
		invoiceTravelers,

		// SQL Optimization
		totalDue,

		// read-only properties
		invoiceNumber,

		// calculated
		salesAmounts
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Enumerated(EnumType.STRING)
	private InvoiceStatus status = InvoiceStatus.Active;
	@ManyToOne()
	@JoinColumn(name="system_user_id", nullable=false)
	private SystemUser systemUser;
	@ManyToOne()
	@JoinColumn(name="customer_id", nullable=false)
	private Customer customer;
	private boolean billCompany;
	private Date date = new Date();
	private Date totalDueDate = new Date();

	// used to optimize SQL
	@SuppressWarnings("unused")
	private BigDecimal totalDue = MoneyUtils.ZERO_VALUE;

	@OneToMany(cascade={CascadeType.ALL}, orphanRemoval=true)
	@JoinColumn(name="invoice_id", nullable=false)
	// have to use hibernate annotation
	@Sort(type = SortType.NATURAL)
	private SortedSet<InvoiceDestination> invoiceDestinations = new TreeSet<InvoiceDestination>();

	@OneToMany(cascade={CascadeType.ALL}, orphanRemoval=true)
	@JoinColumn(name="invoice_id", nullable=false)
	// have to use hibernate annotation
	@Sort(type = SortType.NATURAL)
	private SortedSet<InvoiceTraveler> invoiceTravelers = new TreeSet<InvoiceTraveler>();

	@OneToMany(cascade={CascadeType.ALL}, orphanRemoval=true)
	@JoinColumn(name="invoice_id", nullable=false)
	// have to use hibernate annotation
	@Sort(type = SortType.NATURAL)
	private SortedSet<InvoiceNote> invoiceNotes = new TreeSet<InvoiceNote>();

	@OneToMany(cascade={CascadeType.ALL}, orphanRemoval=true, mappedBy="invoice")
	private List<InvoiceItem> invoiceItems = new ArrayList<InvoiceItem>();

	@OneToMany(cascade={CascadeType.ALL}, orphanRemoval=true, mappedBy="invoice")
	// have to use hibernate annotation
	@Sort(type = SortType.NATURAL)
	private SortedSet<InvoicePayment> invoicePayments = new TreeSet<InvoicePayment>();

	// used only for testing
	@Deprecated
	public Invoice() {
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		Validate.notNull(id);
		this.id = id;
	}

	public SystemUser getSystemUser() {
		return systemUser;
	}
	public void setSystemUser(SystemUser systemUser) {
		Validate.notNull(systemUser);
		this.systemUser = systemUser;
	}

	public InvoiceStatus getStatus() {
		return status;
	}
	public void setStatus(InvoiceStatus status) {
		Validate.notNull(status);
		this.status = status;
	}

	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		Validate.notNull(customer);
		this.customer = customer;
	}

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		Validate.notNull(date);
		this.date = date;
	}

	public Date getTotalDueDate() {
		return totalDueDate;
	}
	public void setTotalDueDate(Date totalDueDate) {
		this.totalDueDate = totalDueDate;
	}

	public boolean isBillCompany() {
		return billCompany;
	}
	public void setBillCompany(boolean billCompany) {
		this.billCompany = billCompany;
	}

	private void calculateTotalDue() {
		this.totalDue = getSalesAmounts().getDue();
	}

	public SortedSet<InvoiceDestination> getInvoiceDestinations() {
		return invoiceDestinations;
	}
	public void addInvoiceDestination(InvoiceDestination invoiceDestination) {
		Validate.notNull(invoiceDestination);
		getInvoiceDestinations().add(invoiceDestination);
	}
	public void removeInvoiceDestination(InvoiceDestination invoiceDestination) {
		Validate.notNull(invoiceDestination);
		getInvoiceDestinations().remove(invoiceDestination);
	}

	public SortedSet<InvoiceTraveler> getInvoiceTravelers() {
		return invoiceTravelers;
	}
	public void addInvoiceTraveler(InvoiceTraveler invoiceTraveler) {
		Validate.notNull(invoiceTraveler);
		getInvoiceTravelers().add(invoiceTraveler);
	}
	public void removeInvoiceTraveler(InvoiceTraveler invoiceTraveler) {
		Validate.notNull(invoiceTraveler);
		getInvoiceTravelers().remove(invoiceTraveler);
	}

	public SortedSet<InvoicePayment> getInvoicePayments() {
		return invoicePayments;
	}
	public void addInvoicePayment(InvoicePayment invoicePayment) {
		Validate.notNull(invoicePayment);
		invoicePayment.setInvoice(this);
		getInvoicePayments().add(invoicePayment);
		calculateTotalDue();
	}
	public void removeInvoicePayment(InvoicePayment invoicePayment) {
		Validate.notNull(invoicePayment);
		getInvoicePayments().remove(invoicePayment);
		invoicePayment.setInvoice(null);
		calculateTotalDue();
	}

	public SortedSet<InvoiceNote> getInvoiceNotes() {
		return invoiceNotes;
	}
	public Collection<InvoiceNote> getPublicInvoiceNotes() {
		return Collections2.filter(getInvoiceNotes(), new Predicate<InvoiceNote>() {
			@Override
			public boolean apply(InvoiceNote invoiceNote) {
				return !invoiceNote.isPrivateNote();
			}
		});
	}
	public void addInvoiceNote(InvoiceNote invoiceNote) {
		Validate.notNull(invoiceNote);
		getInvoiceNotes().add(invoiceNote);
	}
	public void removeInvoiceNote(InvoiceNote invoiceNote) {
		Validate.notNull(invoiceNote);
		getInvoiceNotes().remove(invoiceNote);
	}

	public List<InvoiceItem> getInvoiceItems() {
		Collections.sort(invoiceItems);
		return invoiceItems;
	}
	public void addInvoiceItem(InvoiceItem invoiceItem) {
		Validate.notNull(invoiceItem);
		invoiceItem.setInvoice(this);
		List<InvoiceItem> items = getInvoiceItems();
		if (invoiceItem.getIndex() == null) {
			invoiceItem.setIndex(items.size());
			items.add(invoiceItem);
		} else {
			items.set(invoiceItem.getIndex(), invoiceItem);
		}
		calculateTotalDue();
	}
	public void removeInvoiceItem(InvoiceItem invoiceItem) {
		Validate.notNull(invoiceItem);
		List<InvoiceItem> items = getInvoiceItems();
		items.remove(invoiceItem);
		int index = 0;
		for (InvoiceItem item : items) {
			item.setIndex(index++);
		}
		calculateTotalDue();
		invoiceItem.setInvoice(null);
	}
	public void moveUpItem(InvoiceItem invoiceItem) {
		int index = invoiceItem.getIndex();
		if (index < 1) {
			return;
		}
		swapItems(index, index - 1);
	}
	public void moveDownItem(InvoiceItem invoiceItem) {
		int index = invoiceItem.getIndex();
		if (index >= getInvoiceItems().size() - 1) {
			return;
		}
		swapItems(index, index + 1);
	}

	private void swapItems(int srcIndex, int dstIndex) {
		List<InvoiceItem> items = getInvoiceItems();

		InvoiceItem currentItem = items.get(srcIndex);
		InvoiceItem previousItem = items.get(dstIndex);

		currentItem.setIndex(dstIndex);
		previousItem.setIndex(srcIndex);

		items.set(srcIndex, previousItem);
		items.set(dstIndex, currentItem);
	}

	public SalesAmounts getSalesAmounts() {
		SalesAmounts salesAmount = SalesAmounts.make();

		for (InvoiceItem item : getInvoiceItems()) {
			salesAmount.add(item.getSalesAmounts());
		}

		for (InvoicePayment payment : getInvoicePayments()) {
			salesAmount.addPaid(payment.getAmount());
		}

		return salesAmount;
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
		if (!(object instanceof Invoice)) {
			return false;
		}
		return new EqualsBuilder().append(getId(), ((Invoice) object).getId()).isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public Class<?> getTrueClass() {
		return Invoice.class;
	}

	@Override
	public String getName() {
		return new DecimalFormat("0000000000").format(getId() == null ? 0 : getId());
	}
	public String getInvoiceNumber() {
		return getName();
	}

	public void initialize(Customer customer) {
		setCustomer(customer);
	}

	public boolean isActive() {
		return InvoiceStatus.Active.equals(getStatus());
	}

	public boolean isVoided() {
		return InvoiceStatus.Voided.equals(getStatus());
	}

	public void closeInvoice() {
		if (!isActive() || getInvoiceItems().isEmpty() || !getSalesAmounts().isVerified()) {
			return;
		}
		setStatus(InvoiceStatus.Closed);
	}

	@Override
	public void setName(String name) {}
}
