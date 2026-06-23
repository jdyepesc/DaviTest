package com.davitest.tests;

import com.davitest.config.ConfigReader;
import com.davitest.drivers.DriverManager;
import com.davitest.pages.*;
import com.davitest.utils.ImageUtils;
import com.davitest.utils.ScreenshotUtils;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

/**
 * Suite E2E para OrangeHRM.
 * Flujo: Login → PIM → Agregar Empleado → Subir Foto → Directorio → Buscar → Validar
 */
@Epic("OrangeHRM")
@Feature("Gestion de Empleados")
@Listeners(com.davitest.base.AllureListener.class)
public class OrangeHRMTest {

    // Datos compartidos entre pasos del flujo E2E
    private static String employeeFirstName;
    private static String employeeLastName;
    private static String profileImagePath;

    @BeforeClass(alwaysRun = true)
    public void suiteSetUp() {
        // Nombre unico para evitar colisiones en el entorno demo compartido
        String suffix = String.valueOf(System.currentTimeMillis()).substring(9);
        employeeFirstName = "Auto" + suffix;
        employeeLastName  = "QATester";
        profileImagePath  = ImageUtils.createTestProfileImage();

        DriverManager.initDriver();
        DriverManager.getDriver().get(ConfigReader.getBaseUrl());
    }

    @AfterClass(alwaysRun = true)
    public void suiteTearDown() {
        DriverManager.quitDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void captureFailureScreenshot(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            ScreenshotUtils.attachScreenshotToAllure("Fallo - " + result.getName());
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PASO 1: Login con credenciales de administrador
    // ─────────────────────────────────────────────────────────────────────────

    @Test(priority = 1, description = "Login con credenciales de administrador")
    @Story("Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Ingresa a OrangeHRM con usuario Admin y contrasena admin123")
    public void loginComoAdministrador() {
        LoginPage loginPage = new LoginPage();
        loginPage.loginAs(
            ConfigReader.getAdminUsername(),
            ConfigReader.getAdminPassword()
        );
        loginPage.assertLoginSuccessful();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PASO 2: Navegar al modulo PIM
    // ─────────────────────────────────────────────────────────────────────────

    @Test(priority = 2, description = "Navegar al modulo PIM",
          dependsOnMethods = "loginComoAdministrador")
    @Story("Modulo PIM")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Navega al modulo de administracion de personal (PIM)")
    public void navegarAlModuloPim() {
        DashboardPage dashboard = new DashboardPage();
        dashboard.goToPim();

        PimPage pimPage = new PimPage();
        pimPage.assertPageLoaded();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PASO 3: Agregar nuevo empleado
    // ─────────────────────────────────────────────────────────────────────────

    @Test(priority = 3, description = "Agregar nuevo empleado con nombre y apellido",
          dependsOnMethods = "navegarAlModuloPim")
    @Story("Agregar Empleado")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Crea un nuevo empleado con nombre: {employeeFirstName} {employeeLastName}")
    public void agregarNuevoEmpleado() {
        // Navegar directamente a la pagina de agregar empleado
        // (mas robusto que depender del boton de la lista que puede variar entre versiones)
        DriverManager.getDriver().get(ConfigReader.getAppBaseUrl() + "/pim/addEmployee");

        AddEmployeePage addPage = new AddEmployeePage();
        addPage.addEmployee(employeeFirstName, employeeLastName);
        addPage.assertSaveSuccess();

        Allure.addAttachment(
            "Empleado creado",
            "Nombre: " + employeeFirstName + " | Apellido: " + employeeLastName
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PASO 4: Subir foto de perfil
    // ─────────────────────────────────────────────────────────────────────────

    @Test(priority = 4, description = "Subir foto de perfil del empleado",
          dependsOnMethods = "agregarNuevoEmpleado")
    @Story("Foto de Perfil")
    @Severity(SeverityLevel.NORMAL)
    @Description("Sube una imagen PNG como foto de perfil del empleado recien creado")
    public void subirFotoDePerfilDelEmpleado() {
        EmployeeProfilePage profilePage = new EmployeeProfilePage();
        String employeeId = profilePage.getEmployeeId();

        Allure.addAttachment("ID del empleado", employeeId);

        profilePage.uploadProfilePhoto(profileImagePath);

        Allure.addAttachment(
            "Foto subida",
            "Se subio exitosamente la imagen: " + profileImagePath
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PASO 5: Navegar al modulo Directory
    // ─────────────────────────────────────────────────────────────────────────

    @Test(priority = 5, description = "Navegar al modulo Directory",
          dependsOnMethods = "subirFotoDePerfilDelEmpleado")
    @Story("Modulo Directory")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Navega al modulo Directory desde el menu principal")
    public void navegarAlModuloDirectory() {
        DashboardPage dashboard = new DashboardPage();
        dashboard.goToDirectory();

        DirectoryPage directoryPage = new DirectoryPage();
        directoryPage.assertPageLoaded();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PASO 6: Buscar empleado por nombre
    // ─────────────────────────────────────────────────────────────────────────

    @Test(priority = 6, description = "Buscar empleado usando filtro de nombre",
          dependsOnMethods = "navegarAlModuloDirectory")
    @Story("Busqueda en Directory")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Realiza busqueda en Directory usando el nombre del empleado: {employeeFirstName}")
    public void buscarEmpleadoPorNombre() {
        DirectoryPage directoryPage = new DirectoryPage();
        directoryPage.searchByEmployeeName(employeeFirstName);

        int total = directoryPage.getResultCount();
        Allure.addAttachment(
            "Resultados encontrados",
            "Se encontraron " + total + " resultado(s) para: " + employeeFirstName
        );

        Assert.assertTrue(total > 0,
            "No se encontraron resultados en Directory para el nombre: " + employeeFirstName
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PASO 7: Validar informacion basica del empleado
    // ─────────────────────────────────────────────────────────────────────────

    @Test(priority = 7, description = "Validar que la informacion del empleado es correcta",
          dependsOnMethods = "buscarEmpleadoPorNombre")
    @Story("Validacion de Datos")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifica que el nombre completo del empleado aparece correctamente en los resultados del Directory")
    public void validarInformacionBasicaDelEmpleado() {
        DirectoryPage directoryPage = new DirectoryPage();
        directoryPage.assertEmployeeFound(employeeFirstName, employeeLastName);

        String foundName = directoryPage.getFirstResultName();
        Allure.addAttachment(
            "Validacion exitosa",
            "Empleado encontrado en Directory: '" + foundName + "'" +
            "\nNombre esperado: '" + employeeFirstName + " " + employeeLastName + "'"
        );
    }
}
