package com.qa.OrangeHRMDemoSite.pages;

import com.qa.OrangeHRMDemoSite.Utils.WaitHelpers;
import com.qa.OrangeHRMDemoSite.base.basePage;
import com.qa.OrangeHRMDemoSite.driver.driverMgr;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;
import static com.qa.OrangeHRMDemoSite.Utils.browserUtils.scrollTo;
import static com.qa.OrangeHRMDemoSite.driver.driverMgr.driver;
import static com.qa.OrangeHRMDemoSite.driver.driverMgr.getDriver;

public class candidatePage extends basePage {

    public static final Logger log = LogManager.getLogger(candidatePage.class);
    public candidatePage() {
        super();
    }

    // Menu
    private By recruitemetLink = By.xpath("//span[text()='Recruitment']");
    private By candidatesTab =By.xpath("//a[@class='oxd-topbar-body-nav-tab-item' and text()='Candidates']");
    private By addBtn = By.xpath("//i[contains(@class,'oxd-icon bi-plus')]");

    // Add Candidate fields
    private By firstNmInput =By.xpath("//input[@placeholder='First Name']");
    private By middleNmInput = By.xpath("//input[@placeholder='Middle Name']");
    private By lastNmInput = By.xpath("//input[@placeholder='Last Name']");
    private By selectVacancy = By.xpath("//div[@class='oxd-select-text-input']");
    private By emailInput = By.xpath("(//input[@placeholder='Type here'])[1]");
    private By contactNoInput = By.xpath("(//input[@placeholder='Type here'])[2]");
    private By keywordsInput = By.xpath("//input[@placeholder='Enter comma seperated words...']");
    private By dtOfApplication = By.xpath("//input[@placeholder='yyyy-dd-mm']");
    private By notesInput = By.xpath("//textarea[@placeholder='Type here']");
    private By consentChk = By.xpath("//i[contains(@class,'oxd-icon bi-check')]");
    private By saveBtn = By.xpath("//button[@type='submit' and text()=' Save ']");
    private By cancelBtn = By.xpath("//button[@type='submit' and text()=' Cancel ']");

    // Search Candidate
    private By searchJobTitle=By.xpath("(//div[@class='oxd-select-text-input'])[1]");
    private By searchVacancy = By.xpath("(//div[@class='oxd-select-text-input'])[2]");
    private By searchHiringMgrNm = By.xpath("(//div[@class='oxd-select-text-input'])[3]");
    private By searchStatus = By.xpath("(//div[@class='oxd-select-text-input'])[4]");
    private By searchCandidateNm = By.xpath("//input[@placeholder='Type for hints...']");
    private By searchKeywords = By.xpath("//input[@placeholder='Enter comma seperated words...']");
    private By searchFromDt = By.xpath("//input[@placeholder='From']");
    private By searchToDt = By.xpath("//input[@placeholder='To']");
    private By searchMethodOfApp = By.xpath("(//div[@class='oxd-select-text-input'])[5]");
    private By searchBtn = By.xpath("//button[@type='submit' and text()=' Search ']");

    // Interview Page
    private By viewIcon = By.xpath("//i[contains(@class,'oxd-icon bi-eye')]");
    private By shortlistBtn =  By.xpath("//button[.//text()[normalize-space()='Shortlist']]"); //By.xpath("//button[contains(@class,'oxd-button') and text()=' Shortlist ']");
    private By scheduleInterviewBtn = By.xpath("//button[.//text()[normalize-space()='Schedule Interview']]");
    private By errorMessages = By.xpath("//span[contains(@class,'oxd-input-field-error-message')]");

    private By tableRows = By.xpath("//div[@class='oxd-table-body']//div[@role='row']");
    private By noRows = By.xpath("//span[text()='No Records Found']");

    // =====================================================================================
    // Add Candidate
    // =====================================================================================
    public void addCandidatesInfo(String fNm, String mNm,String lNm,String vacancy,String email,
                                  String contat,String keywords,String appDt,String notes) throws InterruptedException {
        log.info("Adding Candidates: "+fNm+"||"+lNm);

        clickElement(recruitemetLink);
        clickElement(candidatesTab);
        clickElement(addBtn);

        enterInput(firstNmInput,fNm);
        enterInput(middleNmInput,mNm);
        enterInput(lastNmInput,lNm);

        selectDropdownValue(selectVacancy,vacancy);
        enterInput(emailInput,email);
        enterInput(contactNoInput,contat);
        enterInput(keywordsInput,keywords);

        // Date of application selection will come here
        if(!appDt.isEmpty()) {
            setDateFieldReliable(dtOfApplication,dtOfApplication,appDt,"");
        }

        enterInput(notesInput,notes);
        //System.out.println("Before consent - clicked on checkbox.");
        WaitHelpers.waitForReactToSettle();
        WaitHelpers.waitForLoaderToDisappear();
        //clickElement(consentChk);
        //WaitHelpers.waitForLoaderToDisappear();
        WaitHelpers.waitForClickable(saveBtn);
        clickElement(saveBtn);
        jsClick(saveBtn);

        log.info("Candidate added successfully.");
    }

