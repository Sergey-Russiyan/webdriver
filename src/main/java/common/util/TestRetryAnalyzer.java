package common.util;

import org.apache.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class TestRetryAnalyzer implements IRetryAnalyzer {

    private final Logger log = Logger.getLogger(TestRetryAnalyzer.class);

    private int count = 1;
    private static int maxRetryAttempts = 2;

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) {
            log.info("RETRY NUMBER: " + count + " at test: " + iTestResult.getName());
            if (count < maxRetryAttempts) {
                count++;
                iTestResult.setStatus(ITestResult.FAILURE);
                return true;
            } else {
                iTestResult.setStatus(ITestResult.FAILURE);
            }
        } else {
            iTestResult.setStatus(ITestResult.SUCCESS);
        }
        return false;
    }
}