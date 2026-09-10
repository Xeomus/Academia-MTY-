package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BancaPrivadaPage extends BasePage {

    private static final By PRIVATE_BANKING_LINK = By.cssSelector("a[href*='/bp/home/']");

    public BancaPrivadaPage(WebDriver driver) {
        super(driver);
    }

    public void goToPrivateBankingHome() {
        click(PRIVATE_BANKING_LINK);
    }
}
