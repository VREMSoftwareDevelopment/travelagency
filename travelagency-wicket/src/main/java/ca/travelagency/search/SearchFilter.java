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
package ca.travelagency.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;

import ca.travelagency.components.dataprovider.Filter;
import ca.travelagency.components.dataprovider.FilterHelper;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.persistence.query.OrderBy;
import ca.travelagency.utils.StringUtils;

public abstract class SearchFilter<T extends DaoEntity> implements Filter {
	private static final long serialVersionUID = 1L;

	public enum Properties {
		searchText
	}

	private Class<T> clazz;
	private String searchText;

	public SearchFilter(Class<T> clazz) {
		Validate.notNull(clazz);
		this.clazz = clazz;
	}

	public String getSearchText() {
		return searchText;
	}
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	List<Condition> getOrConditions() {
		List<Condition> results = new ArrayList<Condition>();
		String value = getSearchText();
		if (StringUtils.isBlank(value)) {
			return results;
		}
		addConditions(results, value);
		return results;
	}

	@Override
	public Criteria getCriteria(OrderBy orderBy) {
		return Criteria.make(clazz)
				.addOrConditions(getOrConditions())
				.addOrderBy(FilterHelper.getOrderBy(orderBy, getDefaultOrderBy()));

	}

	protected OrderBy getDefaultOrderBy() { return OrderBy.make(DaoEntity.PROPERTY_ID); }
	protected abstract void addConditions(List<Condition> conditions, String value);

}
