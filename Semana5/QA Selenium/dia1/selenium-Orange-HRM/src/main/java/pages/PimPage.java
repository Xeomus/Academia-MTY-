package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PimPage extends BasePage {

    private static final By NAME_INPUT = By.cssSelector("input[placeholder='Type for hints...']");
    private static final By ID_INPUT = By.cssSelector(".oxd-grid-item:nth-child(2) input.oxd-input");
    private static final By SEARCH_BUTTON = By.cssSelector("button.orangehrm-left-space");
    private static final By FIRST_CHECKBOX = By.cssSelector(".oxd-table-card:first-child .oxd-checkbox-input");

    private static final By PIM_TITLE =
            By.className("oxd-topbar-header-title");

    public PimPage(WebDriver driver) {
        super(driver);
    }

    public String getPimTitle() {
        return text(PIM_TITLE);
    }

    public PimPage search(String name, String id){
        write(NAME_INPUT, name);
        write(ID_INPUT, id);
        click(SEARCH_BUTTON);
        return this;
    }

    public PimPage checkResult(){
        click(FIRST_CHECKBOX);
        return this;
    }
}
