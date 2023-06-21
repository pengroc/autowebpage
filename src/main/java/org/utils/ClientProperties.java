package org.utils;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ClientProperties {

    static Logger logger = Logger.getLogger(ClientProperties.class.getName());

    private static Properties p;

    public ClientProperties() {
    }

    public static void init() {
        p = new Properties();
        try {
            File directory = new File(".");

            String durpath = (new StringBuilder(
                    String.valueOf(directory.getCanonicalPath()))).append("/conf/")
                    .toString();
            FileInputStream inputStream = new FileInputStream(
                    (new StringBuilder(String.valueOf(durpath))).append("init.properties")
                            .toString());
            p.load(inputStream);
        } catch (FileNotFoundException e) {
            logger.error((new StringBuilder(String.valueOf(e.getMessage()))).append("\r\n")
                    .append(errorException(e))
                    .toString());
            System.out.println("init.properties文件不存在");
            return;
        } catch (IOException e) {
            logger.error((new StringBuilder(String.valueOf(e.getMessage()))).append("\r\n")
                    .append(errorException(e))
                    .toString());
            System.out.println("init.properties文件解析失败");
            return;
        }
    }

    public static String getProperty(String key) {
        return p.getProperty(key);
    }

    private static String errorException(Exception e) {
        StackTraceElement ste[] = e.getStackTrace();
        StringBuffer sb = new StringBuffer();
        sb.append((new StringBuilder(String.valueOf(e.getMessage()))).append("\n")
                .toString());
        for (int i = 0; i < ste.length; i++)
            sb.append((new StringBuilder(String.valueOf(ste[i].toString()))).append("\n")
                    .toString());

        return sb.toString();
    }

    static {
        init();
    }

}
