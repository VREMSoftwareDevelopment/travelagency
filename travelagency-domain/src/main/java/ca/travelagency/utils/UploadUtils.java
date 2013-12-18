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
package ca.travelagency.utils;

import java.io.File;

import org.apache.commons.lang3.Validate;

public abstract class UploadUtils {
	public static final String MAIN_FOLDER = "/server/";
	public static final String UPLOAD_FOLDER = "upload";
	public static final String COMPANYLOGO_NAME = "companylogo.jpg";

	public static File getUploadFolder(String companyFolder) {
		Validate.notNull(companyFolder);
		File folder = new File(MAIN_FOLDER+companyFolder, UPLOAD_FOLDER);
		folder.mkdirs();
		return folder;
	}

	public static File getCompanyLogoFile(String companyFolder) {
		Validate.notNull(companyFolder);
		return new File(getUploadFolder(companyFolder), COMPANYLOGO_NAME);
	}
}
