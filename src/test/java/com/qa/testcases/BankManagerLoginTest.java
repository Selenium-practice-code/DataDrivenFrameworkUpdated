package com.qa.testcases;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.qa.base.TestBase;

import java.io.IOException;

public class BankManagerLoginTest extends TestBase {

	@Test
	public void bankManagerLoginTest() throws IOException {

		//verifyEquals("abc", "xyz"); // Soft assertion
		click("bmlBtn_CSS");

		Assert.assertTrue(isElementPresent(By.cssSelector(OR.getProperty("addCustBtn_CSS"))), "Login not successful");

	 
	}
}
