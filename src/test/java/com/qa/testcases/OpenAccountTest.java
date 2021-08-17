package com.qa.testcases;


import com.qa.base.TestBase;
import com.qa.utilities.TestUtil;
import org.openqa.selenium.Alert;
import org.testng.Assert;

import org.testng.annotations.Test;

import java.util.Hashtable;

public class OpenAccountTest extends TestBase {

    @Test(dataProviderClass = TestUtil.class, dataProvider = "dp")
    public void openAccountTest(Hashtable<String, String> data) {


        click("openacc_CSS");
        selectFromDropDown("customer_CSS", data.get("customer"));
        selectFromDropDown("currency_CSS", data.get("currency"));
        click("process_CSS");

        Alert alert = waitForAlertPresent(driver, 5);
        Assert.assertTrue(alert.getText().contains(data.get("alerttext")));
        alert.accept();


    }


}