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
package ca.travelagency.components.fields;

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.DaoSupport;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.persistence.query.OrderBy;

public class GenericDropDownChoice<T extends DaoEntity> extends DropDownChoice<T>{
	private static final long serialVersionUID = 1L;

	@SpringBean
	private DaoSupport<T> daoSupport;

	private Class<?> clazz;

	public GenericDropDownChoice(String id, Class<?> clazz) {
		super(id);
		Validate.notNull(clazz);
		this.clazz = clazz;
		setChoices(getDaoEntities());
		setChoiceRenderer(makeRenderer());
	}

	IChoiceRenderer<T> makeRenderer() {
		return new IChoiceRenderer<T>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(T daoEntity) {
				return daoEntity.getName();
			}

			@Override
			public String getIdValue(T daoEntity, int index) {
				return "" + daoEntity.getId();
			}
		};
	}

	List<T> getDaoEntities() {
		return daoSupport.find(makeCriteria());
	}

	Criteria makeCriteria() {
		return Criteria.make(clazz).addOrderBy(OrderBy.make(DaoEntity.PROPERTY_NAME));
	}
}
