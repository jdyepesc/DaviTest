package com.davitest.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class PimPage extends BasePage {

    // "Add Employee" es un link en el topbar del modulo PIM, no un button en la lista
    private final By addEmployeeButton = By.xpath("//a[normalize-space()='Add Employee']");
    private final By pageHeader        = By.xpath("//h5[normalize-space()='Employee Information']");

    @Step("Verificar que la pagina PIM cargo correctamente")
    public void assertPageLoaded() {
        wait.waitForVisible(pageHeader);
    }

    @Step("Hacer clic en 'Add Employee'")
    public void clickAddEmployee() {
        wait.waitForClickable(addEmployeeButton).click();
    }
}
