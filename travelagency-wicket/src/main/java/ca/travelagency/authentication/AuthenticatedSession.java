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
package ca.travelagency.authentication;

import java.io.Serializable;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ca.travelagency.customer.CustomerFilter;
import ca.travelagency.identity.AuthenticationException;
import ca.travelagency.identity.Role;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserRole;
import ca.travelagency.identity.SystemUserService;
import ca.travelagency.invoice.InvoiceFilter;
import ca.travelagency.persistence.DaoSupport;
import ca.travelagency.reconciliation.InvoiceItemFilter;
import ca.travelagency.reconciliation.InvoicePaymentFilter;

public class AuthenticatedSession extends AuthenticatedWebSession {
	private static final long serialVersionUID = 1L;

	@SpringBean
	protected SystemUserService systemUserService;

	@SpringBean
	protected DaoSupport<SystemUser> daoSupport;

	private Long systemUserId;

	// Session Persistent Filters
	private CustomerFilter customerFilter;
	private InvoiceFilter invoiceFilter;
	private InvoiceItemFilter invoiceItemFilter;
	private InvoicePaymentFilter invoicePaymentFilter;

	public AuthenticatedSession(Request request) {
		super(request);
        Injector.get().inject(this);
        initFilters();
	}

	@Override
	public boolean authenticate(final String name, final String password) {
		try {
			SystemUser systemUser = systemUserService.authorize(name, password);
			systemUserId = systemUser.getId();
			return true;
		} catch (AuthenticationException e) {
			systemUserId = null;
			return false;
		}
	}

	@Override
	public final Roles getRoles() {
		Roles roles = new Roles();
		SystemUser systemUser = getSystemUser();
		if (systemUser == null) {
			signOut();
			return roles;
		}
		for (SystemUserRole systemUserRole : systemUser.getSystemUserRoles()) {
			roles.add(systemUserRole.getRole().name());
		}
		return roles;
	}

	public SystemUser getSystemUser() {
		if (!isSignedIn()) {
			return null;
		}
		return daoSupport.find(SystemUser.class, systemUserId);
	}

	public boolean hasRole(Role role) {
		return getRoles().hasRole(role.name());
	}

	@Override
	public void signOut() {
		super.signOut();
		systemUserId = null;
		initFilters();
	}

	Serializable getSystemUserId() {
		return systemUserId;
	}

	// Session Persistent Filters
	private void initFilters() {
		clearCustomerFilter();
        clearInvoiceFilter();
        clearInvoiceItemFilter();
        clearInvoicePaymentFilter();
	}

	public CustomerFilter getCustomerFilter() {
		return customerFilter;
	}
	public void clearCustomerFilter() {
		this.customerFilter = new CustomerFilter();
	}

	public InvoiceFilter getInvoiceFilter() {
		return invoiceFilter;
	}
	public void clearInvoiceFilter() {
		this.invoiceFilter = new InvoiceFilter();
	}

	public InvoiceItemFilter getInvoiceItemFilter() {
		return invoiceItemFilter;
	}
	public void clearInvoiceItemFilter() {
		this.invoiceItemFilter = new InvoiceItemFilter();
	}

	public InvoicePaymentFilter getInvoicePaymentFilter() {
		return invoicePaymentFilter;
	}
	public void clearInvoicePaymentFilter() {
		this.invoicePaymentFilter = new InvoicePaymentFilter();
	}

}
