package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import base.BasePage;

public class LoginPage extends BasePage{
	
	private static final By USER_NAME = By.name("username");
	
	private static final By PASSWORD = By.name("password");
	
	private static final By LOGIN_BUTTON = By.className("orangehrm-login-button");
	
	
	public LoginPage(WebDriver driver) {
		super(driver);
	}

	
	public HomePage login(String username,String password) {

		write(USER_NAME, username);
		write(PASSWORD, password);
		
		click(LOGIN_BUTTON);
		
		return new HomePage(driver);
	}
	
	
	
}
