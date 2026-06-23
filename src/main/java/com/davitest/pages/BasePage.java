package com.davitest.pages;

import com.davitest.drivers.DriverManager;
import com.davitest.utils.WaitUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

public abstract class BasePage {

    protected WebDriver driver;
    protected WaitUtils wait;
    protected Actions actions;

    protected BasePage() {
        this.driver  = DriverManager.getDriver();
        this.wait    = new WaitUtils(driver);
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    @Step("Navegar a: {url}")
    public void navigateTo(String url) {
        driver.get(url);
    }

    @Step("Hacer clic en elemento: {locator}")
    public void click(By locator) {
        wait.waitForClickable(locator).click();
    }

    @Step("Escribir '{text}' en elemento: {locator}")
    public void type(By locator, String text) {
        WebElement element = wait.waitForVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    @Step("Obtener texto de elemento: {locator}")
    public String getText(By locator) {
        return wait.waitForVisible(locator).getText();
    }

    @Step("Verificar que el elemento es visible: {locator}")
    public boolean isDisplayed(By locator) {
        try {
            return wait.waitForVisible(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void scrollToElement(By locator) {
        WebElement element = wait.waitForPresence(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void clickWithJS(By locator) {
        WebElement element = wait.waitForPresence(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
