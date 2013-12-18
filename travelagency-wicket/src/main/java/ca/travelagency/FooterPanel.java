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

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import ca.travelagency.utils.ApplicationProperties;

public class FooterPanel extends Panel {

	private static final long serialVersionUID = 1L;

	static final String VERSION = "version";
	static final String COMPANY = "company";
	static final String YEAR = "year";
	static final String CODE = "code";

	static final String SOFTWARE_COMPANY = "VREM Software Development";

	public FooterPanel(String id) {
		super(id);

		ApplicationProperties applicationProperties = ApplicationProperties.make();
		add(new Label(VERSION, applicationProperties.getVersion()));
		add(new Label(CODE, applicationProperties.getCode()));

		add(new Label(YEAR, ""+Calendar.getInstance().get(Calendar.YEAR)));
		add(new Label(COMPANY, SOFTWARE_COMPANY));
	}
}
