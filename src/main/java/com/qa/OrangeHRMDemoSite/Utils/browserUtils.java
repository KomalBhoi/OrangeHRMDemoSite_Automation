package com.qa.OrangeHRMDemoSite.Utils;

import com.qa.OrangeHRMDemoSite.driver.driverMgr;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

public class browserUtils {
    private static WebDriver driver() {
        return driverMgr.getDriver();
    }

    // Scroll into element view
    public static void scrollTo(By locator) {
        WebElement element = driver().findElement(locator);
        ((JavascriptExecutor) driver()).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    // JS Click
    public static void jsClick(By locator) {
        WebElement element = driver().findElement(locator);
        ((JavascriptExecutor) driver()).executeScript("arguments[0].click();", element);
    }

    // Highlight element temporarily
    public static void highlight(By locator) {
        WebElement element = driver().findElement(locator);
        JavascriptExecutor js = (JavascriptExecutor) driver();
        js.executeScript("arguments[0].style.border='3px solid red'", element);
    }

    // Page Refresh
    public static void refresh() {
        driver().navigate().refresh();
    }

    // Move cursor on element
    public static void hover(By locator) {
        Actions actions = new Actions(driver());
        actions.moveToElement(driver().findElement(locator)).perform();
    }
}
