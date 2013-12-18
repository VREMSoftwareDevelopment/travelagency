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
package ca.travelagency.traveler;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;

import ca.travelagency.components.javascript.JSUtils;
import ca.travelagency.invoice.Invoice;
import ca.travelagency.invoice.InvoiceTraveler;

public class TravelerLookupLink extends Panel {
	private static final long serialVersionUID = 1L;

	public static final String LINK = "link";
	public static final String LINK_MODALWINDOW = "modalwindow";

	private TravelerLookupModalWindow modalWindow;

	public TravelerLookupLink(String id, Form<InvoiceTraveler> travelerForm) {
        super(id);

		modalWindow = new TravelerLookupModalWindow(LINK_MODALWINDOW, travelerForm);
        add(modalWindow);

        AjaxLink<Invoice> link = new AjaxLink<Invoice>(LINK) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				modalWindow.show(target);
				target.appendJavaScript(JSUtils.INITIALIZE);
			}
        };
        add(link);
    }
}