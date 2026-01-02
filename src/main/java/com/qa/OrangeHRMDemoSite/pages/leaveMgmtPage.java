package com.qa.OrangeHRMDemoSite.pages;

import com.qa.OrangeHRMDemoSite.Utils.WaitHelpers;
import com.qa.OrangeHRMDemoSite.base.basePage;
import com.qa.OrangeHRMDemoSite.driver.driverMgr;
import org.openqa.selenium.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import static com.qa.OrangeHRMDemoSite.Utils.browserUtils.scrollTo;


public class leaveMgmtPage extends basePage {

   private static final Logger log=LogManager.getLogger(leaveMgmtPage.class);
   //private WebDriver driver;

    public leaveMgmtPage() {
        super();
    }

    //Assign Leave
    private By leaveLink = By.xpath("//span[normalize-space()='Leave']");
    private By assignLeaveTab = By.xpath("//a[@class='oxd-topbar-body-nav-tab-item' and normalize-space()='Assign Leave']");
    private By empName = By.xpath("//input[contains(@placeholder,'Type for')]");
    private By leaveType = By.xpath("//div[@class='oxd-select-text-input']");
    private By fromDtToSelect = By.xpath("(//input[contains(@placeholder,'yyyy')])[1]");
    private By toDtToSelect = By.xpath("(//input[contains(@placeholder,'yyyy')])[2]");
    private By fromDateContainer = By.xpath("(//div[contains(@class,'oxd-date-input')])[1]");
    private By toDateContainer   = By.xpath("(//div[contains(@class,'oxd-date-input')])[2]");
    private By commentTxt = By.xpath("//textarea[contains(@class,'oxd-textarea')]");
    private By assignBtn = By.xpath("//button[@type='submit' and normalize-space()='Assign']");

    //Search assigned leave
    private By leaveListTab = By.xpath("//a[@class='oxd-topbar-body-nav-tab-item' and normalize-space()='Leave List']");
    private By searchleaveStatus = By.xpath("(//div[@class='oxd-select-text-input'])[1]");
    private By searchleaveType = By.xpath("//div[@class='oxd-layout-context']//label[normalize-space()='Leave Type']" +
            "/following::div[contains(@class,'oxd-select-text-input')][1]");
    private By searchEmpNm = By.xpath("//div[@class='oxd-layout-context']//input[@placeholder='Type for hints...']");
    private By searchSubUnit = By.xpath("//div[@class='oxd-layout-context']//label[text()='Sub Unit']/following::div[contains(@class,'oxd-select-text-input')][1]");
    private By searchBtn = By.xpath("//button[@type='submit']");
    private By tableRows = By.xpath("//div[@class='oxd-table-body']//div[@role='row']");
    private By noRecords = By.xpath("//span[text()='No Records Found']");
    private By toastMsg = By.xpath("//p[contains(text(),'Successfully Assigned') or contains(text(),'Failed')]");
    private By partialDaysSection = By.xpath("//label[text()='Partial Days']");
    private By partialDaysDropdown = By.xpath("//label[text()='Partial Days']/following::div[@class='oxd-select-text-input'][1]");
    private By partialDaysOptionNone = By.xpath("//div[@role='option']//span[text()='None']");
    private By alertOkBtn=By.xpath(".//button[.//text()[normalize-space()='Ok']]");
    private By configureTab = By.xpath("//span[text()='Configure ']");
    private By addLeaveType = By.xpath("//a[@role='menuitem' and normalize-space()='Leave Types']");
    private By addBtnLeaveType = By.xpath("//i[@class='oxd-icon bi-plus oxd-button-icon']");
    private By leaveTypeNm = By.xpath("(//input[@class='oxd-input oxd-input--active'])[2]");
    private By saveBtn = By.xpath("//button[@type='submit']");

