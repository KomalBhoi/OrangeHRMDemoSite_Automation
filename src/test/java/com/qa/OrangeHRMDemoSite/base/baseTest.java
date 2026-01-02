package com.qa.OrangeHRMDemoSite.base;

import com.qa.OrangeHRMDemoSite.Utils.PropertiesReader;
import com.qa.OrangeHRMDemoSite.driver.driverMgr;
import com.qa.OrangeHRMDemoSite.pages.loginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public class baseTest {
    //private driverMgr driverMgr;
    //protected WebDriver driver;

    @BeforeSuite(alwaysRun = true)
    public void setupAllureEnvironment(){
        try{
            Path source=Paths.get("src/test/resources/environment.properties");
            Path targetDir = Paths.get("target/allure-results");

            Files.createDirectories(targetDir);
            Files.copy(source,targetDir.resolve("environment.properties"),
            StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Allure environment.properties copied successfully.");
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method){

        driverMgr.initDriver();
        driverMgr.setDriver(driverMgr.getDriver());

        driverMgr.getDriver().get(PropertiesReader.readKey("url"));

        boolean requiresLogin = method.isAnnotationPresent(Test.class) &&
                Arrays.asList(method.getAnnotation(Test.class).groups()).contains("loginRequired");

        if(requiresLogin){
            loginPage login = new loginPage();
            login.loginToOrangeHRMDemo(
                PropertiesReader.readKey("username"),
                PropertiesReader.readKey("password"));
        }

        //System.out.println("Thread ID: " + Thread.currentThread().getId() +
        //        " | URL: "+driverMgr.getDriver().getCurrentUrl());
    }

    @AfterMethod
    public void tearDown(){
        driverMgr.quitDriver();
        driverMgr.unload();
    }
}
