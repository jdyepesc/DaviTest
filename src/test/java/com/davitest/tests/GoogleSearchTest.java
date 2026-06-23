package com.davitest.tests;

import com.davitest.base.BaseTest;
import com.davitest.pages.GooglePage;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Motor de busqueda")
@Feature("Google Search")
public class GoogleSearchTest extends BaseTest {

    @Test(description = "Busqueda simple retorna resultados")
    @Story("El usuario busca un termino y ve resultados")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifica que al buscar 'Selenium WebDriver' en Google se muestren resultados organicos")
    public void searchReturnsResults() {
        GooglePage google = new GooglePage();
        google.search("Selenium WebDriver");
        Assert.assertTrue(google.hasSearchResults(), "No se mostraron resultados para la busqueda");
    }

    @Test(description = "La URL contiene el termino buscado")
    @Story("La URL refleja la busqueda realizada")
    @Severity(SeverityLevel.NORMAL)
    public void urlContainsSearchTerm() {
        GooglePage google = new GooglePage();
        google.search("TestNG Framework");
        String url = google.getCurrentPageUrl();
        Assert.assertTrue(url.contains("TestNG"), "La URL no contiene el termino buscado. URL: " + url);
    }
}
