package bingsearch;

import com.automation.remarks.testng.VideoListener;
import common.util.RandomUtils;
import common.util.TestBase;
import common.util.loaders.PropertyLoader;
import io.qameta.allure.*;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.BingSearchPage;
import pages.BingSearchResultsPage;
import java.lang.reflect.Method;

@Listeners({VideoListener.class})
@Owner("sergey")
public class BingSearchPageTest extends TestBase {

    BingSearchPage bingSearchPage;
    BingSearchResultsPage bingSearchResultsPage;

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeBingTest() {
        driver = getDriver(PropertyLoader.loadProperty("browser"));
        bingSearchPage = new BingSearchPage(driver);
        bingSearchResultsPage = new BingSearchResultsPage(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void afterTest(Method method, ITestResult result) {
        driverQuit();
    }

    @DataProvider(name = "validSearch")
    public Object[][] columns() {
        return new Object[][]{//todo valid search data
        };
    }

    @Feature("Bing Search")
    @Story("As not logged into account user I should be able to find data via BING search engine")
    @Description("navigate to bing URL, use simple text search to find out data")
    @TmsLink("01")
    @Test(groups = {"Search"}//    , dataProvider = "validSearch"//todo enable after data provider
    )
    public void testSimpleBingSearchWithText() {
        String searchTerm = new RandomUtils().getRandName("some search");
        bingSearchPage.
                atSearchPage().
                searchForText(searchTerm);
        bingSearchResultsPage.verifySearchResults(searchTerm);
    }

    @Feature("Bing Search")
    @Story("As not logged into account user I should NOT be able to find any data with EMPTY search via BING search engine")
    @Description("navigate to bing URL, use  empty search to find NOTHING")
    @TmsLink("02")
    @Test(groups = {"Search"})
    public void testEmptyBingSearch() {
        bingSearchPage.
                atSearchPage().
                searchForText("").
                verifyUserAtBingSearchPage();
    }
}
