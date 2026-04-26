package org.example.saucedemo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class InventoryPage extends BasePage
{
    @FindBy(className = "title")
    private WebElement title;

    @FindBy(className = "inventory_list")
    private WebElement inventoryList;

    public InventoryPage(WebDriver driver)
    {
        super(driver);
    }

    public InventoryPage waitUntilLoaded()
    {
        wait.until(ExpectedConditions.urlContains("/inventory.html"));
        wait.until(ExpectedConditions.visibilityOf(inventoryList));
        return this;
    }

    public String headerTitle()
    {
        return title.getText();
    }
}
