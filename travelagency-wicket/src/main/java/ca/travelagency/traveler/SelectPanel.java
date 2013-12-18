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
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import ca.travelagency.components.javascript.JSUtils;
import ca.travelagency.customer.Traveler;
import ca.travelagency.invoice.InvoiceTraveler;

public class SelectPanel extends Panel {
	private static final long serialVersionUID = 1L;

	static final String LINK = "link";
	static final String LABEL = "label";

	public SelectPanel(String id, final IModel<Traveler> model, final Form<InvoiceTraveler> travelerForm, final ModalWindow modalWindow) {
		super(id);

		AjaxLink<Void> link = new AjaxLink<Void>(LINK) {
			private static final long serialVersionUID = 1L;
			@Override
			public void onClick(AjaxRequestTarget target) {
				travelerForm.getModelObject().copy(model.getObject());
				target.add(travelerForm);
				modalWindow.close(target);
				target.appendJavaScript(JSUtils.INITIALIZE);
			}
		};
        link.add(new Label(LABEL, Model.of(model.getObject().getName())));
        add(link);
	}
}
