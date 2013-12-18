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
package ca.travelagency;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.log.Log4JLogChute;
import org.apache.wicket.Page;
import org.apache.wicket.application.ComponentInstantiationListenerCollection;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.settings.IApplicationSettings;
import org.apache.wicket.settings.IRequestLoggerSettings;
import org.apache.wicket.settings.IResourceSettings;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import ca.travelagency.authentication.AuthenticatedSession;
import ca.travelagency.authentication.SignInPage;
import ca.travelagency.authentication.SignOutPage;
import ca.travelagency.config.CitiesPage;
import ca.travelagency.config.CompanyPage;
import ca.travelagency.config.ProductsPage;
import ca.travelagency.config.ProvincesPage;
import ca.travelagency.config.SalutationsPage;
import ca.travelagency.customer.CustomerPage;
import ca.travelagency.customer.CustomersPage;
import ca.travelagency.errorhandling.AccessDeniedPage;
import ca.travelagency.errorhandling.ErrorPage;
import ca.travelagency.errorhandling.PageExpiredErrorPage;
import ca.travelagency.identity.MyProfilePage;
import ca.travelagency.identity.SystemUserPage;
import ca.travelagency.identity.SystemUsersPage;
import ca.travelagency.invoice.InvoicePage;
import ca.travelagency.invoice.InvoicePreviewPage;
import ca.travelagency.invoice.InvoicesPage;
import ca.travelagency.reconciliation.CommissionPage;
import ca.travelagency.reconciliation.PaymentPage;
import ca.travelagency.salesreports.ReportsPage;


@Component
public class Application extends AuthenticatedWebApplication implements ApplicationContextAware {
	private ApplicationContext applicationContext;

	@Override
	public Class<HomePage> getHomePage() {
		return HomePage.class;
	}

	@Override
	protected void init() {
		super.init();
		initComponentInstantiationListeners();
		initApplicationSettings();
		initRequestLoggerSettings();
		initResourceSettings();
		initVelocity();
		initFriendlyURls();
	}

	private void initVelocity() {
		Velocity.setProperty(Log4JLogChute.RUNTIME_LOG_LOG4J_LOGGER, "root");
		Velocity.init();
	}

	private void initResourceSettings() {
		IResourceSettings resourceSettings = getResourceSettings();
		resourceSettings.setThrowExceptionOnMissingResource(false);
	}

	private void initRequestLoggerSettings() {
		IRequestLoggerSettings requestLoggerSettings = getRequestLoggerSettings();
		requestLoggerSettings.setRequestLoggerEnabled(false);
	}

	private void initApplicationSettings() {
		IApplicationSettings applicationSettings = getApplicationSettings();
		applicationSettings.setAccessDeniedPage(AccessDeniedPage.class);
		applicationSettings.setInternalErrorPage(ErrorPage.class);
		applicationSettings.setPageExpiredErrorPage(PageExpiredErrorPage.class);
	}

	private void initComponentInstantiationListeners() {
		SpringComponentInjector springComponentInjector = new SpringComponentInjector(this, applicationContext);
		ComponentInstantiationListenerCollection componentInstantiationListeners = getComponentInstantiationListeners();
		componentInstantiationListeners.add(springComponentInjector);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
		return AuthenticatedSession.class;
	}

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return SignInPage.class;
	}

	private static final Class<?> [] FRIENDLY_URL_PAGES = new Class<?> [] {
		HomePage.class,
		AccessDeniedPage.class,
		ErrorPage.class,
		PageExpiredErrorPage.class,
		SignInPage.class,
		SignOutPage.class,
		MyProfilePage.class,
		SystemUserPage.class,
		SystemUsersPage.class,
		CustomerPage.class,
		CustomersPage.class,
		InvoicePage.class,
		InvoicePreviewPage.class,
		InvoicesPage.class,
		ReportsPage.class,
		CitiesPage.class,
		CommissionPage.class,
		CompanyPage.class,
		PaymentPage.class,
		ProductsPage.class,
		ProvincesPage.class,
		SalutationsPage.class,
	};

	@SuppressWarnings("unchecked")
	private void initFriendlyURls() {
		for (Class<?> pageClass: FRIENDLY_URL_PAGES) {
			mountPage(pageClass.getSimpleName(), (Class<Page>) pageClass);
		}
	}

}
