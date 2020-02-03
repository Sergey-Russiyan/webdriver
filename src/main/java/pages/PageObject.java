package pages;

import io.qameta.allure.Step;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.TimeUnit;

public abstract class PageObject {

    private static final Logger log = Logger.getLogger(PageObject.class);

    private WebDriver driver;
    public int pageWait = 6;

    protected void init(WebDriver driver){
        BasePage basePage = new BasePage(driver);
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }
    @Step("Navigate to URL")
    public void navigateToGenericUrl(String url){
        log.info("Navigate to URL\n"+url);
        driver.navigate().to(url);
    }
    @Step("Is displayed:{by}")
    public boolean isDisplayed(By by) {
        driver.manage().timeouts().pageLoadTimeout(pageWait, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(driver, pageWait);
        boolean result = false;
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
            result = driver.findElement(by).isDisplayed();
            log.info("PASS.Displayed element:\n"+by+"\n");
        } catch (NoSuchElementException | TimeoutException e) {
            log.info(String.format("\nNot displayed element: '%s'\n", by));
        } catch (StaleElementReferenceException ex){
            log.info("Stale Ex at isDisplayed: "+by);
            wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(by)));
            isDisplayed(by);
        }
        return result;
    }


}
