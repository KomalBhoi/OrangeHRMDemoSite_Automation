package com.qa.OrangeHRMDemoSite.test;

import com.qa.OrangeHRMDemoSite.Utils.PropertiesReader;
import com.qa.OrangeHRMDemoSite.base.baseTest;
import com.qa.OrangeHRMDemoSite.driver.driverMgr;
import com.qa.OrangeHRMDemoSite.pages.dashboardPage;
import com.qa.OrangeHRMDemoSite.pages.loginPage;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.*;

public class loginTest extends baseTest {
    loginPage loginPg;
    dashboardPage dashPage;

    @Description("TC#1 - Login with valid credentials.")
    @Test(priority = 1,groups = "loginRequired")
    public void test_validLoginCred() {
        dashPage = new dashboardPage(driverMgr.getDriver());
        String title=dashPage.userLoggedIn();
        Assert.assertEquals(title,"Dashboard","Page title mismatch!");

        loginPg = new loginPage();
        loginPg.logOutToOrangeHRMDemo();
    }

    @Description("TC#2 - Login with Invalid credentials.")
    @Test(priority = 2)
    public void test_invalidLoginCred(){
        loginPg = new loginPage();
        String error = loginPg.loginWithInvalidCred(PropertiesReader.readKey("invalid_usernm"),
                PropertiesReader.readKey("invalid_pwd"));

        Assert.assertEquals(error,PropertiesReader.readKey("error_msg"));
    }

    @Description("TC#3 - Login with empty credentials.")
    @Test(priority = 3)
    public void test_emptyLoginCred() throws InterruptedException {
        loginPg = new loginPage();
        String msg=loginPg.loginWithEmptyCred("","");
        Assert.assertTrue(msg.contains("Required"),"Username 'Required' not shown!");
    }
}
