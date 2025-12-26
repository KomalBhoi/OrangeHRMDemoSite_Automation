package com.qa.OrangeHRMDemoSite.base;

import com.qa.OrangeHRMDemoSite.Utils.PropertiesReader;
import com.qa.OrangeHRMDemoSite.driver.driverMgr;
import com.qa.OrangeHRMDemoSite.pages.loginPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

public class baseTest {
    //private driverMgr driverMgr;
    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method){

        driver = driverMgr.init();
        driverMgr.setDriver(driver);

        driver.get(PropertiesReader.readKey("url"));

        boolean requiresLogin = method.isAnnotationPresent(Test.class) &&
                Arrays.asList(method.getAnnotation(Test.class).groups()).contains("loginRequired");

        if(requiresLogin){
            loginPage login = new loginPage();
            login.loginToOrangeHRMDemo(
                PropertiesReader.readKey("username"),
                PropertiesReader.readKey("password"));
        }
    }

    @AfterMethod
    public void tearDown(){
        if(driver != null) {
            //driverMgr.tearDown();
            driver.quit();
        }
        driverMgr.unload();
    }
}
