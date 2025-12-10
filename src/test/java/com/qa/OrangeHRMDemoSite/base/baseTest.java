package com.qa.OrangeHRMDemoSite.base;

import com.qa.OrangeHRMDemoSite.Utils.PropertiesReader;
import com.qa.OrangeHRMDemoSite.driver.driverMgr;
import com.qa.OrangeHRMDemoSite.pages.loginPage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

public class baseTest {
    private driverMgr driverMgr;

    @BeforeMethod
    public void setUp(Method method){
        driverMgr.init();

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
       driverMgr.tearDown();
    }
}
