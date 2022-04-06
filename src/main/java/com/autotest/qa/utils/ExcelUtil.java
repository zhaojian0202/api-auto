package com.autotest.qa.utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtil {
    private String filePath;
    private String sheetName;

    public ExcelUtil(String filePath,String sheetName){
        this.filePath=filePath;
        this.sheetName=sheetName;
    }

    public static List<Map<String,Object>> MapList(String filePath,String sheetName){
        List<Map<String,Object>> list=new ArrayList<Map<String, Object>>();
        File file=new File(filePath);
        int sheetIndex=0;
        int totalRows=0;
        int totalCells=0;
        try {
            Workbook workbook= WorkbookFactory.create(file);
            sheetIndex=workbook.getSheetIndex(sheetName);
            Sheet sheet=workbook.getSheetAt(sheetIndex);
            totalRows=sheet.getLastRowNum();
            totalCells=sheet.getRow(0).getPhysicalNumberOfCells();
            Row rowKey=sheet.getRow(0);
            for (int i = 1; i <totalRows ; i++) {
                Row row=sheet.getRow(i);
                Map<String,Object> map=new HashMap<String, Object>();
                for (int j = 0; j <totalCells ; j++) {
                    Cell cell=row.getCell(j);
                    map.put(rowKey.getCell(j).toString(),cell);
                }
                list.add(i-1,map);
            }
            return list;
        }catch (IOException e){
            e.printStackTrace();
        }catch (InvalidFormatException e){
            e.printStackTrace();
        }
        return null;

    }

}
