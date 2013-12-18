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
package ca.travelagency.identity.roles;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import ca.travelagency.components.formdetail.DetailsPanel;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserRole;

public class RolesPanel extends DetailsPanel<SystemUser, SystemUserRole> {
	private static final long serialVersionUID = 1L;

	public RolesPanel(String id, IModel<SystemUser> model) {
		super(id, model);
	}

	@Override
	protected void update(AjaxRequestTarget target, SystemUserRole systemUserRole) {
		SystemUser systemUser = getDaoEntity();
		systemUser.addSystemUserRole(systemUserRole);
		daoSupport.persist(systemUser);
		updateDisplay(target);
	}

	@Override
	protected void delete(AjaxRequestTarget target, SystemUserRole systemUserRole) {
		SystemUser systemUser = getDaoEntity();
		systemUser.removeSystemUserRole(systemUserRole);
		daoSupport.persist(systemUser);
		updateDisplay(target);
	}

	@Override
	public boolean isEditable() {
		return getDaoEntity().isActive();
	}

	@Override
	protected Panel makeDetailFormPanel(String id) {
		return new RoleFormPanel(id, this);
	}

	@Override
	protected Panel makeDetailRowPanel(String id, IModel<SystemUserRole> model) {
		return new RoleRowPanel(id, model, this);
	}

	@Override
	protected List<SystemUserRole> getDetails() {
		return new ArrayList<SystemUserRole>(getDaoEntity().getSystemUserRoles());
	}

	@Override
	protected Panel makeDetailHeaderPanel(String id) {
		return new RolesHeaderPanel(id);
	}
}
