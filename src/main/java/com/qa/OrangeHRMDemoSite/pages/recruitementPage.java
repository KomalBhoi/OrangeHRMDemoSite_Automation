package com.qa.OrangeHRMDemoSite.pages;

import com.qa.OrangeHRMDemoSite.Utils.WaitHelpers;
import com.qa.OrangeHRMDemoSite.base.basePage;
import com.qa.OrangeHRMDemoSite.driver.driverMgr;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class recruitementPage extends basePage {

    private static final Logger log = LogManager.getLogger(recruitementPage.class);

    // Add Vacancy
    WebDriver driver;

    public recruitementPage(){
        super();
    }

    private By recruitemetLink = By.xpath("//span[text()='Recruitment']");
    private By vacanciesTab = By.xpath("//a[@class='oxd-topbar-body-nav-tab-item' and text()='Vacancies']");
    private By plusBtn = By.xpath("//i[contains(@class,'oxd-icon bi-plus')]");
    private By vacancyName = By.xpath("(//input[@class='oxd-input oxd-input--active'])[2]");
    private By jobTitle = By.xpath("//div[@class='oxd-select-text-input']");
    private By jobDesc = By.xpath("//textarea[@placeholder='Type description here']");
    private By hiringMgrName = By.xpath("//label[text()='Hiring Manager']/following::input[1]");
    //By.xpath("//input[@placeholder='Type for hints...']");
    private By noOfPositions = By.xpath("(//input[@class='oxd-input oxd-input--active'])[3]");
    private By activeSwitch = By.xpath("(//span[contains(@class,'oxd-switch-input')])[1]");
    private By publicSwitch = By.xpath("(//span[contains(@class,'oxd-switch-input')])[2]");
    private By saveBtn = By.xpath("//button[@type='submit' and normalize-space()='Save']");
    private By candidatesTab = By.xpath("//a[@class='oxd-topbar-body-nav-tab-item' and text()='Candidates']");

    //Search for job Vacancies
    private By searchJobTitle = By.xpath("(//div[@class='oxd-select-text-input'])[1]");
    private By searchVacancy = By.xpath("(//div[@class='oxd-select-text-input'])[2]");
    private By searchHiringMgrNm = By.xpath("(//div[@class='oxd-select-text-input'])[3]");
    private By searchStatus = By.xpath("(//div[@class='oxd-select-text-input'])[4]");
    private By searchBtn = By.xpath("//button[@type='submit' and normalize-space()='Search']");


    public void addVacancies(String vacancyNm,String jobTtl,String desc,String hiringMgrNm,
                             String noOfPos) throws InterruptedException {
        log.info("Adding Vacancies: "+vacancyNm+" "+jobTtl+" "+desc+" "+hiringMgrNm+" "+noOfPos);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        WaitHelpers.waitForVisible(recruitemetLink);
        clickElement(recruitemetLink);
        WaitHelpers.waitForVisible(vacanciesTab);
        clickElement(vacanciesTab);
        WaitHelpers.waitForVisible(plusBtn);
        clickElement(plusBtn);
        WaitHelpers.waitForVisible(vacancyName);
        enterInput(vacancyName,vacancyNm);
        WaitHelpers.waitForVisible(jobTitle);
        selectDropdownValue(jobTitle,jobTtl);
        enterInput(jobDesc,desc);
        WaitHelpers.waitForClickable(hiringMgrName);
        enterInput(hiringMgrName,hiringMgrNm);
        By suggestionOption = By.xpath("//div[@role='option']//span[contains(text(),'" + hiringMgrNm + "')]");
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(suggestionOption));
        // Click the first suggestion (exact match)
        driver.findElement(suggestionOption).click();
        js.executeScript("window.scrollBy(500,2000);");
        WaitHelpers.waitForVisible(noOfPositions);
        enterInput(noOfPositions,noOfPos);

        clickElement(saveBtn);
        //Thread.sleep(5000);
    }

    public boolean searchForVacancies(String jobTitle,String vacancy,String hiringMgrNm,String status) throws InterruptedException {
        log.info("---- Search for Vacancies ----");

//        WaitHelpers.checkVisibility(driver,recruitemetLink);
//        clickElement(recruitemetLink);
        WaitHelpers.waitForVisible(candidatesTab);
        clickElement(candidatesTab);
        WaitHelpers.waitForVisible(vacanciesTab);
        clickElement(vacanciesTab);
        WaitHelpers.waitForVisible(searchJobTitle);
        selectDropdownValue(searchJobTitle,jobTitle);
        WaitHelpers.waitForVisible(searchVacancy);
        selectDropdownValue(searchVacancy,vacancy);
        WaitHelpers.waitForVisible(searchHiringMgrNm);
        selectDropdownValue(searchHiringMgrNm,hiringMgrNm);
        WaitHelpers.waitForVisible(searchStatus);
        selectDropdownValue(searchStatus,status);

        if(jobTitle !="" || vacancy !="" || hiringMgrNm !="" || status !="") {
            //System.out.println("Inside If");
            clickElement(searchBtn);
            Thread.sleep(5000);
        }else{
            System.out.println("Record not found!!");
        }

        List<WebElement> rows= driverMgr.getDriver().findElements(By.xpath("//div[@class='oxd-table-body']//div[@role='row']"));
        //System.out.println("No of rows: " + rows.size());
        if(rows.size()>0){ return true; }
        else{ return false; }
    }


    public boolean verifyVacancyInList(String jobTitle,String vacancy,String hiringMgrNm){
        List<WebElement> rows=driverMgr.getDriver().findElements(By.xpath("//div[@class='oxd-table-body']//div[@role='row']"));
        for(WebElement row:rows){
            String vacancyNm = row.findElement(By.xpath(".//div[@role='cell'][2]")).getText();
            String jobTtl=row.findElement(By.xpath(".//div[@role='cell'][3]")).getText();
            String hiringMgr=row.findElement(By.xpath(".//div[@role='cell'][4]")).getText();
            //String vacancyStatus =row.findElement(By.xpath(".//div[@role='cell'][5]")).getText();

            if(vacancyNm.equals(vacancy) && jobTtl.equals(jobTitle) && hiringMgr.equals(hiringMgrNm)) {
                return true;
            }
        }
        return false;
    }
}
