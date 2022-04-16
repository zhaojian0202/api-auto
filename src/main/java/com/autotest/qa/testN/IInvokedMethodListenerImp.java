package com.autotest.qa.testN;
import org.testng.*;

public class IInvokedMethodListenerImp implements IInvokedMethodListener {

    @Override
    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        System.out.println("beforeInvocation:"+iTestResult.getName());
    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        System.out.println("afterInvocation:"+iTestResult.getName());
    }
}
