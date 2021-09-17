package com.youtrack.page;

import com.youtrack.model.User;
import io.qameta.allure.Step;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.stream.Collectors;

import static com.youtrack.page.UsersPage.UserTableFields.*;

public class UsersPage extends BasePage {

    @FindBy(xpath = "//*[contains(@data-ring-dropdown,\"Profile\") and contains(@data-ring-dropdown,\"Log out\")]")
    private WebElement userDropDownMenu;

    @FindBy(xpath = "//*[text()=\"Log out\"]")
    private WebElement logOutButton;

    @FindBy(xpath = "//th/span[contains(text(),\"total\")]")
    private WebElement totalUsersNumber;


    public UsersPage(WebDriver webDriver, String baseUrl) {
        super(baseUrl, webDriver);
        PageFactory.initElements(webDriver, this);
    }

    private static User createUser(List<WebElement> userDataCells) {
        return User.builder()
                .login(userDataCells.get(LOGIN.getOrder()).getText())
                .fullName(userDataCells.get(FULL_NAME.getOrder()).getText())
                .contact(userDataCells.get(CONTACT.getOrder()).getText())
                .groups(userDataCells.get(GROUPS.getOrder()).getText())
                .lastAccess(userDataCells.get(LAST_ACCESS.getOrder()).getText())
                .build();
    }

    @Override
    protected String getPageUrlSuffix() {
        return "/users";
    }

    public WebElement getCreateNewUserButton() {
        try {
            return driver.findElement(By.xpath("//a[contains(@id,'createNewUser')]"));
        } catch (WebDriverException e) {
            return null;
        }
    }

    @Step("get users from the table")
    public List<User> getUsers() {
        List<WebElement> rawTableRows = driver.findElements(By.xpath("//table[@class='table users-table']/tbody/tr"));

        return rawTableRows.stream()
                .map(userDataRow -> userDataRow.findElements(By.tagName("td")))
                .map(userDataCells -> createUser(userDataCells))
                .collect(Collectors.toList());
    }


    @Step("click create new user")
    public void clickCreateNewUser() {
        WebElement createNewUserButton = getCreateNewUserButton();
        waiter.waitForElementToBeVisible(createNewUserButton);
        createNewUserButton.click();
    }

    public String getUsersNumberFromSign() {
        return totalUsersNumber.getText();
    }

    @Step("log out")
    public void logout() {
        super.open();
        waiter.waitForElementToBeVisible(userDropDownMenu);
        userDropDownMenu.click();
        waiter.waitForElementToBeVisible(logOutButton);
        logOutButton.click();
    }

    enum UserTableFields {
        LOGIN(0),
        FULL_NAME(1),
        CONTACT(2),
        GROUPS(3),
        LAST_ACCESS(4);

        @Getter
        private final int order;

        UserTableFields(int order) {
            this.order = order;
        }
    }
}
