package org.utils;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;

import java.io.*;

public class DecompressUtil {

    public static boolean decompressTarBz2(File file, String targetPath) {

        boolean res = false;
        FileInputStream fis = null;
        OutputStream fos = null;
        BZip2CompressorInputStream bis = null;
        TarInputStream tis = null;
        try {
            fis = new FileInputStream(file);
            bis = new BZip2CompressorInputStream(fis);
            tis = new TarInputStream(bis, 1024 * 2);
            // 创建输出目录
            createDirectory(targetPath, null);
            TarEntry entry;
            while ((entry = tis.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    createDirectory(targetPath, entry.getName()); // 创建子目录
                } else {
                    fos = new FileOutputStream(new File(targetPath + File.separator + entry.getName()));
                    int count;
                    byte data[] = new byte[2048];
                    while ((count = tis.read(data)) != -1) {
                        fos.write(data, 0, count);
                    }
                    fos.flush();
                }
            }
            res = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (bis != null) {
                    bis.close();
                }
                if (tis != null) {
                    tis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    /**
     * 构建目录
     *
     * @param outputDir 输出目录
     * @param subDir    子目录
     */
    private static void createDirectory(String outputDir, String subDir) {
        File file = new File(outputDir);
        if (!(subDir == null || subDir.trim().equals(""))) {//子目录不为空
            file = new File(outputDir + File.separator + subDir);
        }
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.mkdirs();
        }
    }

}

