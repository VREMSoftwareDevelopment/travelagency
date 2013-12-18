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

import java.io.File;

import org.apache.wicket.markup.html.image.Image;

import ca.travelagency.components.resources.FileImageResource;
import ca.travelagency.utils.ApplicationProperties;
import ca.travelagency.utils.UploadUtils;

public class LogoImage extends Image {
	private static final long serialVersionUID = 1L;

	public LogoImage(String id) {
		super(id);
		String code = ApplicationProperties.make().getCode();
		File file = UploadUtils.getCompanyLogoFile(code);
		if (!file.exists()) {
			setVisible(false);
			return;
		}
		FileImageResource fileImageResource = new FileImageResource(file);
		setImageResource(fileImageResource);
	}

}
