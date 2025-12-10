package com.qa.OrangeHRMDemoSite.Utils;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesReader {

    public static String readKey(String key){

        Properties prop;
        String user_dir =System.getProperty("user.dir");
        String file_path= user_dir + "/src/main/resources/data.properties";
        try{
            FileInputStream fileInputStream=new FileInputStream(file_path);
            prop=new Properties();
            prop.load(fileInputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return  prop.getProperty(key);
    }

}
