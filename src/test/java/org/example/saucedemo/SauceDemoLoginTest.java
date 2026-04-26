package org.example.saucedemo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.saucedemo.pages.InventoryPage;
import org.example.saucedemo.pages.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SauceDemoLoginTest extends BaseSeleniumTest
{
    private static final String PASSWORD = "secret_sauce";

    @ParameterizedTest(name = "{0} logs in and lands on inventory")
    @ValueSource(strings = {
            "standard_user",
            "problem_user",
            "performance_glitch_user",
            "error_user",
            "visual_user"
    })
    void user_logsInAndLandsOnInventory(String username)
    {
        InventoryPage inventory = new LoginPage(driver).open()
                .loginExpectingSuccess(username, PASSWORD);
        assertEquals("Products", inventory.headerTitle());
    }

    @Test
    void lockedOutUser_seesLockoutError()
    {
        LoginPage login = new LoginPage(driver).open()
                .loginExpectingFailure("locked_out_user", PASSWORD);
        assertTrue(login.errorText().toLowerCase().contains("locked out"),
                "Expected lockout message, got: " + login.errorText());
    }
}
