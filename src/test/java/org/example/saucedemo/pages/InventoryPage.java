package org.example.saucedemo.pages;

import java.util.List;

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

    @FindBy(className = "inventory_item")
    private List<WebElement> inventoryItems;

    @FindBy(className = "shopping_cart_link")
    private WebElement cartLink;

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

    public InventoryItem item(int index)
    {
        return new InventoryItem(inventoryItems.get(index));
    }

    public CartPage openCart()
    {
        cartLink.click();
        return new CartPage(driver).waitUntilLoaded();
    }
}
