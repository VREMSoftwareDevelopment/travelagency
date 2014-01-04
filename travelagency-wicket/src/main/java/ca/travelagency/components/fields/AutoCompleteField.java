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

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.DefaultCssAutoCompleteTextField;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.CssReferenceHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.DaoSupport;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.persistence.query.OrderBy;
import ca.travelagency.utils.StringUtils;

import com.google.common.collect.Lists;

public class AutoCompleteField<T extends DaoEntity> extends DefaultCssAutoCompleteTextField<String> {
	private static final long serialVersionUID = 1L;

	public static final int DISPLAY_MAX_SIZE = 10;

	@SpringBean
	private DaoSupport<T> daoSupport;

	private Class<?> clazz;

	public AutoCompleteField(String name, Class<?> clazz) {
		super(name, null);
		Validate.notNull(clazz);
		this.clazz = clazz;
	}

	@Override
	protected Iterator<String> getChoices(String input) {
		return getListOfChoices(input).iterator();
	}

	protected List<String> getListOfChoices(String input) {
		if (StringUtils.isEmpty(input)) {
			return Lists.newArrayList();
		}
		return getResults(input);
	}

	protected List<String> getResults(String input) {
		Criteria criteria = Criteria.make(clazz).setCount(DISPLAY_MAX_SIZE);
		criteria.addAndCondition(Condition.like(getPropertyName(), WordUtils.capitalize(StringUtils.lowerCase(input))));
		criteria.addOrderBy(OrderBy.make(getPropertyName()));
		criteria.setGroupBy(getPropertyName());
		return daoSupport.groupBy(criteria);
	}

	protected String getPropertyName() {
		return DaoEntity.PROPERTY_NAME;
	}

	@Override
	public void renderHead(final IHeaderResponse response) {
		super.renderHead(response);
		CssResourceReference cssResourceReference = new CssResourceReference(AutoCompleteField.class, "AutoCompleteField.css");
		CssReferenceHeaderItem cssReferenceHeaderItem = CssHeaderItem.forReference(cssResourceReference);
		response.render(cssReferenceHeaderItem);
	}

}