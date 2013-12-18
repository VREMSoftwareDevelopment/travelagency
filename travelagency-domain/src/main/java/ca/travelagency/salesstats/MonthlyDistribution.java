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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.SalesAmounts;
import ca.travelagency.utils.DateUtils;

public class MonthlyDistribution implements Serializable, Comparable<MonthlyDistribution> {
	private static final long serialVersionUID = 1L;

	public static MonthlyDistribution make(Date date) {
		MonthlyDistribution result = new MonthlyDistribution();
		Validate.notNull(date);
		result.date = DateUtils.truncate(date, Calendar.MONTH);
		return result;

	}

	public enum Properties {
		dateAsString,
		count,
		salesAmounts
	}

	private Date date;
	private List<Invoice> invoices = new ArrayList<Invoice>();

	public Date getDate() {
		return date;
	}
	public String getDateAsString() {
		return DateUtils.formatDateAsMonth(date);
	}

	public long getCount() {
		return getInvoices().size();
	}

	public List<Invoice> getInvoices() {
		return invoices;
	}
	public void add(Invoice invoice) {
		Validate.notNull(invoice);
		this.invoices.add(invoice);
	}

	public SalesAmounts getSalesAmounts() {
		SalesAmounts salesAmounts = SalesAmounts.make();
		for (Invoice invoice : getInvoices()) {
			salesAmounts.add(invoice.getSalesAmounts());
		}
		return salesAmounts;
	}


	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(getDate())
			.toHashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof MonthlyDistribution)) {
			return false;
		}
		MonthlyDistribution monthlyDistribution = (MonthlyDistribution) object;
		return new EqualsBuilder()
			.append(getDate(), monthlyDistribution.getDate())
			.isEquals();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int compareTo(MonthlyDistribution other) {
		return new CompareToBuilder()
			.append(other.getDate(), getDate())
			.toComparison();
	}
}
