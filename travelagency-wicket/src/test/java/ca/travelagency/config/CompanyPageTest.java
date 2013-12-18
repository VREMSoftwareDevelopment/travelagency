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

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.util.tester.FormTester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ca.travelagency.BasePage;
import ca.travelagency.BaseWicketTester;
import ca.travelagency.components.fields.AutoCompleteField;
import ca.travelagency.components.fields.CityField;
import ca.travelagency.components.fields.CountryField;
import ca.travelagency.components.formheader.ResetPanel;
import ca.travelagency.components.formheader.SavePanel;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.utils.StringUtils;

public class CompanyPageTest extends BaseWicketTester {
	private static final String COMPANY_PATH = CompanyPage.COMPANY_FORM+CompanyPage.PATH_SEPARATOR;
	private static final String SAVE_PATH = COMPANY_PATH+CompanyPage.SAVE_BUTTON+BasePage.PATH_SEPARATOR+SavePanel.SAVE_BUTTON;
	private static final String UPLOAD_PATH = CompanyPage.UPLOAD_FORM+CompanyPage.PATH_SEPARATOR;

	protected Company company;

	@Before
	public void setUp() {
		company = ConfigHelper.makeCompany();
		company.setId(Company.COMPANY_ID);

		tester.startPage(CompanyPage.class);
		tester.assertRenderedPage(CompanyPage.class);

		Mockito.stub(daoSupport.find(Company.class, company.getId())).toReturn(company);
	}

	@After
	public void tearDown() {
		Mockito.verify(daoSupport, Mockito.atLeastOnce()).find(Company.class, company.getId());
	}

	@Test
	public void testFormComponents() {
		tester.assertComponent(CompanyPage.COMPANY_FORM, Form.class);

		tester.assertComponent(COMPANY_PATH+CompanyPage.SAVE_BUTTON, SavePanel.class);
		tester.assertComponent(COMPANY_PATH+CompanyPage.RESET_BUTTON, ResetPanel.class);

		tester.assertComponent(COMPANY_PATH+CompanyPage.FEEDBACK, ComponentFeedbackPanel.class);
		tester.assertComponent(COMPANY_PATH+DaoEntity.PROPERTY_NAME, RequiredTextField.class);
		tester.assertComponent(COMPANY_PATH+Company.Properties.address, RequiredTextField.class);
		tester.assertComponent(COMPANY_PATH+Company.Properties.city, CityField.class);
		tester.assertComponent(COMPANY_PATH+Company.Properties.province, AutoCompleteField.class);
		tester.assertComponent(COMPANY_PATH+Company.Properties.postalCode, RequiredTextField.class);
		tester.assertComponent(COMPANY_PATH+Company.Properties.country, CountryField.class);
		tester.assertComponent(COMPANY_PATH+Company.Properties.email, RequiredTextField.class);
		tester.assertComponent(COMPANY_PATH+Company.Properties.phoneNumber, RequiredTextField.class);
		tester.assertComponent(COMPANY_PATH+Company.Properties.faxNumber, RequiredTextField.class);
		tester.assertComponent(COMPANY_PATH+Company.Properties.taxNumber, RequiredTextField.class);
		tester.assertComponent(COMPANY_PATH+Company.Properties.ticoNumber, RequiredTextField.class);

		tester.assertComponent(CompanyPage.UPLOAD_FORM, Form.class);
		tester.assertComponent(UPLOAD_PATH+CompanyPage.UPLOAD_FIELD, FileUploadField.class);
	}

	@Test
	public void testFormSave() {
		FormTester form = tester.newFormTester(CompanyPage.COMPANY_FORM);
		form.setValue(""+DaoEntity.PROPERTY_NAME, company.getName());
		form.setValue(""+Company.Properties.address, company.getAddress());
		form.setValue(""+Company.Properties.city, company.getCity());
		form.setValue(""+Company.Properties.province, company.getProvince());
		form.setValue(""+Company.Properties.postalCode, company.getPostalCode());
		form.setValue(""+Company.Properties.country, company.getCountry());
		form.setValue(""+Company.Properties.email, company.getEmail());
		form.setValue(""+Company.Properties.phoneNumber, company.getPhoneNumber());
		form.setValue(""+Company.Properties.faxNumber, company.getFaxNumber());
		form.setValue(""+Company.Properties.taxNumber, company.getTaxNumber());
		form.setValue(""+Company.Properties.ticoNumber, company.getTicoNumber());

		tester.executeAjaxEvent(SAVE_PATH, "onclick");

		assertSuccessMessages(new String [] {company.getName()+" saved."});
		tester.assertRenderedPage(CompanyPage.class);

		Mockito.verify(daoSupport).persist(company);
	}

	@Test
	public void testVerifyRequiredFields() throws Exception {
		// setup
		FormTester form = tester.newFormTester(CompanyPage.COMPANY_FORM);
		form.setValue(""+DaoEntity.PROPERTY_NAME, StringUtils.EMPTY);
		form.setValue(""+Company.Properties.address, StringUtils.EMPTY);
		form.setValue(""+Company.Properties.city, StringUtils.EMPTY);
		form.setValue(""+Company.Properties.province, StringUtils.EMPTY);
		form.setValue(""+Company.Properties.postalCode, StringUtils.EMPTY);
		form.setValue(""+Company.Properties.country, StringUtils.EMPTY);
		form.setValue(""+Company.Properties.email, StringUtils.EMPTY);
		form.setValue(""+Company.Properties.phoneNumber, StringUtils.EMPTY);
		form.setValue(""+Company.Properties.faxNumber, StringUtils.EMPTY);
		form.setValue(""+Company.Properties.taxNumber, StringUtils.EMPTY);
		form.setValue(""+Company.Properties.ticoNumber, StringUtils.EMPTY);

		tester.executeAjaxEvent(SAVE_PATH, "onclick");

		tester.assertErrorMessages(new String [] {
		   "'Company Name' is required.",
		   "'Address' is required.",
		   "'City' is required.",
		   "'Province' is required.",
		   "'Postal Code' is required.",
		   "'Country' is required.",
		   "'Email' is required.",
		   "'Phone' is required.",
		   "'Fax' is required.",
		   "'HST Number' is required.",
		   "'TICO Number' is required."
		});
	}

	@Test
	public void testVerifyValidFields() throws Exception {
		// setup
		FormTester form = tester.newFormTester(CompanyPage.COMPANY_FORM);
		form.setValue(""+Company.Properties.email, "EMAIL");
		form.setValue(""+Company.Properties.phoneNumber, "123-1234");
		form.setValue(""+Company.Properties.faxNumber, "123-1234");

		tester.executeAjaxEvent(SAVE_PATH, "onclick");

		tester.assertErrorMessages(new String [] {
			"The value of 'Email' is not a valid email address.",
			"'Phone' is not a valid phone number.",
			"'Fax' is not a valid phone number."
		});
	}
}
