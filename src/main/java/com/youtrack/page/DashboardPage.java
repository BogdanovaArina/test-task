package com.youtrack.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class DashboardPage extends BasePage {

    public DashboardPage(WebDriver webDriver, String baseUrl) {
        super(baseUrl, webDriver);
        PageFactory.initElements(webDriver, this);
    }

    @Override
    protected String getPageUrlSuffix() {
        return "/dashboard";
    }

}
