package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import pages.HomePage;
import pages.LoginPage;
import pages.PimPage;

public class PimTest extends BaseTest {

    @Test
    public void Task1() {

        LoginPage loginPage = new LoginPage(driver);

        HomePage homePage = loginPage.login(
                "Admin",
                "admin123"
        );

        Assert.assertEquals(
                homePage.getDashBoardTitle(),
                "Dashboard"
        );

        PimPage pimPage = homePage.goToPim();

        Assert.assertEquals(
                pimPage.getPimTitle(),
                "PIM"
        );

        pimPage.search("Amelia", "01715");
        pimPage.checkResult();
    }
}