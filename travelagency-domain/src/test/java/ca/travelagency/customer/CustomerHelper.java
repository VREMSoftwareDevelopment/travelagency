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
package ca.travelagency.customer;



public class CustomerHelper {
	public static final String PREFIX = "Customer";
	public static final String LAST_NAME = PREFIX+"LastName";
	public static final String FIRST_NAME = PREFIX+"FirstName";
	public static final String PRIMARY_PHONE = "111-111-1111";

	private static long count = 0;

	public static Customer makeCustomer() {
		Customer customer = makeCustomerWithoutId();
		customer.setId(count);
		return customer;
	}

	public static long getCount() {
		return count;
	}

	public static void setCount(long count) {
		CustomerHelper.count = count;
	}

	public static Customer makeCustomerWithoutId() {
		count--;
		Customer customer = Customer.make(FIRST_NAME+count, LAST_NAME+count, PRIMARY_PHONE);
		customer.setSalutation("Mr");
		customer.setCompanyName(PREFIX+"CompanyName"+count);
		customer.setAddress(PREFIX+"Address"+count);
		customer.setCity(PREFIX+"city"+count);
		customer.setProvince(PREFIX+"province"+count);
		customer.setPostalCode(PREFIX+"postalCode"+count);
		customer.setCountry(PREFIX+"country"+count);
		customer.setEmail(PREFIX+"email"+count+"@email.com");
		customer.setTravelDocumentNumber(PREFIX+"TravelDocumentNumber"+count);
		customer.setTravelDocumentType(PREFIX+"TravelDocumentType"+count);
		customer.setNotes("Notes....");
		return customer;
	}

	public static Traveler makeTravelerWithoutId() {
		count--;
		Traveler traveler = Traveler.make(FIRST_NAME+count, LAST_NAME+count);
		traveler.setSalutation("Mr");
		traveler.setDocumentNumber(PREFIX+"TravelDocumentNumber"+count);
		traveler.setDocumentType(PREFIX+"TravelDocumentType"+count);
		return traveler;
	}

	public static Traveler makeTraveler() {
		Traveler traveler = makeTravelerWithoutId();
		traveler.setId(count);
		return traveler;
	}
}
