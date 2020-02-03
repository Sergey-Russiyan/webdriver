package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static common.util.TestBase.getBaseUrl;
import static org.testng.Assert.assertTrue;

public class BingSearchPage extends BasePage{

    public BingSearchPage(WebDriver driver){
        super(driver);
        init(driver);
    }

    //locators
    //upper left
    private static By imagesToolbarLink = By.id("scopebar_link");
    private static By dotMenu = By.id("dots_overflow_menu_container");

    private static String GENERIC_DOT_MENU_ELEMENT = "//li[@id='%s']//a";//todo to enum
    private static By dotMenuVideo =     By.xpath(String.format(GENERIC_DOT_MENU_ELEMENT,"video"));
    private static By dotMenuMaps =      By.xpath(String.format(GENERIC_DOT_MENU_ELEMENT,"local"));
    private static By dotMenuNews =      By.xpath(String.format(GENERIC_DOT_MENU_ELEMENT,"news"));
    private static By dotMenuMsn =       By.xpath(String.format(GENERIC_DOT_MENU_ELEMENT,"msn"));
    private static By dotMenuOffice =    By.xpath(String.format(GENERIC_DOT_MENU_ELEMENT,"office"));

    //upper right
    private static By userLoginToolbarLink = By.id("id_l");
    private static By userLoginAvatar = By.id("id_a");
    private static By toolbarBurgerMenu = By.id("id_sc");


    //center
    private static By searchIconCamera = By.className("camera icon");
    private static By searchIconKeyboard = By.className("icon tooltip");
    private static By searchIconTooltip = By.className("search icon tooltip");
    private static By searchInputField = By.id("sb_form_q");

    //actions/steps

    public BingSearchPage atSearchPage(){
        navigateToGenericUrl(getBaseUrl());
        return this;
    }
    public BingSearchPage searchForText(String searchQuery){
        driver.findElement(searchInputField).click();
        driver.findElement(searchInputField).sendKeys(searchQuery);
        waitForPageIsLoaded();
        driver.findElement(searchIconTooltip).click();
        return this;
    }



    //verify actions
    public BingSearchPage verifyUserAtBingSearchPage(){
        assertTrue(driver.getTitle().equals("Bing"));
        assertTrue(driver.getCurrentUrl().startsWith(getBaseUrl()), "not navigated to bing search url");
        return this;
    }
    public BingSearchPage verifyLeftToolbarDefaultElements(){
        assertTrue(driver.findElement(imagesToolbarLink).isDisplayed(), "No: " + imagesToolbarLink);
        assertTrue(isDisplayed(dotMenu), "No: " + dotMenu);
        return this;
    }
}
