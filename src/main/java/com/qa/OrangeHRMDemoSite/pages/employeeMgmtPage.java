package com.qa.OrangeHRMDemoSite.pages;

import com.qa.OrangeHRMDemoSite.Utils.WaitHelpers;
import com.qa.OrangeHRMDemoSite.base.basePage;
import com.qa.OrangeHRMDemoSite.driver.driverMgr;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.qa.OrangeHRMDemoSite.Utils.browserUtils.scrollTo;
import static com.qa.OrangeHRMDemoSite.driver.driverMgr.getDriver;

public class employeeMgmtPage extends basePage {
    private static final Logger log = LogManager.getLogger(String.valueOf(employeeMgmtPage.class));

    private WebDriver driver;
    public employeeMgmtPage() {
        super();
    }

    //Add Employee details
    private By pimLink=By.xpath("//span[text()='PIM']");
    private By addEmpBtn=By.xpath("//div[@class='orangehrm-paper-container']/div/" +
            "button[contains(@class,'oxd-button oxd')]/i[contains(@class,'oxd-icon')]");
    //private By empFirstNm=By.xpath("//input[@name='firstName']");
    //private By empMiddleNm = By.xpath("//input[@name='middleName']");
    //private By empLastNm = By.xpath("//input[@name='lastName']");
    private By empFirstNm =By.name("firstName");
    private By empMiddleNm = By.name("middleName");
    private By empLastNm = By.name("lastName");
    private By empId = By.xpath("(//input[contains(@class,'oxd-input oxd-input')])[5]");
    private By addEmpPlusIcon=By.xpath("//div[@class='orangehrm-employee-container']//i[contains(@class,'oxd-icon bi-plus')]");
    public By saveBtn = By.xpath("//button[@type='submit']");

    //Search Employee in EmployeeList
    private By empListTab = By.xpath("//div[@class='oxd-topbar-body']//a[@class='oxd-topbar-body-nav-tab-item' and text()='Employee List']");
    private By addEmpTab =By.xpath("//a[@class='oxd-topbar-body-nav-tab-item' and text()='Add Employee']");
    private By searchEmpName=By.xpath("(//input[contains(@placeholder,'Type')])[1]");
    private By searchEmpId=By.xpath("(//input[contains(@class,'oxd-input')])[2]");
    private By searchEmpStatus = By.xpath("(//div[@class='oxd-select-text-input'])[1]");
    private By searchEmpInclude=By.xpath("(//div[@class='oxd-select-text-input'])[2]");
    private By supervisorName = By.xpath("(//input[contains(@placeholder,'Type')])[2]");
    private By searchEmpJobTitle = By.xpath("(//div[@class='oxd-select-text-input'])[3]");
    private By searchSubUnit = By.xpath("(//div[@class='oxd-select-text-input'])[4]");
    private By searchBtn = By.xpath("//form[@class='oxd-form']//button[@type='submit']");

    //Edit employee details
    private By otherID =By.xpath("(//input[contains(@class,'oxd-input')])[6]");
    private By updateBtn =By.xpath("(//button[text()=' Save '])[1]");

    //Delete employee details
    private By deleteIcon = By.xpath("//button//i[contains(@class,'bi-trash')]");
    private By alertYesBtn = By.xpath("(//div[@role='dialog' and contains(@class,'oxd-overlay')])[2]/div/" +
            "div/div/div[@class='orangehrm-modal-footer']/button[2]");
    private By errorLocator = By.xpath("//span[contains(@class,'oxd-input-field-error-message') and text()='Required']");
    private By tableRows = By.xpath("//div[@class='oxd-table-body']//div[@role='row']");

    public void openAddEmpForm(){
        WaitHelpers.waitForClickable(pimLink);
        clickElement(pimLink);
        WaitHelpers.waitForVisible(addEmpBtn);
        clickElement(addEmpBtn);
    }

