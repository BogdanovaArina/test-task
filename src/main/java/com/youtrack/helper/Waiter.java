package com.youtrack.helper;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Waiter {
    private final WebDriverWait wait;

    public Waiter(WebDriver driver) {
        this.wait = new WebDriverWait(driver, 5);
    }

    public void waitForElementToBeVisible(WebElement element) {
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            throw new NoSuchElementException("Element is not visible on the page" + element, e);
        }
    }

    public void waitForUrlToBe(String url) {
        wait.until(ExpectedConditions.urlToBe(url));
    }
}
