package common.util;

import com.automation.remarks.testng.VideoListener;
import common.entities.EnvironmentDetailsConfig;
import common.util.loaders.PropertyLoader;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.EdgeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;

import io.qameta.allure.Owner;
import io.qameta.allure.Step;
import lombok.SneakyThrows;

import org.apache.log4j.Logger;

import org.openqa.selenium.*;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;

import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.*;
import org.testng.annotations.*;
import pages.PageObject;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static common.entities.EnvironmentDetailsConfig.*;
import static common.util.FileUtils.*;
import static org.openqa.selenium.Platform.WIN10;

@Owner("rse")
@Listeners({VideoListener.class})
public class TestBase extends PageObject implements IHookable {

    public static final Logger log = Logger.getLogger(TestBase.class);

    protected WebDriver driver;
    public WebDriverWait wait;
    private int pageWait = 6;

    public static final String browser = PropertyLoader.loadProperty("browser");

    private static final boolean shouldBeDeletedTestObjectsAfterSuite = Boolean.parseBoolean(PropertyLoader.loadProperty("deleteTestObjectsAfterSuite"));

    /* method will add first 3 symbols from 'user.name' to every created test-object, in order to not affect auto-tests
     * of other users/ci that runs at the same time at the same server
     * */

    @BeforeSuite(alwaysRun = true)
    public void beforeSuiteActions(ITestContext context) {
        executeTestDataPreparationActions();
    }
    @BeforeClass(alwaysRun = true)
    public void beforeClassActions() {
        log.info(":::\nStart of TestClass:\n"+getClass().toString()+" at: "+LocalDateTime.now().toString());
    }
    @AfterClass(alwaysRun = true)
    public void afterClassActions() {
        log.info(":::\nEnd of TestClass:\n"+getClass().toString()+" at: "+LocalDateTime.now().toString());
    }
    @BeforeMethod(alwaysRun = true)
    public void setBrowser(Method method){
        log.info(":::\nStart of Test:\n"+method.getName()+"\n:::\n");
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod(Method method){
        log.info(":::\nEnd of Test:\n"+method.getName()+"\n:::\n");
    }

    @AfterSuite(alwaysRun = true)
    public void cleanup() {
        log.debug("post-requisite steps");
    }
    /** should be invoked only at env's which are != 'automation'(default) */
    private void executeTestDataPreparationActions() {
       log.info("TODO");
    }
    protected WebDriver getDriver(String browserType){
        boolean isEdge = browserType.contains("edge");
        if (browserType.equals("firefox")){
            FirefoxDriverManager.firefoxdriver().setup();
            driver = getFirefoxDriver();
        } else if (browserType.equals("chrome")) {
            ChromeDriverManager.chromedriver().
                    setup();
            getChromeDriver();
        } else if(browserType.contains("explorer")||browserType.contains("ie")){
            InternetExplorerDriverManager.iedriver().arch32().version("3.4").setup();//verify is recent version with fixed issue [March 2018]
            getIeDriver();
        } else if (isEdge){
            EdgeDriverManager.iedriver().setup();
            getEdgeDriver();
        }else{
            log.info("Unknown/unsupported browser type: "+browserType+"\n will be used default value: chrome");
            getChromeDriver();
        }
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        if(!isEdge) {// see 'readme' known issues
            driver.manage().deleteAllCookies();
        }
        driver.manage().window().maximize(); //unknown error: failed to change window state to normal, current state is maximized
        return driver;
    }
    protected String getCurrentUrl() {
        String result = driver.getCurrentUrl();
        log.info("Current URL:\n"+result);
        return result;
    }
    public static String getBaseUrl() {
        return resolveEnvironmentPropertyValueFromApplicationPropFile().getBaseUrl();
    }

    public static EnvironmentDetailsConfig resolveEnvironmentPropertyValueFromApplicationPropFile() {
        String environment = getEnvironment();
        switch (environment){
            case "prod":
                return PROD;
            case "dev":
                return DEV;
            default:
                return TEST;
        }
    }
    static String getEnvironment() {
        return PropertyLoader.loadProperty("environment");
    }
    @SneakyThrows
    protected void driverQuit() {
        if (driver != null) {
            log.info("Closing browser after TestClass");
            driver.quit();
        } else {
            log.error("Driver is null at AfterClass (TestBase)");
        }
        log.info("Teardown - Exiting");
    }
    private WebDriver getFirefoxDriver() {
        FirefoxOptions capability = new FirefoxOptions();
        capability.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
        capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("browser.helperApps.alwaysAsk.force", false);
        profile.setPreference("browser.startup.homepage_override.mstone", "ignore");
        profile.setPreference("startup.homepage_welcome_url.additional",  "about:blank");
        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.dir", downloadFilePath);
        profile.setPreference("browser.download.manager.showWenStarting", false);
        profile.setPreference("browser.download.manager.focusWhenStarting", false);
        profile.setPreference("browser.download.manager.useWindow", false);
        profile.setPreference("browser.download.manager.showAlertOnComplete", false);
        profile.setPreference("browser.download.manager.closeWhenDone", true);
        profile.setPreference("pdfjs.disabled", true);  // disable the built-in PDF viewer
        return new FirefoxDriver();
    }
    @SneakyThrows
    private WebDriver getChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);
        options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        options.addArguments("chrome.switches");
        options.addArguments("--enable-experimental-extension-apis");
        options.addArguments("--app=vnd.openxmlformats-officedocument.wordprocessingml.document");
        options.addArguments("--allow-http-screen-capture");
        options.addArguments("--allow-silent-push");
        options.addArguments("--app-auto-launched");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--always-authorize-plugins");
        options.addArguments("enable-automation");
        options.addArguments("--dns-prefetch-disable");
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-plugins-discovery");
        options.addArguments("--disable-zero-browsers-open-for-tests");
        options.addArguments("--no-sandbox");
        options.addArguments("--enable-nacl");
        options.addArguments("--log-level=3");
        options.addArguments("--silent");
        options.addArguments("--enable-remote-extension");
        options.addArguments("disable-features=NetworkService");
        options.addArguments("--force-device-scale-factor=1");
        options.addArguments("--force-app-mode");
        options.addArguments("--force-renderer-accessibility");
        options.addArguments("--test-type");
        options.addArguments("--wm-window-animations-disabled");
        options.addArguments("--ignore-certificate-errors");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--dns-prefetch-disable");
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("disable-default-apps"));
        Map<String, Object> prefs = new HashMap<>();
        options.setExperimentalOption("prefs", prefs);
        Map<String, Object> preferences = new Hashtable<>();
        preferences.put("profile.default_content_settings.popups", 0);
        preferences.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
        preferences.put("download.prompt_for_download", false);
        preferences.put("download.default_directory", downloadFilePath);
        options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        options.setCapability(ChromeOptions.CAPABILITY, options);
        options.setExperimentalOption("prefs", preferences);

        System.setProperty("DISPLAY", ":19");
        System.setProperty("java.net.preferIPv4Stack" , "true");
        driver = new ChromeDriver(options);
        return driver;
    }
    private WebDriver getIeDriver() {
        DesiredCapabilities capabilities = new  DesiredCapabilities("iexplorer", "11", WIN10);
        driver = new InternetExplorerDriver();
        return driver;
    }
    private WebDriver getEdgeDriver() {
        driver = new EdgeDriver();
        return driver;
    }
    @Step("Is displayed Alert?")
    protected boolean isAlertPresent() {
        try {
            this.driver.switchTo().alert();
            log.info("Alert present, switching to it");
            return true;
        }
        catch (Exception Ex) {
            return false;
        }
    }
    @Step("Accept Alert")
    protected void acceptAlert() {
        if(isAlertPresent()) {
            Alert alert = this.driver.switchTo().alert();
            log.info("Going to accept alert popup with text: "+alert.getText());
            alert.accept();
        }
    }
    @Step("Cancel Alert")
    protected void cancelAlert() {
        if(isAlertPresent()) {
            Alert alert = this.driver.switchTo().alert();
            log.info("Going to Cancel alert popup with text: "+alert.getText());
            alert.dismiss();
        }
    }
    @Step("Switch to frame")
    public void switchToFrame(String frameName){
        switchToDefaultContent();
        log.info("Switching to frame with name: "+frameName);
        driver.switchTo().frame(frameName);
    }
    private void switchToDefaultContent() {
        log.info("Switching to default content");
        driver.switchTo().defaultContent();
    }
    @Step("Clear cookies")
    public void clearCookies() {
        driver.manage().deleteAllCookies();
        log.info("Cookies cleared: "+driver.manage().getCookies().toString());
    }

    protected String currentUrl() {
        return driver.getCurrentUrl();
    }

    @Step("Get page title")
    protected String getPageTitle() {
        log.info("Current Title:\n"+driver.getTitle()+"\n");
        return driver.getTitle();
    }

    @Override
    public void run(IHookCallBack callBack, ITestResult testResult) {
        callBack.runTestMethod(testResult);
    }

}

