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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.Validate;

import ca.travelagency.identity.SystemUser;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.SalesAmounts;
import ca.travelagency.utils.MoneyUtils;

import com.google.common.collect.Lists;

public class InvoiceSales implements Serializable {
	private static final long serialVersionUID = 1L;

	public static InvoiceSales make(List<Invoice> invoices) {
		Validate.notNull(invoices);
		InvoiceSales invoiceSales = new InvoiceSales();
		invoiceSales.invoices = invoices;
		return invoiceSales;
	}

	public enum Properties {
		monthlyDistribution
	}

	private List<Invoice> invoices;
	private SystemUser systemUser;

	private InvoiceSales() {
	}

	public SystemUser getSystemUser() {
		return systemUser;
	}
	public InvoiceSales setSystemUser(SystemUser systemUser) {
		this.systemUser = systemUser;
		return this;
	}

	public Collection<Invoice> getInvoices() {
		return invoices;
	}

	public List<MonthlyDistribution> getMonthlyDistribution() {
		Map<Date, MonthlyDistribution> monthlyDistribution = new TreeMap<Date, MonthlyDistribution>();
		for (Invoice invoice : getInvoices()) {
			MonthlyDistribution key = MonthlyDistribution.make(invoice.getDate());
			MonthlyDistribution value = monthlyDistribution.get(key.getDate());
			if (value == null) {
				value = key;
			}
			value.add(invoice);
			monthlyDistribution.put(value.getDate(), value);
		}
		List<MonthlyDistribution> results = Lists.newArrayList(monthlyDistribution.values());
		Collections.sort(results);
		return results;
	}

	public SalesAmounts getTotalSales() {
		SalesAmounts salesAmounts = SalesAmounts.make();
		for (Invoice invoice : getInvoices()) {
			salesAmounts.add(invoice.getSalesAmounts());
		}
		return salesAmounts;
	}

	public BigDecimal getCommissionEstimated() {
		if (systemUser == null) {
			return MoneyUtils.ZERO_VALUE;
		}
		BigDecimal commission = getTotalSales().getCommission();
		BigDecimal commissionRate = new BigDecimal(systemUser.getCommissionRate() / 100);
		return MoneyUtils.round(commission.multiply(commissionRate));
	}
	public String getCommissionEstimatedAsString() {
		return MoneyUtils.format(getCommissionEstimated());
	}

	public BigDecimal getCommissionRPayable() {
		if (systemUser == null) {
			return MoneyUtils.ZERO_VALUE;
		}
		BigDecimal commissionReceived = getTotalSales().getCommissionReceived();
		BigDecimal commissionRate = new BigDecimal(systemUser.getCommissionRate() / 100);
		return MoneyUtils.round(commissionReceived.multiply(commissionRate));
	}
	public String getCommissionRPayableAsString() {
		return MoneyUtils.format(getCommissionRPayable());
	}

	public BigDecimal getCommissionVPayable() {
		if (systemUser == null) {
			return MoneyUtils.ZERO_VALUE;
		}
		BigDecimal commissionVerified = getTotalSales().getCommissionVerified();
		BigDecimal commissionRate = new BigDecimal(systemUser.getCommissionRate() / 100);
		return MoneyUtils.round(commissionVerified.multiply(commissionRate));
	}
	public String getCommissionVPayableAsString() {
		return MoneyUtils.format(getCommissionVPayable());
	}
}
