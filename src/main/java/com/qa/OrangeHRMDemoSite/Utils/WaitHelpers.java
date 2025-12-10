package com.qa.OrangeHRMDemoSite.Utils;

import com.qa.OrangeHRMDemoSite.driver.driverMgr;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class WaitHelpers {

    private static final int DEFAULT_TIMEOUT = 20;

    private static WebDriver getDriver() {
        return driverMgr.getDriver(); // Fetch fresh driver every time
    }

    private static WebDriverWait getWait(int timeout) {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(timeout));
    }

    // Explicit waits — recommended for all elements
    public static void waitForClickable(By locator) {
        getWait(DEFAULT_TIMEOUT).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static void checkVisibility(By locator) {
        getWait(DEFAULT_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void waitForPresence(By locator) {
        getWait(DEFAULT_TIMEOUT).until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static void waitForInvisibility(By locator) {
        getWait(DEFAULT_TIMEOUT).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    // For elements that frequently throw StaleElementException
    public static WebElement waitForStaleSafe(By locator) {
        return getWait(DEFAULT_TIMEOUT).until(driver -> {
            try {
                WebElement element = driver.findElement(locator);
                if (element.isDisplayed()) return element;
                return null;
            } catch (StaleElementReferenceException e) {
                return null;
            }
        });
    }

    // Generic wait for text inside element
    public static void waitForText(By locator, String text) {
        getWait(DEFAULT_TIMEOUT).until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    // Custom timeout if needed
    public static void checkVisibility(By locator, int seconds) {
        getWait(seconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void visibilityOfElement(By locator) {
        getWait(DEFAULT_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    //Wait for WebElement
    public static WebElement waitForClickable(WebElement element) {
        return getWait(DEFAULT_TIMEOUT).until(ExpectedConditions.elementToBeClickable(element));
    }

    //Return WebElement after presence
    public static WebElement getElement(By locator) {
        return getWait(DEFAULT_TIMEOUT).until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    //Wait for visible + return WebElement
    public static WebElement waitForVisible(By locator) {
        return getWait(DEFAULT_TIMEOUT).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ignored) {
        }
    }
}