    /** 🔎 Check if leave type already exists */
    public boolean isLeaveTypePresent(String leaveTypeText) {
        clickElement(leaveListTab);
        clickElement(leaveListTab);
        WaitHelpers.waitForPresence(tableRows);

        return driver().findElements(
                By.xpath("//div[@role='cell']//span[text()='" + leaveTypeText + "']")
        ).size() > 0;
    }
//
//    public void createLeaveTypeIfRequired(String leaveTypeNm) throws InterruptedException
//    {
//        addLeaveType(leaveTypeNm);
//    }
//

    public void initializeLeaveModuleIfRequired() {

        log.info("Checking Leave module initialization...");

        // Navigate to Leave module
        WaitHelpers.waitForClickable(leaveLink);
        clickElement(leaveLink);

        // Month/Year Save page appears only on first access
        By saveBtn = By.xpath("//button[@type='submit' and contains(.,'Save')]");

        if (isElementPresent(saveBtn, 5)) {
            log.info("Leave module initial setup detected. Clicking Save.");

            WaitHelpers.waitForClickable(saveBtn);
            clickElement(saveBtn);

            // Wait for Leave sub-menu to appear
            WaitHelpers.waitForInvisibility(saveBtn);
            WaitHelpers.waitForClickable(assignLeaveTab);

            log.info("Leave module initialized successfully.");
        } else {
            log.info("Leave module already initialized. Skipping setup.");
        }
    }

    public void ensureLeaveTypeExists(String leaveTypeName) {

        log.info("Ensuring Leave Type exists: " + leaveTypeName);

        // Navigate to Configure → Leave Types
        WaitHelpers.waitForClickable(configureTab);
        clickElement(configureTab);

        WaitHelpers.waitForClickable(leaveType);
        clickElement(leaveType);

        WaitHelpers.waitForVisible(leaveType);

        // Check if Leave Type already exists
        By leaveTypeRow = By.xpath("//div[@role='row']//div[text()='" + leaveTypeName + "']");

        if (isElementPresent(leaveTypeRow, 5)) {
            log.info("Leave Type already exists: " + leaveTypeName);
            return;
        }

        log.info("Leave Type not found. Creating: " + leaveTypeName);

        // Add Leave Type
        WaitHelpers.waitForClickable(addBtnLeaveType);
        clickElement(addBtnLeaveType);

        WaitHelpers.waitForVisible(leaveTypeNm);
        enterInput(leaveTypeNm, leaveTypeName);

        WaitHelpers.waitForClickable(saveBtn);
        clickElement(saveBtn);

        // Verify creation
        WaitHelpers.waitForPresence(leaveTypeRow);

        log.info("Leave Type created successfully: " + leaveTypeName);
    }

