package com.autotest.qa.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExcelUtils {
    private String filePath;
    private String sheetName;

    public ExcelUtils(String filePath,String sheetName){
        this.filePath=filePath;
        this.sheetName=sheetName;
    }
    public static Object[][] readExcel(String filePath,String sheetName){
        File file=new File(filePath);
        try {
            FileInputStream inputStream = new FileInputStream(file);
            XSSFWorkbook workbook=new XSSFWorkbook(inputStream);
            XSSFSheet sheet=workbook.getSheet(sheetName);
            int rowNum=sheet.getPhysicalNumberOfRows();
            int columnNum=sheet.getRow(0).getPhysicalNumberOfCells();
            Object[][] data=new Object[rowNum][columnNum];
            for (int i = 1; i <rowNum ; i++) {
                for (int j = 0; j <columnNum ; j++) {
                    sheet.getRow(i).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                    data[i-1][j]=sheet.getRow(i).getCell(j).getStringCellValue();
                }
            }
            workbook.close();
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
