package org.weblogs;


import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.utils.ClientProperties;
import org.utils.DecompressUtil;
import org.utils.FtpUtil;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CountDomain {

    public static void main(String[] args) {
        try {
            String dateStr = ClientProperties.getProperty("parse_log_date");
            Pattern r = Pattern.compile("\\d{4}(\\-|\\/|.)\\d{1,2}\\1\\d{1,2}");
            Matcher m = r.matcher(dateStr);
            if (!m.matches()) {
                System.out.println("日期格式错误...\n程序终止...");
                Thread.sleep(1500);
                System.exit(-1);
            }
            File directory = new File(".");
            String downloadPath = (new StringBuilder(directory
                    .getCanonicalPath())).append("/download/").toString();
            String dataPath = (new StringBuilder(directory
                    .getCanonicalPath())).append("/data/").toString();
            // 清空download文件夹
            System.out.println("清空download文件夹...\n");
            FileUtils.cleanDirectory(new File(downloadPath));
            // 连接ftp下载日志压缩包
            System.out.println("连接ftp下载日志包...\n");
            String channel = ClientProperties.getProperty("jiameng_channel");
            String fileName = "";
            String hbTimeStr = "_00-00-01";
            if ("91jm".equals(channel)) {
                fileName = ClientProperties.getProperty("91_log_prefix") + dateStr + "_24.tar.bz2";
                // 解密password
                byte[] decrypt = AESUtil.decrypt(ParseSystemUtil.parseHexStr2Byte(ClientProperties.getProperty("91_ftp_password")), "jiameng");
                String password = new String(decrypt, "utf-8");
                FtpUtil.downloadFtpFile(ClientProperties.getProperty("91_ftp_ip"), ClientProperties.getProperty("91_ftp_username"),
                        password, 21,
                        "/", downloadPath, fileName);
            } else if ("hanbaojm".equals(channel)) {
                fileName = ClientProperties.getProperty("hanbaojm_log_prefix") + dateStr + hbTimeStr + "_24.tar.bz2";
                // 解密password
                byte[] decrypt = AESUtil.decrypt(ParseSystemUtil.parseHexStr2Byte(ClientProperties.getProperty("hanbaojm_ftp_password")), "jiameng");
                String password = new String(decrypt, "utf-8");
                // 连接ftp
                FTPClient ftpClient = FtpUtil.getFTPClient(ClientProperties.getProperty("hanbaojm_ftp_ip"), ClientProperties.getProperty("hanbaojm_ftp_username"),
                        password, 21);
                FTPFile[] remoteFiles = ftpClient.listFiles(fileName);
                if (remoteFiles.length == 0) {
                    hbTimeStr = "_00-00-02";
                    fileName = ClientProperties.getProperty("hanbaojm_log_prefix") + dateStr + hbTimeStr + "_24.tar.bz2";
                }
                FtpUtil.downloadFtpFile(ftpClient, "/", downloadPath, fileName);
            } else if ("jiameng".equals(channel)) {

            } else {
                System.out.println("未匹配到合法渠道...\n程序终止...");
                Thread.sleep(1500);
                System.exit(-1);
            }
            // 获取下载的日志压缩包
            File downloadFile = new File(downloadPath + fileName);
            if (downloadFile.exists()) {
                System.out.println(downloadFile.getName());
                // 清空data文件夹
                FileUtils.cleanDirectory(new File(dataPath));
                // 解压至data文件夹
                System.out.println("解压日志包（稍等片刻，请勿关闭程序）...");
                boolean res = DecompressUtil.decompressTarBz2(downloadFile, dataPath);
                if (!res) {
                    System.out.println("日志包解压失败...\n程序终止...");
                    Thread.sleep(1500);
                    System.exit(-1);
                }
                System.out.println("日志包解压完成...");
                if ("91jm".equals(channel)) {
                    // 读取www.91jm.com.access.log文件
                    System.out.println("www日志解析中...");
                    ClassifyUtil.initData("www", dataPath + dateStr + "_24/www.91jm.com.access.log");
                    // 读取wap.91jm.com.access.log文件
                    System.out.println("wap日志解析中...");
                    ClassifyUtil.initData("wap", dataPath + dateStr + "_24/wap.91jm.com.access.log");
                    // 读取mip.91jm.com.access.log文件
                    System.out.println("mip日志解析中...");
                    ClassifyUtil.initData("mip", dataPath + dateStr + "_24/mip.91jm.com.access.log");
                    // 读取top.91jm.com.access.log文件
                    System.out.println("top日志解析中...");
                    ClassifyUtil.initData("top", dataPath + dateStr + "_24/top.91jm.com.access.log");
                    // 读取projects.91jm.com.access.log文件
                    System.out.println("pro日志解析中...");
                    ClassifyUtil.initData("pro", dataPath + dateStr + "_24/projects.91jm.com.access.log");
                    // 读取so.91jm.com.access.log文件
                    System.out.println("so日志解析中...");
                    ClassifyUtil.initData("so", dataPath + dateStr + "_24/so.91jm.com.access.log");
                    // 读取ask.91jm.com.access.log文件
                    System.out.println("ask日志解析中...");
                    ClassifyUtil.initData("ask", dataPath + dateStr + "_24/ask.91jm.com.access.log");
                } else if ("hanbaojm".equals(channel)) {
                    // 读取www.hanbaojm.com.access.log文件
                    System.out.println("www日志解析中...");
                    ClassifyUtil.initData("www", dataPath + dateStr + hbTimeStr + "_24/www.hanbaojm.com.access.log");
                    // 读取wap.hanbaojm.com.access.log文件
                    System.out.println("wap日志解析中...");
                    ClassifyUtil.initData("wap", dataPath + dateStr + hbTimeStr + "_24/wap.hanbaojm.com.access.log");
                    // 读取mip.hanbaojm.com.access.log文件
                    System.out.println("mip日志解析中...");
                    ClassifyUtil.initData("mip", dataPath + dateStr + hbTimeStr + "_24/mip.hanbaojm.com.access.log");
                    // 读取top.hanbaojm.com.access.log文件
                    System.out.println("top日志解析中...");
                    ClassifyUtil.initData("top", dataPath + dateStr + hbTimeStr + "_24/top.hanbaojm.com.access.log");
                    // 读取ask.hanbaojm.com.access.log文件
                    System.out.println("ask日志解析中...");
                    ClassifyUtil.initData("ask", dataPath + dateStr + hbTimeStr + "_24/ask.hanbaojm.com.access.log");
                } else if ("jiameng".equals(channel)) {

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("OVER!!!");
    }

}
