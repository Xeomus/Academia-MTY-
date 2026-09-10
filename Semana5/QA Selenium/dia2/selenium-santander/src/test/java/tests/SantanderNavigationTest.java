package tests;

import base.BaseTest;
import java.util.Set;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.AcercaDelBancoPage;
import pages.BancaPrivadaPage;
import pages.EmpresasPage;
import pages.PersonasPage;
import pages.PymesPage;

public class SantanderNavigationTest extends BaseTest {

    private void verifyNavigation(
            SoftAssert softAssert,
            String nombre,
            Runnable navegacion) {

        try {
            returnHome();
            Set<String> ventanasAntes = getOpenWindows();

            navegacion.run();
            adjustWindowAfterNavigation(ventanasAntes);

            System.out.println("[" + nombre + "] visited");

        } catch (Exception error) {
            System.err.println(
                "[" + nombre + "] failed: "
                + error.getClass().getSimpleName()
                + ": "
                + error.getMessage()
            );

            softAssert.fail(
                nombre
                + " failed -> "
                + error.getClass().getSimpleName()
                + ": "
                + error.getMessage()
            );
        }
    }

    @Test(priority = 1)
    public void shouldNavigatePeopleCreditLinks() {
        SoftAssert softAssert = new SoftAssert();
        PersonasPage page = new PersonasPage(driver);

        verifyNavigation(softAssert, "People - Credit Cards", page::goToCreditCards);
        verifyNavigation(softAssert, "People - Personal Loan", page::goToPersonalLoan);
        verifyNavigation(softAssert, "People - Mortgage Loan", page::goToMortgageLoan);
        verifyNavigation(softAssert, "People - Mortgage Simulator", page::goToMortgageSimulator);
        verifyNavigation(softAssert, "People - Auto Loan", page::goToAutoLoan);
        verifyNavigation(softAssert, "People - Credit Bureau", page::goToCreditBureau);

        softAssert.assertAll();
    }

    @Test(priority = 2)
    public void shouldNavigatePeopleDigitalChannels() {
        SoftAssert softAssert = new SoftAssert();
        PersonasPage page = new PersonasPage(driver);

        verifyNavigation(softAssert, "People - Santander Digital", page::goToSantanderDigital);
        verifyNavigation(softAssert, "People - Santander App", page::goToSantanderApp);
        verifyNavigation(softAssert, "People - Santander Web", page::goToSantanderWeb);
        verifyNavigation(softAssert, "People - Transaction Limit", page::goToTransactionLimit);

        softAssert.assertAll();
    }

    @Test(priority = 3)
    public void shouldNavigatePeopleAccountLinks() {
        SoftAssert softAssert = new SoftAssert();
        PersonasPage page = new PersonasPage(driver);

        verifyNavigation(softAssert, "People - Accounts", page::goToAccounts);
        verifyNavigation(softAssert, "People - Basic Account", page::goToBasicAccount);
        verifyNavigation(softAssert, "People - Payroll Account", page::goToPayrollAccount);
        verifyNavigation(softAssert, "People - Checking Account", page::goToCheckingAccount);
        verifyNavigation(softAssert, "People - Payroll Portability", page::goToPayrollPortability);

        softAssert.assertAll();
    }

    @Test(priority = 4)
    public void shouldNavigatePeopleInvestmentsAndInsurance() {
        SoftAssert softAssert = new SoftAssert();
        PersonasPage page = new PersonasPage(driver);

        verifyNavigation(softAssert, "People - Investment Funds", page::goToInvestmentFunds);
        verifyNavigation(softAssert, "People - Term Investments", page::goToTermInvestments);
        verifyNavigation(softAssert, "People - Structured Notes", page::goToStructuredNotes);

        verifyNavigation(softAssert, "People - Auto Insurance", page::goToAutoInsurance);
        verifyNavigation(softAssert, "People - Life Insurance", page::goToLifeInsurance);
        verifyNavigation(softAssert, "People - Home Insurance", page::goToHomeInsurance);
        verifyNavigation(softAssert, "People - Savings Insurance", page::goToSavingsInsurance);
        verifyNavigation(softAssert, "People - Medical Expenses Insurance", page::goToMedicalExpensesInsurance);
        verifyNavigation(softAssert, "People - Belongings Insurance", page::goToBelongingsInsurance);

        softAssert.assertAll();
    }

