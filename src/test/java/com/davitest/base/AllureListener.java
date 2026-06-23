package com.davitest.base;

import com.davitest.utils.ScreenshotUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.TestResult;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class AllureListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        ScreenshotUtils.attachScreenshotToAllure("Screenshot - Fallo");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ScreenshotUtils.attachScreenshotToAllure("Screenshot - Omitido");
    }
}
