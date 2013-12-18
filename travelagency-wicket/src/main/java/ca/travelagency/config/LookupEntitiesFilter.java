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
package ca.travelagency.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.WordUtils;

import ca.travelagency.components.dataprovider.Filter;
import ca.travelagency.components.dataprovider.FilterHelper;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.persistence.query.OrderBy;
import ca.travelagency.utils.StringUtils;

public class LookupEntitiesFilter implements Filter {
	private static final long serialVersionUID = 1L;

	private Class<?> clazz;
	private String name;

	public LookupEntitiesFilter(Class<?> clazz) {
		Validate.notNull(clazz);
		this.clazz = clazz;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = WordUtils.capitalize(StringUtils.lowerCase(name));
	}

	private List<Condition> getAndConditions() {
		List<Condition> results = new ArrayList<Condition>();
		String value = getName();
		if (StringUtils.isNotEmpty(value)) {
			results.add(Condition.likeCapitalize(DaoEntity.PROPERTY_NAME, value));
		}
		return results;
	}

	@Override
	public String getId() {
		return LookupEntitiesFilter.class.getName() + ":" + clazz.getName();
	}

	@Override
	public Criteria getCriteria(OrderBy orderBy) {
		return Criteria.make(clazz).addAndConditions(getAndConditions()).addOrderBy(FilterHelper.getOrderBy(orderBy));
	}

}
