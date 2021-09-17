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


public class WarningsTest extends BaseTest {
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

    static Stream<Arguments> loginValidationsDataProvider() {
        UserCreationInfo userInfoWithTakenLogin = UserCreationInfo.builder()
                .login("admin")
                .password("111")
                .confirmPassword("111")
                .build();

        UserCreationInfo userInfoWithSpaceInLogin = UserCreationInfo.builder()
                .login("user user")
                .password("111")
                .confirmPassword("111")
                .build();

        UserCreationInfo userInfoWithTagInLogin = UserCreationInfo.builder()
                .login("</>")
                .password("111")
                .confirmPassword("111")
                .build();


        return Stream.of(
                Arguments.arguments("Value should be unique", userInfoWithTakenLogin, "Not possible to create user with already taken login."),
                Arguments.arguments("Restricted character ' ' in the name", userInfoWithSpaceInLogin, "Not possible to create user with login that contains space."),
                Arguments.arguments("login shouldn't contain characters", userInfoWithTagInLogin, "Not possible to create user with login containing \"</>\" ")
        );
    }

    @BeforeEach
    public void beforeTests() {
        usersPage.open();
        usersPage.clickCreateNewUser();
        createUserDialog.waitForLoaded();
    }

    @MethodSource("passwordValidationDataProvider")
    @ParameterizedTest(name = "{2}")
    @Story("Check warnings appear")
    public void passwordWarningTests(String expectedWarningText, UserCreationInfo userInfo, String storyName) {
        createUserDialog.createUser(userInfo);
        createUserDialog.checkWarningIconIsShown();
        assertThat(createUserDialog.getWarningText(), containsString(expectedWarningText));
    }

    @MethodSource("loginValidationsDataProvider")
    @ParameterizedTest(name = "{2}")
    @Story("Check warnings appear")
    public void loginWarningTests(String expectedWarningText, UserCreationInfo userInfo, String storyName) {
        MessageErrorPopUp messageErrorPopUp = new MessageErrorPopUp(driver);

        createUserDialog.createUser(userInfo);

        assertThat(messageErrorPopUp.getWarningText(), containsString(expectedWarningText));
    }

    @Test
    @Story("Trying to create user without login")
    public void loginShouldBeRequired() {
        createUserDialog.clickOk();
        assertThat(createUserDialog.getWarningText(), containsString("Login is required!"));
    }

    @AfterEach
    public void afterEach() {
        closeBrowserAfterTest();
    }

}
