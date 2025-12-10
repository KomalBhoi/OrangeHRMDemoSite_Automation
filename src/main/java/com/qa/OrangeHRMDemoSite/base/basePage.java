package com.qa.OrangeHRMDemoSite.base;

import com.qa.OrangeHRMDemoSite.Utils.PropertiesReader;
import com.qa.OrangeHRMDemoSite.Utils.WaitHelpers;
import com.qa.OrangeHRMDemoSite.driver.driverMgr;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.qa.OrangeHRMDemoSite.driver.driverMgr.getDriver;

public class basePage {

    private static final Logger log= LogManager.getLogger(basePage.class);

    // NO local driver stored → prevents NPE
    protected WebDriver driver() {
        return driverMgr.getDriver();
    }

    public basePage() {}

    public void openOrangeHRMUrl() {
        driver().get(PropertiesReader.readKey("url"));
    }

    // Click
    protected void clickElement(By locator) {
        WaitHelpers.waitForClickable(locator);
        driver().findElement(locator).click();
    }

    // Type text
    protected void enterInput(By locator, String text) {
        WaitHelpers.visibilityOfElement(locator);
        WebElement ele = driver().findElement(locator);
        ele.clear();
        ele.sendKeys(text);
    }

    // Get Text
    protected String getText(By locator) {
        WaitHelpers.visibilityOfElement(locator);
        return driver().findElement(locator).getText();
    }

    protected String getAttributeValue(By locator){
        WaitHelpers.visibilityOfElement(locator);
        return driver().findElement(locator).getAttribute("value");
    }

    public void clearText(By by) {
        WebElement element = driver().findElement(by);
        element.click();

        Actions actions = new Actions(driver());
        actions.keyDown(Keys.CONTROL)
                .sendKeys("a")
                .keyUp(Keys.CONTROL)
                .sendKeys(Keys.DELETE)
                .perform();
    }

    // Set date
    public void setDateField(By fromLocator, By toLocator, String fromDate, String toDate) {
        JavascriptExecutor js = (JavascriptExecutor) driver();

        WebElement from = driver().findElement(fromLocator);
        js.executeScript("arguments[0].removeAttribute('readonly'); arguments[0].value = arguments[1];",
                from, fromDate);
        js.executeScript("arguments[0].dispatchEvent(new Event('change'))", from);

        WebElement to = driver().findElement(toLocator);
        js.executeScript("arguments[0].removeAttribute('readonly'); arguments[0].value = arguments[1];",
                to, toDate);
        js.executeScript("arguments[0].dispatchEvent(new Event('change'))", to);
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

    public void selectDate(By calendarInput, String yyyy_mm_dd) {

        WaitHelpers.waitForClickable(calendarInput);
        clickElement(calendarInput); // open calendar

        String[] parts = yyyy_mm_dd.split("-");
        String year = parts[0];
        String monthName = getMonthName(Integer.parseInt(parts[1]));  // 11 -> November
        String day = parts[2];

        By monthSelector = By.xpath("(//div[@class='oxd-calendar-selector-month'])[1]");
        By yearSelector  = By.xpath("(//div[@class='oxd-calendar-selector-year'])[1]");
        By leftArrow     = By.xpath("(//button[@class='oxd-icon-button'])[1]");
        By rightArrow    = By.xpath("(//button[@class='oxd-icon-button'])[2]");

        // 1️⃣ WAIT for calendar UI
        WaitHelpers.waitForVisible(monthSelector);

        // 2️⃣ ADJUST YEAR
        while (true) {
            String currentYear = getDriver().findElement(yearSelector).getText().trim();
            if (currentYear.equals(year)) break;
            if (Integer.parseInt(currentYear) < Integer.parseInt(year)) {
                clickElement(rightArrow);
            } else {
                clickElement(leftArrow);
            }
        }

        // 3️⃣ ADJUST MONTH
        while (true) {
            String currentMonth = getDriver().findElement(monthSelector).getText().trim();
            if (currentMonth.equalsIgnoreCase(monthName)) break;
            clickElement(rightArrow);
        }

        // 4️⃣ SELECT DAY
        By dayLocator = By.xpath("//div[@class='oxd-calendar-date' and text()='" + Integer.parseInt(day) + "']");
        WaitHelpers.waitForClickable(dayLocator);
        clickElement(dayLocator);
    }

    public String getMonthName(int monthNumber) {
        return java.time.Month.of(monthNumber).name().substring(0,1)
                + java.time.Month.of(monthNumber).name().substring(1).toLowerCase();
    }

    public void setDateDirect(By locator, String yyyy_dd_mm) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        WebElement input = getDriver().findElement(locator);

        js.executeScript("""
        arguments[0].removeAttribute('readonly');
        arguments[0].value = arguments[1];
        arguments[0].dispatchEvent(new Event('change', { bubbles: true }));
        arguments[0].dispatchEvent(new Event('input', { bubbles: true }));
    """, input, yyyy_dd_mm);

        try { Thread.sleep(400); } catch (Exception ignored) {}
    }

