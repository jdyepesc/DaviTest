package com.davitest.utils;

import com.davitest.drivers.DriverManager;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

public class ScreenshotUtils {

    private ScreenshotUtils() {}

    public static byte[] captureScreenshot() {
        WebDriver driver = DriverManager.getDriver();
        if (driver == null) return new byte[0];
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    public static void attachScreenshotToAllure(String name) {
        byte[] screenshot = captureScreenshot();
        if (screenshot.length > 0) {
            Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshot), ".png");
        }
    }

    public static void attachScreenshotOnFailure(String testName) {
        attachScreenshotToAllure("Fallo en: " + testName);
    }
}
