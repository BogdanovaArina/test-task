package com.youtrack;

import com.youtrack.common.BaseTest;
import com.youtrack.helper.StringProvider;
import com.youtrack.model.User;
import com.youtrack.model.UserCreationInfo;
import com.youtrack.page.EditUserPage;
import com.youtrack.pageElement.ChangePasswordDialog;
import com.youtrack.pageElement.Header;
import com.youtrack.pageElement.MessageErrorPopUp;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;


@Slf4j
@Feature("Create new user feature")
public class CreateUserTest extends BaseTest {

    private String createdUserLogin;

    static Stream<UserCreationInfo> userCreationInfoProvider() {

        StringProvider randomAlphabetic = () -> RandomStringUtils.randomAlphabetic(1, 50);
        StringProvider randomNumeric = () -> RandomStringUtils.randomNumeric(1, 50);
        StringProvider randomSpecialSymbolsWithoutTags = () -> RandomStringUtils.random(15, "$&@?~!%#".toCharArray());
        StringProvider randomSpecialSymbols = () -> RandomStringUtils.random(15, "$&@?~!%#</>");
        StringProvider randomMixedSymbols = () -> RandomStringUtils.randomAlphabetic(20) + RandomStringUtils.randomNumeric(20) + RandomStringUtils.random(10, "$&@?~!%#".toCharArray());

        UserCreationInfo userWithAlphabeticFields = createUserWithRequiredFields(randomAlphabetic);
        UserCreationInfo userWithNumericFields = createUserWithRequiredFields(randomNumeric);
        UserCreationInfo userWithSpecialSymbolsFields = createUserWithRequiredFields(randomSpecialSymbolsWithoutTags, randomSpecialSymbols);
        UserCreationInfo userWithMixedFields = createUserWithRequiredFields(randomMixedSymbols);

        return Stream.of(
                userWithAlphabeticFields,
                userWithNumericFields,
                userWithSpecialSymbolsFields,
                userWithMixedFields
        );
    }

    private static UserCreationInfo createUserWithRequiredFields(StringProvider loginProvider, StringProvider fieldDataProvider) {
        String login = loginProvider.get();
        String password = fieldDataProvider.get();
        String fullName = fieldDataProvider.get();
        String email = fieldDataProvider.get();
        String jabber = fieldDataProvider.get();
        return UserCreationInfo.builder()
                .login(login)
                .password(password)
                .confirmPassword(password)
                .fullName(fullName)
                .email(email)
                .jabber(jabber)
                .build();
    }

    private static UserCreationInfo createUserWithRequiredFields(StringProvider fieldDataProvider) {
        return createUserWithRequiredFields(fieldDataProvider, fieldDataProvider);
    }

    @BeforeEach
    public void beforeTests() {
        editUserPage = new EditUserPage(driver);
        usersPage.open();
        usersPage.clickCreateNewUser();
        createUserDialog.waitForLoaded();
    }

    @Story("Creation of user with all fields filled in with different types of data: alphabetic, numeric, special symbols and mixed")
    @MethodSource("userCreationInfoProvider")
    @ParameterizedTest
    public void shouldCreateUserWithAllFields(UserCreationInfo userInfo) {
        User expectedUser = User.builder()
                .login(userInfo.getLogin())
                .fullName(userInfo.getFullName())
                .contact(userInfo.getEmail() + "\n" + userInfo.getJabber())
                .groups("All Users, New Users")
                .lastAccess("—")
                .build();

        createUserDialog.createUser(userInfo);

        //save user login in order to delete it later
        createdUserLogin = userInfo.getLogin();

        editUserPage.waitForLoading();
        usersPage.open();

        List<User> existingUsers = usersPage.getUsers();

        assertThat("Created user was not found in Users table", existingUsers, hasItem(expectedUser));
    }

    @Test
    @Story("Create user with required fields")
    public void shouldCreateUserWithRequiredFields() {
        UserCreationInfo userInfo = UserCreationInfo.builder()
                .login("user_with_required_fields" + RandomStringUtils.randomNumeric(3) + "__autotest")
                .password("333")
                .confirmPassword("333")
                .passwordChangeCheckbox(false)
                .build();

        User expectedUser = User.builder()
                .login(userInfo.getLogin())
                .fullName(userInfo.getLogin())
                .contact("")
                .groups("All Users, New Users")
                .lastAccess("—")
                .build();

        createUserDialog.createUser(userInfo);

        //save user login in order to delete it later
        createdUserLogin = userInfo.getLogin();

        editUserPage.waitForLoading();
        usersPage.open();
        List<User> existingUsers = usersPage.getUsers();
        assertThat("Created user was not found in Users table", existingUsers, hasItem(expectedUser));
    }

    @Test
    @Story("Creating user with force password checkbox.")
    public void checkForcePassword() {
        ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog(driver);
        MessageErrorPopUp messageErrorPopUp = new MessageErrorPopUp(driver);
        Header header = new Header(driver);

        UserCreationInfo userInfo = UserCreationInfo.builder()
                .login("user_force_password" + RandomStringUtils.randomNumeric(3) + "__autotest")
                .password("111")
                .confirmPassword("111")
                .passwordChangeCheckbox(true)
                .build();

        //save user login in order to delete it later
        createdUserLogin = createUserDialog.createUser(userInfo);
        log.debug("user {} created", createdUserLogin);
        usersPage.logout();
        loginPage.login(userInfo);

        messageErrorPopUp.checkChangePasswordWarningAppears();
        changePasswordDialog.checkChangePasswordDialogAppears();
        changePasswordDialog.setNewPassword(userInfo.getPassword(), "222");
        header.checkUserLoggedIn(userInfo.getLogin());
    }

    @AfterEach
    public void afterEach() {
        deleteUserFromTable(createdUserLogin);
        closeBrowserAfterTest();
    }

}
