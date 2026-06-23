package com.davitest.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

public class GooglePage extends BasePage {

    private final By searchBox      = By.name("q");
    private final By resultsSection = By.id("search");
    private final By firstResult    = By.cssSelector("h3");

    @Step("Buscar: {query}")
    public void search(String query) {
        wait.waitForClickable(searchBox).sendKeys(query + Keys.ENTER);
    }

    @Step("Verificar que aparecieron resultados")
    public boolean areResultsDisplayed() {
        return isDisplayed(resultsSection);
    }

    @Step("Verificar que hay al menos un resultado en la lista")
    public boolean hasSearchResults() {
        return isDisplayed(firstResult);
    }

    @Step("Obtener titulo de la pagina")
    public String getPageTitle() {
        return getTitle();
    }

    @Step("Obtener URL actual")
    public String getCurrentPageUrl() {
        return getCurrentUrl();
    }
}
