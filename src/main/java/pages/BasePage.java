package pages;

import lombok.Getter;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class BasePage extends PageObject {

    private final Logger log = Logger.getLogger(BasePage.class);
    public final WebDriver driver;

    public BasePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    //LOCATORS FOR GENERIC BUTTONS WHICH CAN BE DISPLAYED AT DIFFERENT PAGES.
    @Getter
    public enum Button {
        ADD (By.xpath("//*[.='Add']")),
       ;

        private final By buttonLocator;
        Button(By buttonLocator) {
            this.buttonLocator = buttonLocator;
        }
    }

    public void waitForPageIsLoaded() {
        log.info("Loading page: \n"+driver.getCurrentUrl());
        driver.manage().timeouts().implicitlyWait(pageWait, TimeUnit.SECONDS);
        ExpectedCondition<Boolean> pageLoadCondition = driver ->
                ((JavascriptExecutor) Objects.requireNonNull(driver)).executeScript("return document.readyState").equals("complete");
        WebDriverWait wait = new WebDriverWait(driver, pageWait);
        wait.until(pageLoadCondition);
    }

}
