/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portalweb.demo.fundamentals.knowledgebase;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * @author Brian Wing Shun Chan
 */
public class DeleteKBAArticleSectionsCMKBArTest extends BaseTestCase {
	public void testDeleteKBAArticleSectionsCMKBAr() throws Exception {
		selenium.open("/web/guest/home/");
		loadRequiredJavaScriptModules();

		for (int second = 0;; second++) {
			if (second >= 90) {
				fail("timeout");
			}

			try {
				if (selenium.isVisible("link=Knowledge Base Article Test Page")) {
					break;
				}
			}
			catch (Exception e) {
			}

			Thread.sleep(1000);
		}

		selenium.clickAt("link=Knowledge Base Article Test Page",
			RuntimeVariables.replace("Knowledge Base Article Test Page"));
		selenium.waitForPageToLoad("30000");
		loadRequiredJavaScriptModules();
		assertEquals(RuntimeVariables.replace("Knowledge Base Article 1"),
			selenium.getText("//div[@class='kb-title']"));
		assertEquals(RuntimeVariables.replace("Delete"),
			selenium.getText(
				"//div[@class='kb-article-icons']/table/tbody/tr/td[4]/span/a"));
		selenium.clickAt("//div[@class='kb-article-icons']/table/tbody/tr/td[4]/span/a",
			RuntimeVariables.replace("Delete"));
		selenium.waitForPageToLoad("30000");
		loadRequiredJavaScriptModules();
		assertTrue(selenium.getConfirmation()
						   .matches("^Are you sure you want to delete this[\\s\\S] It will be deleted immediately.$"));
		assertEquals(RuntimeVariables.replace(
				"The selected article no longer exists."),
			selenium.getText("//div[@class='portlet-msg-error']"));
	}
}