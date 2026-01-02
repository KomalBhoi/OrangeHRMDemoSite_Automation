package com.qa.OrangeHRMDemoSite.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.qa.OrangeHRMDemoSite.Utils.ExtentReportManager;
import com.qa.OrangeHRMDemoSite.Utils.ScreenshotUtils;
import com.qa.OrangeHRMDemoSite.driver.driverMgr;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    private static ExtentReports extent = ExtentReportManager.getInstance();
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
        test.set(extentTest);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        //test.get().pass("✅ Test Passed");
        test.get().log(Status.PASS,"Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = driverMgr.getDriver();
        String screenshotPath = ScreenshotUtils.takeScreenshot(result.getMethod().getMethodName());
        test.get().fail("❌ Test Failed: " + result.getThrowable(),
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        //test.get().addScreenCaptureFromPath(screenshotPath);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        //test.get().skip("⚠️ Test Skipped: " + result.getThrowable());
        test.get().log(Status.SKIP,"Test Skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        //extent.flush();
        driverMgr.quitDriver();
    }
}
