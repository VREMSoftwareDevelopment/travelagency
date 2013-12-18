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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.DaoSupport;
import ca.travelagency.persistence.query.Condition;
import ca.travelagency.persistence.query.Criteria;
import ca.travelagency.utils.StringUtils;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

@Repository
public class ParameterRepositoryImpl implements ParameterRepository {
	@Autowired
	private DaoSupport<Parameter> daoSupport;

	@Override
	public String getDefaultDeparturePlace() {
		return getDefaultParameter(ParameterName.DeparturePlace);
	}

	@Override
	public String getDefaultTravelDocumentType() {
		return getDefaultParameter(ParameterName.TravelDocumentType);
	}

	@Override
	public List<String> getDefaultInvoiceNotes() {
		return getDefaultParameters(ParameterName.InvoiceNote);
	}

	private String getDefaultParameter(ParameterName parameterName) {
		List<String> results = getDefaultParameters(Condition.equals(DaoEntity.PROPERTY_NAME, parameterName.name()));
		return results.isEmpty() ? StringUtils.EMPTY : results.get(0);
	}

	private List<String> getDefaultParameters(ParameterName parameterName) {
		return getDefaultParameters(Condition.like(DaoEntity.PROPERTY_NAME, parameterName.name()));
	}

	private List<String> getDefaultParameters(Condition condition) {
		Criteria criteria = Criteria.make(Parameter.class).addAndCondition(condition);
		List<Parameter> parameters = daoSupport.find(criteria);
		return Lists.newArrayList(Collections2.transform(parameters, new Transform()));
	}

	private class Transform implements Function<Parameter, String> {
		@Override
		public String apply(Parameter parameter) {
			return parameter.getValue();
		}
	}
}