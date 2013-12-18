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

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.Validate;

class Where implements Serializable {
	private static final long serialVersionUID = 1L;

	static Where make(String whereAsSql, List<Object> parameters) {
		Validate.notNull(whereAsSql);
		Validate.notNull(parameters);
		Where result = new Where();
		result.whereAsSql = whereAsSql;
		result.parameters = parameters;
		return result;
	}

	private String whereAsSql;
	private List<Object> parameters;

	private Where() {
	}

	String getWhereAsSql() {
		return whereAsSql;
	}

	List<Object> getParameters() {
		return Collections.unmodifiableList(parameters);
	}

}
