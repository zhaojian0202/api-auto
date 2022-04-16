package com.autotest.qa.testN;

import org.testng.IInvokedMethod;
import org.testng.ITestNGListener;
import org.testng.ITestResult;

public interface IInvokedMethodListener extends ITestNGListener {

    void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult);
    void afterInvocation(IInvokedMethod var1, ITestResult var2);

}
