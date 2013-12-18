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
package ca.travelagency;

import java.net.HttpURLConnection;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.DatabaseTester;
import ca.travelagency.webservice.AuthenticationFilter;

public abstract class WebserviceTester<T extends DaoEntity> extends DatabaseTester<T> {
	@BeforeClass
	public static void setUpClass() throws Exception {
		ServerTester.INSTANCE.start();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
// Let JVM shutdown server
//		ServerTester.INSTANCE.stop();
	}

	protected void setupAuthorization(HttpURLConnection httpURLConnection) {
		httpURLConnection.setRequestProperty(AuthenticationFilter.AUTHORIZATION_KEY, AuthenticationFilter.MAGIC_VALUE);
	}

}
