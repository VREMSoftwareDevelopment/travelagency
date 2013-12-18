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
package ca.travelagency.components.resources;

import java.io.File;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

import ca.travelagency.BaseWicketTester;

import com.google.common.io.Files;


public class FileImageResourceTest extends BaseWicketTester {

	@Test
	public void testGetImageData() throws Exception {
		// setup
		URL resource = FileImageResourceTest.class.getResource("imagefile.jpg");
		File file = new File(resource.toURI());
		byte[] expected = Files.toByteArray(file);
		FileImageResource fixture = new FileImageResource(file);
		// execute
		byte[] actual = fixture.getImageData(null);
		// validate
		Assert.assertArrayEquals(expected, actual);
	}

}
