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

import java.util.ArrayList;
import java.util.List;

import ca.travelagency.components.dataprovider.Filter;
import ca.travelagency.components.dataprovider.FilterHelper;
import ca.travelagency.customer.Traveler;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.persistence.query.OrderBy;
import ca.travelagency.utils.StringUtils;

public class TravelerFilter implements Filter {
	private static final long serialVersionUID = 1L;

	static final OrderBy DEFAULT_ORDER_BY = OrderBy.make(Traveler.Properties.lastName.name());

	public enum Properties {
		searchText,
	}

	private String searchText;

	public TravelerFilter() {
	}

	public TravelerFilter(String searchText) {
		setSearchText(searchText);
	}

	public String getSearchText() {
		return searchText;
	}
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	private List<Condition> getOrConditions() {
		List<Condition> results = new ArrayList<Condition>();
		String value = getSearchText();
		if (StringUtils.isBlank(value)) {
			return results;
		}
		results.add(Condition.likeCapitalize(Traveler.Properties.firstName.name(), value));
		results.add(Condition.likeCapitalize(Traveler.Properties.lastName.name(), value));
		return results;
	}

	@Override
	public String getId() {
		return TravelerFilter.class.getName();
	}
	@Override
	public Criteria getCriteria(OrderBy orderBy) {
		return Criteria.make(Traveler.class)
			.addOrConditions(getOrConditions())
			.addOrderBy(FilterHelper.getOrderBy(orderBy, DEFAULT_ORDER_BY));
	}
}
