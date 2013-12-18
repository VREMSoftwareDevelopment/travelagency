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
package ca.travelagency.config;


public class ConfigHelper {
	private static long count = 0;

	public static Company makeCompany() {
		count--;
		Company company = new Company();
		company.setId(count);
		company.setName("Travel Company");
		company.setAddress("Address");
		company.setCity("City");
		company.setProvince("Province");
		company.setPostalCode("Postal Code");
		company.setCountry("Country");
		company.setEmail("travel.company@email.com");
		company.setPhoneNumber("111-111-1111");
		company.setFaxNumber("222-222-2222");
		company.setTaxNumber("TAX-1234567890");
		company.setTicoNumber("TICO-1234567890");
		return company;
	}

	public static Province makeProvince() {
		Province province = makeProvinceWithoutId();
		province.setId(count);
		return province;
	}

	public static Province makeProvinceWithoutId() {
		count--;
		return Province.make("Province"+count);
	}

	public static City makeCity() {
		City city = makeCityWithoutId();
		city.setId(count);
		return city;
	}

	public static City makeCityWithoutId() {
		count--;
		return City.make("City"+count);
	}

	public static Product makeProduct() {
		Product product = makeProductWithoutId();
		product.setId(count);
		return product;
	}

	public static Product makeProductWithoutId() {
		count--;
		return Product.make("Product"+count);
	}

	public static Supplier makeSupplier() {
		Supplier supplier = makeSupplierWithoutId();
		supplier.setId(count);
		return supplier;
	}

	public static Supplier makeSupplierWithoutId() {
		count--;
		return Supplier.make("Supplier"+count);
	}

}
