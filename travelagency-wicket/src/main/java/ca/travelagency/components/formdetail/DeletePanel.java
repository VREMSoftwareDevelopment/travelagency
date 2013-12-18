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
package ca.travelagency.components.formdetail;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ca.travelagency.components.AjaxLinkCallback;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.DaoSupport;

public class DeletePanel<T extends DaoEntity> extends Panel implements AjaxLinkCallback {
	private static final long serialVersionUID = 1L;

	static final String LINK = "link";

	@SpringBean
	private DaoSupport<T> daoSupport;

	public DeletePanel(String id, T daoEntity, Component component) {
		super(id, DaoEntityModelFactory.make(daoEntity));
        add(new DeleteLink(LINK, this, component));
	}

	@Override
	public void onClick(AjaxRequestTarget target) {
		@SuppressWarnings("unchecked")
		T daoEntity = (T) getDefaultModelObject();
		daoSupport.remove(daoEntity);
	}

}
