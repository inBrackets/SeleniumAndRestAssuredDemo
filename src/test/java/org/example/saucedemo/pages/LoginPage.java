package org.example.saucedemo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage
{
    public static final String URL = "https://www.saucedemo.com/";

    @FindBy(id = "user-name")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(id = "login-button")
    private WebElement loginButton;

    @FindBy(css = "[data-test='error']")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver)
    {
        super(driver);
    }

    public LoginPage open()
    {
        driver.get(URL);
        wait.until(ExpectedConditions.visibilityOf(usernameField));
        return this;
    }

    public InventoryPage loginExpectingSuccess(String username, String password)
    {
        submitCredentials(username, password);
        return new InventoryPage(driver).waitUntilLoaded();
    }

    public LoginPage loginExpectingFailure(String username, String password)
    {
        submitCredentials(username, password);
        wait.until(ExpectedConditions.visibilityOf(errorMessage));
        return this;
    }

    public String errorText()
    {
        return errorMessage.getText();
    }

    private void submitCredentials(String username, String password)
    {
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();
    }
}
