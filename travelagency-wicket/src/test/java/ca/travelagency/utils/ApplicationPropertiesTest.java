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
package ca.travelagency.utils;

import org.junit.Assert;
import org.junit.Test;

public class ApplicationPropertiesTest {
	@Test
	public void testValidProperties() throws Exception {
		// execute
		ApplicationProperties applicationProperties = ApplicationProperties.make();
		// validate
		Assert.assertEquals("${pom.version}", applicationProperties.getVersion());
		Assert.assertEquals("${pom.name}", applicationProperties.getName());
		Assert.assertEquals("travelagency", applicationProperties.getCode());
		Assert.assertEquals(5, applicationProperties.getSystemUserMax());
	}

	@Test(expected = RuntimeException.class)
	public void testNoPropertyFileExists() throws Exception {
		// execute
		ApplicationProperties.make("xyz");
	}

	@Test
	public void testInvalidProperties() throws Exception {
		// execute
		ApplicationProperties applicationProperties = ApplicationProperties.make("test-application.properties");
		// validate
		Assert.assertNull(applicationProperties.getVersion());
		Assert.assertNull(applicationProperties.getName());
		Assert.assertNull(applicationProperties.getCode());
		Assert.assertEquals(Integer.MAX_VALUE, applicationProperties.getSystemUserMax());
	}
}
