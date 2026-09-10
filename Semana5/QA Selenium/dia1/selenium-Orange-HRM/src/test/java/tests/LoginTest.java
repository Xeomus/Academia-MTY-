package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseTest;
import pages.HomePage;
import pages.LoginPage;

public class LoginTest extends BaseTest{

	@Test
	public void sholudLoginSuccessfully() {
		
		LoginPage loginPage = new LoginPage(driver);
		
		HomePage homePage = loginPage.login("Admin", "admin123");
	
		Assert.assertEquals(homePage.getDashBoardTitle(), "Dashboard");
		
	}

}
