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

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JsonUtils {
	private Gson gson;

	public JsonUtils() {
		gson = new GsonBuilder()
		   .registerTypeAdapter(Date.class, new DateSerializer())
		   .registerTypeAdapter(Date.class, new DateDeserializer())
		   .registerTypeAdapter(byte[].class, new ByteArraySerializer())
		   .registerTypeAdapter(byte[].class, new ByteArrayDeserializer())
		   .create();
	}

	public <T> T deserialize(String json, Class<T> clazz) {
		return gson.fromJson(json, clazz);
	}

	public <T> T deserialize(Reader json, Class<T> clazz) {
		return gson.fromJson(json, clazz);
	}

	public <T> String serialize(T object) {
		return gson.toJson(object);
	}
	public <T> void serialize(T object, Writer writer) throws IOException {
		gson.toJson(object, writer);
		writer.flush();
	}

	public <T> T deserializeUsingCompression(String json, Class<T> clazz) throws IOException {
		byte[] decode = Base64Utils.decode(json);
		String uncompress = ZipUtils.uncompress(decode);
		return gson.fromJson(uncompress, clazz);
	}

	public <T> String serializeUsingCompression(T object) throws IOException {
		String json = gson.toJson(object);
		byte[] compress = ZipUtils.compress(json);
		return Base64Utils.encode(compress);
	}

	public static class ByteArrayDeserializer implements JsonDeserializer<byte[]> {
		@Override
		public byte[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		    try {
				return json == null ? null : Base64Utils.decode(json.getAsString());
			} catch (IOException e) {
				throw new JsonParseException(e);
			}
		}
	}

	public static class ByteArraySerializer implements JsonSerializer<byte[]> {
		@Override
		public JsonElement serialize(byte[] src, Type typeOfSrc, JsonSerializationContext context) {
			return src == null ? null : new JsonPrimitive(Base64Utils.encode(src));
		}
	}

	public static class DateDeserializer implements JsonDeserializer<Date> {
		@Override
		public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		    return json == null ? null : new Date(json.getAsLong());
		}
	}

	public static class DateSerializer implements JsonSerializer<Date> {
		@Override
		public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
			return src == null ? null : new JsonPrimitive(src.getTime());
		}
	}
}
