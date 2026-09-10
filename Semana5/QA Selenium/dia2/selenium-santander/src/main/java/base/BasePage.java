package base;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final Actions actions;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(12));
        this.actions = new Actions(driver);
    }

    protected WebElement find(By locator) {
        return wait.until(driver -> {
            List<WebElement> elements = driver.findElements(locator);
            for (WebElement element : elements) {
                try {
                    if (element.isDisplayed()) {
                        return element;
                    }
                } catch (StaleElementReferenceException ignored) {
                }
            }
            return null;
        });
    }

    protected void click(By locator) throws ElementClickInterceptedException {
        WebElement element = wait.until(driver -> {
            List<WebElement> elements = driver.findElements(locator);
            for (WebElement current : elements) {
                try {
                    if (current.isDisplayed() && current.isEnabled()) {
                        return current;
                    }
                } catch (StaleElementReferenceException ignored) {
                }
            }
            return null;
        });

        JavascriptExecutor js = (JavascriptExecutor) driver;

        js.executeScript(
            "arguments[0].scrollIntoView({block:'center', inline:'nearest'});",
            element
        );

        try {
            element.click();
        } catch (ElementNotInteractableException error) {
            System.out.println("[INFO] Using JavaScript click for: " + locator);
            js.executeScript("arguments[0].click();", element);
        } catch (StaleElementReferenceException error) {
            System.out.println("[INFO] Retrying DOM lookup for: " + locator);
            WebElement nuevoElemento = find(locator);
            js.executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});",
                nuevoElemento
            );
            js.executeScript("arguments[0].click();", nuevoElemento);
        }
    }

    protected void write(By locator, String value) {
        WebElement element = find(locator);
        element.clear();
        element.sendKeys(value);
    }

    protected String text(By locator) {
        return find(locator).getText();
    }

    protected void selectByText(By locator, String value) {
        new Select(find(locator)).selectByVisibleText(value);
    }

    protected boolean isSelected(By locator) {
        return find(locator).isSelected();
    }

    protected boolean exists(By locator) {
        return !driver.findElements(locator).isEmpty();
    }
}
