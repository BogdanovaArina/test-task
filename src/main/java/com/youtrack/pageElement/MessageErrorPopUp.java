package com.youtrack.pageElement;

import com.youtrack.helper.Waiter;
import com.youtrack.helper.WebElementAssert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MessageErrorPopUp {
    private final Waiter waiter;
    private final WebElementAssert webElementAssert;

    @FindBy(xpath = "//*[@class='errorSeverity']")
    private WebElement warning;

    @FindBy(xpath = "//*[text()='Please change your password!']")
    private WebElement changePasswordWarning;


    public MessageErrorPopUp(WebDriver webDriver) {
        this.waiter = new Waiter(webDriver);
        this.webElementAssert = new WebElementAssert(webDriver);
        PageFactory.initElements(webDriver, this);
    }

    public String getWarningText() {
        waiter.waitForElementToBeVisible(warning);
        return warning.getText();
    }

    public void checkChangePasswordWarningAppears() {
        webElementAssert.assertElementPresent(this.changePasswordWarning);
    }
}
