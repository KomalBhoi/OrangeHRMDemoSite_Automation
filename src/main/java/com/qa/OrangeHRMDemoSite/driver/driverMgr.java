package com.qa.OrangeHRMDemoSite.driver;

import com.qa.OrangeHRMDemoSite.Utils.PropertiesReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public class driverMgr {

    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    //private driverMgr() {}
    //public static WebDriver driver;

    //Get thread-safe driver
    public static WebDriver getDriver() {
        //WebDriver driver =tlDriver.get();
        if(tlDriver.get() == null) {
            throw new IllegalStateException("Webdriver is NULL.Did you forget init()?");
        }
        return tlDriver.get();
    }

    public static void setDriver(WebDriver driver) {
        if(driverMgr.getDriver() == null){
            throw new IllegalArgumentException("Driver must be set");
        }
        tlDriver.set(driver);
    }

    // When we want to start the browser
    public static void initDriver() {

//        if(tlDriver.get() != null){
//            return; // already initialized for this thread.
//        }

        String browserNm = PropertiesReader.readKey("browser").toLowerCase();
        WebDriver driver;

        switch (browserNm) {

            case "chrome": {
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                //chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--disable-notifications");
                driver = new ChromeDriver(chromeOptions);
                break;
            }

            case "edge": {
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                //edgeOptions.addArguments("--guest");
                driver = new EdgeDriver(edgeOptions);
                break;
            }

            case "firefox": {
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                driver = new FirefoxDriver(firefoxOptions);
                driver.manage().window().maximize(); // Firefox needs manual maximize
                break;
            }

            default:
                throw new RuntimeException("Unsupported browser: " + browserNm +
                        ". Supported: chrome | edge | firefox");
        }

        // ✅ Common configuration (for ALL browsers)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));

        tlDriver.set(driver);

        System.out.println("Class: "+ driver.getClass().getSimpleName()+"| Thread: " + Thread.currentThread().getId());
       // return driver;
    }

    // When we want to close the browser
    public static void quitDriver() {
        //WebDriver driver = tlDriver.get();
        if (tlDriver.get() != null) {
            tlDriver.get().quit();
            tlDriver.remove();
        }
    }

    public static void unload(){
        tlDriver.remove();
    }
}
