package pages;

import org.openqa.selenium.WebDriver;

import static common.util.TestBase.getBaseUrl;
import static org.fest.assertions.Assertions.assertThat;
import static org.testng.Assert.assertTrue;

public class BingSearchResultsPage  extends BasePage{

    public BingSearchResultsPage(WebDriver driver){
        super(driver);
        init(driver);
    }



    //    b_searchbox
    public BingSearchResultsPage verifySearchResults(String searchQueryUsed){
        assertThat(driver.getTitle().startsWith(searchQueryUsed));
        assertTrue(driver.getCurrentUrl().startsWith(getBaseUrl()+  "/search?q=" + searchQueryUsed), "not navigated to bing search RESULTS url");
        //todo
        return this;

    }
}
