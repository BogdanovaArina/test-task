package com.youtrack.pageElement;

import com.youtrack.helper.WebElementAssert;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.openqa.selenium.support.PageFactory.initElements;

public class Header {
    private final WebDriver driver;
    private final WebElementAssert webElementAssert;

    public Header(WebDriver webDriver) {
        this.driver = webDriver;
        this.webElementAssert = new WebElementAssert(driver);
        initElements(driver, this);
    }

    @Step("check user is logged in")
    public void checkUserLoggedIn(String userLogin) {
        webElementAssert.assertElementPresent(driver.findElement(By.xpath("//span[text()=\"" + userLogin + "\"]")));
    }
}
