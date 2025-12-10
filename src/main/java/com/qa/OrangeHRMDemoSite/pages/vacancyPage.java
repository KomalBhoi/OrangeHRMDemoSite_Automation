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
    private By noOfPositions = By.xpath("(//input[@class='oxd-input oxd-input--active'])[3]");
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


    // =====================================================================================
    //  Add Vacancy
    // =====================================================================================
    public void addVacancies(String vacancyNm,String jobTtl,String desc,String MgrNm,
                             String noPos) throws InterruptedException {
        log.info("Adding Vacancies: "+vacancyNm);

        WaitHelpers.waitForClickable(recruitemetLink);
        clickElement(recruitemetLink);

        WaitHelpers.waitForClickable(vacanciesTab);
        clickElement(vacanciesTab);

        WaitHelpers.waitForClickable(addBtn);
        clickElement(addBtn);

        WaitHelpers.checkVisibility(vacancyName);
        enterInput(vacancyName,vacancyNm);

        WaitHelpers.waitForClickable(jobTitle);
        selectDropdownValue(jobTitle,jobTtl);

        WaitHelpers.checkVisibility(jobDesc);
        enterInput(jobDesc,desc);

        WaitHelpers.waitForClickable(hiringMgrName);
        enterInput(hiringMgrName,MgrNm);

        // Select suggestion value
        By suggestionOption = By.xpath("//div[@role='option']//span[contains(text(),'" + MgrNm + "')]");
//        new WebDriverWait(driver, Duration.ofSeconds(10))
//                .until(ExpectedConditions.visibilityOfElementLocated(suggestionOption));
        // Click the first suggestion (exact match)
//        driver.findElement(suggestionOption).click();
        WaitHelpers.waitForPresence(suggestionOption);
        clickElement(suggestionOption);
        //js.executeScript("window.scrollBy(500,2000);");
        //WaitHelpers.waitForPresence(noOfPositions);
        enterInput(noOfPositions,noPos);
        //WaitHelpers.waitForPresence(saveBtn);
        scrollTo(saveBtn);
        clickElement(saveBtn);

        log.info("Vacancy successfully added.");
    }

    // =====================================================================================
    // Search Vacancies
    // =====================================================================================
    public boolean searchForVacancies(String jobTitle,String vacancy,String hiringMgrNm,String status) throws InterruptedException {

        log.info("---- Search for Vacancies ----");

        //System.out.println("1");
        WaitHelpers.waitForClickable(candidatesTab);
        clickElement(candidatesTab);
        WaitHelpers.waitForClickable(vacanciesTab);
        clickElement(vacanciesTab);
        //WaitHelpers.waitForClickable(searchJobTitle);
        selectDropdownValue(searchJobTitle,jobTitle);
        //WaitHelpers.waitForClickable(searchVacancy);
        selectDropdownValue(searchVacancy,vacancy);
        //WaitHelpers.waitForClickable(searchHiringMgrNm);
        selectDropdownValue(searchHiringMgrNm,hiringMgrNm);
        //WaitHelpers.waitForClickable(searchStatus);
        selectDropdownValue(searchStatus,status);

        clickElement(searchBtn);
        scrollTo(searchBtn);

        // if "No Records Found"
        WaitHelpers.waitForPresence(noRows);
        String noRecordFound_msg=driver().findElement(noRows).getText();
        //System.out.println(driverMgr.getDriver().findElement(noRows).getText());
        if(noRecordFound_msg.equalsIgnoreCase("No Records Found")) {
            return false;
        }

        WaitHelpers.waitForPresence(tableRows);
        List<WebElement> rows = driver().findElements(tableRows);
        return  rows.size() > 0;

//        if(jobTitle !="" || vacancy !="" || hiringMgrNm !="" || status !="") {
//            //System.out.println("Inside If");
//            WaitHelpers.waitForPresence(searchBtn);
//            clickElement(searchBtn);
//            scrollTo(searchBtn);
//            //Thread.sleep(5000);
//        }else{
//            System.out.println("Record not found!!");
//        }
//
//        WaitHelpers.waitForPresence(noRowsFound);
//        String noRecordFound_msg=driverMgr.getDriver().findElement(noRowsFound).getText();
//        //System.out.println(driverMgr.getDriver().findElement(noRowsFound).getText());
//        if(noRecordFound_msg.equalsIgnoreCase("No Records Found")) {
//            return false;
//        }
//        else {
//            WaitHelpers.waitForPresence(tableRows);
//            List<WebElement> rows = driverMgr.getDriver().findElements(tableRows);
//            //System.out.println("No of rows: " + rows.size());
//            if (rows.size() > 0) {
//                return true;
//            } else {
//                return false;
//            }
//        }
    }

    // =====================================================================================
    // Verify row in list
    // =====================================================================================
    public boolean verifyVacancyInList(String jobTitle,String vacancy,String hiringMgrNm){

        WaitHelpers.waitForPresence(tableRows);
        List<WebElement> rows=driver().findElements(tableRows);

        for(WebElement row:rows){
            String vacNm = row.findElement(By.xpath(".//div[@role='cell'][2]")).getText();
            String jobTtl=row.findElement(By.xpath(".//div[@role='cell'][3]")).getText();
            String mgrNm=row.findElement(By.xpath(".//div[@role='cell'][4]")).getText();
            //String vacancyStatus =row.findElement(By.xpath(".//div[@role='cell'][5]")).getText();

            if(vacNm.equals(vacancy) && jobTtl.equals(jobTitle) && mgrNm.equals(hiringMgrNm)) {
                return true;
            }
        }
        return false;
    }
}
