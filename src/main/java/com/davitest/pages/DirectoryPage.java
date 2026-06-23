package com.davitest.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

public class DirectoryPage extends BasePage {

    private final By pageHeader        = By.xpath("//h5[normalize-space()='Directory']");
    private final By employeeNameInput = By.xpath("//input[@placeholder='Type for hints...']");
    // Clase real del option verificada con MCP: oxd-autocomplete-option
    private final By autocompleteOption = By.cssSelector(".oxd-autocomplete-option");
    // Search es type=submit en OrangeHRM
    private final By searchButton      = By.xpath("//button[@type='submit']");
    // Clase real del card verificada con MCP: orangehrm-directory-card
    private final By resultCards       = By.cssSelector(".orangehrm-directory-card");
    // Clase real del nombre verificada con MCP: orangehrm-directory-card-header (sin -name)
    private final By cardEmployeeName  = By.cssSelector(".orangehrm-directory-card-header");

    @Step("Verificar que el modulo Directory cargo")
    public void assertPageLoaded() {
        wait.waitForVisible(pageHeader);
    }

    @Step("Buscar empleado por nombre: {employeeName}")
    public void searchByEmployeeName(String employeeName) {
        WebElement input = wait.waitForClickable(employeeNameInput);
        input.clear();
        input.sendKeys(employeeName);

        // Esperar que el autocomplete reaccione, luego navegar con teclado
        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}

        input.sendKeys(org.openqa.selenium.Keys.ARROW_DOWN);
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        input.sendKeys(org.openqa.selenium.Keys.ENTER);
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        wait.waitForClickable(searchButton).click();
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
    }

    @Step("Validar que el empleado '{expectedFirstName} {expectedLastName}' aparece en resultados")
    public void assertEmployeeFound(String expectedFirstName, String expectedLastName) {
        try {
            wait.waitForVisible(resultCards);
        } catch (Exception e) {
            Assert.fail("No se encontraron tarjetas de resultado en el directorio para: "
                + expectedFirstName + " " + expectedLastName);
        }

        List<WebElement> nameElements = driver.findElements(cardEmployeeName);
        Assert.assertFalse(nameElements.isEmpty(),
            "No se encontraron elementos de nombre en las tarjetas del directorio");

        // OrangeHRM muestra el nombre como "FirstName  LastName" (doble espacio por middleName vacio)
        // Normalizar espacios multiples a uno solo para la comparacion
        String expected = (expectedFirstName + " " + expectedLastName)
            .trim().replaceAll("\\s+", " ");

        boolean found = nameElements.stream()
            .anyMatch(el -> el.getText().trim().replaceAll("\\s+", " ")
                             .equalsIgnoreCase(expected));

        Assert.assertTrue(found,
            "El empleado '" + expected + "' no aparece en los resultados del directorio. "
            + "Nombres encontrados: "
            + nameElements.stream()
                          .map(el -> el.getText().trim().replaceAll("\\s+", " "))
                          .toList()
        );
    }

    @Step("Obtener cantidad de resultados")
    public int getResultCount() {
        try {
            wait.waitForVisible(resultCards);
            return driver.findElements(resultCards).size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Step("Obtener nombre del primer resultado")
    public String getFirstResultName() {
        wait.waitForVisible(cardEmployeeName);
        return driver.findElements(cardEmployeeName).get(0)
                     .getText().trim().replaceAll("\\s+", " ");
    }
}
