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
package ca.travelagency;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.test.ApplicationContextMock;
import org.apache.wicket.util.tester.WicketTester;
import org.apache.wicket.util.tester.WicketTesterHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import ca.travelagency.authentication.AuthenticatedSession;
import ca.travelagency.config.ParameterRepository;
import ca.travelagency.customer.CustomerService;
import ca.travelagency.identity.Role;
import ca.travelagency.identity.SystemUser;
import ca.travelagency.identity.SystemUserHelper;
import ca.travelagency.identity.SystemUserService;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.DaoSupport;
import ca.travelagency.persistence.query.Criteria;

@RunWith(MockitoJUnitRunner.class)
public abstract class BaseWicketTester {
	protected static final String COMPONENT_ID = "component";
	protected static final String COMPONENT_PATH = COMPONENT_ID+BasePage.PATH_SEPARATOR;

	@Mock
	protected DaoSupport<DaoEntity> daoSupport;
	@Mock
	protected SystemUserService systemUserService;
	@Mock
	protected CustomerService customerService;
	@Mock
	protected ParameterRepository parameterRepository;

	protected WicketTester tester;
	protected SystemUser currentSystemUser;

	@Before
	public void setUpBaseWicketTester() throws Exception {
		Application application = new Application() {
			@Override
			protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
				return AuthenticatedSessionTester.class;
			}
		};

		application.setApplicationContext(makeApplicationContextMock());

		tester = new WicketTester(application);

		usingAdminSystemUser();
	}

	private ApplicationContextMock makeApplicationContextMock() {
		ApplicationContextMock applicationContextMock = new ApplicationContextMock();
		applicationContextMock.putBean("daoSupport", daoSupport);
		applicationContextMock.putBean("systemUserService", systemUserService);
		applicationContextMock.putBean("customerService", customerService);
		applicationContextMock.putBean("parameterRepository", parameterRepository);
		return applicationContextMock;
	}

	@After
	public void tearDownUpBaseWicketTester() throws Exception {
		((AuthenticatedSession) tester.getSession()).signOut();
	}

	protected void validateSortableColumn(String attribute, IColumn<?, String> column) {
		validateSortableColumn(attribute, attribute, column);
	}

	protected void validateSortableColumn(String sortAttribute, String propertyAttribute, IColumn<?, String> column) {
		Assert.assertEquals(sortAttribute, column.getSortProperty());
		validateColumn(propertyAttribute, column);
	}

	protected void validateNotSortableColumn(String attribute, IColumn<?, String> column) {
		Assert.assertNull(column.getSortProperty());
		validateColumn(attribute, column);
	}

	protected void validateColumn(String attribute, IColumn<?, String> column) {
		Assert.assertEquals(attribute, ((PropertyColumn<?, String>) column).getPropertyExpression());
	}

	protected void stubSystemUserField(List<SystemUser> systemUsers) {
		Validate.notNull(systemUsers);
		List<DaoEntity> results = new ArrayList<DaoEntity>();
		for (SystemUser systemUser : systemUsers) {
			results.add(systemUser);
		}
		Mockito.stub(daoSupport.find(Criteria.make(SystemUser.class))).toReturn(results);
	}

	protected SystemUser usingNoSystemUser() {
		return makeSystemUser(new Role[] {});
	}

	protected SystemUser usingAdminSystemUser() {
		return makeSystemUser(Role.ADMIN, Role.AGENT);
	}

	protected SystemUser usingAgentSystemUser() {
		return makeSystemUser(Role.AGENT);
	}

	private SystemUser makeSystemUser(Role ... roles) {
		currentSystemUser = (roles.length == 0
			? SystemUserHelper.makeSystemUser()
			: SystemUserHelper.makeSystemUserWithRole(roles));
		AuthenticatedSessionTester authenticatedSessionTester = (AuthenticatedSessionTester) tester.getSession();
		authenticatedSessionTester.setSystemUser(currentSystemUser);
		return currentSystemUser;
	}

	protected Form<SystemUser> makeTestForm() {
		TdFormPanel tdFormPanel = new TdFormPanel("form");
		tester.startComponentInPage(tdFormPanel);
		return tdFormPanel.getForm();
	}

	protected void assertSuccessMessages(String... expectedSuccessMessages) {
		List<Serializable> actualMessages = tester.getMessages(FeedbackMessage.SUCCESS);
		WicketTesterHelper.assertEquals(Arrays.asList(expectedSuccessMessages), actualMessages);
	}

	protected void assertNoSuccessMessage() {
		List<Serializable> messages = tester.getMessages(FeedbackMessage.SUCCESS);
		Assert.assertTrue("expect no success message, but contains\n" + WicketTesterHelper.asLined(messages), messages.isEmpty());
	}

}
