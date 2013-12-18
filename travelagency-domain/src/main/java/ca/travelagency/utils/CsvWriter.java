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

public class CsvWriter {
	private StringBuilder stringBuilder;
	private boolean first = true;

	public CsvWriter() {
		stringBuilder = new StringBuilder();
	}

	public CsvWriter write(Object value) {
		if (!first) {
			stringBuilder.append(",");
		}
		stringBuilder.append("\"");
		if (value != null) {
			stringBuilder.append(value.toString().replace("\"", "\"\"").replace("\n", " "));
		}
		stringBuilder.append("\"");
		first = false;
		return this;
	}

	public CsvWriter endLine() {
		stringBuilder.append("\r\n");
		first = true;
		return this;
	}

	@Override
	public String toString() {
		return stringBuilder.toString();
	}
}
