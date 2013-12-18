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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.velocity.markup.html.VelocityPanel;
import org.junit.Test;

import ca.travelagency.utils.StringUtils;

public class BasePrintPageTest extends BaseWicketTester {

	private static final String MY_PAGE_TITLE = "My Page Title";
	private static final String TEST_TEMPLATE = "test";

	@Test
	public void testPageComponents() {
		tester.startPage(new BasePrintPage(TEST_TEMPLATE, Model.of(StringUtils.EMPTY)) {
			private static final long serialVersionUID = 1L;
			@Override
			protected String getPageTitle() {
				return MY_PAGE_TITLE;
			}

		});
		tester.assertRenderedPage(BasePrintPage.class);

		tester.assertComponent(BasePrintPage.TITLE, Label.class);
		tester.assertComponent(BasePrintPage.PREVIEW, VelocityPanel.class);

		tester.assertInvisible(BasePrintPage.PREVIEW + BasePrintPage.PATH_SEPARATOR + BasePrintPage.LOGO_IMAGE);

		tester.assertModelValue(BasePrintPage.TITLE, MY_PAGE_TITLE);
	}

}
