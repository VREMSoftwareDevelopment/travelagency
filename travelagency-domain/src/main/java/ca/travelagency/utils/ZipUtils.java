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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.google.common.base.Charsets;
import com.google.common.io.Closeables;

public abstract class ZipUtils {
	private static final int BUFFER_SIZE = 1024;

	public static byte[] compress(String source) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			byte[] bytes = source.getBytes(Charsets.UTF_8);
			Deflater deflater = new Deflater();
			deflater.setInput(bytes);
			deflater.finish();

			byteArrayOutputStream = new ByteArrayOutputStream(bytes.length);
			byte[] buffer = new byte[1024];
			while (!deflater.finished()) {
				int count = deflater.deflate(buffer);
				byteArrayOutputStream.write(buffer, 0, count);
			}
			return byteArrayOutputStream.toByteArray();
		} finally {
			Closeables.close(byteArrayOutputStream, true);
		}
	}

	public static String uncompress(byte[] source) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			Inflater inflater = new Inflater();
			inflater.setInput(source);

			byteArrayOutputStream = new ByteArrayOutputStream(source.length);
			byte[] buffer = new byte[BUFFER_SIZE];
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				byteArrayOutputStream.write(buffer, 0, count);
			}
			return new String(byteArrayOutputStream.toByteArray(), Charsets.UTF_8);
		} catch (DataFormatException e) {
			throw new IOException(e);
		} finally {
			Closeables.close(byteArrayOutputStream, true);
		}
	}
}
