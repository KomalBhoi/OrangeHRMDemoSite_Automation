package com.qa.OrangeHRMDemoSite.Utils;

import com.qa.OrangeHRMDemoSite.driver.driverMgr;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class dropdownUtils {

    private static WebDriver driver() {
        return driverMgr.getDriver();
    }

    // Generic dropdown selector for OrangeHRM
    public static void selectFromDropdown(By dropdownLocator, String value) {
        // Open dropdown
        WaitHelpers.waitForClickable(dropdownLocator);
        driver().findElement(dropdownLocator).click();

        // Select value
        By option = By.xpath("//div[@role='option']//span[text()='" + value + "']");
        new WebDriverWait(driver(), Duration.ofSeconds(15))
                .until(ExpectedConditions.visibilityOfElementLocated(option));
        driver().findElement(option).click();
    }
}