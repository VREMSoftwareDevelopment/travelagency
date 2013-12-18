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
package ca.travelagency.authentication;

import org.apache.wicket.Page;


class PageInfo {
	static PageInfo makeAdmin(Class<? extends Page> page) {
		return new PageInfo(page, true, false);
	}

	static PageInfo makeAgent(Class<? extends Page> page) {
		return new PageInfo(page, true, true);
	}

	private Class<? extends Page> page;
	private boolean adminVisible;
	private boolean agentVisible;

	private PageInfo(Class<? extends Page> page, boolean adminVisible, boolean agentVisible) {
		this.page = page;
		this.adminVisible = adminVisible;
		this.agentVisible = agentVisible;
	}

	Class<? extends Page> getPage() {
		return page;
	}

	boolean isAdminVisible() {
		return adminVisible;
	}

	boolean isAgentVisible() {
		return agentVisible;
	}
}