    public boolean isEmployeePresent(String firstName, String lastName) {

        String fullName = firstName + " " + lastName;

        By searchNameInput = By.xpath("//input[@placeholder='Type for hints...']");
        By searchBtn = By.xpath("//button[@type='submit']");
        By employeeRow = By.xpath("//div[@class='oxd-table-cell oxd-padding-cell']//div[text()='" + fullName + "']");

        enterInput(searchNameInput, fullName);
        clickElement(searchBtn);

        WaitHelpers.waitForPresence(By.xpath("//div[@class='oxd-table-body']"));

        return driver().findElements(employeeRow).size() > 0;
    }

    public void createEmployee(String firstName, String lastName) {

        clickElement(By.xpath("//button[normalize-space()='Add']"));

        enterInput(By.name("firstName"), firstName);
        enterInput(By.name("lastName"), lastName);

        clickElement(By.xpath("//button[@type='submit']"));

        WaitHelpers.waitForInvisibility(By.className("oxd-form-loader"));
    }

    public void fillEmpDetails(String firstNm,String middleNm,String lastNm,String Id) throws InterruptedException {
        WaitHelpers.waitForVisible(empFirstNm);
        enterInput(empFirstNm,firstNm);
        WaitHelpers.waitForVisible(empMiddleNm);
        enterInput(empMiddleNm,middleNm);
        WaitHelpers.waitForVisible(empLastNm);
        enterInput(empLastNm,lastNm);
        WaitHelpers.waitForVisible(empId);
        enterInput(empId,Id);
        clickElement(saveBtn);
    }

    public void addEmpDetails(String firstNm,String middleNm,String lastNm,String Id,boolean needNavigation, boolean singleData) throws InterruptedException {
        log.info("Adding Employee: "+firstNm + " "+ lastNm + "| ID: "+Id);

        if(needNavigation){
            System.out.println("if");
            WaitHelpers.waitForClickable(pimLink);
            clickElement(pimLink);
            System.out.println("1");
            WaitHelpers.waitForVisible(addEmpBtn);
            clickElement(addEmpBtn);
        }
        else{
            //System.out.println("else");
            WaitHelpers.waitForClickable(addEmpTab);
            clickElement(addEmpTab);
        }

        if(!singleData){
            //System.out.println("Second if");
            fillEmpDetails(firstNm,middleNm,lastNm,Id);
            clickElement(addEmpTab);
        }
        else {
            //System.out.println("Single data - Second if");
            clearEmpDetails();
            WaitHelpers.waitForVisible(empFirstNm);
            fillEmpDetails(firstNm,middleNm,lastNm,Id);
            WaitHelpers.waitForVisible(addEmpPlusIcon);
            clickElement(addEmpPlusIcon);
            WebElement fileInput = driverMgr.getDriver().findElement(By.cssSelector("input[type=file]"));
            String filePath = "C:\\Users\\bhoik\\Downloads\\ProfilePic1.jpg";
            fileInput.sendKeys(filePath);
            safeClick(saveBtn);
        }
    }

    public void searchEmployeeDetails(String empNm,String empId,String empStatus,String empInclude,
                                      String supervisorNm,String empJobTitle,String empSubUnit) throws InterruptedException {
        log.info("Search Employee Details..");
        WaitHelpers.waitForPresence(pimLink);
        clickElement(pimLink);
        WaitHelpers.waitForVisible(By.xpath("//h6[text()='PIM']"));
        WaitHelpers.waitForVisible(pimLink);
        scrollTo(pimLink);

        WaitHelpers.waitForStaleSafe(pimLink);
        WaitHelpers.waitForClickable(empListTab);
        clickElement(empListTab);
        WaitHelpers.waitForVisible(searchEmpName);
        enterInput(searchEmpName,empNm);
        enterInput(searchEmpId,empId);
        WaitHelpers.waitForClickable(searchEmpName);
        clickElement(searchEmpName);
        WaitHelpers.waitForClickable(searchEmpStatus);
        clickElement(searchEmpStatus);
        WaitHelpers.waitForClickable(searchEmpInclude);
        clickElement(searchEmpInclude);
        WaitHelpers.waitForClickable(searchEmpJobTitle);
        clickElement(searchEmpJobTitle);
        WaitHelpers.waitForClickable(searchSubUnit);
        clickElement(searchSubUnit);

        if(!empNm.isEmpty() || !empId.isEmpty() || !empStatus.isEmpty() || !empInclude.isEmpty() || !supervisorNm.isEmpty()
                || !empJobTitle.isEmpty() || !empSubUnit.isEmpty()){
            clickElement(searchBtn);
            scrollTo(saveBtn);
        }
    }

