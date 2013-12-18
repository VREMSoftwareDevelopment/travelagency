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
package ca.travelagency.identity;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.travelagency.persistence.DaoSupport;
import ca.travelagency.utils.StringUtils;

@Service
public class SystemUserServiceImpl implements SystemUserService {
	private static final Logger LOGGER = Logger.getLogger(SystemUserServiceImpl.class);

	@Autowired
	private DaoSupport<SystemUser> daoSupport;

	@Override
	public SystemUser authorize(String name, String password) throws AuthenticationException {
		if (StringUtils.isBlank(name) || StringUtils.isEmpty(password)) {
			LOGGER.error("Authentication: Name/Password empty");
			throw new AuthenticationException();
		}
		List<SystemUser> result = daoSupport.find(NameCriteria.make(name));
		if (result.isEmpty()) {
			LOGGER.error("Authentication: Did not find any users [" + name + "]");
			throw new AuthenticationException();
		}
		if (result.size() > 1) {
			LOGGER.error("Authentication: Found too many users [" + name + "]");
			throw new AuthenticationException();
		}
		SystemUser systemUser = result.get(0);
		if (!systemUser.isActive()) {
			LOGGER.error("Authentication: User is not active [" + name + "]");
			throw new AuthenticationException();
		}
		if (!systemUser.doesPasswordMatch(password)) {
			LOGGER.error("Authentication: Password is invalid [" + name + "]");
			throw new AuthenticationException();
		}
		return systemUser;
	}

	void setDaoSupport(DaoSupport<SystemUser> daoSupport) {
		this.daoSupport = daoSupport;
	}

}
