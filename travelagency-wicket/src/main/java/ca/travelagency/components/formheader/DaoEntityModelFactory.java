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

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import ca.travelagency.persistence.DaoEntity;

public class DaoEntityModelFactory {
	public static <T extends DaoEntity> boolean isPersisted(T daoEntity) {
		return daoEntity != null && daoEntity.getId() != null;
	}
	public static <T extends DaoEntity> IModel<T> make(T daoEntity, Class<?> clazz) {
		return new CompoundPropertyModel<T>(DaoEntityModel.make(daoEntity, clazz));
	}
	public static <T extends DaoEntity> IModel<T> make(T daoEntity) {
		return make(daoEntity, daoEntity.getTrueClass());
	}
	public static <T extends DaoEntity> IModel<T> make(Class<?> clazz) {
		return make(null, clazz);
	}
}
