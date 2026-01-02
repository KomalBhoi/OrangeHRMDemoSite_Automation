package com.qa.OrangeHRMDemoSite.Utils;

import com.qa.OrangeHRMDemoSite.driver.driverMgr;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitHelpers {

    private static final int DEFAULT_TIMEOUT = 20;

    private static WebDriver driver() {
        return driverMgr.getDriver();
    }

    private static WebDriverWait wait(int seconds) {
        return new WebDriverWait(driver(), Duration.ofSeconds(seconds));
    }

    // ==============================
    //   GLOBAL ORANGEHRM WAITERS
    // ==============================

    // 1️⃣ Wait for loader/spinner to disappear
    private static final By LOADER = By.cssSelector(".oxd-form-loader");

//    public static void waitForLoaderToDisappear() {
//        By loader = By.xpath("//div[contains(@class,'oxd-form-loader')]");
//        try {
//            getWait(25).until(ExpectedConditions.invisibilityOfElementLocated(loader));
//        } catch (TimeoutException e) {
//            // ignore for demo/docker slowness
//        }
//    }

    public static void waitForLoaderToDisappear(){
        try{
            WebDriverWait wait = new WebDriverWait(driverMgr.getDriver(),Duration.ofSeconds(20));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".oxd-form-loader")));
        }catch(TimeoutException e){}
    }

    public static void waitForPageReady() {
        getWait(20).until(driver ->
                ((JavascriptExecutor) driver)
                        .executeScript("return document.readyState")
                        .equals("complete")
        );
    }

    // 2️⃣ Wait for DOM ready (document.readyState === "complete")
    public static void waitForPageToBeReady() {
        try {
            wait(20).until((ExpectedCondition<Boolean>) driver ->
                    ((JavascriptExecutor) driver)
                            .executeScript("return document.readyState").toString().equals("complete"));
        } catch (Exception ignored) {}
    }

    // 3️⃣ Wait for AJAX calls to finish (React + fetch + jQuery fallback)
    public static void waitForAjax() {
        try {
            wait(20).until(driver -> {
                JavascriptExecutor js = (JavascriptExecutor) driver;

                // jQuery active requests
                Boolean jqueryDone = true;
                try {
                    jqueryDone = (Boolean) js.executeScript(
                            "return (window.jQuery == null) || (jQuery.active === 0);");
                } catch (Exception ignored) {}

                // fetch/XHR requests
                Boolean ajaxDone = (Boolean) js.executeScript(
                        "return (window.activeRequests == 0) || (typeof window.activeRequests === 'undefined');");

                return jqueryDone && ajaxDone;
            });
        } catch (Exception ignored) {}
    }


    // ==============================
    //   ELEMENT WAITS
    // ==============================

    public static void waitForClickable(By locator) {
        waitForLoaderToDisappear();
        wait(DEFAULT_TIMEOUT).until(ExpectedConditions.elementToBeClickable(locator));
    }

    //Wait for WebElement
    public static WebElement waitForClickable(WebElement element) {
        return getWait(DEFAULT_TIMEOUT).until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void waitForVisible(By locator) {
        wait(DEFAULT_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private static WebDriverWait getWait(int timeout) {
        return new WebDriverWait(driver(), Duration.ofSeconds(timeout));
    }
    //Wait for visible + return WebElement
    public static WebElement waitForVisible_returnWebElement(By locator) {
        return getWait(DEFAULT_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForPresence(By locator) {
        return wait(DEFAULT_TIMEOUT).until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static void waitForInvisibility(By locator) {
        wait(DEFAULT_TIMEOUT).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // Handle stale elements gracefully
    public static WebElement waitForStaleSafe(By locator) {
        return wait(DEFAULT_TIMEOUT).until(driver -> {
            try {
                WebElement el = driver.findElement(locator);
                return el.isDisplayed() ? el : null;
            } catch (StaleElementReferenceException e) {
                return null;
            }
        });
    }

    public static void waitForText(By locator, String text) {
        wait(DEFAULT_TIMEOUT)
                .until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }


    // ==============================
    //   HELPER: SAFE SLEEP
    // ==============================
    public static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (Exception ignored) {}
    }

    public static void waitForReactToSettle() {
        WebDriver driver = driverMgr.getDriver();

        new WebDriverWait(driver, Duration.ofSeconds(20)).until(d -> {
            JavascriptExecutor js = (JavascriptExecutor) d;
            return (Boolean) js.executeScript(
                    "return document.readyState === 'complete' && " +
                            "(!window.fetch || window.fetch.length === 1);"
            );
        });

        sleep(500); // small buffer
    }

}