    public void clearEmpDetails(){
        log.info("----Clears the textbox values----");
        WaitHelpers.waitForVisible(empFirstNm);
        clearText(empFirstNm);
        clearText(empMiddleNm);
        clearText(empLastNm);
        clearText(empId);
    }

    public boolean verifyEmpInList(String EmpId,String firstNm, String lastNm){
        WaitHelpers.waitForPresence(tableRows);
        List<WebElement> rows=driverMgr.getDriver().findElements(tableRows);
        for(WebElement row:rows){
            String id = row.findElement(By.xpath(".//div[@role='cell'][2]")).getText();
            String name=row.findElement(By.xpath(".//div[@role='cell'][3]")).getText();
            String lname=row.findElement(By.xpath(".//div[@role='cell'][4]")).getText();

            if(id.equals(EmpId) && name.equals(firstNm) && lastNm.equals(lname)) {
                return true;
            }
        }
        return false;
    }

    public String addEmpDetail_NegativeTC(String nFirstNm, String nLastNm,String nEmpId) throws InterruptedException {
        log.info("----Add Employee Details with blank values----");

        Actions actions = new Actions(getDriver());
        WaitHelpers.waitForVisible(pimLink);
        clickElement(pimLink);
        WaitHelpers.waitForVisible(addEmpBtn);
        clickElement(addEmpBtn);
        clearEmpDetails();
        WaitHelpers.waitForVisible(empFirstNm);
        clickElement(empFirstNm);
        actions.sendKeys(Keys.TAB).perform();  // tab out of field
        clickElement(empLastNm);
        actions.sendKeys(Keys.TAB).perform();
        clickElement(empId);
        actions.sendKeys(Keys.TAB).perform();
        clickElement(saveBtn);

        WebElement msg = WaitHelpers.waitForPresence(errorLocator); //wait.until(ExpectedConditions.presenceOfElementLocated(errorLocator));
        return msg.getText();
    }

    public String editEmpDetails(String otherIdInput) throws InterruptedException {
        log.info("----Edit employee details----");

        WaitHelpers.waitForPresence(tableRows);
        List<WebElement> rows=driverMgr.getDriver().findElements(tableRows);
        //System.out.println("Total Rows1: "+rows.size());
        if(rows.size()>0){
            //System.out.println("inside if");

            WebElement firstrow = rows.get(0);
            WaitHelpers.waitForClickable(firstrow);
            firstrow.click();
            WaitHelpers.waitForVisible(otherID);
            clearText(otherID);
            enterInput(otherID,otherIdInput);
            clickElement(updateBtn);
            return "Updated";
        }else{
            //System.out.println("else");
            System.out.println("No record found..");
            return "NotFound";
        }
    }

    public String deleteEmpDetails() throws InterruptedException {
        log.info("----Delete employee details----");

        WaitHelpers.waitForVisible(By.xpath("//h6[text()='PIM']"));
        scrollTo(pimLink);
        WaitHelpers.waitForStaleSafe(pimLink);
        WaitHelpers.waitForClickable(empListTab);
        clickElement(empListTab);
        WaitHelpers.waitForPresence(tableRows);
        List<WebElement> rows=driverMgr.getDriver().findElements(tableRows);
        System.out.println("No of rows: "+rows.size());
        if(rows.size()>0) {
            //System.out.println("inside if");
            WebElement firstrow = rows.get(0);
            WaitHelpers.waitForClickable(firstrow);
            clickElement(deleteIcon);
            clickElement(alertYesBtn);
            return "deleted";
        }else{
            //System.out.println("else");
            System.out.println("No record found for deletion!");
            return "NotFound";
        }
    }
}
