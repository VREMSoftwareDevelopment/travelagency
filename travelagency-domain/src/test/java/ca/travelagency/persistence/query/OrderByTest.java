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

import org.junit.Assert;
import org.junit.Test;

public class OrderByTest {

	@Test
	public void testOrderByAsSqlAscending() {
		OrderBy orderBy = OrderBy.make("property");
		String actual = orderBy.toStringAsSql();
		Assert.assertEquals("property ASC", actual);
	}

	@Test
	public void testOrderByAsSqlDescending() {
		OrderBy orderBy = OrderBy.make("property", false);
		String actual = orderBy.toStringAsSql();
		Assert.assertEquals("property DESC", actual);
	}

}
