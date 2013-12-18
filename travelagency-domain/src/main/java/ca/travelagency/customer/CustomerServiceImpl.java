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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.travelagency.persistence.DaoSupport;

@Repository
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private DaoSupport<Customer> daoSupport;

	@Override
	public boolean duplicated(Customer customer) {
		return !daoSupport.find(DuplicateCriteria.make(customer)).isEmpty();
	}

	void setDaoSupport(DaoSupport<Customer> daoSupport) {
		this.daoSupport = daoSupport;
	}

}