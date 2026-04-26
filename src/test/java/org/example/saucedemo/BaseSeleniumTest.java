package org.example.saucedemo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public abstract class BaseSeleniumTest
{
    protected WebDriver driver;

    @BeforeEach
    void startBrowser()
    {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--lang=en-US");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void quitBrowser()
    {
        if (driver != null) {
            driver.quit();
        }
    }
}
