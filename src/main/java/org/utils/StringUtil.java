package org.utils;

import org.apache.log4j.Level;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author heyukun
 * @version [版本号, 2011-10-10]
 */
public class StringUtil {
    /**
     * 16进制数字字符集
     */
    private static String hexString = "0123456789ABCDEF";

    /**
     * 把ip字符串转换为long型
     *
     * @param ip
     * @return long
     */
    public static long StingIpToLong(String ip) {
        String[] arr = ip.split("\\.");
        return (Long.valueOf(arr[0]) * 0x1000000 + Long.valueOf(arr[1])
                * 0x10000 + Long.valueOf(arr[2]) * 0x100 + Long.valueOf(arr[3]));
    }

    /**
     * toString
     *
     * @param object
     * @return String
     */
    public static String toString(Object object) {
        if (null == object) {
            return "";
        }
        return object.toString();
    }

    /**
     * 获取毫秒
     *
     * @param data
     * @return
     */
    public static long getMiss(String data) {
        if (StringUtil.isEmpty(data))
            return 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            long time = sdf.parse(data).getTime();
            return time;
        } catch (ParseException e) {
            return 0;
        }

    }

    /**
     * 判断字符串是否为空
     *
     * @param arg 字符串
     * @return 返回是否为空
     */
    public static boolean isEmpty(String arg) {
        if (null == arg) {
            return true;
        }
        if ("".equals(arg)) {
            return true;
        }
        return false;
    }

    /**
     * 前后去空格
     *
     * @param arg
     */
    public static String trim(String arg) {
        if (null == arg) {
            return "";
        }
        return arg.trim();
    }

    /**
     * 前后去空格
     *
     * @param arg
     */
    public static String conversionNull(String arg) {
        if (null == arg) {
            return "";
        }
        return arg;
    }

    /**
     * 将特殊符号转换成html代码
     */
    public static String htmlspecialchars(String str) {
        if (null == str || "".equals(str)) {
            return "";
        }
        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("\"", "&quot;");
        return str;
    }

    /**
     * 将特殊符号转换成html代码
     */
    public static String deHtmlspecialchars(String str) {
        if (null == str || "".equals(str)) {
            return "";
        }
        str = str.replaceAll("&amp;", "&");
        str = str.replaceAll("&lt;", "<");
        str = str.replaceAll("&gt;", ">");
        str = str.replaceAll("&quot;", "\"");
        return str;
    }

    /**
     * 将特殊符号转换成html代码
     */
    public static String convertContent(String str) {
        if (null == str || "".equals(str)) {
            return "";
        }
        str = str.replaceAll("&amp;", "");
        str = str.replaceAll("&lt;", "");
        str = str.replaceAll("&gt;", "");
        str = str.replaceAll("&quot;", "");
        str = str.replaceAll("&nbsp;", "");
        str = str.replaceAll("&reg;", "");
        return str;
    }

    /**
     * 返回错误吗
     */
    public static String returnCode(String code) {
        if (null == code || "".equals(code)) {
            return "error";
        }
        if ("000000".equals(code)) {
            return "success";
        }
        return "error";
    }

    /**
     * 获取随机数
     */
    public static String getRandomStr(int length) {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < length; i++) {
            int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写还是小写  
            result += String.valueOf((char) (choice + random.nextInt(26)));
        }
        return result;
    }

    /**
     * 产生一个随机的数字字符串
     *
     * @param length
     * @return
     */
    public static String getRandomNum(int length) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 判断字符串是否是数字
     *
     * @param str
     * @return true or false
     */
    public static boolean isNum(String str) {
        return str.matches("^([0-9]+)$");
    }

    /**
     * 判断是否是IP
     */
    public static boolean isIp(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        String regex = "^(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|[1-9])\\."
                + "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.(25[0-5]|2[0-4]\\d|1\\d{2}|"
                + "[1-9]\\d|\\d)\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|[1-9])$";
        return str.matches(regex);
    }

    /**
     * 将字符串编码成16进制数字,适用于所有字符（包括中文）
     *
     * @param str
     * @return 16进制的编码
     */
    public static String encodetoHex(String str) {
        // 根据默认编码获取字节数组
        byte[] bytes = str.getBytes();
        StringBuilder builder = new StringBuilder(bytes.length * 2);
        // 将字节数组中每个字节拆解成2位16进制整数
        for (int i = 0; i < bytes.length; i++) {
            builder.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
            builder.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
        }
        return builder.toString();
    }

    /**
     * 将16进制数字解码成字符串,适用于所有字符（包括中文）
     *
     * @param str
     * @return 字符
     */
    public static String decodeHextoString(String str) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(str.length() / 2);
        // 将每2位16进制整数组装成一个字节
        for (int i = 0; i < str.length(); i += 2)
            baos.write((hexString.indexOf(str.charAt(i)) << 4 | hexString.indexOf(str.charAt(i + 1))));
        return new String(baos.toByteArray());
    }

    public static int getRandom(int index) {
        int count = (int) (Math.random() * index);
        return count;
    }

    public static Map<String, Object> getParamMap(Object... paramMap) {
        Map<String, Object> pmap = new HashMap<String, Object>();
        for (int i = 0; i < paramMap.length; i++) {
            if (i % 2 != 0) {
                continue;
            }
            pmap.put(String.valueOf(paramMap[i]), paramMap[i + 1]);
        }
        return pmap;
    }

    /**
     * 转为字节数组
     *
     * @param string 字符串
     * @return byte[]
     */
    public static byte[] getBytes(String string) {
        try {
            return string.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            LogFactory.getInstance(StringUtil.class).log(Level.ERROR,
                    "parse encoding error ",
                    e);
        }
        return new byte[0];
    }

    public static String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式 
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式 
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式 

        Pattern p_script = Pattern.compile(regEx_script,
                Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); //过滤script标签 

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); //过滤style标签 

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); //过滤html标签 

        return htmlStr.trim(); //返回文本字符串 
    }

    public static String toUpperCase(String str) {
        if (!StringUtil.isEmpty(str)) {
            return str.toUpperCase();
        } else {
            return "";
        }
    }

    public static String toLowerCase(String str) {
        if (!StringUtil.isEmpty(str)) {
            return str.toLowerCase();
        } else {
            return "";
        }
    }

    /**
     * 非法字符替换
     *
     * @param paramValue
     * @return String
     */
    public static String illegalStrReplace(String paramValue) {
        if (!isEmpty(paramValue)) {
            paramValue = paramValue.replaceAll("\t", "");
            paramValue = paramValue.replaceAll("\r\n", "");
            paramValue = paramValue.replaceAll("\r", "");
            paramValue = paramValue.replaceAll("\n", "");
            paramValue = paramValue.replaceAll(" ", "");
            paramValue = paramValue.replaceAll("%0a", "");
            paramValue = paramValue.replaceAll("%0d", "");
            paramValue = paramValue.replaceAll("%0D", "");
            paramValue = paramValue.replaceAll("%0A", "");
            paramValue = paramValue.replaceAll("%", "");
            paramValue = paramValue.replaceAll("<", "&lt");
            paramValue = paramValue.replaceAll(">", "&gt");
            paramValue = paramValue.replaceAll("%3C", "&lt");
            paramValue = paramValue.replaceAll("%3E", "&gt");
            paramValue = paramValue.replaceAll("#", "");
            paramValue = paramValue.replaceAll("/", "");
            paramValue = paramValue.replaceAll("\\\\", "");
            paramValue = paramValue.replaceAll("'", "");
            paramValue = paramValue.replaceAll("\"", "");
        }
        return paramValue;
    }

    public static String byte2HexString(byte[] b) {
        char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F'};
        char[] newChar = new char[b.length * 2];
        for (int i = 0; i < b.length; i++) {
            newChar[2 * i] = hex[(b[i] & 0xf0) >> 4];
            newChar[2 * i + 1] = hex[b[i] & 0xf];
        }
        return new String(newChar);
    }

    public static byte[] hexString2ByteArray(String hexString) {
        char[] chars = hexString.toCharArray();
        byte[] b = new byte[chars.length / 2];
        for (int i = 0; i < b.length; i++) {
            int high = Character.digit(chars[2 * i], 16) << 4;
            int low = Character.digit(chars[2 * i + 1], 16);
            b[i] = (byte) (high | low);
        }
        return b;
    }

    public static String htmlEscapeDecimal(String html) {
        if (null != html) {
            html = html.replaceAll("ˎ̥", "&#718;&#805;");
            html = html.replaceAll("•", "&bull;");
            html = html.replaceAll("Ÿ", "&middot;");
            html = html.replaceAll("·", "&middot;");
            html = html.replaceAll("²", "&sup2;");
            html = html.replaceAll("Ø", "&Oslash;");
            html = html.replaceAll("¥", "&yen;");
            html = html.replaceAll("♣", "&clubs;");
            html = html.replaceAll("™", "&trade;");
            html = html.replaceAll("®", "&reg;");
            html = html.replaceAll("Ü", "&Uuml;");
            html = html.replaceAll("µ", "&micro;");
            html = html.replaceAll("‧", "&#8231;");
            html = html.replaceAll("æ", "&aelig;");
            html = html.replaceAll("·", "&#903;");
            html = html.replaceAll("ê", "&ecirc;");
            html = html.replaceAll("ç", "&ccedil;");
            html = html.replaceAll("õ", "&otilde;");
            html = html.replaceAll("ã", "&atilde;");
            html = html.replaceAll("⇒", "&#8658;");
            html = html.replaceAll("ä", "&auml;");
            html = html.replaceAll("ö", "&ouml;");
        }
        return html;
    }

    public static Integer getChineseNum(String str) {
        Integer count = 0;
        if (!isEmpty(str)) {
            String temp = null;
            Pattern p = Pattern.compile("[\u4E00-\u9FA5]+");
            Matcher m = p.matcher(str);
            while (m.find()) {
                temp = m.group(0);
                count = count + temp.length();
            }
        }
        return count;
    }

    public static String delHTMLTag2(String htmlStr) {
        if (isEmpty(htmlStr)) {
            return "";
        }
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式 
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式 
        String regEx_html = "<((?!img))[^>]*>"; //定义HTML标签的正则表达式 
        String regEx_img = "<(img).*?src=(\\S*).*?(/>|></(img)>|>)";
        String regEx_alt = " alt=\"(.*?)\"";
        String regEx_title = " title=\"(.*?)\"";
        String regEx_styleEx = " style=\"(.*?)\""; //标签style属性

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); //过滤script标签 

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); //过滤style标签 

        Pattern p_alt = Pattern.compile(regEx_alt, Pattern.CASE_INSENSITIVE);
        Matcher m_alt = p_alt.matcher(htmlStr);
        htmlStr = m_alt.replaceAll("");

        Pattern p_title = Pattern.compile(regEx_title, Pattern.CASE_INSENSITIVE);
        Matcher m_title = p_title.matcher(htmlStr);
        htmlStr = m_title.replaceAll("");

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); //过滤html标签 

        Pattern p_styleEx = Pattern.compile(regEx_styleEx, Pattern.CASE_INSENSITIVE);
        Matcher m_styleEx = p_styleEx.matcher(htmlStr);
        htmlStr = m_styleEx.replaceAll("");

        Pattern p_img = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        Matcher m_img = p_img.matcher(htmlStr);
        htmlStr = m_img.replaceAll("");

        return htmlStr.trim(); //返回文本字符串 
    }

}
