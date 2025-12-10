package com.qa.OrangeHRMDemoSite.pages;

import com.qa.OrangeHRMDemoSite.Utils.WaitHelpers;
import com.qa.OrangeHRMDemoSite.base.basePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class dashboardPage extends basePage {

    public dashboardPage(WebDriver driver) {
        super();
    }

    private By dashboardPage = By.cssSelector("h6");

    public String userLoggedIn(){
        WaitHelpers.waitForPresence(dashboardPage);
        return getText(dashboardPage);
    }
}
