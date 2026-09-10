package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AcercaDelBancoPage extends BasePage {

    private static final By ABOUT_MENU = By.id("firstLevel-mainItem-4-menu-button");
    private static final By FOUNDATION_LINK = By.cssSelector("a[href*='fundacion-santander.html']");
    private static final By BLOG_LINK = By.cssSelector("a[href*='blog.html']");
    private static final By SUSTAINABILITY_LINK = By.cssSelector("a[href*='responsabilidad-social.html']");
    private static final By FINANCIAL_EDUCATION_LINK = By.cssSelector("a[href*='educacion-financiera']");
    private static final By INVESTORS_LINK = By.cssSelector("a[href*='ir/home/']");
    private static final By NEWSROOM_LINK = By.cssSelector("a[href*='sala_prensa']");
    private static final By CAREERS_LINK = By.cssSelector("a[href*='bolsa-de-trabajo.html']");

    public AcercaDelBancoPage(WebDriver driver) {
        super(driver);
    }

    private void openAboutMenu() {
        click(ABOUT_MENU);
    }

    public void goToSantanderFoundation() {
        openAboutMenu();
        click(FOUNDATION_LINK);
    }

    public void goToBlog() {
        openAboutMenu();
        click(BLOG_LINK);
    }

    public void goToSustainability() {
        openAboutMenu();
        click(SUSTAINABILITY_LINK);
    }

    public void goToFinancialEducation() {
        openAboutMenu();
        click(FINANCIAL_EDUCATION_LINK);
    }

    public void goToInvestors() {
        openAboutMenu();
        click(INVESTORS_LINK);
    }

    public void goToNewsroom() {
        openAboutMenu();
        click(NEWSROOM_LINK);
    }

    public void goToCareers() {
        openAboutMenu();
        click(CAREERS_LINK);
    }
}
