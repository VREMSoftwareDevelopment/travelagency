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

import java.util.Calendar;

import org.junit.Test;

public class FooterPanelTest extends BaseWicketTester {

	@Test
	public void testComponents() {
		tester.startComponentInPage(new FooterPanel(COMPONENT_ID));

		tester.assertLabel(COMPONENT_PATH+FooterPanel.COMPANY, FooterPanel.SOFTWARE_COMPANY);
		tester.assertLabel(COMPONENT_PATH+FooterPanel.YEAR, ""+Calendar.getInstance().get(Calendar.YEAR));
		tester.assertLabel(COMPONENT_PATH+FooterPanel.VERSION, "${pom.version}");
		tester.assertLabel(COMPONENT_PATH+FooterPanel.CODE, "travelagency");
	}

}
