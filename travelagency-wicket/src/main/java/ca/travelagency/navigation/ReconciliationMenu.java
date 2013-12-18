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
package ca.travelagency.navigation;

import org.apache.wicket.Page;

import ca.travelagency.reconciliation.CommissionPage;
import ca.travelagency.reconciliation.PaymentPage;

public class ReconciliationMenu extends BaseNavigationMenu {
	private static final long serialVersionUID = 1L;

	static final String COMMISSIONS = "commissions";
	static final String PAYMENTS = "payments";

	public ReconciliationMenu(String id, Class<? extends Page> currentPage) {
		super(id);
		commonInit(currentPage);
	}

	private void commonInit(Class<? extends Page> currentPage) {
		add(bookmarkablePageLink(COMMISSIONS, CommissionPage.class, currentPage));
		add(bookmarkablePageLink(PAYMENTS, PaymentPage.class, currentPage));
	}
}
