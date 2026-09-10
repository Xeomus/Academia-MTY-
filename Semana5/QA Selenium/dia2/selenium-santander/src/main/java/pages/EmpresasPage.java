package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class EmpresasPage extends BasePage {

    private static final By COMPANIES_MENU = By.id("firstLevel-mainItem-1-menu-button");
    private static final By GOVERNMENT_LINK = By.cssSelector("a[href*='bei/home.html']");
    private static final By MULTINATIONALS_LINK = By.cssSelector("a[href*='multinacionales.html']");

    public EmpresasPage(WebDriver driver) {
        super(driver);
    }

    private void openCompaniesMenu() {
        click(COMPANIES_MENU);
    }

    public void goToCompaniesAndGovernment() {
        openCompaniesMenu();
        click(GOVERNMENT_LINK);
    }

    public void goToMultinationals() {
        openCompaniesMenu();
        click(MULTINATIONALS_LINK);
    }
}