    protected boolean isElementPresent(By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait =
                    new WebDriverWait(driverMgr.getDriver(), Duration.ofSeconds(timeoutSeconds));
            wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean assignLeave(String empNm, String leaveTy, String comments) {
        log.info("---- Assign Leave Started ----");

        clickElement(leaveLink);
        clickElement(assignLeaveTab);

        // Employee
        WaitHelpers.waitForLoaderToDisappear();
        WaitHelpers.waitForClickable(empName);
        enterInput(empName, empNm);

        By suggestion = By.xpath("//div[@role='option']//span[contains(text(),'" + empNm + "')]");
        WaitHelpers.waitForClickable(suggestion);
        clickElement(suggestion);

        // Leave type
        WaitHelpers.waitForClickable(leaveType);
        selectDropdownValue(leaveType, leaveTy);

        // Pick dates using real UI calendar
        setDateReact(fromDtToSelect, "2025-12-15");
        setDateReact(toDtToSelect, "2025-12-18");
        //setDate(fromDtToSelect,"2025-26-11");
        //setDate(toDtToSelect,"2025-30-11");

        String uiFrom = getAttributeValue(fromDtToSelect);
        String uiTo   = getAttributeValue(toDtToSelect);

        log.info("UI shows dates: FROM=" + uiFrom + " TO=" + uiTo);

        if (uiFrom.isBlank() || uiTo.isBlank()) {
            log.error("DATE NOT SET — React did NOT update!");
            return false;
        }

        //triggerLeaveEntitlementAjax();

//        // **WAIT FOR Partial Days dropdown to appear**
//        WaitHelpers.waitForPresence(partialDaysSection);
//        WaitHelpers.waitForClickable(partialDaysDropdown);
//        clickElement(partialDaysDropdown);
//       // WaitHelpers.waitForClickable(partialDaysOptionNone);
//        clickElement(partialDaysOptionNone);

        try {
            // CASE 1: If Partial Days section appears
            if (isPartialDaysVisible()) {
                log.info("Partial Days visible → selecting 'None'");
                By partialDrop = By.xpath("//label[text()='Partial Days']/following::div[@class='oxd-select-text-input'][1]");
                By partialOpt = By.xpath("//span[text()='All Days']");

                WaitHelpers.waitForClickable(partialDrop);
                clickElement(partialDrop);

                WaitHelpers.waitForClickable(partialOpt);
                clickElement(partialOpt);
            }
            // CASE 2: If only Duration appears (like your screenshot)
            if (isDurationVisible()) {
                log.info("Duration visible → selecting 'Full Day'");
                By durationDrop = By.xpath("//label[text()='Duration']/following::div[@class='oxd-select-text-input'][1]");
                By fullDayOpt   = By.xpath("//span[text()='Half Day - Morning']");

                WaitHelpers.waitForClickable(durationDrop);
                clickElement(durationDrop);

                WaitHelpers.waitForClickable(fullDayOpt);
                clickElement(fullDayOpt);
            }

            // CASE 3: Neither visible (should not happen)
            else {
                log.warn("Neither Partial Days nor Duration is visible.");
            }
        }catch(Exception e){
            log.error("Unexpected control behavior: " + e.getMessage());
        }

        // Comment
        enterInput(commentTxt, comments);

        // Assign Leave
        WaitHelpers.waitForClickable(assignBtn);
        clickElement(assignBtn);

        WaitHelpers.waitForLoaderToDisappear();
        WaitHelpers.waitForClickable(alertOkBtn);
        clickElement(alertOkBtn);

        log.info("Assign clicked.");

        // Validation Error?
        List<WebElement> errors = driverMgr.getDriver().findElements(
                By.xpath("//span[contains(@class,'oxd-input-field-error-message')]")
        );

        if(!errors.isEmpty()) {
            log.error("Validation ERROR: " + errors.get(0).getText());
            return false;
        }

        // Toast (optional)
        try {
            By toast = By.xpath("//p[contains(text(),'Assign') or contains(text(),'Success')]");
            WebElement txt = WaitHelpers.waitForVisible_returnWebElement(toast);
            log.info("Toast: " + txt.getText());
        } catch(Exception ignore){}

        log.info("---- Assign Leave Completed ----");
        return true;
    }

    private boolean isPartialDaysVisible() {
        return driverMgr.getDriver().findElements(By.xpath("//label[text()='Partial Days']")).size() > 0;
    }

    private boolean isDurationVisible() {
        return driverMgr.getDriver().findElements(By.xpath("//label[text()='Duration']")).size() > 0;
    }

    /** Validates YYYY-DD-MM */
    private void validateDateFormat(String date) {
        if (date == null || date.isBlank()) return;

        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalArgumentException("Invalid date format. Use YYYY-DD-MM → Given: " + date);
        }
    }

    // ===== Clear fields on Assign Leave form =====
    public void clearLeaveDetails() {
        WaitHelpers.waitForClickable(leaveLink);
        clickElement(leaveLink);
        WaitHelpers.waitForClickable(assignLeaveTab);
        clickElement(assignLeaveTab);

        WaitHelpers.waitForVisible(empName);
        clearText(empName);
        WaitHelpers.waitForVisible(commentTxt);
        clearText(commentTxt);
    }

    // ===== Search Assigned Leave With Retry-Mechanism =====
    public boolean searchAssignedLeaveWithRetry(String fromDt, String toDt, String leaveSt,
                                                String leaveTy, String empNm, String subUnit, int retries, int waitSec) {

        for (int i = 1; i <= retries; i++) {
            boolean result = searchAssignedLeave(fromDt, toDt, leaveSt, leaveTy, empNm, subUnit);

            if (result) {
                log.info("Search succeeded on attempt {} of {}", i, retries);
                return true;
            }

            log.warn("Search attempt {} of {} failed. Retrying after {} sec...", i, retries, waitSec);
            try { Thread.sleep(waitSec * 1000L); } catch (Exception ignored) {}
        }

        log.error("Search failed after {} retries.", retries);
        return false;
    }

