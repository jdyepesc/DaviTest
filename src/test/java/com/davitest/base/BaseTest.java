package com.davitest.base;

import com.davitest.config.ConfigReader;
import com.davitest.drivers.DriverManager;
import com.davitest.utils.ScreenshotUtils;
import io.qameta.allure.Allure;
import org.testng.ITestResult;
import org.testng.annotations.*;

@Listeners(com.davitest.base.AllureListener.class)
public abstract class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverManager.initDriver();
        DriverManager.getDriver().get(ConfigReader.getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            ScreenshotUtils.attachScreenshotOnFailure(result.getName());
            Allure.addAttachment("URL al fallar", DriverManager.getDriver().getCurrentUrl());
        }
        DriverManager.quitDriver();
    }
}
