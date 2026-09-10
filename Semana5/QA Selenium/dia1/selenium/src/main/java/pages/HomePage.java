package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.BasePage;

public class HomePage extends BasePage {

	private static final By PRODUCTS_TITLE = By.className("title");

	private static final By ADD_PRODUCT =
			By.id("add-to-cart-sauce-labs-backpack");

	private static final By CART_ICON =
			By.className("shopping_cart_link");

	public HomePage(WebDriver driver) {
		super(driver);
	}

	public String getProductsTitle() {
		return text(PRODUCTS_TITLE);
	}

	public HomePage addProduct() {
		click(ADD_PRODUCT);
		System.out.println("Adding product");
		return this;
	}

	public CartPage goToCart() {
		click(CART_ICON);
		return new CartPage(driver);
	}
}