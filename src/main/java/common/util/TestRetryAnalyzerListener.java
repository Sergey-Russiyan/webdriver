package common.util;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class TestRetryAnalyzerListener implements IAnnotationTransformer {

    public TestRetryAnalyzerListener() {
    }
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        IRetryAnalyzer retry = annotation.getRetryAnalyzer();
        if (annotation.getRetryAnalyzer() == null) {
            annotation.setRetryAnalyzer(TestRetryAnalyzer.class);
        }
        if (retry == null)    {
            annotation.setRetryAnalyzer(TestRetryAnalyzer.class);
        }
    }
}
