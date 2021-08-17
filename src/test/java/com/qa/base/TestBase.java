package com.qa.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.qa.utilities.TestUtil;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.Status;
import com.qa.listeneres.CustomListeners;
import com.qa.utilities.ExcelReader;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestBase {
    /*
     * WebDriver Mailing logs DB Excel properties
     *
     */

    public static WebDriver driver;
    public static Properties config = new Properties();
    public static Properties OR = new Properties();
    public static FileInputStream fis;
    public static Logger log = Logger.getLogger(TestBase.class.getName());
    public static ExcelReader excel = new ExcelReader(
            System.getProperty("user.dir") + "/src/test/resources/excel/testdata.xlsx");
    public static WebDriverWait wait;
    public static WebElement dropdown;

    @BeforeSuite
    public void setUp() {

        if (driver == null) {

            PropertyConfigurator.configure("./src/test/resources/properties/log4j.properties");

            try {
                fis = new FileInputStream(
                        System.getProperty("user.dir") + "/src/test/resources/properties/Config.properties");
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                config.load(fis);
                log.info("Config File Loaded");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                fis = new FileInputStream(
                        System.getProperty("user.dir") + "/src/test/resources/properties/OR.properties");
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                OR.load(fis);
                log.info("OR File Loaded");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // Declare the Browser and load using Config File
            if (config.getProperty("browser").equals("chrome")) {

                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                log.info("Launching the Chrome Browser");

            } else if (config.getProperty("browser").equals("firefox")) {

                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                log.info("Launching the Firefox Browser");

            } else if (config.getProperty("browser").equals("safari")) {

                driver = new SafariDriver();
                log.info("Launching the Safari Browser");

            }

            // launching the URL
            driver.get(config.getProperty("testSiteUrl"));
            log.info("Navigated to " + config.getProperty("testSiteUrl"));

            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Integer.parseInt(config.getProperty("implicitWait")),
                    TimeUnit.SECONDS);
        }

    }

    public boolean isElementPresent(By by) {

        try {

            driver.findElement(by);
            return true;

        } catch (NoSuchElementException e) {

            return false;
        }
    }

    public void click(String locator) {

        if (locator.endsWith("_CSS")) {
            driver.findElement(By.cssSelector(OR.getProperty(locator))).click();
        } else if (locator.endsWith("_XPATH")) {
            driver.findElement(By.xpath(OR.getProperty(locator))).click();
        } else if (locator.endsWith("_ID")) {
            driver.findElement(By.id(OR.getProperty(locator))).click();
        }
        CustomListeners.testReport.get().log(Status.INFO, "Clicking on : " + locator);
    }

    public void type(String locator, String value) {

        if (locator.endsWith("_CSS")) {
            driver.findElement(By.cssSelector(OR.getProperty(locator))).sendKeys(value);
        } else if (locator.endsWith("_XPATH")) {
            driver.findElement(By.xpath(OR.getProperty(locator))).sendKeys(value);
        } else if (locator.endsWith("_ID")) {
            driver.findElement(By.id(OR.getProperty(locator))).sendKeys(value);
        }

        CustomListeners.testReport.get().log(Status.INFO, "Typing in : " + locator + " entered value as " + value);

    }

    public WebElement waitForElementPresent(WebDriver driver, By locator, int timeout) {

        wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));

        return driver.findElement(locator);

    }

    public Alert waitForAlertPresent(WebDriver driver, int timeout) {

        wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions.alertIsPresent());

    }

    public static void verifyEquals(String expected, String actual) throws IOException {

        try {

            Assert.assertEquals(actual, expected);
        } catch (Throwable t) {

            TestUtil.captureScreenshot();
            // Extent Reports
            CustomListeners.testReport.get().log(Status.FAIL, " Verification failed with exception : " + t.getMessage());
            CustomListeners.testReport.get().fail("<b>" + "<font color=" + "red>" + "Screenshot of failure" + "</font>" + "</b>",
                    MediaEntityBuilder.createScreenCaptureFromPath(TestUtil.screenshotName)
                            .build());
        }

    }

    public void selectFromDropDown(String locator, String value) {

        if (locator.endsWith("_CSS")) {
            dropdown = driver.findElement(By.cssSelector(OR.getProperty(locator)));
        } else if (locator.endsWith("_XPATH")) {
            dropdown = driver.findElement(By.xpath(OR.getProperty(locator)));
        } else if (locator.endsWith("_ID")) {
            dropdown = driver.findElement(By.id(OR.getProperty(locator)));
        }

        Select select = new Select(dropdown);
        select.selectByVisibleText(value);

        CustomListeners.testReport.get().log(Status.INFO, "Selecting from dropdown : " + locator + " entered value as " + value);

    }

    @AfterSuite
    public void tearDown() {

        if (driver != null) {

            driver.quit();
        }

        log.info("Test Execution successfully !!!");

    }

}
