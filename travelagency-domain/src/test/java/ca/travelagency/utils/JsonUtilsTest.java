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

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.utils.JsonUtils.ByteArrayDeserializer;
import ca.travelagency.utils.JsonUtils.ByteArraySerializer;
import ca.travelagency.utils.JsonUtils.DateDeserializer;
import ca.travelagency.utils.JsonUtils.DateSerializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;


public class JsonUtilsTest {
	private JsonUtils fixture;
	private SystemUser systemUser;

	@Before
	public void setUp() throws Exception {
		fixture = new JsonUtils();
		systemUser = SystemUserHelper.makeSystemUserWithRole();
	}

	@Test
	public void testJsonWithString() throws Exception {
		// execute
		String expectedJson = fixture.serialize(systemUser);
		SystemUser actual = fixture.deserialize(expectedJson, SystemUser.class);
		String actualJson = fixture.serialize(actual);
		// validate
		Assert.assertNotSame(systemUser, actual);
		Assert.assertEquals(systemUser, actual);
		Assert.assertEquals(expectedJson, actualJson);
	}

	@Test
	public void testJsonWithReaderAndWriter() throws Exception {
		// setup
		String expectedJson = fixture.serialize(systemUser);
		StringReader reader = new StringReader(expectedJson);
		StringWriter writer = new StringWriter();
		// execute
		SystemUser actual = fixture.deserialize(reader, SystemUser.class);
		fixture.serialize(actual, writer);
		// validate
		Assert.assertNotSame(systemUser, actual);
		Assert.assertEquals(systemUser, actual);
		StringBuffer actualJson = writer.getBuffer();
		Assert.assertEquals(expectedJson, actualJson.toString());
	}

	@Test
	public void testJsonWithEmptyReader() throws Exception {
		// setup
		StringReader reader = new StringReader(StringUtils.EMPTY);
		// execute
		SystemUser actual = fixture.deserialize(reader, SystemUser.class);
		// validate
		Assert.assertNull(actual);
	}

	@Test
	public void testDateDeserializeWithNull() throws Exception {
		// setup
		DateDeserializer deserializer = new DateDeserializer();
		// execute
		Date actual = deserializer.deserialize(null, null, null);
		// validate
		Assert.assertNull(actual);
	}

	@Test
	public void testDateDeserializeWithDate() throws Exception {
		// setup
		DateDeserializer deserializer = new DateDeserializer();
		Date expected = new Date();
		JsonPrimitive jsonPrimitive = new JsonPrimitive(expected.getTime());
		// execute
		Date actual = deserializer.deserialize(jsonPrimitive, null, null);
		// validate
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testJsonWithCompression() throws Exception {
		// execute
		String expectedJson = fixture.serializeUsingCompression(systemUser);
		SystemUser actual = fixture.deserializeUsingCompression(expectedJson, SystemUser.class);
		String actualJson = fixture.serializeUsingCompression(actual);
		// validate
		Assert.assertNotSame(systemUser, actual);
		Assert.assertEquals(systemUser, actual);
		Assert.assertEquals(expectedJson, actualJson);
	}

	@Test
	public void testDateSerializeWithNull() throws Exception {
		// setup
		DateSerializer serializer = new DateSerializer();
		// execute
		JsonElement actual = serializer.serialize(null, null, null);
		// validate
		Assert.assertNull(actual);
	}

	@Test
	public void testDateSerializeWithDate() throws Exception {
		// setup
		DateSerializer serializer = new DateSerializer();
		Date expected = new Date();
		// execute
		JsonElement actual = serializer.serialize(expected, null, null);
		// validate
		Assert.assertEquals(expected.getTime(), actual.getAsLong());
	}

	@Test
	public void testByteArrayDeserializeWithNull() throws Exception {
		// setup
		DateDeserializer deserializer = new DateDeserializer();
		// execute
		Date actual = deserializer.deserialize(null, null, null);
		// validate
		Assert.assertNull(actual);
	}

	@Test
	public void testByteArrayDeserializeWithByteArray() throws Exception {
		// setup
		ByteArrayDeserializer deserializer = new ByteArrayDeserializer();
		String expected = "This is a test";
		JsonPrimitive jsonPrimitive = new JsonPrimitive(Base64Utils.encode(expected.getBytes()));
		// execute
		byte[] actual = deserializer.deserialize(jsonPrimitive, null, null);
		// validate
		Assert.assertArrayEquals(expected.getBytes(), actual);
		Assert.assertEquals(expected, new String(actual));
	}

	@Test
	public void testByteArraySerializeWithNull() throws Exception {
		// setup
		ByteArraySerializer serializer = new ByteArraySerializer();
		// execute
		JsonElement actual = serializer.serialize(null, null, null);
		// validate
		Assert.assertNull(actual);
	}

	@Test
	public void testByteArraySerializeWithByteArray() throws Exception {
		// setup
		ByteArraySerializer serializer = new ByteArraySerializer();
		String expected = "This is a test";
		// execute
		JsonElement actual = serializer.serialize(expected.getBytes(), null, null);
		// validate
		Assert.assertEquals(Base64Utils.encode(expected.getBytes()), actual.getAsString());
	}

}
