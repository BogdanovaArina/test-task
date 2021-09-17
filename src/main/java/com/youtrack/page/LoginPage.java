package com.youtrack.page;

import com.youtrack.model.UserCreationInfo;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@Slf4j
public class LoginPage extends BasePage {
    private final String adminUserLogin;
    private final String adminUserPassword;

    @FindBy(xpath = "//input[contains(@id,'login')]")
    private WebElement loginField;

    @FindBy(xpath = "//input[contains(@id,'password')]")
    private WebElement passwordField;

    @FindBy(xpath = "//input[contains(@id,'loginButton')]")
    private WebElement logInButton;

    public LoginPage(WebDriver webDriver, String applicationBaseUrl, String adminUserLogin, String adminUserPassword) {
        super(applicationBaseUrl, webDriver);
        this.adminUserLogin = adminUserLogin;
        this.adminUserPassword = adminUserPassword;
        PageFactory.initElements(webDriver, this);
    }

    @Override
    protected String getPageUrlSuffix() {
        return "/login";
    }

    @Step("login as admin")
    public void loginAsAdmin() {
        log.debug("Opening admin page...");
        super.open();
        log.debug("Logging as an admin");
        loginField.sendKeys(adminUserLogin);
        passwordField.sendKeys(adminUserPassword);
        logInButton.click();

    }

    @Step("login using {user}")
    public void login(UserCreationInfo user) {
        super.open();
        loginField.sendKeys(user.getLogin());
        passwordField.sendKeys(user.getPassword());
        logInButton.click();
    }
}
