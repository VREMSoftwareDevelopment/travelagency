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
package ca.travelagency.persistence.query;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.Validate;

public abstract class QuerySQL {
	public static QuerySQL count(Criteria criteria, EntityManager entityManager) {
		return new QuerySQL(criteria, entityManager) {
			@Override
			protected String sqlAsString() {
				return SELECT+COUNT_START+simpleName+COUNT_END+from()+where();
			}
		};
	}

	public static QuerySQL find(Criteria criteria, EntityManager entityManager) {
		return new QuerySQL(criteria, entityManager) {
			@Override
			protected String sqlAsString() {
				return SELECT+DISTINCT+simpleName+from()+where()+orderBy();
			}
			@Override
			protected void addLimits(Query query) {
				if (criteria.getCount() != 0) {
					// FIXME JPA Query uses int we use long
					query.setFirstResult((int) criteria.getOffset());
					query.setMaxResults((int) criteria.getCount());
				}
			}
		};
	}

	public static QuerySQL groupBy(Criteria criteria, EntityManager entityManager) {
		return new QuerySQL(criteria, entityManager) {
			@Override
			protected String sqlAsString() {
				String groupBy = criteria.getGroupBy();
				Validate.notBlank(groupBy, "GroupBy query requires group by to be specified");
				return SELECT+DISTINCT+simpleName+Condition.SEPARATOR+groupBy+from()+where()+groupBy()+orderBy();
			}
			@Override
			protected void addLimits(Query query) {
				if (criteria.getCount() != 0) {
					// FIXME JPA Query uses int we use long
					query.setMaxResults((int) criteria.getCount());
				}
			}
		};
	}


	private static final String SELECT = "SELECT ";
	private static final String DISTINCT = "DISTINCT ";
	private static final String FROM = " FROM ";
	private static final String COUNT_START = "COUNT("+DISTINCT;
	private static final String COUNT_END = ")";

	protected Criteria criteria;
	protected String simpleName;
	private EntityManager entityManager;

	private QuerySQL(Criteria criteria, EntityManager entityManager) {
		Validate.notNull(criteria);
		Validate.notNull(entityManager);
		this.criteria = criteria;
		this.simpleName = criteria.getClazz().getSimpleName();
		this.entityManager = entityManager;
	}

	protected String from() {
		return FROM + criteria.getClazz().getName() + " " + simpleName + criteria.from(simpleName);
	}
	protected String where() {
		return criteria.where(simpleName).getWhereAsSql();
	}
	protected String orderBy() {
		return criteria.getOrderByAsSql(simpleName);
	}
	protected String groupBy() {
		return criteria.getGroupByAsSql(simpleName);
	}

	public Query makeQuery() {
		String sqlAsString = sqlAsString();
		Query query = entityManager.createQuery(sqlAsString);
		addLimits(query);
		addParameters(query);
//		displaySql();
		return query;
	}

	void displaySql() {
		System.out.println("SQL:["+sqlAsString()+"]");
		List<Object> parameters = criteria.where(simpleName).getParameters();
		int index = 0;
		System.out.print("PARAMETERS:[");
		for (Object parameter : parameters) {
			System.out.print(""+index+":"+parameter+" ");
		}
		System.out.println("]");
	}

	protected abstract String sqlAsString();
	protected void addLimits(Query query) {}

	private void addParameters(Query query) {
		Where where = criteria.where(simpleName);
		List<Object> parameters = where.getParameters();
		int index = 0;
		for (Object parameter : parameters) {
			query.setParameter(index++, parameter);
		}
	}

}
