package com.autotest.qa.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.Properties;

public class KTAPropertiesUtil {

    public static final String CONFIG_FILE_PATH="conf/config.properties";

    //通过传入的路径及key,获得对应的值
    public static String getValue(String path,String key)  {
        Properties props=new Properties();
        try {
            ClassPathResource resource=new ClassPathResource(path);
            InputStream in=resource.getInputStream();
            BufferedReader bf=new BufferedReader(new InputStreamReader(in,"UTF-8"));
            props.load(bf);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return props.getProperty(key);

    }



    //通过key直接获取对应的值
    public static String getValue(String key)  {
        Properties props=new Properties();
        try {
            ClassPathResource resource=new ClassPathResource(CONFIG_FILE_PATH);
            InputStream in=resource.getInputStream();
            BufferedReader bf=new BufferedReader(new InputStreamReader(in,"UTF-8"));
            props.load(bf);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return props.getProperty(key);

    }
    public static String getEnv(String key){
        String env=getValue("run.env");
        String path="env/environment-"+env+".properties";;
        return  getValue(path,key);
    }

}
