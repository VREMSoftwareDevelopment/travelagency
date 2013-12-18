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

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class DateUtilsTest {

	private Date date;

	@Before
	public void setUp() {
		date = DateUtils.setMilliseconds(new Date(), 0);
		date = DateUtils.setMilliseconds(date, 0);
		date = DateUtils.setSeconds(date, 0);
		date = DateUtils.setMinutes(date, 15);
		date = DateUtils.setHours(date, 14);
		date = DateUtils.setDays(date, 21);
		date = DateUtils.setMonths(date, 6);
		date = DateUtils.setYears(date, 2011);
	}

	@Test
	public void testFormatDate() {
		Assert.assertEquals("July 21, 2011", DateUtils.formatDate(date));
		Assert.assertEquals("21/07/11", DateUtils.formatDateShort(date));
		Assert.assertEquals("2011 Jul", DateUtils.formatDateAsMonth(date));
		Assert.assertEquals("July 21, 2011 2:15:00 EDT PM", DateUtils.formatDateTime(date));
	}

	@Test
	public void testFormatDateCustom() {
		Assert.assertEquals("Jul 21, 2011", DateUtils.formatDateCustom(date));
		Assert.assertEquals(DateUtils.truncate(date, Calendar.DAY_OF_MONTH), DateUtils.parseDateCustom("Jul 21, 2011"));
	}
}
