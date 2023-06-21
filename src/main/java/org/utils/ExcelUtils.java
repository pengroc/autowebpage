package org.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class ExcelUtils {

    private static final String XLSX = ".xlsx";
    private static final String XLS = ".xls";

    public static <T> void writeUrlExcel(String filePath, String sheetName, String time, Map<String, Integer> map) throws IOException {
        // 构造workbook
        Workbook wb;
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream fs = new FileInputStream(filePath);
            wb = new XSSFWorkbook(fs);
        } else {
            wb = new XSSFWorkbook();
        }
        // 创建sheet页
        Sheet sheet = wb.getSheet(sheetName);
        Row row;
        if (null == sheet) {
            sheet = wb.createSheet(sheetName);
            row = sheet.createRow(0);
            row.createCell(0).setCellValue(sheetName);
            int i = 0;
            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                row.createCell(++i).setCellValue(key);
            }
            row = sheet.createRow(1);
        } else {
            // 追加数据
            int lastRowNum = sheet.getLastRowNum();
            row = sheet.createRow(++lastRowNum);
        }
        // 填充数据
        row.createCell(0).setCellValue(time);
        int i = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            row.createCell(++i).setCellValue(entry.getValue());
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            wb.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    public static <T> void writeTotalExcel(String filePath, String time, String source, int totalNum, int noRepeatNum, int ipNum, Map<String, Integer> map) throws IOException {
        // 构造workbook
        Workbook wb;
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream fs = new FileInputStream(filePath);
            wb = new XSSFWorkbook(fs);
        } else {
            wb = new XSSFWorkbook();
        }
        // 创建sheet页
        Sheet sheet = wb.getSheet("汇总");
        Row row;
        if (null == sheet) {
            sheet = wb.createSheet("汇总");
            row = sheet.createRow(0);
            // 设置表头
            String[] headers = {"日期", "页面类型", "总抓取", "200", "唯一抓取", "唯一抓取占比", "ip", "301", "302", "304", "403", "404", "408", "499", "500", "502", "503", "504", "备注"};
            for (short j = 0; j < headers.length; j++) {
                row.createCell(j).setCellValue(headers[j]);
            }
            row = sheet.createRow(1);

        } else {
            // 追加数据
            int lastRowNum = sheet.getLastRowNum();
            row = sheet.createRow(++lastRowNum);
        }
        // 填充数据
        row.createCell(0).setCellValue(time);
        row.createCell(1).setCellValue(source);
        row.createCell(2).setCellValue(totalNum);
        row.createCell(3).setCellValue(map.get("200"));
        row.createCell(4).setCellValue(noRepeatNum);
        row.createCell(5).setCellValue((Math.round(noRepeatNum * 10000 / totalNum) / 100.0) + "%");
        row.createCell(6).setCellValue(ipNum);
        row.createCell(7).setCellValue(map.get("301"));
        row.createCell(8).setCellValue(map.get("302"));
        row.createCell(9).setCellValue(map.get("304"));
        row.createCell(10).setCellValue(map.get("403"));
        row.createCell(11).setCellValue(map.get("404"));
        row.createCell(12).setCellValue(map.get("408"));
        row.createCell(13).setCellValue(map.get("499"));
        row.createCell(14).setCellValue(map.get("500"));
        row.createCell(15).setCellValue(map.get("502"));
        row.createCell(16).setCellValue(map.get("503"));
        row.createCell(17).setCellValue(map.get("504"));
        row.createCell(18).setCellValue("");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            wb.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

}
