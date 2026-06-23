package com.davitest.pages;

import com.davitest.config.ConfigReader;
import com.davitest.utils.ScreenshotUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class EmployeeProfilePage extends BasePage {

    // Campos en la pagina Personal Details
    private final By firstNameField = By.xpath("//input[@name='firstName']");
    private final By lastNameField  = By.xpath("//input[@name='lastName']");

    // Pagina de foto: input con opacity:0 (Selenium puede hacer sendKeys directamente)
    private final By fileInput  = By.cssSelector("input[type='file']");
    private final By saveButton = By.xpath("//button[@type='submit']");

    @Step("Obtener ID del empleado desde la URL")
    public String getEmployeeId() {
        // URL: /pim/viewPersonalDetails/empNumber/XXXX
        String url = getCurrentUrl();
        String[] parts = url.split("/");
        return parts[parts.length - 1];
    }

    @Step("Subir foto de perfil del empleado")
    public void uploadProfilePhoto(String imagePath) {
        ScreenshotUtils.attachScreenshotToAllure("Pagina perfil - estado inicial");

        String employeeId = getEmployeeId();

        // Navegar directamente a la pagina dedicada de foto del empleado
        // Al hacer clic en img[alt='profile picture'] en viewPersonalDetails
        // se navega a /pim/viewPhotograph/empNumber/{id}
        navigateTo(ConfigReader.getAppBaseUrl() + "/pim/viewPhotograph/empNumber/" + employeeId);

        // Esperar que la pagina cargue verificando que el Save button existe
        wait.waitForPresence(saveButton);

        // El input[type='file'] tiene opacity:0 pero Selenium puede interactuar con el
        // directamente via sendKeys sin necesidad de hacerlo visible
        WebElement input = wait.waitForPresence(fileInput);
        input.sendKeys(imagePath);

        // Pequena pausa para que la preview cargue
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}

        // Hacer clic en Save (type=submit)
        wait.waitForClickable(saveButton).click();

        // Esperar confirmacion: puede redirigir a viewPersonalDetails
        try {
            wait.waitForUrlContains("viewPersonalDetails");
        } catch (Exception e) {
            // Puede quedarse en viewPhotograph con la foto actualizada
        }

        ScreenshotUtils.attachScreenshotToAllure("Foto de perfil guardada");
    }

    @Step("Obtener nombre del empleado en perfil")
    public String getFirstName() {
        return wait.waitForVisible(firstNameField).getAttribute("value");
    }

    @Step("Obtener apellido del empleado en perfil")
    public String getLastName() {
        return wait.waitForVisible(lastNameField).getAttribute("value");
    }
}
