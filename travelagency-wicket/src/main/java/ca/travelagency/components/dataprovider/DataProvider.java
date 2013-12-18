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
package ca.travelagency.components.dataprovider;

import java.util.Iterator;

import org.apache.commons.lang3.Validate;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.DaoSupport;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.persistence.query.OrderBy;

public class DataProvider<T extends DaoEntity> extends SortableDataProvider<T, String> {
	private static final long serialVersionUID = 1L;

	@SpringBean
	private DaoSupport<T> daoSupport;

	private IModel<? extends Filter> model;

	public DataProvider(IModel<? extends Filter> model) {
		Validate.notNull(model);
		this.model = model;
		Injector.get().inject(this);
	}

	@Override
	public Iterator<T> iterator(long first, long count) {
		Criteria criteria = getCriteria().setOffset(first).setCount(count);
		return daoSupport.find(criteria).iterator();
	}

	Criteria getCriteria() {
		return model.getObject().getCriteria(getOrderBy());
	}

	private OrderBy getOrderBy() {
		SortParam<String> sortParam = getSort();
		return sortParam == null ? null : OrderBy.make(sortParam.getProperty(), sortParam.isAscending());
	}

	@Override
	public long size() {
		return daoSupport.count(getCriteria()).longValue();
	}

	@Override
	public void detach() {
	}

	@Override
	public IModel<T> model(T object) {
		return DaoEntityModelFactory.make(object, object.getTrueClass());
	}
}