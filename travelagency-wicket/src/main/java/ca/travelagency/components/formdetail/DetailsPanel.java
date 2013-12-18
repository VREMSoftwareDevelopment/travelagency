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

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.OddEvenListItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.components.javascript.JSUtils;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.DaoSupport;

public abstract class DetailsPanel<T extends DaoEntity, D extends DaoEntity> extends Panel {

	private static final long serialVersionUID = 1L;

	public static final String HEADER = "header";
	public static final String FORM = "form";
	public static final String ROWS_CONTAINER = "rowsContainer";
	public static final String ROWS = "rows";
	public static final String ROW = "row";

	@SpringBean
	protected DaoSupport<T> daoSupport;

	private WebMarkupContainer webMarkupContainer;
	private ListView<D> details;

	public DetailsPanel(String id, IModel<T> model) {
		super(id, model);
		setOutputMarkupId(true);

		webMarkupContainer = new WebMarkupContainer(ROWS_CONTAINER);
		webMarkupContainer.setOutputMarkupId(true);
		add(webMarkupContainer);

		webMarkupContainer.add(makeDetailFormPanel(FORM).setVisible(isEditable()));
		webMarkupContainer.add(makeDetailHeaderPanel(HEADER));

		LoadableDetachableModel<List<D>> listModel = new LoadableDetachableModel<List<D>>() {
			private static final long serialVersionUID = 1L;
			@Override
			protected List<D> load() {
				return getDetails();
			}
		};

		details = new ListView<D>(ROWS, listModel) {
			private static final long serialVersionUID = 1L;
			@Override
			protected ListItem<D> newItem(int index, IModel<D> itemModel) {
				return new OddEvenListItem<D>(index, itemModel);
			}
			@Override
			protected void populateItem(ListItem<D> item) {
				item.setModel(DaoEntityModelFactory.make(item.getModelObject()));
				item.add(makeDetailRowPanel(ROW, item.getModel()));
			}
		};

		webMarkupContainer.add(details);
	}

	protected abstract Panel makeDetailHeaderPanel(String id);
	protected abstract Panel makeDetailFormPanel(String id);
	protected abstract Panel makeDetailRowPanel(String id, IModel<D> model);
	protected abstract List<D> getDetails();
	protected abstract void update(AjaxRequestTarget target, D detail);
	protected abstract void delete(AjaxRequestTarget target, D detail);
	protected abstract boolean isEditable();

	protected void updateDisplay(AjaxRequestTarget target) {
		details.detachModels();
		target.add(webMarkupContainer);
	}

	@SuppressWarnings("unchecked")
	public T getDaoEntity() {
		return (T) getDefaultModelObject();
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(OnDomReadyHeaderItem.forScript(JSUtils.INITIALIZE));
	}

	public int getSize() {
		return getDetails().size();
	}
}
