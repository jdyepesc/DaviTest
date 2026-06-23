package com.davitest.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;

public class AddEmployeePage extends BasePage {

    private final By pageHeader    = By.xpath("//h6[normalize-space()='Add Employee']");
    private final By firstNameInput = By.xpath("//input[@name='firstName']");
    private final By lastNameInput  = By.xpath("//input[@name='lastName']");
    private final By saveButton     = By.xpath("//button[@type='submit']");
    private final By successToast   = By.cssSelector(".oxd-toast-content");

    @Step("Verificar que la pagina Add Employee cargo")
    public void assertPageLoaded() {
        wait.waitForVisible(pageHeader);
    }

    @Step("Ingresar nombre: {firstName}")
    public void enterFirstName(String firstName) {
        wait.waitForVisible(firstNameInput).clear();
        wait.waitForVisible(firstNameInput).sendKeys(firstName);
    }

    @Step("Ingresar apellido: {lastName}")
    public void enterLastName(String lastName) {
        wait.waitForVisible(lastNameInput).clear();
        wait.waitForVisible(lastNameInput).sendKeys(lastName);
    }

    @Step("Guardar nuevo empleado")
    public void clickSave() {
        wait.waitForClickable(saveButton).click();
    }

    @Step("Agregar empleado con nombre '{firstName} {lastName}'")
    public void addEmployee(String firstName, String lastName) {
        assertPageLoaded();
        enterFirstName(firstName);
        enterLastName(lastName);
        clickSave();
        // Esperar que la URL cambie a la pagina de detalle del empleado
        wait.waitForUrlContains("viewPersonalDetails");
    }

    @Step("Validar que el empleado fue guardado exitosamente")
    public void assertSaveSuccess() {
        Assert.assertTrue(
            getCurrentUrl().contains("viewPersonalDetails"),
            "No redireccionó a la página de detalle del empleado después de guardar"
        );
    }
}
