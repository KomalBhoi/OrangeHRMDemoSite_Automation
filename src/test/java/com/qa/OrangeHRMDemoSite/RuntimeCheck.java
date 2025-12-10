package com.qa.OrangeHRMDemoSite;

public class RuntimeCheck {
    @org.testng.annotations.Test
    public void showRuntime() {
        System.out.println("Runtime JDK version = " + System.getProperty("java.version"));
    }
}
