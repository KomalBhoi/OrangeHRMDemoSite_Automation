package com.qa.OrangeHRMDemoSite.base;

import com.qa.OrangeHRMDemoSite.Utils.PropertiesReader;
import com.qa.OrangeHRMDemoSite.Utils.WaitHelpers;
import com.qa.OrangeHRMDemoSite.driver.driverMgr;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.time.Duration;
import java.util.List;

import static com.qa.OrangeHRMDemoSite.driver.driverMgr.getDriver;

public class basePage {

    private static final Logger log = LogManager.getLogger(basePage.class);

    protected WebDriver driver() {
        return driverMgr.getDriver();
    }

    public basePage() {}


    // ==========================
    //   GLOBAL NAVIGATION
    // ==========================
    public void openOrangeHRMUrl() {
        driver().get(PropertiesReader.readKey("url"));
        WaitHelpers.waitForLoaderToDisappear();     // NEW GLOBAL WAIT
        WaitHelpers.waitForPageToBeReady();         // Page ready
    }


    // ==========================
    //   CLICK (SUPER-STABLE)
    // ==========================
    protected void clickElement(By locator) {
        WaitHelpers.waitForLoaderToDisappear();
        WaitHelpers.waitForClickable(locator);
        driver().findElement(locator).click();
    }


    // ==========================
    //    TYPE
    // ==========================
    protected void enterInput(By locator, String text) {
        WaitHelpers.waitForVisible(locator);
        WebElement ele = driver().findElement(locator);
        scrollIntoView(ele);
        ele.clear();
        ele.sendKeys(text);
    }


    // ==========================
    //   GET TEXT
    // ==========================
    protected String getText(By locator) {
        WaitHelpers.waitForVisible(locator);
        return driver().findElement(locator).getText();
    }


    public String getAttributeValue(By locator) {
        WaitHelpers.waitForVisible(locator);
        return driver().findElement(locator).getAttribute("value");
    }


    // ==========================
    //   CLEAR INPUT
    // ==========================
    public void clearText(By locator) {
        WaitHelpers.waitForVisible(locator);
        WebElement ele = driver().findElement(locator);
        scrollIntoView(ele);
        ele.click();

        Actions a = new Actions(driver());
        a.keyDown(Keys.CONTROL)
                .sendKeys("a")
                .keyUp(Keys.CONTROL)
                .sendKeys(Keys.DELETE)
                .perform();
    }


    // ==========================
    //   SUPER RELIABLE DATE SETTER
    // ==========================
    public void setDate(By locator, String yyyy_mm_dd) {

        JavascriptExecutor js = (JavascriptExecutor) driver();

        WebElement ele = driver().findElement(locator);

        scrollIntoView(ele);

        js.executeScript("""
            arguments[0].removeAttribute('readonly');
            arguments[0].value = arguments[1];
            arguments[0].dispatchEvent(new Event('input', {bubbles: true}));
            arguments[0].dispatchEvent(new Event('change', {bubbles: true}));
        """, ele, yyyy_mm_dd);

        WaitHelpers.sleep(400);   // Allow HRM AJAX recalculation
        WaitHelpers.waitForLoaderToDisappear();
    }

    public void setDateFieldReliable(By fromLocator, By toLocator, String fromDate, String toDate) {

        WebDriver driver = getDriver();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        WebElement from = driver.findElement(fromLocator);
        WebElement to = driver.findElement(toLocator);

        // 1 — Click field so HRM attaches its listeners
        //clickElement(fromLocator);
        from.click();
        //System.out.println("from click");
        js.executeScript("arguments[0].value = arguments[1];", from, fromDate);
        js.executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", from);

        // 2 — Click TO field so HRM recalculates duration
        to.click();
        //clickElement(toLocator);
        //System.out.println("to click");
        js.executeScript("arguments[0].value = arguments[1];", to, toDate);
        js.executeScript("arguments[0].dispatchEvent(new Event('change', { bubbles: true }));", to);

        // 3 — Give HRM time to load "Partial Days"
        WaitHelpers.waitForPresence(By.xpath("//label[text()='Partial Days']"));

        log.info("Dates set AND HRM recalculation triggered successfully.");
    }

    // ==========================
    //   SELECT DROPDOWN VALUE
    // ==========================
    public void selectDropdownValue(By locator, String text) {

        clickElement(locator);

        if (text.isEmpty()) return;

        By option = By.xpath("//div[@role='listbox']//span[normalize-space()='" + text + "']");

        WaitHelpers.waitForVisible(option);

        clickElement(option);
    }


    // ==========================
    //    JS CLICK
    // ==========================
    protected void jsClick(By locator) {
        WebElement ele = driver().findElement(locator);
        scrollIntoView(ele);
        ((JavascriptExecutor) driver()).executeScript("arguments[0].click();", ele);
    }


    // ==========================
    //   SCROLL INTO VIEW
    // ==========================
    private void scrollIntoView(WebElement ele) {
        ((JavascriptExecutor) driver())
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", ele);
    }


    // ==========================
    //   DISPLAY CHECK
    // ==========================
    public boolean isDisplayed(By locator) {
        try {
            return driver().findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void safeClick(By locator) {
        WebDriverWait wait = new WebDriverWait(driver(), Duration.ofSeconds(10));
        wait.ignoring(StaleElementReferenceException.class)
                .until(d -> {
                    d.findElement(locator).click();
                    return true;
                });
    }

    public boolean selectDropdownIfOptionsExist(By dropdown, String optionText) {

        WaitHelpers.waitForLoaderToDisappear();
        WaitHelpers.waitForVisible(dropdown);
        clickElement(dropdown);

        By options = By.xpath("//div[@role='listbox']//span");
        List<WebElement> optionList = getDriver().findElements(options);

        if (optionList.isEmpty()) {
            log.warn("Dropdown is EMPTY. Cannot select: " + optionText);
            getDriver().findElement(By.tagName("body")).click();
            return false;
        }

        for (WebElement opt : optionList) {
            if (opt.getText().trim().equalsIgnoreCase(optionText)) {
                opt.click();
                return true;
            }
        }

        log.warn("Option not found: " + optionText);
        return false;
    }

    public void setDateReact(By locator, String value) {
        WebElement ele = getDriver().findElement(locator);
        JavascriptExecutor js = (JavascriptExecutor) getDriver();

        js.executeScript(
                "arguments[0].removeAttribute('readonly');" +
                        "arguments[0].value = arguments[1];" +
                        "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                        "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));" +
                        "arguments[0].dispatchEvent(new Event('blur', { bubbles: true }));",
                ele, value
        );
    }

    public void selectAutoComplete(By inputLocator, String value) {

        WebElement input = driver().findElement(inputLocator);
        input.clear();
        input.sendKeys(value);

        By option = By.xpath(
                "//div[@role='option']//span[contains(text(),'" + value + "')]"
        );

        WaitHelpers.waitForPresence(option);
        clickElement(option);

        // Force blur so React commits value
        input.sendKeys(Keys.TAB);

        WaitHelpers.sleep(300);
    }

    protected boolean isElementPresent(By locator) {
        try {
            return driver().findElements(locator).size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    protected void confirmActionIfPopup() {
        By confirmBtn =
                By.xpath("//button[normalize-space()='Ok' or normalize-space()='Confirm']");
        if (isElementPresent(confirmBtn)) {
            clickElement(confirmBtn);
        }
    }

}