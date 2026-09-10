package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PersonasPage extends BasePage {

    private static final By PEOPLE_MENU = By.id("firstLevel-mainItem-0-menu-button");

    private static final By CREDIT_CARDS_LINK = By.cssSelector("a[href*='tarjetas-de-credito']");
    private static final By PERSONAL_LOAN_LINK = By.cssSelector("a[href*='creditos-personales']");
    private static final By MORTGAGE_LINK = By.cssSelector("a[href$='creditos-hipotecarios/']");
    private static final By MORTGAGE_SIMULATOR_LINK = By.cssSelector("a[href*='simulador-hipotecario']");
    private static final By AUTO_LOAN_LINK = By.cssSelector("a[href*='credito-automotriz']");
    private static final By CREDIT_BUREAU_LINK = By.cssSelector("a[href*='buro-de-credito']");

    private static final By DIGITAL_BANKING_LINK = By.cssSelector("a[href$='santander-digital/']");
    private static final By SANTANDER_APP_LINK = By.cssSelector("a[href*='app-santander']");
    private static final By SANTANDER_WEB_LINK = By.cssSelector("a[href*='santander-web']");
    private static final By TRANSACTION_LIMIT_LINK = By.cssSelector("a[href*='limite-por-transaccion']");

    private static final By ACCOUNTS_LINK = By.cssSelector("a[href$='personas/cuentas/']");
    private static final By BASIC_ACCOUNT_LINK = By.cssSelector("a[href*='cuentas/basica/']");
    private static final By PAYROLL_ACCOUNT_LINK = By.cssSelector("a[href*='basica-nomina']");
    private static final By CHECKING_ACCOUNT_LINK = By.cssSelector("a[href*='cheque-saldo-promedio']");
    private static final By PAYROLL_PORTABILITY_LINK = By.cssSelector("a[href*='portabilidad-de-nomina']");

    private static final By INVESTMENT_FUNDS_LINK = By.cssSelector("a[href*='#fondos-de-inversion']");
    private static final By TERM_INVESTMENTS_LINK = By.cssSelector("a[href*='#inversiones-a-plazo']");
    private static final By STRUCTURED_NOTES_LINK = By.cssSelector("a[href*='#notas-estructuradas']");

    private static final By AUTO_INSURANCE_LINK = By.cssSelector("a[href*='seguros.html#auto']");
    private static final By LIFE_INSURANCE_LINK = By.cssSelector("a[href*='seguros.html#vida']");
    private static final By HOME_INSURANCE_LINK = By.cssSelector("a[href*='seguros.html#hogar']");
    private static final By SAVINGS_INSURANCE_LINK = By.cssSelector("a[href*='seguros.html#ahorro']");
    private static final By MEDICAL_INSURANCE_LINK = By.cssSelector("a[href*='seguros.html#gastos-medicos']");
    private static final By BELONGINGS_INSURANCE_LINK = By.cssSelector("a[href*='seguros.html#pertenencias']");

    private static final By SUPERLINE_LINK = By.cssSelector("a[href*='superlinea.html']");
    private static final By BRANCHES_LINK = By.cssSelector("a[href*='sucursales.html']");
    private static final By ATMS_LINK = By.cssSelector("a[href*='cajeros-automaticos.html']");
    private static final By ALTERNATIVE_CHANNELS_LINK = By.cssSelector("a[href*='operaciones-canales-alternos']");
    private static final By HELP_CENTER_LINK = By.cssSelector("a[href*='centro-de-ayuda.html']");
    private static final By SECURITY_CENTER_LINK = By.cssSelector("a[href*='centro-de-seguridad']");
    private static final By TUTORIALS_LINK = By.cssSelector("a[href*='tutoriales.html']");
    private static final By TERMS_LINK = By.cssSelector("a[href*='terminos-y-condiciones.html']");
    private static final By REGULATION_LINK = By.cssSelector("a[href*='tramite-por-defuncion.html']");

    private static final By SELECT_LINK = By.cssSelector("a[href*='select.html']");
    private static final By PROMOTIONS_LINK = By.cssSelector("a[href$='promociones/']");
    private static final By UNIQUE_REWARDS_LINK = By.cssSelector("a[href*='uniquerewards']");
    private static final By GROUPS_LINK = By.cssSelector("a[href*='colectivos.html']");
    private static final By HOME_WORLD_LINK = By.cssSelector("a[href*='mundohogar']");
    private static final By CASHBACK_LINK = By.cssSelector("a[href*='cashback.html']");

    public PersonasPage(WebDriver driver) {
        super(driver);
    }

    private void openPeopleMenu() {
        click(PEOPLE_MENU);
    }

    public void goToCreditCards() {
        openPeopleMenu();
        click(CREDIT_CARDS_LINK);
    }

    public void goToPersonalLoan() {
        openPeopleMenu();
        click(PERSONAL_LOAN_LINK);
    }

    public void goToMortgageLoan() {
        openPeopleMenu();
        click(MORTGAGE_LINK);
    }

    public void goToMortgageSimulator() {
        openPeopleMenu();
        click(MORTGAGE_SIMULATOR_LINK);
    }

    public void goToAutoLoan() {
        openPeopleMenu();
        click(AUTO_LOAN_LINK);
    }

    public void goToCreditBureau() {
        openPeopleMenu();
        click(CREDIT_BUREAU_LINK);
    }

    public void goToSantanderDigital() {
        openPeopleMenu();
        click(DIGITAL_BANKING_LINK);
    }

    public void goToSantanderApp() {
        openPeopleMenu();
        click(SANTANDER_APP_LINK);
    }

    public void goToSantanderWeb() {
        openPeopleMenu();
        click(SANTANDER_WEB_LINK);
    }

    public void goToTransactionLimit() {
        openPeopleMenu();
        click(TRANSACTION_LIMIT_LINK);
    }

    public void goToAccounts() {
        openPeopleMenu();
        click(ACCOUNTS_LINK);
    }

    public void goToBasicAccount() {
        openPeopleMenu();
        click(BASIC_ACCOUNT_LINK);
    }

    public void goToPayrollAccount() {
        openPeopleMenu();
        click(PAYROLL_ACCOUNT_LINK);
    }

    public void goToCheckingAccount() {
        openPeopleMenu();
        click(CHECKING_ACCOUNT_LINK);
    }

    public void goToPayrollPortability() {
        openPeopleMenu();
        click(PAYROLL_PORTABILITY_LINK);
    }

    public void goToInvestmentFunds() {
        openPeopleMenu();
        click(INVESTMENT_FUNDS_LINK);
    }

    public void goToTermInvestments() {
        openPeopleMenu();
        click(TERM_INVESTMENTS_LINK);
    }

    public void goToStructuredNotes() {
        openPeopleMenu();
        click(STRUCTURED_NOTES_LINK);
    }

    public void goToAutoInsurance() {
        openPeopleMenu();
        click(AUTO_INSURANCE_LINK);
    }

    public void goToLifeInsurance() {
        openPeopleMenu();
        click(LIFE_INSURANCE_LINK);
    }

    public void goToHomeInsurance() {
        openPeopleMenu();
        click(HOME_INSURANCE_LINK);
    }

    public void goToSavingsInsurance() {
        openPeopleMenu();
        click(SAVINGS_INSURANCE_LINK);
    }

    public void goToMedicalExpensesInsurance() {
        openPeopleMenu();
        click(MEDICAL_INSURANCE_LINK);
    }

    public void goToBelongingsInsurance() {
        openPeopleMenu();
        click(BELONGINGS_INSURANCE_LINK);
    }

    public void goToSuperline() {
        openPeopleMenu();
        click(SUPERLINE_LINK);
    }

    public void goToBranches() {
        openPeopleMenu();
        click(BRANCHES_LINK);
    }

    public void goToAtms() {
        openPeopleMenu();
        click(ATMS_LINK);
    }

    public void goToAlternativeChannels() {
        openPeopleMenu();
        click(ALTERNATIVE_CHANNELS_LINK);
    }

    public void goToHelpCenter() {
        openPeopleMenu();
        click(HELP_CENTER_LINK);
    }

    public void goToSecurityCenter() {
        openPeopleMenu();
        click(SECURITY_CENTER_LINK);
    }

    public void goToTutorials() {
        openPeopleMenu();
        click(TUTORIALS_LINK);
    }

    public void goToTermsAndConditions() {
        openPeopleMenu();
        click(TERMS_LINK);
    }

    public void goToRegulation() {
        openPeopleMenu();
        click(REGULATION_LINK);
    }

    public void goToSelect() {
        openPeopleMenu();
        click(SELECT_LINK);
    }

    public void goToPromotions() {
        openPeopleMenu();
        click(PROMOTIONS_LINK);
    }

    public void goToUniqueRewards() {
        openPeopleMenu();
        click(UNIQUE_REWARDS_LINK);
    }

    public void goToGroups() {
        openPeopleMenu();
        click(GROUPS_LINK);
    }

    public void goToHomeWorld() {
        openPeopleMenu();
        click(HOME_WORLD_LINK);
    }

    public void goToCashback() {
        openPeopleMenu();
        click(CASHBACK_LINK);
    }
}
