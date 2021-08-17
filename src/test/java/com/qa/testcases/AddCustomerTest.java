package com.qa.testcases;

import com.qa.utilities.TestUtil;
import org.openqa.selenium.Alert;
import org.testng.Assert;
import org.testng.SkipException;

import org.testng.annotations.Test;

import com.qa.base.TestBase;

import java.util.Hashtable;

public class AddCustomerTest extends TestBase {

    @Test(dataProviderClass = TestUtil.class, dataProvider = "dp")
    public void addCustomerTest(Hashtable<String, String> data) {

        if (!data.get("runmode").equalsIgnoreCase("Y"))
            throw new SkipException("Skipped the testData as the RunMode is No");

        click("addCustBtn_CSS");
        type("firstname_CSS", data.get("firstname"));
        type("lastname_CSS", data.get("lastname"));
        type("postcode_CSS", data.get("postcode"));
        click("addCBtn_CSS");

        Alert alert = waitForAlertPresent(driver, 5);

        Assert.assertTrue(alert.getText().contains(data.get("alerttext")));
        alert.accept();

    }
}