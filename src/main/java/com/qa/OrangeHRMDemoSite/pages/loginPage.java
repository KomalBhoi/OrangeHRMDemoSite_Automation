package com.qa.OrangeHRMDemoSite.pages;

import com.qa.OrangeHRMDemoSite.Utils.WaitHelpers;
import com.qa.OrangeHRMDemoSite.base.basePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class loginPage extends basePage {

    private static final Logger log = LogManager.getLogger(loginPage.class);

    public loginPage() {
        super();
    }

   // Locators
    private By username=By.cssSelector("input[placeholder='Username']");
    private By password=By.cssSelector("input[placeholder='Password']");
    private By loginBtn=By.xpath("//button[@type='submit']");
    private By dropdownIcon =By.xpath("(//i[contains(@class,'oxd-icon')])[4]");
    private By logoutLink = By.xpath("(//a[@class='oxd-userdropdown-link'])[4]");
    private By error_msg = By.xpath("(//p[contains(@class,'oxd-text')])[1]");
    private By validation_msg = By.xpath("//span[normalize-space()='Required']");


    // ============================================================
    // Login with Valid Credentials
    // ============================================================
   public void loginToOrangeHRMDemo(String user,String pwd){
       log.info("Login with valid credentials: "+user+" "+pwd);

       openOrangeHRMUrl();

       WaitHelpers.waitForLoaderToDisappear();
       WaitHelpers.waitForVisible(username);
       enterInput(username,user);
       enterInput(password,pwd);

       WaitHelpers.waitForLoaderToDisappear();
       WaitHelpers.waitForClickable(loginBtn);
       clickElement(loginBtn);
   }

    // ============================================================
    // Invalid Credentials
    // ============================================================
   public String loginWithInvalidCred(String user,String pwd){
       log.info("Login with invalid credentials: "+user);

       openOrangeHRMUrl();
       WaitHelpers.waitForVisible(username);
       enterInput(username,user);
       enterInput(password,pwd);
       clickElement(loginBtn);

       WaitHelpers.waitForPresence(error_msg);
       return getText(error_msg);
   }

    // ============================================================
    // Empty Credentials
    // ============================================================
   public String loginWithEmptyCred(String u, String p) throws InterruptedException {
       log.info("Login with empty credentials.");

       openOrangeHRMUrl();   // comes from basePage
       WaitHelpers.waitForVisible(username);

       clearText(username);
       clearText(password);
       clickElement(loginBtn);  // basePage method

       // --- Handle the case when validation is slow ---
       try {
           WaitHelpers.waitForPresence(validation_msg);
       } catch (TimeoutException e) {
           // Force field blur to trigger Required validation
           clickElement(username);
           clickElement(password);
           clickElement(loginBtn);
           WaitHelpers.waitForPresence(validation_msg);
       }

       // Return first "Required" message found
       WebElement msg = WaitHelpers.waitForStaleSafe(validation_msg);
       return msg.getText();
   }

    // ============================================================
    // Logout
    // ============================================================
   public void logOutToOrangeHRMDemo(){
       WaitHelpers.waitForClickable(dropdownIcon);
       clickElement(dropdownIcon);

       WaitHelpers.waitForClickable(logoutLink);
       clickElement(logoutLink);
   }
}
