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

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ca.travelagency.persistence.DatabaseTester;


public class ParameterRepositoryImplTest extends DatabaseTester<Parameter> {
	@Autowired
	private ParameterRepository parameterRepository;

	@Test
	public void testDeparturePlaceValue() throws Exception {
		// execute
		String actual = parameterRepository.getDefaultDeparturePlace();
		// validate
		Assert.assertEquals("Toronto", actual);
	}

	@Test
	public void testTravelDocumentTypeValue() throws Exception {
		// execute
		String actual = parameterRepository.getDefaultTravelDocumentType();
		// validate
		Assert.assertEquals("Canadian Passport", actual);
	}

	@Test
	public void testInvoiceNotesValue() throws Exception {
		// execute
		List<String> actual = parameterRepository.getDefaultInvoiceNotes();
		// validate
		Assert.assertEquals(2, actual.size());
		Assert.assertEquals("<pre><span style=\"font-size:larger; font-weight:bold;\">Insurance - Medical                            DECLINED</span></pre>",
					actual.get(0));
		Assert.assertEquals("<pre><span style=\"font-size:larger; font-weight:bold;\">Insurance - Cancellation/Interruption          DECLINED</span></pre>",
					actual.get(1));
	}



}
