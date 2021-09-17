package com.youtrack.pageElement;

import com.youtrack.helper.Waiter;
import com.youtrack.helper.WebElementAssert;
import com.youtrack.model.UserCreationInfo;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class CreateUserDialog {
    private final Waiter waiter;
    private final WebElementAssert webElementAssert;

    @FindBy(xpath = "//input[contains(@id,'login')]")
    private WebElement loginField;
    @FindBy(xpath = "//input[contains(@id,'password')]")
    private WebElement passwordField;
    @FindBy(xpath = "//input[contains(@id,'confirmPassword')]")
    private WebElement confirmPasswordField;
    @FindBy(xpath = "//input[contains(@id,'forcePasswordChange')]")
    private WebElement forcePasswordCheckbox;
    @FindBy(xpath = "//input[contains(@id,'fullName')]")
    private WebElement fullNameField;
    @FindBy(xpath = "//input[contains(@id,'email')]")
    private WebElement emailField;
    @FindBy(xpath = "//input[contains(@id,'jabber')]")
    private WebElement jabber;
    @FindBy(xpath = "//button[contains(@id,'createUserOk')]")
    private WebElement okButton;
    @CacheLookup
    @FindBy(xpath = "//button[contains(@id,'createUserCancel')]")
    private WebElement cancelButton;
    @CacheLookup
    @FindBy(xpath = "//*[contains(@id,\"closeCreateUserDlg\")]")
    private WebElement closeCreateUserWindow;
    @FindBy(className = "error-bulb2")
    private WebElement warningIcon;
    @FindBy(xpath = "//*[contains(@class, \"error-tooltip\")]")
    private WebElement warningMessage;

    public CreateUserDialog(WebDriver webDriver) {
        this.waiter = new Waiter(webDriver);
        this.webElementAssert = new WebElementAssert(webDriver);
        PageFactory.initElements(webDriver, this);
    }

    @Step("fill in user data {user}")
    public void fillInUserData(UserCreationInfo user) {
        String login = user.getLogin();
        if (login != null) {
            loginField.sendKeys(login);
        }

        String password = user.getPassword();
        if (password != null) {
            passwordField.sendKeys(password);
        }

        String confirmPassword = user.getConfirmPassword();
        if (confirmPassword != null) {
            confirmPasswordField.sendKeys(user.getConfirmPassword());
        }

        String fullName = user.getFullName();
        if (fullName != null) {
            fullNameField.sendKeys(fullName);
        }

        String email = user.getEmail();
        if (email != null) {
            emailField.sendKeys(email);
        }

        String jabber = user.getJabber();
        if (jabber != null) {
            this.jabber.sendKeys(jabber);
        }

        Boolean passwordChangeCheckbox = user.getPasswordChangeCheckbox();
        if (passwordChangeCheckbox != null && passwordChangeCheckbox) {
            forcePasswordCheckbox.click();
        }
    }

    @Step("Create user {user}")
    public String createUser(UserCreationInfo user) {
        fillInUserData(user);
        clickOk();
        return user.getLogin();
    }

    @Step("click OK")
    public void clickOk() {
        okButton.click();
    }

    @Step("click Cancel")
    public void clickCancel() {
        cancelButton.click();
    }

    @Step("check all fields are empty")
    public void checkAllFieldsAreEmpty() {
        waiter.waitForElementToBeVisible(loginField);
        waiter.waitForElementToBeVisible(passwordField);
        waiter.waitForElementToBeVisible(confirmPasswordField);

        assertEquals(loginField.getAttribute("value"), "");
        assertEquals(passwordField.getAttribute("value"), "");
        assertEquals(confirmPasswordField.getAttribute("value"), "");
        assertEquals(emailField.getAttribute("value"), "");
        assertEquals(jabber.getAttribute("value"), "");
        assertEquals(fullNameField.getAttribute("value"), "");
        assertFalse(forcePasswordCheckbox.isSelected());
    }

    @Step("close dialog")
    public void closeDialog() {
        closeCreateUserWindow.click();
    }

    @Step("wait until form is loaded")
    public void waitForLoaded() {
        waiter.waitForElementToBeVisible(loginField);
    }

    @Step("check warning icon is shown")
    public void checkWarningIconIsShown() {
        webElementAssert.assertElementPresent(warningIcon);
    }

    public String getWarningText() {
        waiter.waitForElementToBeVisible(warningIcon);
        warningIcon.click();
        waiter.waitForElementToBeVisible(warningMessage);
        return warningMessage.getText();
    }

}