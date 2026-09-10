package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PymesPage extends BasePage {

    private static final By SMES_MENU = By.id("firstLevel-mainItem-2-menu-button");
    private static final By SANTANDER_SME_LINK = By.cssSelector("a[href$='/pyme/']");
    private static final By FX_HEDGING_LINK = By.cssSelector("a[href*='coberturas-y-cambios.html']");
    private static final By ACCOUNTS_LINK = By.cssSelector("a[href*='pyme/cuentas.html']");
    private static final By INTERNATIONAL_BUSINESS_LINK = By.cssSelector("a[href*='negocio-internacional.html']");
    private static final By PACKAGES_LINK = By.cssSelector("a[href*='paquetes-pymes.html']");
    private static final By LOANS_LINK = By.cssSelector("a[href*='pyme/creditos.html']");
    private static final By INSURANCE_LINK = By.cssSelector("a[href*='pyme/seguros.html']");
    private static final By PARTNERSHIPS_LINK = By.cssSelector("a[href*='alianzas.html']");
    private static final By TRANSACTIONAL_BUSINESS_LINK = By.cssSelector("a[href*='negocio-transaccional.html']");
    private static final By FINANCIAL_ECOSYSTEM_LINK = By.cssSelector("a[href*='ecosistemas-pyme.html']");
    private static final By INVESTMENTS_LINK = By.cssSelector("a[href*='pyme/inversiones.html']");

    public PymesPage(WebDriver driver) {
        super(driver);
    }

    private void openSmesMenu() {
        click(SMES_MENU);
    }

    public void goToSantanderSme() {
        openSmesMenu();
        click(SANTANDER_SME_LINK);
    }

    public void goToForeignExchangeAndHedging() {
        openSmesMenu();
        click(FX_HEDGING_LINK);
    }

    public void goToSmeAccounts() {
        openSmesMenu();
        click(ACCOUNTS_LINK);
    }

    public void goToInternationalBusiness() {
        openSmesMenu();
        click(INTERNATIONAL_BUSINESS_LINK);
    }

    public void goToSmePackages() {
        openSmesMenu();
        click(PACKAGES_LINK);
    }

    public void goToSmeLoans() {
        openSmesMenu();
        click(LOANS_LINK);
    }

    public void goToSmeInsurance() {
        openSmesMenu();
        click(INSURANCE_LINK);
    }

    public void goToPartnerships() {
        openSmesMenu();
        click(PARTNERSHIPS_LINK);
    }

    public void goToTransactionalBusiness() {
        openSmesMenu();
        click(TRANSACTIONAL_BUSINESS_LINK);
    }

    public void goToFinancialEcosystem() {
        openSmesMenu();
        click(FINANCIAL_ECOSYSTEM_LINK);
    }

    public void goToSmeInvestments() {
        openSmesMenu();
        click(INVESTMENTS_LINK);
    }
}
