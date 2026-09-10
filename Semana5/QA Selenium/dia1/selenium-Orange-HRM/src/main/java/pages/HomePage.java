package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.BasePage;

public class HomePage extends BasePage {

	private static final By DASHBOARD = By.className("oxd-topbar-header-title");

	private static final By PIM_PAGE = By.cssSelector("a[href*='viewPimModule']");

	public HomePage(WebDriver driver) {
		super(driver);
	}

	public String getDashBoardTitle() {
		return text(DASHBOARD);
	}

	public PimPage goToPim() {
		click(PIM_PAGE);
		return new PimPage(driver);
	}

}