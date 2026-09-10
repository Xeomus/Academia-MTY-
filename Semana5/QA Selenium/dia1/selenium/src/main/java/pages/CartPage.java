package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import base.BasePage;

public class CartPage extends BasePage {

    private static final By REMOVE_BACKPACK_BUTTON = By.id("remove-sauce-labs-backpack");

    private static final By CART_ITEM = By.className("cart_item");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public CartPage removeBackpack() {
        click(REMOVE_BACKPACK_BUTTON);
        System.out.println("Removing backpack");
        return this;
    }

    public boolean isCartEmpty() {
        return driver.findElements(CART_ITEM).isEmpty();
    }
}
