package com.davitest.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.testng.Assert;

public class LoginPage extends BasePage {

    private final By usernameInput  = By.name("username");
    private final By passwordInput  = By.name("password");
    private final By loginButton    = By.cssSelector("button[type='submit']");
    private final By dashboardTitle = By.cssSelector(".oxd-topbar-header-breadcrumb");
    private final By errorMessage   = By.cssSelector(".oxd-alert-content-text");

    @Step("Navegar a la pagina de login")
    public LoginPage open(String url) {
        navigateTo(url);
        wait.waitForVisible(usernameInput);
        return this;
    }

    @Step("Ingresar usuario: {username}")
    public LoginPage enterUsername(String username) {
        type(usernameInput, username);
        return this;
    }

    @Step("Ingresar contrasena")
    public LoginPage enterPassword(String password) {
        type(passwordInput, password);
        return this;
    }

    @Step("Hacer clic en boton Login")
    public void clickLogin() {
        click(loginButton);
    }

    @Step("Login con usuario '{username}'")
    public void loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        wait.waitForVisible(dashboardTitle);
    }

    @Step("Verificar que el login fue exitoso")
    public void assertLoginSuccessful() {
        Assert.assertTrue(isDisplayed(dashboardTitle), "El dashboard no se mostro despues del login");
    }
}
