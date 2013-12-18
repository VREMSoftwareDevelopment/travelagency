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

import ca.travelagency.config.CitiesPage;
import ca.travelagency.config.CompanyPage;
import ca.travelagency.config.ProductsPage;
import ca.travelagency.config.ProvincesPage;
import ca.travelagency.config.SalutationsPage;
import ca.travelagency.config.SuppliersPage;

public class ConfigMenu extends BaseNavigationMenu {
	private static final long serialVersionUID = 1L;

	static final String CITIES = "cities";
	static final String COMPANY = "company";
	static final String PRODUCTS = "products";
	static final String PROVINCES = "provinces";
	static final String SALUTATIONS = "salutations";
	static final String SUPPLIERS = "suppliers";

	public ConfigMenu(String id, Class<? extends Page> currentPage) {
		super(id);
		commonInit(currentPage);
	}

	private void commonInit(Class<? extends Page> currentPage) {
		add(bookmarkablePageLink(CITIES, CitiesPage.class, currentPage));
		add(bookmarkablePageLink(COMPANY, CompanyPage.class, currentPage));
		add(bookmarkablePageLink(PRODUCTS, ProductsPage.class, currentPage));
		add(bookmarkablePageLink(PROVINCES, ProvincesPage.class, currentPage));
		add(bookmarkablePageLink(SALUTATIONS, SalutationsPage.class, currentPage));
		add(bookmarkablePageLink(SUPPLIERS, SuppliersPage.class, currentPage));
	}
}
