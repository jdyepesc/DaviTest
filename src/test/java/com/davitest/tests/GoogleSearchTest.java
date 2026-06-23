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
    @Description("Verifica que al buscar 'Selenium WebDriver' en Google se muestren resultados")
    public void searchReturnsResults() {
        GooglePage google = new GooglePage();
        google.search("Selenium WebDriver");
        Assert.assertTrue(google.areResultsDisplayed(), "No se mostraron resultados para la busqueda");
    }

    @Test(description = "El titulo de la pagina contiene el termino buscado")
    @Story("El titulo refleja la busqueda realizada")
    @Severity(SeverityLevel.NORMAL)
    public void titleContainsSearchTerm() {
        GooglePage google = new GooglePage();
        google.search("TestNG Framework");
        String title = google.getPageTitle();
        Assert.assertTrue(title.contains("TestNG"), "El titulo no contiene el termino buscado. Titulo: " + title);
    }
}
