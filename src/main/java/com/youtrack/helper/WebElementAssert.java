package com.youtrack.helper;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WebElementAssert {

    private final Waiter waiter;

    public WebElementAssert(WebDriver driver) {
        this.waiter = new Waiter(driver);
    }

    public void assertElementPresent(WebElement element) {
        try {
            waiter.waitForElementToBeVisible(element);
        } catch (NoSuchElementException e) {
            throw new AssertionError("Element " + element + " is not present on " +
                    "the page");
        }
    }
}
