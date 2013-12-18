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

import java.util.List;

import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.query.OrderBy;

import com.google.common.collect.Lists;

public class FilterHelper {
	public static List<OrderBy> getOrderBy(OrderBy orderBy) {
		return getOrderBy(orderBy, OrderBy.make(DaoEntity.PROPERTY_NAME));
	}

	public static List<OrderBy> getOrderBy(OrderBy orderBy, OrderBy defaultOrderBy) {
		if (orderBy == null) {
			orderBy = defaultOrderBy;
		}
		if (DaoEntity.PROPERTY_ID.equals(orderBy.getProperty())) {
			return Lists.newArrayList(orderBy);
		}
		return Lists.newArrayList(orderBy, OrderBy.make(DaoEntity.PROPERTY_ID, orderBy.isAscending()));
	}
}
