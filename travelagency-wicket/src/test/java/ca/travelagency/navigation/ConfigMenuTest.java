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

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Test;

import ca.travelagency.BaseWicketTester;
import ca.travelagency.HomePage;
import ca.travelagency.config.CitiesPage;
import ca.travelagency.config.CompanyPage;
import ca.travelagency.config.ProductsPage;
import ca.travelagency.config.ProvincesPage;
import ca.travelagency.config.SalutationsPage;
import ca.travelagency.config.SuppliersPage;

public class ConfigMenuTest extends BaseWicketTester {
	@Test
	public void testMenuRendersSuccessfully() {
		tester.startComponentInPage(new ConfigMenu(COMPONENT_ID, HomePage.class));

		tester.assertBookmarkablePageLink(COMPONENT_PATH+ConfigMenu.CITIES, CitiesPage.class, new PageParameters());
		tester.assertBookmarkablePageLink(COMPONENT_PATH+ConfigMenu.COMPANY, CompanyPage.class, new PageParameters());
		tester.assertBookmarkablePageLink(COMPONENT_PATH+ConfigMenu.PRODUCTS, ProductsPage.class, new PageParameters());
		tester.assertBookmarkablePageLink(COMPONENT_PATH+ConfigMenu.PROVINCES, ProvincesPage.class, new PageParameters());
		tester.assertBookmarkablePageLink(COMPONENT_PATH+ConfigMenu.SALUTATIONS, SalutationsPage.class, new PageParameters());
		tester.assertBookmarkablePageLink(COMPONENT_PATH+ConfigMenu.SUPPLIERS, SuppliersPage.class, new PageParameters());
	}
}
