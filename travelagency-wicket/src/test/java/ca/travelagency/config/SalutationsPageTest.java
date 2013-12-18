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

public class SalutationsPageTest extends BaseWicketTester {
	@Test
	public void testInheritance() {
		tester.startPage(SalutationsPage.class);
		tester.assertRenderedPage(SalutationsPage.class);
		SalutationsPage page = (SalutationsPage) tester.getLastRenderedPage();

		Assert.assertEquals(SalutationsPage.PAGE_KEY, page.getPageTitleKey());
		Assert.assertEquals(SalutationsPage.LABEL_KEY, page.getLabelNameKey());
		Assert.assertEquals(Salutation.class, page.getTrueClass());
		Assert.assertTrue(page.makeLookupEntitiesPage(null) instanceof SalutationsPage);
	}
}