    public void triggerLeaveEntitlementAjax() {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("""
        if (typeof validateLeaveEntitlementAjax === 'function') {
            validateLeaveEntitlementAjax();
        }
    """);
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

    // Dropdown selection
    public void selectDropdownValue(By dropdownLocator,String optionText) {
        WaitHelpers.checkVisibility(dropdownLocator);
        clickElement(dropdownLocator);

        if(optionText.isEmpty()) return;

        By option = By.xpath("//div[@role='listbox']//span[text()='" + optionText + "']");
        new WebDriverWait(driver(), Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(option))
                .click();
    }

    public void safeClick(By locator) {
        WebDriverWait wait = new WebDriverWait(driver(), Duration.ofSeconds(10));
        wait.ignoring(StaleElementReferenceException.class)
                .until(d -> {
                    d.findElement(locator).click();
                    return true;
                });
    }

    protected boolean isDisplayed(By locator) {
        try {
            return driver().findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected void jsClick(By locator) {
        JavascriptExecutor js = (JavascriptExecutor) driver();
        js.executeScript("arguments[0].click();", driver().findElement(locator));
    }

    public void pickDate(By dateContainer, String yyyyDDMM) {

        // Convert YYYY-DD-MM → YYYY-MM-DD
        String[] p = yyyyDDMM.split("-");
        String yyyy = p[0];
        String dd   = p[1];
        String mm   = p[2];

        String correctFormat = yyyy + "-" + mm + "-" + dd; // <-- UI-expected format

        int year  = Integer.parseInt(yyyy);
        int month = Integer.parseInt(mm);
        int day   = Integer.parseInt(dd);

        // 1️⃣ Open calendar
        forceOpenCalendar(dateContainer);

        // 2️⃣ Select YEAR
        WaitHelpers.waitForClickable(By.xpath("//div[@class='oxd-calendar-selector-year']"));
        clickElement(By.xpath("//div[@class='oxd-calendar-selector-year']"));
        clickElement(By.xpath("//li[text()='" + year + "']"));

        // 3️⃣ Select MONTH
        clickElement(By.xpath("//div[@class='oxd-calendar-selector-month']"));
        clickElement(By.xpath("//li[@role='option'][" + month + "]"));  // month index 1–12

        // 4️⃣ Select DAY
        By dayLocator = By.xpath("//div[@class='oxd-calendar-date'][text()='" + day + "']");
        WaitHelpers.waitForClickable(dayLocator);
        clickElement(dayLocator);

        log.info("Date selected successfully: " + correctFormat);
    }

    public void openCalendar(By dateContainer) {
        WebElement container = driverMgr.getDriver().findElement(dateContainer);

        WebElement icon = container.findElement(By.xpath(".//i[contains(@class,'calendar')]"));
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView(true);", icon);

        icon.click();  // This opens the OrangeHRM date picker
    }

    public void forceOpenCalendar(By dateContainer) {
        WebDriver driver = getDriver();
        WebElement container = driver.findElement(dateContainer);

        // 1. Try normal click on the field first
        try {
            container.click();
            Thread.sleep(400);
            if (isCalendarOpen()) return; // success
        } catch (Exception ignored) {}

        // 2. Try clicking calendar icon
        try {
            WebElement icon = container.findElement(By.xpath(".//i[contains(@class,'calendar')]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", icon);
            icon.click();
            Thread.sleep(400);
            if (isCalendarOpen()) return; // success
        } catch (Exception ignored) {}

        // 3. JS click as final fallback
        try {
            WebElement icon = container.findElement(By.xpath(".//i[contains(@class,'calendar')]"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", icon);
            Thread.sleep(400);
            if (isCalendarOpen()) return; // success
        } catch (Exception ignored) {}

        throw new RuntimeException("Calendar did NOT open after 3 attempts!");
    }

    public boolean isCalendarOpen() {
        return getDriver().findElements(By.xpath("//div[contains(@class,'oxd-calendar')]")).size() > 0;
    }
}