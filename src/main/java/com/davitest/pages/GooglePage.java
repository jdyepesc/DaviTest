package com.davitest.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class GooglePage extends BasePage {

    private final By searchBox     = By.name("q");
    private final By searchButton  = By.name("btnK");
    private final By resultsStats  = By.id("result-stats");

    @Step("Buscar: {query}")
    public void search(String query) {
        type(searchBox, query);
        click(searchButton);
    }

    @Step("Verificar que aparecieron resultados")
    public boolean areResultsDisplayed() {
        return isDisplayed(resultsStats);
    }

    @Step("Obtener titulo de la pagina")
    public String getPageTitle() {
        return getTitle();
    }
}
