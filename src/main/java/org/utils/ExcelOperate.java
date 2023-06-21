package org.utils;


import org.apache.log4j.Level;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExcelOperate {

    /**
     * 读取Excel的内容，第一维数组存储的是一行中格列的值，二维数组存储的是多少个行
     *
     * @param file       读取数据的源Excel
     * @param ignoreRows 读取数据忽略的行数，比喻行头不需要读入 忽略的行数为1
     * @return 读出的Excel中数据的内容
     */
    public static String[][] getData(File file, int ignoreRows)
            throws Exception {

        /*String[][] returnArray;
        BufferedInputStream in = null;
        try {
            List<String[]> result = new ArrayList<String[]>();
            int rowSize = 0;
            in = new BufferedInputStream(new FileInputStream(file));
            // 打开HSSFWorkbook
            POIFSFileSystem fs = new POIFSFileSystem(in);
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFCell cell = null;
            for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
                HSSFSheet st = wb.getSheetAt(sheetIndex);
                // 第一行为标题，不取
                for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
                    HSSFRow row = st.getRow(rowIndex);
                    if (row == null) {
                        continue;
                    }
                    int tempRowSize = row.getLastCellNum() + 1;
                    if (tempRowSize > rowSize) {
                        rowSize = tempRowSize;
                    }
                    String[] values = new String[rowSize];
                    Arrays.fill(values, "");
                    boolean hasValue = false;
                    int columnIndexMax = row.getLastCellNum() + 1;
                    for (short columnIndex = 0; columnIndex <= row
                            .getLastCellNum(); columnIndex++) {
                        String value = "";
                        cell = row.getCell(columnIndex);
                        if (cell != null) {
                            // 注意：一定要设成这个，否则可能会出现乱码
                            cell.setEncoding(HSSFCell.ENCODING_UTF_16);
                            switch (cell.getCellType()) {
                                case HSSFCell.CELL_TYPE_STRING:
                                    value = cell.getStringCellValue();
                                    break;
                                case HSSFCell.CELL_TYPE_NUMERIC:
                                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                        Date date = cell.getDateCellValue();
                                        if (date != null) {
                                            value = new SimpleDateFormat(
                                                    "yyyy-MM-dd HH:mm:ss")
                                                    .format(date);
                                        } else {
                                            value = "";
                                        }
                                    } else {
                                        value = cell.getNumericCellValue() + "";
                                    }
                                    break;
                                case HSSFCell.CELL_TYPE_FORMULA:
                                    // 导入时如果为公式生成的数据则无值
                                    if (!cell.getStringCellValue().equals("")) {
                                        value = cell.getStringCellValue();
                                    } else {
                                        value = cell.getNumericCellValue() + "";
                                    }
                                    break;
                                case HSSFCell.CELL_TYPE_BLANK:
                                    break;
                                case HSSFCell.CELL_TYPE_ERROR:
                                    value = "";
                                    break;
                                case HSSFCell.CELL_TYPE_BOOLEAN:
                                    value = (cell.getBooleanCellValue() == true ? "Y"
                                            : "N");
                                    break;
                                default:
                                    value = "";
                            }
                        }

                        if (value.trim().equals("")) {
                            columnIndexMax--;

                        }
                        values[columnIndex] = rightTrim(value);
                    }
                    if (columnIndexMax > 0) {
                        hasValue = true;
                    }
                    if (hasValue) {
                        result.add(values);
                    }
                }
            }
            returnArray = new String[result.size()][rowSize];
            for (int i = 0; i < returnArray.length; i++) {
                returnArray[i] = (String[]) result.get(i);
            }
            return returnArray;
        } catch (Exception e) {
            LogFactory.getInstance(ExcelOperate.class).log(Level.ERROR,
                    "ExcelOperate", "getData", new String[]{},
                    new String[]{}, "", e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    LogFactory.getInstance(ExcelOperate.class).log(
                            Level.ERROR,
                            "BufferedInputStream close fail, this exception is "
                                    + e);
                }
            }
        }*/
        return null;
    }

    public static String[][] getProductData(File file) {
        String[][] returnArray;
        BufferedInputStream in = null;
        try {
            List<String[]> result = new ArrayList<>();
            int rowSize = 0;
            FileInputStream fis = new FileInputStream(file);   //文件流对象
            Workbook wb = new HSSFWorkbook(fis);
            //开始解析
            Sheet sheet = wb.getSheetAt(0);     //读取sheet 0

            int firstRowIndex = sheet.getFirstRowNum()+1;   //第一行是列名，所以不读
            int lastRowIndex = sheet.getLastRowNum();
//            System.out.println("firstRowIndex: "+firstRowIndex);
//            System.out.println("lastRowIndex: "+lastRowIndex);

            for(int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
//                System.out.println("rIndex: " + rIndex);
                Row row = sheet.getRow(rIndex);
                if (row != null) {
                    int firstCellIndex = row.getFirstCellNum();
                    int lastCellIndex = row.getLastCellNum();
                    int tempRowSize = row.getLastCellNum() + 1;
                    if (tempRowSize > rowSize) {
                        rowSize = tempRowSize;
                    }
                    String[] values = new String[rowSize];
                    Arrays.fill(values, "");
                    boolean hasValue = false;
                    int columnIndexMax = row.getLastCellNum() + 1;
                    for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {   //遍历列
                        String value = "";
                        Cell cell = row.getCell(cIndex);
                        if (cell != null) {
//                            System.out.println(cell.toString());
                            value = cell.toString();
                            values[cIndex] = rightTrim(value);
                        }
                        if (value.trim().equals("")) {
                            columnIndexMax--;
                        }
                        values[cIndex] = rightTrim(value);
                    }
                    if (columnIndexMax > 0) {
                        hasValue = true;
                    }
                    if (hasValue) {
                        result.add(values);
                    }
                }
            }
            returnArray = new String[result.size()][rowSize];
            for (int i = 0; i < returnArray.length; i++) {
                returnArray[i] = result.get(i);
            }
            return returnArray;
        } catch (Exception e) {
            LogFactory.getInstance(ExcelOperate.class).log(Level.ERROR,
                    "ExcelOperate", "getData", new String[]{},
                    new String[]{}, "", e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    LogFactory.getInstance(ExcelOperate.class).log(
                            Level.ERROR,
                            "BufferedInputStream close fail, this exception is "
                                    + e);
                }
            }
        }
        return null;
    }

    /**
     * 去掉字符串右边的空格
     *
     * @param str 要处理的字符串
     * @return 处理后的字符串
     */

    public static String rightTrim(String str) {
        if (str == null) {
            return "";
        }
        int length = str.length();
        for (int i = length - 1; i >= 0; i--) {
            if (str.charAt(i) != 0x20) {
                break;
            }
            length--;
        }
        return str.substring(0, length);

    }

}
