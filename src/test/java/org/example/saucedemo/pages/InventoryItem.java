package org.example.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class InventoryItem
{
    private static final By NAME = By.className("inventory_item_name");
    private static final By DESCRIPTION = By.className("inventory_item_desc");
    private static final By PRICE = By.className("inventory_item_price");
    private static final By ADD_BUTTON = By.cssSelector("button.btn_inventory");

    private final WebElement root;

    public InventoryItem(WebElement root)
    {
        this.root = root;
    }

    public String name()
    {
        return root.findElement(NAME).getText();
    }

    public String description()
    {
        return root.findElement(DESCRIPTION).getText();
    }

    public String price()
    {
        return root.findElement(PRICE).getText();
    }

    public void addToCart()
    {
        root.findElement(ADD_BUTTON).click();
    }
}