    @Test(priority = 5)
    public void shouldNavigatePeopleHelpAndBenefits() {
        SoftAssert softAssert = new SoftAssert();
        PersonasPage page = new PersonasPage(driver);

        verifyNavigation(softAssert, "People - SuperLine", page::goToSuperline);
        verifyNavigation(softAssert, "People - Branches", page::goToBranches);
        verifyNavigation(softAssert, "People - ATMs", page::goToAtms);
        verifyNavigation(softAssert, "People - Alternative Channels", page::goToAlternativeChannels);
        verifyNavigation(softAssert, "People - Help Center", page::goToHelpCenter);
        verifyNavigation(softAssert, "People - Security Center", page::goToSecurityCenter);
        verifyNavigation(softAssert, "People - Tutorials", page::goToTutorials);
        verifyNavigation(softAssert, "People - Terms and Conditions", page::goToTermsAndConditions);
        verifyNavigation(softAssert, "People - Regulation", page::goToRegulation);

        verifyNavigation(softAssert, "People - Select", page::goToSelect);
        verifyNavigation(softAssert, "People - Promotions", page::goToPromotions);
        verifyNavigation(softAssert, "People - Unique Rewards", page::goToUniqueRewards);
        verifyNavigation(softAssert, "People - Groups", page::goToGroups);
        verifyNavigation(softAssert, "People - Home World", page::goToHomeWorld);
        verifyNavigation(softAssert, "People - Cashback", page::goToCashback);

        softAssert.assertAll();
    }

    @Test(priority = 6)
    public void shouldNavigateCompanyLinks() {
        SoftAssert softAssert = new SoftAssert();
        EmpresasPage page = new EmpresasPage(driver);

        verifyNavigation(softAssert, "Companies - Companies and Government", page::goToCompaniesAndGovernment);
        verifyNavigation(softAssert, "Companies - Multinationals", page::goToMultinationals);

        softAssert.assertAll();
    }

    @Test(priority = 7)
    public void shouldNavigateSmeLinks() {
        SoftAssert softAssert = new SoftAssert();
        PymesPage page = new PymesPage(driver);

        verifyNavigation(softAssert, "SMEs - Santander SME", page::goToSantanderSme);
        verifyNavigation(softAssert, "SMEs - Foreign Exchange and Hedging", page::goToForeignExchangeAndHedging);
        verifyNavigation(softAssert, "SMEs - Accounts", page::goToSmeAccounts);
        verifyNavigation(softAssert, "SMEs - International Business", page::goToInternationalBusiness);
        verifyNavigation(softAssert, "SMEs - Packages", page::goToSmePackages);
        verifyNavigation(softAssert, "SMEs - Loans", page::goToSmeLoans);
        verifyNavigation(softAssert, "SMEs - Insurance", page::goToSmeInsurance);
        verifyNavigation(softAssert, "SMEs - Partnerships", page::goToPartnerships);
        verifyNavigation(softAssert, "SMEs - Transactional Business", page::goToTransactionalBusiness);
        verifyNavigation(softAssert, "SMEs - Financial Ecosystem", page::goToFinancialEcosystem);
        verifyNavigation(softAssert, "SMEs - Investments", page::goToSmeInvestments);

        softAssert.assertAll();
    }

    @Test(priority = 8)
    public void shouldNavigatePrivateBankingLink() {
        SoftAssert softAssert = new SoftAssert();
        BancaPrivadaPage page = new BancaPrivadaPage(driver);

        verifyNavigation(softAssert, "Private Banking", page::goToPrivateBankingHome);

        softAssert.assertAll();
    }

    @Test(priority = 9)
    public void shouldNavigateAboutBankLinks() {
        SoftAssert softAssert = new SoftAssert();
        AcercaDelBancoPage page = new AcercaDelBancoPage(driver);

        verifyNavigation(softAssert, "About the Bank - Santander Foundation", page::goToSantanderFoundation);
        verifyNavigation(softAssert, "About the Bank - Blog", page::goToBlog);
        verifyNavigation(softAssert, "About the Bank - Sustainability", page::goToSustainability);
        verifyNavigation(softAssert, "About the Bank - Financial Education", page::goToFinancialEducation);
        verifyNavigation(softAssert, "About the Bank - Investors", page::goToInvestors);
        verifyNavigation(softAssert, "About the Bank - Newsroom", page::goToNewsroom);
        verifyNavigation(softAssert, "About the Bank - Careers", page::goToCareers);

        softAssert.assertAll();
    }
}
