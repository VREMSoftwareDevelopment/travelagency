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
package ca.travelagency.components.formheader;

import org.apache.commons.lang3.Validate;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.DaoSupport;

public class DaoEntityModel<T extends DaoEntity> extends LoadableDetachableModel<T> {
	private static final long serialVersionUID = 1L;

	public static <T extends DaoEntity> DaoEntityModel<T> make(T daoEntity, Class<?> clazz) {
		DaoEntityModel<T> daoEntityModel = new DaoEntityModel<T>();
		if (daoEntity == null) {
			daoEntityModel.setClazz(clazz);
			daoEntityModel.newInstance = createNewDaoEntity(clazz);
		} else {
			daoEntityModel.setClazz(daoEntity.getTrueClass());
			if (daoEntity.getId() == null) {
				daoEntityModel.newInstance = daoEntity;
			} else {
				daoEntityModel.setId(daoEntity.getId());
			}
		}
		return daoEntityModel;
	}

	@SuppressWarnings("unchecked")
	private static <T> T createNewDaoEntity(Class<?> clazz) {
		try {
			return (T) clazz.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Can not create object of class: " + clazz.getName(), e);
		}
	}

	@SpringBean
	private DaoSupport<T> daoSupport;

	private Long id;
	private Class<?> clazz;
	private T newInstance;

	private DaoEntityModel() {
		super();
        Injector.get().inject(this);
	}

	private void setClazz(Class<?> clazz) {
		Validate.notNull(clazz);
		this.clazz = clazz;
	}

	private void setId(Long id) {
		Validate.notNull(id);
		this.id = id;
	}

	@Override
	protected T load() {
    	return isPersistedDaoEntity() ? (T) daoSupport.find(clazz, id) : newInstance;
	}

	public boolean isPersistedDaoEntity() {
		if (id != null) {
			return true;
		}
		if (newInstance != null && newInstance.getId() != null) {
			setId(newInstance.getId());
			newInstance = null;
			return true;
		}
		return false;
	}

	T getNewInstance() {
		return newInstance;
	}
}