package com.youtrack.common;

import com.youtrack.page.DashboardPage;
import com.youtrack.page.EditUserPage;
import com.youtrack.page.LoginPage;
import com.youtrack.page.UsersPage;
import com.youtrack.pageElement.CreateUserDialog;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.PageLoadStrategy.EAGER;

@Slf4j
public abstract class BaseTest {
    private static final String XPATH_TEMPLATE_TO_REMOVE_USER_BUTTON = "//*[contains(@id, 'UserLogin') and contains(text(),'%s')]/../..//*[contains(@id,'deleteUser')]";

    private static String applicationBaseUrl;
    private static String adminUserLogin;
    private static String adminUserPassword;

    protected WebDriver driver;
    protected CreateUserDialog createUserDialog;
    protected UsersPage usersPage;
    protected LoginPage loginPage;
    protected EditUserPage editUserPage;
    protected DashboardPage dashboardPage;

    @BeforeAll
    public static void beforeClass() {
        applicationBaseUrl = Optional.ofNullable(System.getProperty("applicationBaseUrl")).orElse("http://localhost:8080");
        adminUserLogin = Optional.ofNullable(System.getProperty("adminUserLogin")).orElse("admin");
        adminUserPassword = Optional.ofNullable(System.getProperty("adminUserPassword")).orElse("111");
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void preCondition() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("enable-automation");
        options.addArguments("--headless");
        options.setPageLoadStrategy(EAGER);

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(3, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(3, TimeUnit.SECONDS);

        usersPage = new UsersPage(driver, applicationBaseUrl);
        dashboardPage = new DashboardPage(driver, applicationBaseUrl);
        loginPage = new LoginPage(driver, applicationBaseUrl, adminUserLogin, adminUserPassword);
        createUserDialog = new CreateUserDialog(driver);

        loginPage.loginAsAdmin();
        dashboardPage.waitForLoading();
    }


    protected void closeBrowserAfterTest() {
        log.debug("Closing web driver");
        driver.quit();
    }

    protected void deleteUserFromTable(String partOfLogin) {
        usersPage.open();
        List<WebElement> permissionDenidedWebElement = driver.findElements(By.className("errorPage-content"));
        if (!permissionDenidedWebElement.isEmpty()) {
            loginPage.loginAsAdmin();
            usersPage.open();
        }

        try {
            List<WebElement> deleteUserButtons;
            do {
                String xpathToDeleteUserButton = String.format(XPATH_TEMPLATE_TO_REMOVE_USER_BUTTON, partOfLogin);
                deleteUserButtons = driver.findElements(By.xpath(xpathToDeleteUserButton));
                deleteUserButtons.get(0).click();
                log.debug("clicked to delete {}", partOfLogin);
                driver.switchTo().alert().accept();
                log.debug("alert ok");
                usersPage.open();
            } while (deleteUserButtons.size() != 1);
        } catch (Exception e) {
            log.warn("Couldn't remove user with login " + partOfLogin + "from the page", e);
        }
        log.debug("User was removed");
    }
}