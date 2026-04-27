package org.example.saucedemo.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CartPage extends BasePage
{
    private static final By ITEM_NAME = By.className("inventory_item_name");

    @FindBy(className = "cart_list")
    private WebElement cartList;

    public CartPage(WebDriver driver)
    {
        super(driver);
    }

    public CartPage waitUntilLoaded()
    {
        wait.until(ExpectedConditions.urlContains("/cart.html"));
        wait.until(ExpectedConditions.visibilityOf(cartList));
        return this;
    }

    public List<String> itemNames()
    {
        return cartList.findElements(ITEM_NAME).stream()
                .map(WebElement::getText)
                .toList();
    }
}
