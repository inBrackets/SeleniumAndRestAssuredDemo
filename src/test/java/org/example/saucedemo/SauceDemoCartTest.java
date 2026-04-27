package org.example.saucedemo;

import static org.assertj.core.api.Assertions.assertThat;

import org.example.saucedemo.pages.CartPage;
import org.example.saucedemo.pages.InventoryItem;
import org.example.saucedemo.pages.InventoryPage;
import org.example.saucedemo.pages.LoginPage;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SauceDemoCartTest extends BaseSeleniumTest
{
    private static final String USERNAME = "standard_user";
    private static final String PASSWORD = "secret_sauce";

    @ParameterizedTest(name = "standard_user adds product[{0}] to cart")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void standardUser_addsItemAtIndexToCart(int index)
    {
        InventoryPage inventory = new LoginPage(driver).open()
                .loginExpectingSuccess(USERNAME, PASSWORD);

        InventoryItem item = inventory.item(index);
        String name = item.name();

        assertThat(name).isNotBlank();
        assertThat(item.description()).isNotBlank();
        assertThat(item.price()).isNotBlank();

        item.addToCart();
        CartPage cart = inventory.openCart();

        assertThat(cart.itemNames()).containsExactly(name);
    }
}
