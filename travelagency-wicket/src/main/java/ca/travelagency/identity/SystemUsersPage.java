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
package ca.travelagency.identity;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ca.travelagency.BasePage;
import ca.travelagency.components.CheckMarkPanel;
import ca.travelagency.components.dataprovider.DataProvider;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.DaoSupport;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.search.SearchButtons;
import ca.travelagency.search.SearchPanel;
import ca.travelagency.utils.ApplicationProperties;

@AuthorizeInstantiation({"ADMIN"})
public class SystemUsersPage extends BasePage {
	private static final long serialVersionUID = 1L;

	static final String DATA_TABLE = "dataTable";
	static final String LINK = "link";
	static final String LINK_LABEL = "label";
	static final String CREATE = "create";

	static final String SEARCH_PANEL = "searchPanel";

	static final String PAGE_TITLE = "systemUsers.title";

	@SpringBean
	private DaoSupport<SystemUser> daoSupport;

    public SystemUsersPage() {
    	this(Model.of(new SystemUserFilter()));
    }

    public SystemUsersPage(final IModel<SystemUserFilter> model) {
    	super();

		add(new SearchPanel<SystemUser, SystemUserFilter>(SEARCH_PANEL, model, new SystemUsersPageSearchButtons(model)));

    	add(new AjaxFallbackDefaultDataTable<SystemUser, String>(DATA_TABLE, makeColumns(), new DataProvider<SystemUser>(model), DATA_TABLE_PER_PAGE));

    	add(new Link<String>(CREATE) {
    		private static final long serialVersionUID = 1L;
    		@Override
    		public void onClick() {
    			setResponsePage(new SystemUserPage());
    		}
    	}.setVisible(canCreateSystemUser()));
	}

	private List<IColumn<SystemUser, String>> makeColumns() {
		List<IColumn<SystemUser, String>> columns = new ArrayList<IColumn<SystemUser, String>>();

		columns.add(new PropertyColumn<SystemUser, String>(new ResourceModel(DaoEntity.PROPERTY_NAME), DaoEntity.PROPERTY_NAME, DaoEntity.PROPERTY_NAME) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<SystemUser>> item, String componentId, IModel<SystemUser> rowModel) {
				item.add(new LinkPanel(componentId, rowModel.getObject()));
			}
		});
		columns.add(new PropertyColumn<SystemUser, String>(new ResourceModel(SystemUser.Properties.firstName.name()), SystemUser.Properties.firstName.name(), SystemUser.Properties.firstName.name()));
		columns.add(new PropertyColumn<SystemUser, String>(new ResourceModel(SystemUser.Properties.lastName.name()), SystemUser.Properties.lastName.name(), SystemUser.Properties.lastName.name()));
		columns.add(new PropertyColumn<SystemUser, String>(new ResourceModel(SystemUser.Properties.email.name()), SystemUser.Properties.email.name()));
		columns.add(new PropertyColumn<SystemUser, String>(new ResourceModel(SystemUser.Properties.primaryPhone.name()), SystemUser.Properties.primaryPhone.name()));
		columns.add(new PropertyColumn<SystemUser, String>(new ResourceModel(SystemUser.Properties.secondaryPhone.name()), SystemUser.Properties.secondaryPhone.name()));
		columns.add(new PropertyColumn<SystemUser, String>(new ResourceModel(SystemUser.Properties.active.name()), SystemUser.Properties.active.name()) {
			private static final long serialVersionUID = 1L;
			@Override
			public void populateItem(Item<ICellPopulator<SystemUser>> item, String componentId, IModel<SystemUser> rowModel) {
				item.add(new CheckMarkPanel(componentId, rowModel.getObject().isActive()));
			}
		});
		return columns;
	}

	private static class LinkPanel extends Panel {
		private static final long serialVersionUID = 1L;

		public LinkPanel(String id, final SystemUser systemUser) {
	        super(id);
	        Link<SystemUser> link = new Link<SystemUser>(LINK) {
				private static final long serialVersionUID = 1L;
				@Override
				public void onClick() {
					setResponsePage(new SystemUserPage(systemUser));
				}
	        };
	        link.add(new Label(LINK_LABEL, Model.of(systemUser.getName())));
	        add(link);
	    }
	}

	@Override
	public String getPageTitleKey() {
		return PAGE_TITLE;
	}

	public class SystemUsersPageSearchButtons implements SearchButtons {
		private static final long serialVersionUID = 1L;
		private IModel<SystemUserFilter> model;
		public SystemUsersPageSearchButtons(IModel<SystemUserFilter> model) {
			this.model = model;
		}
		@Override
		public void search() {
			setResponsePage(new SystemUsersPage(model));
		}
		@Override
		public void clear() {
			setResponsePage(new SystemUsersPage());
		}
	};

	private boolean canCreateSystemUser() {
		int systemUserMax = ApplicationProperties.make().getSystemUserMax();
		Long count = daoSupport.count(Criteria.make(SystemUser.class));
		return count < systemUserMax;
	}
}
