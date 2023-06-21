package org.utils;

import org.apache.log4j.Level;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密工具类
 *
 * @author heyukun
 * @version [版本号, 2013-1-30]
 */
public class AESEncrypter {

    static final int I1 = 82;

    static final int I2 = 22;

    static final int I3 = 50;

    static final int I4 = 44;

    static final int I5 = -16;

    static final int I6 = 124;

    static final int I7 = -40;

    static final int I8 = -114;

    static final int I9 = -87;

    static final int I10 = -40;

    static final int I11 = 37;

    static final int I12 = 23;

    static final int I13 = -56;

    static final int I14 = 23;

    static final int I15 = -33;

    static final int I16 = 75;

    static final int K1 = -42;

    static final int K2 = 35;

    static final int K3 = 67;

    static final int K4 = -86;

    static final int K5 = 19;

    static final int K6 = 29;

    static final int K7 = -11;

    static final int K8 = 84;

    static final int K9 = 94;

    static final int K10 = 111;

    static final int K11 = 75;

    static final int K12 = -104;

    static final int K13 = 71;

    static final int K14 = 46;

    static final int K15 = 86;

    static final int K16 = -21;

    static final int K17 = -119;

    static final int K18 = 110;

    static final int K19 = -11;

    static final int K20 = -32;

    static final int K21 = -28;

    static final int K22 = 91;

    static final int K23 = -33;

    static final int K24 = -46;

    static final int K25 = 99;

    static final int K26 = 49;

    static final int K27 = 2;

    static final int K28 = 66;

    static final int K29 = -101;

    static final int K30 = -11;

    static final int K31 = -8;

    static final int K32 = 56;

    static final int C1 = 128;

    static final int C2 = 2;

    static final int C3 = 16;

    static final int NUM_0XFF = 0xff;

    static final int NUM_0X10 = 0x10;

    /**
     * 数组
     */
    private static byte[] iv = new byte[]{I1, I2, I3, I4, I5, I6, I7, I8, I9,
            I10, I11, I12, I13, I14, I15, I16};

    private static AESEncrypter aes = null;

    /**
     * 返回实例
     *
     * @return AESEncrypter AESEncrypter
     */
    public static synchronized AESEncrypter getInstance() {
        if (aes == null) {
            aes = new AESEncrypter();
        }
        return aes;
    }

    /**
     * 加密方法
     *
     * @param msg  msg
     * @param sKey sKey
     * @return String 加密后的密码
     */
    public String encrypt(String msg, String sKey) {
        String str = "";
        try {
            if (null == sKey) {
                // 记录日志,加密的key值为空
                LogFactory.getInstance(AESEncrypter.class).log(Level.ERROR,
                        "AES encrypt key is null!");
            } else {
                if (C3 != sKey.length()) {
                    // 记录日志,加密的key值长度大于16
                    LogFactory.getInstance(AESEncrypter.class).log(Level.ERROR,
                            "AES encrypt the length of key is longer than 16!");
                }

                byte[] raw = sKey.getBytes("UTF-8");
                SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                IvParameterSpec para = new IvParameterSpec(iv);
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, para);

                byte[] encrypted = cipher.doFinal(msg.getBytes());

                str = new BASE64Encoder().encode(encrypted);
            }
        } catch (Exception e) {
            // 记录日志,加密失败
            LogFactory.getInstance(AESEncrypter.class).log(Level.ERROR,
                    "AES encrypt failed and password is :******");
        }
        return str;
    }

    /**
     * 解密工具类
     *
     * @param value value
     * @param sKey  sKey
     * @return String 解密后密码
     */
    public String decrypt(String value, String sKey) {
        String str = "";
        try {
            if (null == sKey) {
                // 记录日志,加密的key值为空
                LogFactory.getInstance(AESEncrypter.class).log(Level.ERROR,
                        "AES encrypt key is null!");
            } else {
                if (C3 != sKey.length()) {
                    // 记录日志,加密的key值长度大于16
                    LogFactory.getInstance(AESEncrypter.class).log(Level.ERROR,
                            "AES encrypt the length of key is longer than 16!");
                }
                byte[] raw = sKey.getBytes("UTF-8");
                SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                IvParameterSpec para = new IvParameterSpec(iv);
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, para);
                byte[] encrypted1 = new BASE64Decoder().decodeBuffer(value);
                try {
                    byte[] original = cipher.doFinal(encrypted1);
                    String originalString = new String(original);
                    return originalString;
                } catch (Exception e) {
                    // 记录日志,加密失败
                    LogFactory.getInstance(AESEncrypter.class)
                            .log(Level.ERROR,
                                    "AES encrypt failed and encrypted password is *******:");
                }
            }
        } catch (Exception e) {
            // 记录日志,加密失败
            LogFactory.getInstance(AESEncrypter.class).log(Level.ERROR,
                    "AES encrypt failedand encrypted password is :*****");
        }
        return str;
    }

}
