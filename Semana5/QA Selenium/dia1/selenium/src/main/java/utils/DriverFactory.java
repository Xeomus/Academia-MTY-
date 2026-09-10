package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverFactory {
	
	private DriverFactory() {
		
	}
	
	public static WebDriver createChromeDriver() {
		
		WebDriver driver = new ChromeDriver();
		
		driver.manage().window().maximize();
		
		return driver;
	}
	
}
