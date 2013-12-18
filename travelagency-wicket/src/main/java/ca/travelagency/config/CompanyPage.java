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

import java.io.File;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import ca.travelagency.BasePage;
import ca.travelagency.components.behaviours.AjaxOnBlurBehaviour;
import ca.travelagency.components.decorators.FieldDecorator;
import ca.travelagency.components.fields.CityField;
import ca.travelagency.components.fields.CountryField;
import ca.travelagency.components.fields.ProvinceField;
import ca.travelagency.components.fields.StringFieldHelper;
import ca.travelagency.components.formheader.DaoEntityModelFactory;
import ca.travelagency.components.formheader.ResetPanel;
import ca.travelagency.components.formheader.SavePanel;
import ca.travelagency.components.validators.PhoneNumberValidator;
import ca.travelagency.persistence.DaoEntity;
import ca.travelagency.persistence.DaoSupport;
import ca.travelagency.utils.ApplicationProperties;
import ca.travelagency.utils.UploadUtils;

@AuthorizeInstantiation({"ADMIN"})
public class CompanyPage extends BasePage {
	private static final long serialVersionUID = 1L;

	@SpringBean
	protected DaoSupport<Company> daoSupport;

	static final String PAGE_TITLE = "company.title";

	static final String COMPANY_FORM = "companyForm";
	static final String SAVE_BUTTON = "saveButton";
	static final String RESET_BUTTON = "resetButton";
	static final String FEEDBACK = "feedback";

	static final String UPLOAD_FORM = "uploadForm";
	static final String UPLOAD_FIELD = "uploadField";

	private FileUploadField fileUploadField;

	public CompanyPage() {
		super();

		Company company = daoSupport.find(Company.class, Company.COMPANY_ID);
		IModel<Company> model = DaoEntityModelFactory.make(company, Company.class);
		add(companyForm(model));
		add(uploadForm());
	}

	private Form<Void> uploadForm() {
		Form<Void> form = new Form<Void>(UPLOAD_FORM) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit() {
				FileUpload fileUpload = fileUploadField.getFileUpload();
				if (fileUpload == null) {
					return;
				}
				String code = ApplicationProperties.make().getCode();
				File companyLogoFile = UploadUtils.getCompanyLogoFile(code);
				try	{
					companyLogoFile.createNewFile();
					fileUpload.writeTo(companyLogoFile);
				} catch (Exception e) {
					throw new RuntimeException("Unable to write logo image", e);
				}
			}
		};
		form.setMultiPart(true);
		form.setMaxSize(Bytes.kilobytes(100));

		form.add(new ComponentFeedbackPanel(FEEDBACK, form));

		fileUploadField = new FileUploadField(UPLOAD_FIELD, new ListModel<FileUpload>());
		form.add(fileUploadField);

		return form;
	}

	private Form<Company> companyForm(IModel<Company> model) {
		Form<Company> form = new Form<Company>(COMPANY_FORM, model);
		form.setOutputMarkupId(true);

		form.add(new ComponentFeedbackPanel(FEEDBACK, form));

		form.add(new RequiredTextField<String>(DaoEntity.PROPERTY_NAME)
			.setLabel(new ResourceModel("company.name"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new RequiredTextField<String>(Company.Properties.address.name())
			.setLabel(new ResourceModel("company.address"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new CityField(Company.Properties.city.name())
			.setLabel(new ResourceModel("company.city"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.setRequired(true)
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new ProvinceField(Company.Properties.province.name())
			.setLabel(new ResourceModel("company.province"))
			.setRequired(true)
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new RequiredTextField<String>(Company.Properties.postalCode.name())
			.setLabel(new ResourceModel("company.postalCode"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new CountryField(Company.Properties.country.name())
			.setLabel(new ResourceModel("company.country"))
			.setRequired(true)
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new RequiredTextField<String>(Company.Properties.email.name())
			.setLabel(new ResourceModel("company.email"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(EmailAddressValidator.getInstance())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new RequiredTextField<String>(Company.Properties.phoneNumber.name())
			.setLabel(new ResourceModel("company.phone"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new PhoneNumberValidator(), new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new RequiredTextField<String>(Company.Properties.faxNumber.name())
			.setLabel(new ResourceModel("company.fax"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new PhoneNumberValidator(), new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new RequiredTextField<String>(Company.Properties.taxNumber.name())
			.setLabel(new ResourceModel("company.HSTNumber"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));
		form.add(new RequiredTextField<String>(Company.Properties.ticoNumber.name())
			.setLabel(new ResourceModel("company.TICONumber"))
			.add(StringFieldHelper.maxLenFieldValidator())
			.add(new FieldDecorator(), new AjaxOnBlurBehaviour()));

		form.add(new SavePanel<Company>(SAVE_BUTTON, form));
		form.add(new ResetPanel<Company>(RESET_BUTTON, form));

		return form;
	}

	@Override
	public String getPageTitleKey() {
		return PAGE_TITLE;
	}
}
