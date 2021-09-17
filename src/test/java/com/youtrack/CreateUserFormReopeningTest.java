package com.youtrack;

import com.youtrack.common.BaseTest;
import com.youtrack.model.UserCreationInfo;
import com.youtrack.pageElement.MessageErrorPopUp;
import io.qameta.allure.Story;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;


public class CreateUserFormReopeningTest extends BaseTest {
    @BeforeEach
    public void beforeTests() {
        usersPage.open();
        usersPage.clickCreateNewUser();
        createUserDialog.waitForLoaded();
    }

    static Stream<Arguments> passwordValidationDataProvider() {
        UserCreationInfo userWithoutPassword = UserCreationInfo.builder()
                .login("user_for_password_required_autotest")
                .build();

        UserCreationInfo userWithDifferentPasswords = UserCreationInfo.builder()
                .login("user_for_passwords_match_autotest")
                .password("111")
                .build();


        return Stream.of(
                Arguments.arguments("Password is required!", userWithoutPassword, "Trying to create user without password."),
                Arguments.arguments("Password doesn't match!", userWithDifferentPasswords, "Trying to create user with different password and confirm password values.")
        );
    }

    @Test
    @Story("Create User Dialog is cleaned after closing")
    public void noInfoInCreateUserFormAfterClosing() {
        UserCreationInfo userInfo = UserCreationInfo.builder()
                .login("User_close_form")
                .password("111")
                .confirmPassword("111")
                .passwordChangeCheckbox(true)
                .fullName("User full name")
                .email("u@gmail.com")
                .jabber("User Jabber")
                .build();

        createUserDialog.fillInUserData(userInfo);
        createUserDialog.closeDialog();
        usersPage.clickCreateNewUser();
        createUserDialog.checkAllFieldsAreEmpty();
    }

    @Test
    @Story("Create User Dialog is cleaned after cancelling")
    public void noInfoInCreateUserFormAfterClickToCancel() {

        UserCreationInfo userInfo = UserCreationInfo.builder()
                .login("user_click_cancel")
                .password("111")
                .confirmPassword("111")
                .passwordChangeCheckbox(true)
                .fullName("User full name")
                .email("u@gmail.com")
                .jabber("User Jabber")
                .build();

        createUserDialog.fillInUserData(userInfo);
        createUserDialog.clickCancel();
        usersPage.clickCreateNewUser();
        createUserDialog.checkAllFieldsAreEmpty();
    }


    @AfterEach
    public void afterEach() {
        closeBrowserAfterTest();
    }

}
