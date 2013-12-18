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

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;


public class MoneyUtilsTest {

	@Test
	public void testFormat() {
		Assert.assertEquals(StringUtils.EMPTY, MoneyUtils.format(null));

		Assert.assertEquals("$100.12", MoneyUtils.format(new BigDecimal(100.12)));
		Assert.assertEquals("$100.00", MoneyUtils.format(new BigDecimal(100)));
		Assert.assertEquals("$1,200.23", MoneyUtils.format(new BigDecimal(1200.23)));

		Assert.assertEquals("$100.12", MoneyUtils.format(100.12));
		Assert.assertEquals("$100.00", MoneyUtils.format(100));
		Assert.assertEquals("$1,200.23", MoneyUtils.format(1200.23));
	}

	@Test
	public void testConvert() {
		Assert.assertEquals(new BigDecimal("100.12"), MoneyUtils.round(100.124));
		Assert.assertEquals(new BigDecimal("100.13"), MoneyUtils.round(100.125));

		Assert.assertEquals(new BigDecimal("100.12"), MoneyUtils.round(new BigDecimal("100.124")));
		Assert.assertEquals(new BigDecimal("100.13"), MoneyUtils.round(new BigDecimal("100.125")));

		Assert.assertEquals(MoneyUtils.ZERO_VALUE, MoneyUtils.round(null));
	}

	@Test
	public void testMultiply() {
		Assert.assertEquals(new BigDecimal("1001.24"), MoneyUtils.multiply(new BigDecimal("100.124"), Integer.valueOf(10)));

		Assert.assertEquals(MoneyUtils.ZERO_VALUE, MoneyUtils.multiply(null, null));
		Assert.assertEquals(MoneyUtils.ZERO_VALUE, MoneyUtils.multiply(new BigDecimal("100.124"), null));
		Assert.assertEquals(MoneyUtils.ZERO_VALUE, MoneyUtils.multiply(null, Integer.valueOf(10)));
	}

	@Test
	public void testAdd() {
		Assert.assertEquals(new BigDecimal("300.30"), MoneyUtils.add(new BigDecimal("100.10"), new BigDecimal("200.20")));
		Assert.assertEquals(MoneyUtils.ZERO_VALUE, MoneyUtils.add(null, null));
		Assert.assertEquals(new BigDecimal("100.12"), MoneyUtils.add(new BigDecimal("100.12"), null));
		Assert.assertEquals(new BigDecimal("100.20"), MoneyUtils.add(null, new BigDecimal("100.20")));
	}

}
