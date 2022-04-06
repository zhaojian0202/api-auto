package com.autotest.qa.common;
import com.autotest.qa.annotations.DataDriver;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.annotations.DataProvider;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;
public class ExcelDataHeleper {

    @DataProvider(name = "excel")
    public Iterator<Object[]> dataMethod(Method m) {
        System.out.println(m.getName());
        DataDriver d = m.getAnnotation(DataDriver.class);
        System.out.println(d.filePath() + "   " + d.sheetName());
        List<Object> item = new ArrayList<Object>();
        List<List<String>> list = read(d.filePath(), d.sheetName());
        if (list != null || list.size() > 0) {
            int size = list.get(0).size();
            for (int i = 1; i < list.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                for (int j = 0; j < size; j++) {
                    map.put(list.get(0).get(j), list.get(i).get(j));
                }
                item.add(map);
            }
        }
        List<Object[]> users = new ArrayList<Object[]>();
        for (Object u : item) {
            //做一个形式转换
            users.add(new Object[]{u});
        }
        return users.iterator();
    }


    public List<List<String>> read(String fileName, String sheetName) {
        List<List<String>> maps = new ArrayList<>();
        if (fileName == null || !fileName.matches("^.+\\.(?i)((xls)|(xlsx))$"))
            return maps;
        try {
            InputStream inputStream = new FileInputStream(System.getProperty("user.dir") + File.separator + "src/main/resources/" + fileName);
            System.out.println(inputStream);
            Workbook wb = WorkbookFactory.create(inputStream);
            maps = read(wb, sheetName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maps;
    }

    private int totalRows = 0;// 总行数
    private int totalCells = 0;// 总列数

    private List<List<String>> read(Workbook wb, String sheetName) {
        List<List<String>> maps = new ArrayList<>();
        List<List<String>> list = new ArrayList<List<String>>();
        int delnumber = 0;// 第一页去除行数
        Sheet sheet = wb.getSheet(sheetName);
        this.totalRows = sheet.getPhysicalNumberOfRows() - delnumber; // 获取工作表中行数
        if (this.totalRows >= 1 && sheet.getRow(delnumber) != null) {
            this.totalCells = sheet.getRow(0)
                    .getPhysicalNumberOfCells(); // 得到当前行的所有单元格
            for (int j = 0; j < totalRows; j++) {
                List<String> rowLst = new ArrayList<String>();
                for (int f = 0; f < totalCells; f++) {
                    if (totalCells > 0) {
                        String value = getCell(sheet.getRow(j).getCell(f));
                        rowLst.add(value);
                    }
                }
                list.add(rowLst);
            }
        }
        return list;
    }
    /*
     * private String getRightStr(String sNum) { DecimalFormat decimalFormat =
     * new DecimalFormat("##.00"); String resultStr = decimalFormat.format(new
     * Double(sNum)); if (resultStr.matches("^[-+]?\\d+\\.[0]+$")) { resultStr =
     * resultStr.substring(0, sNum.indexOf(".")); } return resultStr; }
     */

    public String getCell(Cell cell) {
        String cellValue = null;
        HSSFDataFormatter hSSFDataFormatter = new HSSFDataFormatter();
        cellValue = hSSFDataFormatter.formatCellValue(cell); // 使用EXCEL原来格式的方式取得值
        return cellValue;
    }

    /**
     * 去除 字符串收尾的 特殊的Unicode [ "\uFEFF" ]
     * csv 文件可能会带有该编码
     *
     * @param str
     * @return
     */
    private static String specialUnicode(String str) {
        if (str.startsWith("\uFEFF")) {
            str = str.replace("\uFEFF", "");
        } else if (str.endsWith("\uFEFF")) {
            str = str.replace("\uFEFF", "");
        }
        return str;
    }
}