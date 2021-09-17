package com.youtrack;

import com.youtrack.common.BaseTest;
import com.youtrack.model.UserCreationInfo;
import com.youtrack.page.EditUserPage;
import io.qameta.allure.Story;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.nullValue;

@Slf4j
public class MaximumNumberOfUsersTest extends BaseTest {
    @BeforeEach
    public void beforeTests() {
        usersPage.open();
        editUserPage = new EditUserPage(driver);
    }

    @Test
    @Story("It is possible to create only 11 users")
    public void shouldNotCreateMoreThanSpecificNumberOfUsers() {

        String randomPassword = RandomStringUtils.randomNumeric(5);
        log.debug("Users on a page {}", usersPage.getUsers().size());
        for (int i = usersPage.getUsers().size(); i < 11; i++) {
            String randomNumber = RandomStringUtils.randomNumeric(5);
            UserCreationInfo userCreationInfo = UserCreationInfo.builder()
                    .login("user" + randomNumber + "__autotest")
                    .password(randomPassword)
                    .confirmPassword(randomPassword)
                    .build();

            usersPage.open();
            usersPage.clickCreateNewUser();
            createUserDialog.waitForLoaded();
            createUserDialog.createUser(userCreationInfo);
            editUserPage.waitForLoading();

        }
        usersPage.open();

        assertThat(usersPage.getUsersNumberFromSign(), containsString("11"));
        assertThat(usersPage.getCreateNewUserButton(), nullValue());

    }

    @AfterEach
    public void afterEach() {
        deleteUserFromTable("__autotest");
        closeBrowserAfterTest();
    }
}