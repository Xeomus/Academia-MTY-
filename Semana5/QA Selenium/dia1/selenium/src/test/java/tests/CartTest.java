package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.CartPage;
import pages.HomePage;
import pages.LoginPage;

public class CartTest extends BaseTest {

    @Test
    public void shouldAddAndRemoveProductFromCart() {

        LoginPage loginPage = new LoginPage(driver);

        HomePage homePage = loginPage.login(
                "standard_user",
                "secret_sauce"
        );

        CartPage cartPage = homePage
                .addProduct()
                .goToCart();

        cartPage.removeBackpack();

        Assert.assertTrue(
                cartPage.isCartEmpty(),
                "The cart is empty"
        );
    }
}