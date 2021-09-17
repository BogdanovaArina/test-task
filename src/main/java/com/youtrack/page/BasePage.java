package com.youtrack.page;

import com.youtrack.helper.Waiter;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

public abstract class BasePage {
    protected final String pageUrl;
    protected final WebDriver driver;
    protected final Waiter waiter;


    protected BasePage(String baseUrl, WebDriver driver) {
        this.pageUrl = baseUrl + getPageUrlSuffix();
        this.driver = driver;
        this.waiter = new Waiter(driver);
    }


    protected abstract String getPageUrlSuffix();

    @Step("open page {this.pageUrl}")
    public void open() {
        driver.get(pageUrl);
        waitForLoading();
    }

    @Step("wait for loading of the {this.pageUrl}")
    public void waitForLoading() {
        waiter.waitForUrlToBe(pageUrl);
    }
}
