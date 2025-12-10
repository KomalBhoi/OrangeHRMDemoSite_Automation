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

    public static WebDriver driver;
    public static WebDriver getDriver() {
        return driver;
    }

    public static void setDriver(WebDriver driver) {
        driverMgr.driver = driver;
    }
    // When we want to start the browser
    public static void init() {
        String browserNm = PropertiesReader.readKey("browser");
        browserNm = browserNm.toLowerCase();

        switch (browserNm) {
            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                edgeOptions.addArguments("--guest");
                driver = new EdgeDriver(edgeOptions);
                break;
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                driver = new ChromeDriver(chromeOptions);
                break;
            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--start-maximized");
                driver = new FirefoxDriver(firefoxOptions);
                break;
            default:
                System.out.println("Not browser Supported!!!");

                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
                driver.get(PropertiesReader.readKey("url")); // ✅ Launch app here
                System.out.println("✅ Driver initialized for browser: " + browserNm);
                System.out.println("🌐 Opened URL: " + PropertiesReader.readKey("url"));

        }
    }

    // When we want to close the browser
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
