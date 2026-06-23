package com.davitest.pages;

import com.davitest.config.ConfigReader;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class DashboardPage extends BasePage {

    private final By sidebarMenu = By.cssSelector(".oxd-main-menu");

    private By navItemByName(String moduleName) {
        return By.xpath("//a[@class='oxd-main-menu-item']//span[normalize-space()='" + moduleName + "']");
    }

    @Step("Navegar al modulo: {moduleName}")
    public void navigateToModule(String moduleName) {
        wait.waitForVisible(sidebarMenu);
        click(navItemByName(moduleName));
    }

    @Step("Navegar directamente a URL del modulo PIM")
    public void goToPim() {
        navigateTo(ConfigReader.getAppBaseUrl() + "/pim/viewEmployeeList");
    }

    @Step("Navegar directamente al modulo Directory")
    public void goToDirectory() {
        navigateTo(ConfigReader.getAppBaseUrl() + "/directory/viewDirectory");
    }
}
