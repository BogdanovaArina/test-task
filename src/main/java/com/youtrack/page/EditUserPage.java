package com.youtrack.page;

import com.youtrack.helper.Waiter;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class EditUserPage {
    private final Waiter waiter;


    @FindBy(xpath = "//div[contains(@id,'editUserPanel')]")
    private WebElement editUserPageTitle;

    public EditUserPage(WebDriver webDriver) {
        this.waiter = new Waiter(webDriver);
        PageFactory.initElements(webDriver, this);
    }

    @Step("wait for loading of edit user page")
    public void waitForLoading() {
        waiter.waitForElementToBeVisible(this.editUserPageTitle);
    }

}
