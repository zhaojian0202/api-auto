package com.autotest.qa.common;

import com.autotest.qa.utils.ExcelUtil;
import com.autotest.qa.utils.ExcelUtils;
import org.testng.annotations.DataProvider;


import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestData {

    @DataProvider(name="testData")
    public Object[][] testData(String filePath,String SheetName){
        Object[][] objects= ExcelUtils.readExcel(filePath,SheetName);
        for (int i = 0; i < objects.length; i++) {
            System.out.println(Arrays.toString(objects[i]));
        }
        return objects;
    }
}
