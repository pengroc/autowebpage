package org.domain;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.utils.MD5Util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class PWD91MyDomain {

    public static void main(String[] args) throws IOException, WriteException {


        File directory = new File(".");
        String log = (new StringBuilder(
                String.valueOf(directory.getCanonicalPath()))).append("/log/")
                .toString();
        File xlsFile = new File(log + "91my_password.xls");
        // 创建一个工作簿
        WritableWorkbook workbook = Workbook.createWorkbook(xlsFile);
        // 创建一个工作表
        WritableSheet sheet = workbook.createSheet("key", 0);
        // 第一列
        sheet.addCell(new Label(0, 0, "date"));
        sheet.addCell(new Label(1, 0, "password"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        for (int i = 1; i < 396; i++) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, i);
            Date d = calendar.getTime();
            String str = df.format(d);
            sheet.addCell(new Label(0, i, str));
            sheet.addCell(new Label(1, i, MD5Util.MD5(str)));
        }
        workbook.write();
        workbook.close();
        System.out.println("OVER!");

    }

}
