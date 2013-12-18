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
package ca.travelagency.config;

import org.junit.Assert;
import org.junit.Test;

import ca.travelagency.BaseWicketTester;

public class SuppliersPageTest extends BaseWicketTester {
	@Test
	public void testInheritance() {
		tester.startPage(SuppliersPage.class);
		tester.assertRenderedPage(SuppliersPage.class);
		SuppliersPage page = (SuppliersPage) tester.getLastRenderedPage();

		Assert.assertEquals(SuppliersPage.PAGE_KEY, page.getPageTitleKey());
		Assert.assertEquals(SuppliersPage.LABEL_KEY, page.getLabelNameKey());
		Assert.assertEquals(Supplier.class, page.getTrueClass());
		Assert.assertTrue(page.makeLookupEntitiesPage(null) instanceof SuppliersPage);
	}
}
