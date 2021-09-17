package com.youtrack.pageElement;

import com.youtrack.helper.WebElementAssert;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class ChangePasswordDialog {
    private final WebElementAssert assertElementPresent;
    @FindBy(xpath = "//*[contains(@id,\"ChangePasswordDialog\")]")
    private WebElement changePasswordDialog;
    @FindBy(xpath = "//input[contains(@id,\"oldPassword\")]")
    private WebElement oldPasswordInput;
    @FindBy(xpath = "//input[contains(@id,\"newPassword1\")]")
    private WebElement newPasswordInput;
    @FindBy(xpath = "//input[contains(@id,\"newPassword2\")]")
    private WebElement confirmNewPasswordInput;
    @FindBy(xpath = "//*[contains(@id,\"passOk\")]")
    private WebElement saveNewPassword;

    public ChangePasswordDialog(WebDriver webDriver) {
        this.assertElementPresent = new WebElementAssert(webDriver);
        PageFactory.initElements(webDriver, this);
    }

    @Step("check password dialog appears")
    public void checkChangePasswordDialogAppears() {
        assertElementPresent.assertElementPresent(changePasswordDialog);
    }

    @Step("set new password")
    public void setNewPassword(String oldPassword, String newPassword) {
        oldPasswordInput.sendKeys(oldPassword);
        newPasswordInput.sendKeys(newPassword);
        confirmNewPasswordInput.sendKeys(newPassword);
        saveNewPassword.click();
    }
}
