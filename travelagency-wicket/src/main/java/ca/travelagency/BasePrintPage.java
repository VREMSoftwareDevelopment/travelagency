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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.wicket.core.util.resource.UrlResourceStream;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.MapModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.velocity.markup.html.VelocityPanel;

import ca.travelagency.config.Company;
import ca.travelagency.persistence.DaoSupport;
import ca.travelagency.utils.ContextClassLoaderUtils;
import ca.travelagency.utils.DateUtils;
import ca.travelagency.utils.StringUtils;

public abstract class BasePrintPage extends WebPage {
	private static final long serialVersionUID = 1L;

	public static final String PREVIEW = "preview";
	public static final String TITLE = "title";
	public static final String LOGO_IMAGE = "logoImage";
	public static final String EXTENSION = ".vm";

	@SpringBean
	private DaoSupport<Company> daoSupport;

	public BasePrintPage(String velocityTemplate, IModel<?> model) {
		super(model);
		Validate.notBlank(velocityTemplate);

		Map<String, Object> map = getObjectMap();
		map.put("company", getCompany());
		map.put("dateUtils", new VelocityDateUtils());
		map.put("stringUtils", new VelocityStringUtils());
		MapModel<String, Object> context = new MapModel<String, Object>(map);

		URL resource = ContextClassLoaderUtils.getResource(velocityTemplate+EXTENSION);
		IResourceStream templateResource = new UrlResourceStream(resource);

		VelocityPanel velocityPanel = VelocityPanel.forTemplateResource(PREVIEW, context, templateResource);
		velocityPanel.add(new LogoImage(LOGO_IMAGE));
		add(velocityPanel);

		add(new Label(TITLE, getPageTitle()));
	}

	protected abstract String getPageTitle();

	private Company getCompany() {
		return daoSupport.find(Company.class, Company.COMPANY_ID);
	}

	protected Map<String, Object> getObjectMap() {
		return new HashMap<String, Object>();
	}

	private class VelocityDateUtils extends DateUtils {
		private static final long serialVersionUID = 1L;
	}

	private class VelocityStringUtils extends StringUtils {
		private static final long serialVersionUID = 1L;
	}
}