    // ===== Search Assigned Leave =====
    public boolean searchAssignedLeave(String fromDt,String toDt,String leaveStatus, String leaveTypeVal,
                                    String empNameVal,String subUnitVal) {
        log.info("Search assigned leave: status={}, type={}, emp={}, subUnit={}", leaveStatus, leaveTypeVal, empNameVal, subUnitVal);

        WaitHelpers.waitForClickable(leaveLink);
        clickElement(leaveLink);

        // Navigate to leave list tab
        WaitHelpers.waitForClickable(leaveListTab);
        clickElement(leaveListTab);

        // set date filters only if provided (format YYYY-DD-MM)
        if(fromDt !=null && !fromDt.isBlank()) {
            WaitHelpers.waitForVisible(fromDtToSelect);
            setDateFieldReliable(fromDtToSelect,toDtToSelect,fromDt,toDt == null?"":toDt);
        }

        if(leaveStatus !=null && !leaveStatus.isBlank()) {
            selectDropdownValue(searchleaveStatus, leaveStatus);
        }

        if(leaveTypeVal !=null && !leaveTypeVal.isBlank()) {
            selectDropdownValue(searchleaveType, leaveTypeVal);
        }

        //System.out.println("--Search Employee Name--");
        if(empNameVal !=null && !empNameVal.isBlank()) {
            WaitHelpers.waitForVisible(searchEmpNm);
            enterInput(searchEmpNm, empNameVal);

            By suggestion = By.xpath("//div[@role='option']//span[contains(normalize-space(),'" + empNameVal + "')]");
            WaitHelpers.waitForPresence(suggestion);
            clickElement(suggestion);
        }

        if(subUnitVal !=null && !subUnitVal.isBlank()) {
            selectDropdownValue(searchSubUnit, subUnitVal);
        }

        // perform search
        WaitHelpers.waitForClickable(searchBtn);
        clickElement(searchBtn);
        scrollTo(searchBtn);

        // if "No Records Found" visible => return false
//        WaitHelpers.waitForPresence(tableRows);
//        List<WebElement> rows= driverMgr.getDriver().findElements(tableRows);
//        log.info("Search returned {} rows.", rows.size());
//        return rows.size() > 0;
        WaitHelpers.waitForLoaderToDisappear();
        WebDriver driver = driverMgr.getDriver();

        //Wait for either ROWS or NO RECORDS
        new WebDriverWait(driver,Duration.ofSeconds(20)).until(d ->
            d.findElements(tableRows).size() > 0 || d.findElements(noRecords).size() > 0
        );

        if(driver.findElements(noRecords).size() > 0){
            log.info("No leave records found - valid state");
            return false;
        }

        log.info("Leave records found");
        return true;
    }

    // ===== Cancel leave if present (first available) =====
    public void cancelLeaveIfPresent() {
        log.info("Attempting to cancel leave if present...");

        WaitHelpers.waitForClickable(leaveLink);
        clickElement(leaveLink);

        // wait for Cancel link if present
        By cancelLink = By.xpath("//a[text()='Cancel']");
        if (isElementPresent(cancelLink)) {
            driverMgr.getDriver().findElements(cancelLink).get(0).click();

            // wait for success toast or message
            try {
                By successMsg = By.xpath("//p[contains(text(),'Successfully Updated') or contains(text(),'Successfully')]");
                WaitHelpers.waitForVisible(successMsg);
            } catch (Exception e) {
                log.warn("No success message shown after cancel (demo behavior).");
            }
        } else {
            log.info("No cancel link found.");
        }
    }

//    private boolean isElementPresent(By locator){
//        return driver().findElements(locator).size() > 0;
//    }

}
