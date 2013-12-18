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
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang3.Validate;

import ca.travelagency.invoice.InvoicePayment;
import ca.travelagency.invoice.PaymentType;

import com.google.common.collect.Lists;

public class PaymentDistribution implements Serializable {
	private static final long serialVersionUID = 1L;

	private SortedSet<InvoicePayment> invoicePayments = new TreeSet<InvoicePayment>();

	public static PaymentDistribution make(SortedSet<InvoicePayment> invoicePayments) {
		return new PaymentDistribution(invoicePayments);
	}

	private PaymentDistribution(SortedSet<InvoicePayment> invoicePayments) {
		Validate.notNull(invoicePayments);
		this.invoicePayments = invoicePayments;
	}

	public final Collection<InvoicePayment> getInvoicePayments() {
		return invoicePayments;
	}

	public List<InvoicePaymentDistribution> getDistributions() {
		Map<PaymentType, InvoicePaymentDistribution> distributions = new TreeMap<PaymentType, InvoicePaymentDistribution>();
		for (InvoicePayment invoicePayment : getInvoicePayments()) {
			InvoicePaymentDistribution value = distributions.get(invoicePayment.getPaymentType());
			if (value == null) {
				value = InvoicePaymentDistribution.make(invoicePayment.getPaymentType());
			}
			value.add(invoicePayment);
			distributions.put(value.getPaymentType(), value);
		}
		List<InvoicePaymentDistribution> results = Lists.newArrayList(distributions.values());
		Collections.sort(results);
		return results;
	}
}
