package base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import utils.DriverFactory;

public abstract class BaseTest {
	
	protected WebDriver driver;
	
	@BeforeMethod
	public void setup() {
		
		driver = DriverFactory.createChromeDriver();
		
		driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
		

	}
	
//	@AfterMethod
//	public void tearDown() {
//		if (driver !=null) {
//			driver.quit();
//		}
//
//	}
	
	
	
}
