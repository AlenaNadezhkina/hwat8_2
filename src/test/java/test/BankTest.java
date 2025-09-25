package test;

import data.DataHelper;
import data.SQLHelper;
import org.junit.jupiter.api.*;
import page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static data.SQLHelper.cleanAuthCodes;
import static data.SQLHelper.cleanDatabase;

public class BankTest {
    LoginPage loginPage;
    DataHelper.AuthInfo authInfo = DataHelper.getAuthInfoWithTestData();

    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
    }

    @AfterEach
    void tearDown() {
        cleanAuthCodes();
    }

    @BeforeEach
    void setUp() {
        loginPage = open("http://localhost:9999", LoginPage.class);
    }

    @Test
    @DisplayName("Should successfully login to dashboard with exist login and password from sut test data")
    void shouldSuccessfulLogin() {
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = SQLHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    @DisplayName("Should get error notification if user is not exist in base")
    void shouldGetErrorNotificationIfLoginWithRandomUserWithoutAddingToBase() {
        var authInfo = DataHelper.generateRandomUser();
        loginPage.login(authInfo);
        loginPage.verifyErrorNotification("Ошибка! \nНеверно указан погин или пароль");
    }
}