    // =====================================================================================
    // Search Candidate
    // =====================================================================================
    public boolean searchCandidate(String jobTitle,String vacancyOpt,String hiringMgrNm,
                                String status, String candidateNm,String keywords,
                                String frmDt,String toDt,String methodOfApp) throws InterruptedException {

        log.info("Searching candidate: "+candidateNm);

        clickElement(recruitemetLink);
        clickElement(candidatesTab);

        //WaitHelpers.checkVisibility(searchJobTitle);
        selectDropdownValue(searchJobTitle,jobTitle);
        selectDropdownValue(searchVacancy,vacancyOpt);
        selectDropdownValue(searchHiringMgrNm,hiringMgrNm);
        selectDropdownValue(searchStatus,status);
        //WaitHelpers.checkVisibility(searchCandidateNm);
        enterInput(searchCandidateNm,candidateNm);

        if(!candidateNm.isEmpty()) {
            By suggestionOption = By.xpath("//div[@role='option']//span[contains(text(),'" + candidateNm + "')]");
            WaitHelpers.waitForPresence(suggestionOption);
            clickElement(suggestionOption);
        }

        enterInput(keywordsInput,keywords);
        if(!frmDt.isEmpty() || !toDt.isEmpty()) {
            setDateFieldReliable(searchFromDt, searchToDt, frmDt, toDt);
        }

        selectDropdownValue(searchMethodOfApp,methodOfApp);

        clickElement(searchBtn);
        scrollTo(searchBtn);
        By resultRows = By.xpath("//div[@class='oxd-table-body']//div[@role='row']");
        WaitHelpers.waitForVisible(resultRows);
        boolean found = getDriver().findElements(resultRows).size() > 0;
        System.out.println("Candidate Page found: "+found);
        if (found) {
            log.info("Candidate FOUND: " + candidateNm);
        } else {
            log.warn("Candidate NOT found: " + candidateNm);
        }

        return found;

//        if(jobTitle !="" || vacancyOpt !="" || hiringMgrNm !="" || status !="" || candidateNm !=""
//                || keywords !="" || frmDt !="" || toDt !="" || methodOfApp !=""){
//            WaitHelpers.waitForPresence(searchBtn);
//            clickElement(searchBtn);
//            scrollTo(searchBtn);
////            js.executeScript("window.scrollBy(500,2000);");
////            Thread.sleep(5000);
//        }else{
//            System.out.println("Record not found!!");
//        }
    }

    // =====================================================================================
    // Initiate Interview Process
    // =====================================================================================
    public boolean initiateInterviewProcess() throws InterruptedException {

        log.info("---- Starting Interview Process ----");

        WaitHelpers.waitForClickable(viewIcon);
        clickElement(viewIcon);

        WaitHelpers.waitForLoaderToDisappear();

        if(isElementPresent(shortlistBtn)) {
            log.info("Candidate not shortlisted. Clicking Shortlist..");
            //WaitHelpers.waitForClickable(shortlistBtn);
            clickElement(shortlistBtn);
            confirmActionIfPopup();
        }else if(isElementPresent(scheduleInterviewBtn)) {
//        WaitHelpers.waitForClickable(scheduleInterviewBtn);
         log.info("Candidate already shortlisted. Proceeding..");
         clickElement(scheduleInterviewBtn);
         WaitHelpers.waitForClickable(saveBtn);
         clickElement(saveBtn);
        }else{
            log.warn("No actionable interview buttons found. Candidate already processed. ");
            return false;
        }

        WaitHelpers.waitForPresence(
                By.xpath("//div[contains(@class,'oxd-table-body')]")
        );

        // 2️⃣ Check if "No Records Found" exists (WITHOUT wait)
        if (driver().findElements(noRows).size() > 0) {
            String msg = driver().findElement(noRows).getText().trim();
            if (msg.equalsIgnoreCase("No Records Found")) {
                log.info("No vacancy records found.");
                return false;
            }
        }

        // 3️⃣ Otherwise check for table rows
        List<WebElement> rows = driver().findElements(tableRows);
        if (rows.size() > 0) {
            log.info("Vacancy records found: " + rows.size());
            return true;
        }

        // 4️⃣ Defensive fallback
        log.warn("Search completed but no rows and no 'No Records Found' message.");
        return false;

        //Check for validation errors
//        WaitHelpers.waitForPresence(By.xpath("//span[contains(@class,'oxd-input-field-error-message')]"));
//
//        List<WebElement> errors = driver().findElements(
//                By.xpath("//span[contains(@class,'oxd-input-field-error-message')]"));
//
//        if(!errors.isEmpty()) {
//            throw new AssertionError("Validation failed: " + errors.get(0).getText());
//        }

        //log.info("---- Interview process initiated successfully ----");
    }

    public boolean isErrorDisplayed() {
        return driver().findElements(errorMessages).size()>0;
    }
}
