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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.travelagency.invoice.InvoiceItem;
import ca.travelagency.invoice.SalesAmounts;
import ca.travelagency.utils.StringUtils;

public class InvoiceItemDistribution implements Serializable, Comparable<InvoiceItemDistribution> {
	private static final long serialVersionUID = 1L;

	static final String UNKNOWN = "Unknown";

	public static InvoiceItemDistribution make(String key) {
		InvoiceItemDistribution result = new InvoiceItemDistribution();
		result.key = StringUtils.isBlank(key) ? UNKNOWN : key;
		return result;
	}

	private String key;
	private List<InvoiceItem> invoiceItems = new ArrayList<InvoiceItem>();

	public String getKey() {
		return key;
	}

	public long getCount() {
		return getInvoiceItems().size();
	}

	public List<InvoiceItem> getInvoiceItems() {
		return invoiceItems;
	}
	public void add(InvoiceItem invoiceItem) {
		Validate.notNull(invoiceItem);
		this.invoiceItems.add(invoiceItem);
	}

	public SalesAmounts getSalesAmounts() {
		SalesAmounts salesAmounts = SalesAmounts.make();
		for (InvoiceItem invoiceItem : getInvoiceItems()) {
			salesAmounts.add(invoiceItem.getSalesAmounts());
		}
		return salesAmounts;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getKey())
			.toHashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof InvoiceItemDistribution)) {
			return false;
		}
		InvoiceItemDistribution invoiceItemDistribution = (InvoiceItemDistribution) object;
		return new EqualsBuilder()
			.append(getKey(), invoiceItemDistribution.getKey())
			.isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int compareTo(InvoiceItemDistribution other) {
		return new CompareToBuilder()
			.append(getKey(), other.getKey())
			.toComparison();
	}
}
