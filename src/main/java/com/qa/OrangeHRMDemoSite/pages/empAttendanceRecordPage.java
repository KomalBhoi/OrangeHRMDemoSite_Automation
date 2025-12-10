package com.qa.OrangeHRMDemoSite.pages;

import com.qa.OrangeHRMDemoSite.base.basePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class empAttendanceRecordPage extends basePage {

    WebDriver driver;
    public empAttendanceRecordPage(WebDriver driver) {
        this.driver = driver;
    }

    // Time tab
    private By timeLink=By.xpath("//span[contains(@class,'oxd-text oxd-text--span') and text()='Time']");
    private By attendanceTopBar = By.xpath("//span[contains(@class,'oxd-topbar-body-nav-tab-item') and text()='Attendance ']");
    private By punchInOutMenu = By.xpath("//a[contains(@class,'oxd-topbar-body-nav-tab-link') and text()='Punch In/Out']");
    private By attendanceDt = By.xpath("//input[@placeholder='yyyy-dd-mm']");
    private By punchInOutTime = By.xpath("//input[@placeholder='hh:mm']");
    private By timeHourInput = By.xpath("//input[contains(@class,'oxd-input oxd-input--active oxd-time-hour')]");


}
