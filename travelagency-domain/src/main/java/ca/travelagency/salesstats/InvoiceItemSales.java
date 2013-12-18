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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.CompareToBuilder;

import ca.travelagency.invoice.InvoiceItem;
import ca.travelagency.invoice.SalesAmounts;

import com.google.common.collect.Lists;

public abstract class InvoiceItemSales implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<InvoiceItem> invoiceItems;

	protected InvoiceItemSales(List<InvoiceItem> invoiceItems) {
		Validate.notNull(invoiceItems);
		this.invoiceItems = invoiceItems;
		Comparator<InvoiceItem> comparator = new Comparator<InvoiceItem>() {
			@Override
			public int compare(InvoiceItem invoiceItem1, InvoiceItem invoiceItem2) {
				return	new CompareToBuilder()
					.append(invoiceItem1.getInvoice().getDate(), invoiceItem2.getInvoice().getDate())
					.append(invoiceItem1.getInvoice().getId(), invoiceItem2.getInvoice().getId())
					.append(invoiceItem1.getId(), invoiceItem2.getId())
					.toComparison();
			}
		};
		Collections.sort(this.invoiceItems, comparator);
	}

	public final Collection<InvoiceItem> getInvoiceItems() {
		return invoiceItems;
	}

	public List<InvoiceItemDistribution> getDistributions() {
		Map<String, InvoiceItemDistribution> distributions = new TreeMap<String, InvoiceItemDistribution>();
		for (InvoiceItem invoiceItem : getInvoiceItems()) {
			InvoiceItemDistribution key = InvoiceItemDistribution.make(makeKey(invoiceItem));
			InvoiceItemDistribution value = distributions.get(key.getKey());
			if (value == null) {
				value = key;
			}
			value.add(invoiceItem);
			distributions.put(value.getKey(), value);
		}
		List<InvoiceItemDistribution> results = Lists.newArrayList(distributions.values());
		Collections.sort(results);
		return results;
	}

	public final SalesAmounts getTotalSales() {
		SalesAmounts salesAmounts = SalesAmounts.make();
		for (InvoiceItem invoiceItem : getInvoiceItems()) {
			salesAmounts.add(invoiceItem.getSalesAmounts());
		}
		return salesAmounts;
	}

	protected abstract String makeKey(InvoiceItem invoiceItem);
}
