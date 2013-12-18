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
package ca.travelagency.persistence;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Condition.Operator;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.persistence.query.QuerySQL;
import ca.travelagency.utils.StringUtils;


@Repository
public class DaoSupportImpl<T extends DaoEntity> implements DaoSupport<T> {

	@PersistenceContext
	private EntityManager entityManager;

	@Transactional
	@Override
	public void persist(T daoEntity) {
		Validate.notNull(daoEntity);
		entityManager.persist(daoEntity);
	}

	@Transactional
	@Override
	public void merge(T daoEntity) {
		Validate.notNull(daoEntity);
		entityManager.merge(daoEntity);
	}

	@Transactional
	@Override
	public void remove(T daoEntity) {
		Validate.notNull(daoEntity);
		Object reference = entityManager.getReference(daoEntity.getTrueClass(), daoEntity.getId());
		entityManager.remove(reference);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T find(Class<?> type, Long id) {
		Validate.notNull(type);
		Validate.notNull(id);
		return (T) entityManager.find(type, id);
	}

	@Override
	public Long count(DaoCriteria daoCriteria) {
		Query query = QuerySQL.count(daoCriteria.getCriteria(), entityManager).makeQuery();
		Number count = (Number) query.getSingleResult();
		return count.longValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> find(DaoCriteria daoCriteria) {
		Query query = QuerySQL.find(daoCriteria.getCriteria(), entityManager).makeQuery();
		return (List<T>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> groupBy(DaoCriteria daoCriteria) {
		Query query = QuerySQL.groupBy(daoCriteria.getCriteria(), entityManager).makeQuery();
		return (List<String>) query.getResultList();
	}

	@Override
	public boolean duplicated(T daoEntity) {
		Criteria criteria = Criteria.make(daoEntity.getTrueClass());
		String name = daoEntity.getName();
		if (StringUtils.isNotEmpty(name)) {
			criteria.addAndCondition(Condition.make(DaoEntity.PROPERTY_NAME, Operator.EQUAL, name));
		}
		Serializable id = daoEntity.getId();
		if (id != null) {
			criteria.addAndCondition(Condition.make(DaoEntity.PROPERTY_ID, Operator.NOTEQUAL, id));
		}
		List<T> results = find(criteria);
		return !results.isEmpty();
	}

	@Override
	public List<T> find(Class<?> clazz) {
		return find(Criteria.make(clazz));
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}

}
