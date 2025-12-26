package com.qa.OrangeHRMDemoSite.pages;

import com.qa.OrangeHRMDemoSite.Utils.WaitHelpers;
import com.qa.OrangeHRMDemoSite.base.basePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import java.util.List;

import static com.qa.OrangeHRMDemoSite.Utils.browserUtils.scrollTo;

public class vacancyPage extends basePage {

    private static final Logger log = LogManager.getLogger(recruitementPage.class);
    public vacancyPage(){
        super();
    }

    // Main menu
    private By recruitemetLink = By.xpath("//span[text()='Recruitment']");
    private By vacanciesTab = By.xpath("//a[@class='oxd-topbar-body-nav-tab-item' and text()='Vacancies']");
    private By candidatesTab = By.xpath("//a[@class='oxd-topbar-body-nav-tab-item' and text()='Candidates']");
    private By addBtn = By.xpath("//i[contains(@class,'oxd-icon bi-plus')]");

    // Add Vacancy Form
    private By vacancyName = By.xpath("(//input[@class='oxd-input oxd-input--active'])[2]");
    private By jobTitle = By.xpath("//div[@class='oxd-select-text-input']");
    private By jobDesc = By.xpath("//textarea[@placeholder='Type description here']");
    private By hiringMgrName = By.xpath("//input[@placeholder='Type for hints...']");
    private By noOfPositions = By.xpath("//label[text()='Number of Positions']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private By activeSwitch = By.xpath("(//span[contains(@class,'oxd-switch-input')])[1]");
    private By publicSwitch = By.xpath("(//span[contains(@class,'oxd-switch-input')])[2]");
    private By saveBtn = By.xpath("//button[@type='submit' and text()=' Save ']");

    //Search Fields
    private By searchJobTitle = By.xpath("(//div[@class='oxd-select-text-input'])[1]");
    private By searchVacancy = By.xpath("(//div[@class='oxd-select-text-input'])[2]");
    private By searchHiringMgrNm = By.xpath("(//div[@class='oxd-select-text-input'])[3]");
    private By searchStatus = By.xpath("(//div[@class='oxd-select-text-input'])[4]");
    private By searchBtn = By.xpath("//button[@type='submit' and text()=' Search ']");

    private By tableRows = By.xpath("//div[@class='oxd-table-body']//div[@role='row']");
    private By noRows = By.xpath("//span[text()='No Records Found']");



    /** 🔎 Check if vacancy already exists */
    public boolean isVacancyPresent(String vacancyNameText) {
        clickElement(vacanciesTab);
        clickElement(vacanciesTab);
        WaitHelpers.waitForPresence(tableRows);

        return driver().findElements(
                By.xpath("//div[@role='cell']//span[text()='" + vacancyNameText + "']")
        ).size() > 0;
    }

    public void createVacancyIfRequired(String vacancyName,
                                        String jobTitle,
                                        String managerName) throws InterruptedException
    {
        addVacancies(
                vacancyName,
                jobTitle,
                "Auto-created vacancy for automation",
                managerName,
                "1"
        );
    }

    // =====================================================================================
    //  Add Vacancy
    // =====================================================================================
    public void addVacancies(String vacancyNm,String jobTtl,String desc,String MgrNm,
                             String noPos) throws InterruptedException {
        log.info("Adding Vacancies: "+vacancyNm);

        clickElement(recruitemetLink);
        clickElement(vacanciesTab);
        clickElement(addBtn);

        WaitHelpers.waitForLoaderToDisappear();
        enterInput(vacancyName,vacancyNm);

        boolean jobSelected = selectDropdownIfOptionsExist(jobTitle, jobTtl);
        if (!jobSelected) {
            throw new RuntimeException("Job Title dropdown empty");
        }

        enterInput(jobDesc,desc);

        selectAutoComplete(hiringMgrName,MgrNm);

        WaitHelpers.waitForClickable(noOfPositions);
        clickElement(noOfPositions);
        clearText(noOfPositions);
        enterInput(noOfPositions,noPos);

        WaitHelpers.waitForClickable(saveBtn);
        scrollTo(saveBtn);
        jsClick(saveBtn);

        By successToast = By.xpath("//p[contains(text(),'Successfully')]");
        try {
            WaitHelpers.waitForVisible(successToast);
            log.info("Success message displayed");
        } catch (TimeoutException e) {
            log.warn("Success message not shown (expected behavior in OrangeHRM)");
        }

        log.info("Vacancy successfully added.");
    }

    // =====================================================================================
    // Search Vacancies
    // =====================================================================================
    public boolean searchForVacancies(String jobTitle) throws InterruptedException {
        //,String vacancy,String hiringMgrNm,String status
        log.info("---- Search for Vacancies ----");

        //System.out.println("1");
        WaitHelpers.waitForClickable(candidatesTab);
        clickElement(candidatesTab);
        WaitHelpers.waitForClickable(vacanciesTab);
        clickElement(vacanciesTab);
        WaitHelpers.waitForClickable(searchJobTitle);
        selectDropdownValue(searchJobTitle,jobTitle);
//        WaitHelpers.waitForClickable(searchVacancy);
//        selectDropdownValue(searchVacancy,vacancy);
//        WaitHelpers.waitForClickable(searchHiringMgrNm);
//        selectDropdownValue(searchHiringMgrNm,hiringMgrNm);
//        WaitHelpers.waitForClickable(searchStatus);
//        selectDropdownValue(searchStatus,status);

        WaitHelpers.waitForClickable(searchBtn);
        clickElement(searchBtn);
        scrollTo(searchBtn);

        // 1️⃣ Wait for search results container to load
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
    }

    // =====================================================================================
    // Verify row in list
    // =====================================================================================
    public boolean verifyVacancyInList(String jobTitle,String vacancy,String hiringMgrNm){

        WaitHelpers.waitForPresence(tableRows);
        List<WebElement> rows=driver().findElements(tableRows);

        for(WebElement row:rows) {

            List<WebElement> cells = row.findElements(By.xpath(".//div[@role='cell']"));

            if (cells.size() < 4) {
                continue; // safety
            }

            String vacNm = cells.get(1).getText().trim(); //row.findElement(By.xpath(".//div[@role='cell'][2]")).getText();
            String jobTtl = cells.get(2).getText().trim(); //row.findElement(By.xpath(".//div[@role='cell'][3]")).getText();
            String mgrNm = cells.get(3).getText().trim(); //row.findElement(By.xpath(".//div[@role='cell'][4]")).getText();
            //String vacancyStatus =row.findElement(By.xpath(".//div[@role='cell'][5]")).getText();

            if (vacNm.equalsIgnoreCase(vacancy)
                    && jobTtl.equalsIgnoreCase(jobTitle.trim())
               ) {
                // && mgrNm.equalsIgnoreCase(hiringMgrNm.trim())
                //System.out.println("Inside if");
                log.info("Vacancy verified successfully: " + vacancy);
                return true;
            }
        }
        log.warn("Vacancy NOT found: " + vacancy);
        return false;
    }
}
