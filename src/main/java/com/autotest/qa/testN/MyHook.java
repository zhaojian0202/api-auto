package com.autotest.qa.testN;

import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;

public class MyHook implements IHookable {
    @Override
    public void run(IHookCallBack iHookCallBack, ITestResult iTestResult) {
        System.out.println("hi jim");
        iHookCallBack.runTestMethod(iTestResult);
    }
}
