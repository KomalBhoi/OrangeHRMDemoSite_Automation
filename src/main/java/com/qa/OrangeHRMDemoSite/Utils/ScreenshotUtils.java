package com.qa.OrangeHRMDemoSite.Utils;

import com.qa.OrangeHRMDemoSite.driver.driverMgr;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {

    public static String takeScreenshot(String testname){
        WebDriver driver = driverMgr.getDriver();
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String destPath = System.getProperty("user.dir") + "/screenshots/" + testname + "_" + timestamp + ".png";
        File scrFile =((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try{
            FileUtils.copyFile(scrFile,new File(destPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return destPath;
    }
}
