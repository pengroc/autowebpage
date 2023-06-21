package org.weblogs;

import org.utils.ClientProperties;
import org.utils.ExcelUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

public class ClassifyUtil {

    private static final String[] BAIDU_IP = {"220.181.108.", "111.206.198.", "116.179.32.", "123.125.71.",
            "111.206.221.", "124.166.232.", "180.76.15.", "180.76.5.", "116.179.37."};

    private static final String BAIDU_SPIDER_XLSX = "_baidu_spider.xlsx";

    public static void initData(String source, String logPath) throws Exception {
        File logFile = new File(logPath);
        if (logFile.exists()) {
            List<String> urlList = new ArrayList<String>();
            LinkedHashSet<String> urlSet = new LinkedHashSet<String>();
            Set<String> ipSet = new HashSet<String>();
            List<String> statusList = new ArrayList<String>();
            String time = null;

            FileInputStream stream = new FileInputStream(logPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));
            String strLine;
            int i = 0;
            while ((strLine = br.readLine()) != null) {
                String[] arr = strLine.split(" ");
                // 判断百度蜘蛛ip段
                String ip = arr[0];
                String ipEx = ip.substring(0, ip.lastIndexOf(".") + 1);
                List<String> baiduIps = Arrays.asList(ClientProperties.getProperty("baidu_spider_ips").split(","));
                if (baiduIps.contains(ipEx)) {
                    String url = arr[4].replace("[", "").replace("]", "").toLowerCase();
                    urlList.add(url);
                    urlSet.add(url);
                    ipSet.add(ip);
                    if (50 == i) {
                        // [29/Jan/2023:00:01:26
                        String[] times = arr[2].replace("[", "").split("/");
                        time = getNumberMonth(times[1]) + times[0] + "日";
                    }
                    i++;
                    statusList.add(arr[5].replace("]", "").replace("[HTTP_", ""));
                }
            }
            stream.close();

            File directory = new File(".");
            String excelPath = (new StringBuilder(directory.getCanonicalPath())).append("/" + ClientProperties.getProperty("jiameng_channel") + BAIDU_SPIDER_XLSX).toString();
            // status分类
            LinkedHashMap<String, Integer> statusMap = ClassifyUtil.statusClassify(statusList);
            ExcelUtils.writeTotalExcel(excelPath, time, source, urlList.size(), urlSet.size(), ipSet.size(), statusMap);
            // url分类
            LinkedHashMap<String, Integer> urlMap = null;
            if ("www".equals(source)) {
                urlMap = ClassifyUtil.urlClassifyPc(urlList);
            } else if ("wap".equals(source)) {
                urlMap = ClassifyUtil.urlClassifyWap(urlList);
            } else if ("mip".equals(source)) {
                urlMap = ClassifyUtil.urlClassifyMip(urlList);
            } else if ("pro".equals(source)) {
                urlMap = ClassifyUtil.urlClassifyPro(urlList);
            }
            if (null != urlMap && !urlMap.isEmpty()) {
                ExcelUtils.writeUrlExcel(excelPath, source, time, urlMap);
            }
        }

    }

    public static LinkedHashMap<String, Integer> statusClassify(List<String> list) {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
        map.put("200", 0);
        map.put("301", 0);
        map.put("302", 0);
        map.put("304", 0);
        map.put("403", 0);
        map.put("404", 0);
        map.put("408", 0);
        map.put("499", 0);
        map.put("500", 0);
        map.put("502", 0);
        map.put("503", 0);
        map.put("504", 0);
        for (String str : list) {
            if ("200".equals(str)) {
                map.put("200", map.get("200") + 1);
            } else if ("301".equals(str)) {
                map.put("301", map.get("301") + 1);
            } else if ("302".equals(str)) {
                map.put("302", map.get("302") + 1);
            } else if ("304".equals(str)) {
                map.put("304", map.get("304") + 1);
            } else if ("403".equals(str)) {
                map.put("403", map.get("403") + 1);
            } else if ("404".equals(str)) {
                map.put("404", map.get("404") + 1);
            } else if ("408".equals(str)) {
                map.put("408", map.get("408") + 1);
            } else if ("499".equals(str)) {
                map.put("499", map.get("499") + 1);
            } else if ("500".equals(str)) {
                map.put("500", map.get("500") + 1);
            } else if ("502".equals(str)) {
                map.put("502", map.get("502") + 1);
            } else if ("503".equals(str)) {
                map.put("503", map.get("503") + 1);
            } else if ("504".equals(str)) {
                map.put("504", map.get("504") + 1);
            }
        }
        return map;
    }

    public static LinkedHashMap<String, Integer> urlClassifyPc(List<String> list) {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
        map.put("total", list.size());
        map.put("/liebiao/", 0);
        map.put("/baike/", 0);
        map.put("/pinpai/", 0);
        map.put("/tuku/", 0);
        map.put("com/news/*(tag)", 0);
        map.put("com/news/", 0);
        map.put("/news/tag/", 0);
        map.put("com/*/news/", 0);
        map.put("/*/ask/", 0);
        map.put("[ask列表]", 0);
        map.put("/*/ask/*.htm", 0);
        map.put("/*/shop/", 0);
        map.put("/*/pic/", 0);
        map.put("[项目主页]", 0);
        map.put("/*/jiamengfei/", 0);

        String liebiao_REG = "^.*jm.com/liebiao/.*";
        String baike_REG = "^.*jm.com/baike/.*";
        String pinpai_REG = "^.*jm.com/pinpai/.*";
        String tuku_REG = "^.*jm.com/tuku/.*";
        String all_news_REG = "^.*jm.com/news/.*";
        //    String news_REG = "";
        String news_tag_REG = "^.*jm.com/news/tag/.*";
        String news_brand_REG = "^.*jm.com/.+/news/.*";
        String all_ask_REG = "^.*jm.com/.+/ask/.*";
        String ask_list_REG = "^.*jm.com/.+/ask/$";
        String ask_detail_REG = "^.*jm.com/.+/ask/.*.htm$";
        String shop_REG = "^.*jm.com/([a-zA-Z-0-9_]{1,25})?/shop/.*";
        String pic_REG = "^.*jm.com/([a-zA-Z-0-9_]{1,25})?/pic/.*";
        String brand_REG = "^.*jm.com/([a-zA-Z-0-9_]{1,25})+/$";
        String jmf_REG = "^.*jm.com/([a-zA-Z-0-9_]{1,25})?/jiamengfei/$";

        Pattern p1 = Pattern.compile(liebiao_REG);
        Pattern p2 = Pattern.compile(baike_REG);
        Pattern p3 = Pattern.compile(pinpai_REG);
        Pattern p4 = Pattern.compile(tuku_REG);
        Pattern p5 = Pattern.compile(all_news_REG);
        Pattern p6 = Pattern.compile(news_tag_REG);
        Pattern p7 = Pattern.compile(news_brand_REG);
        Pattern p8 = Pattern.compile(all_ask_REG);
        Pattern p9 = Pattern.compile(ask_list_REG);
        Pattern p10 = Pattern.compile(ask_detail_REG);
        Pattern p11 = Pattern.compile(shop_REG);
        Pattern p12 = Pattern.compile(pic_REG);
        Pattern p13 = Pattern.compile(brand_REG);
        Pattern p14 = Pattern.compile(jmf_REG);

        for (String url : list) {
            String str = url.substring(url.lastIndexOf("/"));
            if (!url.endsWith("/") && !str.contains(".")) {
                url += "/";
            }
            if (p1.matcher(url).find()) {
                map.put("/liebiao/", map.get("/liebiao/") + 1);
            } else if (p2.matcher(url).find()) {
                map.put("/baike/", map.get("/baike/") + 1);
            } else if (p3.matcher(url).find()) {
                map.put("/pinpai/", map.get("/pinpai/") + 1);
            } else if (p4.matcher(url).find()) {
                map.put("/tuku/", map.get("/tuku/") + 1);
            } else if (p5.matcher(url).find()) {
                map.put("com/news/*(tag)", map.get("com/news/*(tag)") + 1);
                if (p6.matcher(url).find()) {
                    map.put("/news/tag/", map.get("/news/tag/") + 1);
                }
            } else if (p7.matcher(url).find()) {
                map.put("com/*/news/", map.get("com/*/news/") + 1);
            } else if (p8.matcher(url).find()) {
                map.put("/*/ask/", map.get("/*/ask/") + 1);
                if (p9.matcher(url).find()) {
                    map.put("[ask列表]", map.get("[ask列表]") + 1);
                } else if (p10.matcher(url).find()) {
                    map.put("/*/ask/*.htm", map.get("/*/ask/*.htm") + 1);
                }
            } else if (p11.matcher(url).find()) {
                map.put("/*/shop/", map.get("/*/shop/") + 1);
            } else if (p12.matcher(url).find()) {
                map.put("/*/pic/", map.get("/*/pic/") + 1);
            } else if (p14.matcher(url).find()) {
                map.put("/*/jiamengfei/", map.get("/*/jiamengfei/") + 1);
            } else if (p13.matcher(url).find()) {
                map.put("[项目主页]", map.get("[项目主页]") + 1);
            }
        }
        map.put("com/news/", map.get("com/news/*(tag)") - map.get("/news/tag/"));
        return map;
    }

    public static LinkedHashMap<String, Integer> urlClassifyWap(List<String> list) {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
        map.put("total", list.size());
        map.put("/pinpai/", 0);
        map.put("com/news/", 0);
        map.put("com/*/news/", 0);
        map.put("/ask/v/", 0);
        map.put("/*/ask/", 0);
        map.put("/top/", 0);
        map.put("/liebiao/", 0);
        map.put("/tuku/", 0);
        map.put("/news/tag/", 0);
        map.put("/resource/css/", 0);
        map.put("/resource/js/", 0);
        map.put("/ajax/", 0);
        map.put("[项目主页]", 0);
        map.put("/*/jiamengfei/", 0);

        String pinpai_REG = "^.*jm.com/pinpai/.*";
        String news_REG = "^.*jm.com/news/.*";
        String news_brand_REG = "^.*jm.com/.+/news/.*";
        String ask_detail_REG = "^.*jm.com/ask/v/.*";
        String ask_brand_REG = "^.*jm.com/.+/ask/.*";
        String top_REG = "^.*jm.com/top/.*";
        String liebiao_REG = "^.*jm.com/liebiao/.*";
        String tuku_REG = "^.*jm.com/tuku/.*";
        String tag_REG = "^.*jm.com/news/tag/.*";
        String css_REG = "^.*jm.com/resource/css/.*";
        String js_REG = "^.*jm.com/resource/js/.*";
        String ajax_REG = "^.*jm.com/ajax/.*";
        String brand_REG = "^.*jm.com/([a-zA-Z-0-9_]{1,25})+/$";
        String jmf_REG = "^.*jm.com/([a-zA-Z-0-9_]{1,25})?/jiamengfei/$";

        Pattern p1 = Pattern.compile(pinpai_REG);
        Pattern p2 = Pattern.compile(news_REG);
        Pattern p3 = Pattern.compile(news_brand_REG);
        Pattern p4 = Pattern.compile(ask_detail_REG);
        Pattern p5 = Pattern.compile(ask_brand_REG);
        Pattern p6 = Pattern.compile(top_REG);
        Pattern p7 = Pattern.compile(liebiao_REG);
        Pattern p8 = Pattern.compile(tuku_REG);
        Pattern p9 = Pattern.compile(tag_REG);
        Pattern p10 = Pattern.compile(css_REG);
        Pattern p11 = Pattern.compile(js_REG);
        Pattern p12 = Pattern.compile(ajax_REG);
        Pattern p13 = Pattern.compile(brand_REG);
        Pattern p14 = Pattern.compile(jmf_REG);

        for (String url : list) {
            String str = url.substring(url.lastIndexOf("/"));
            if (!url.endsWith("/") && !str.contains(".")) {
                url += "/";
            }
            if (p1.matcher(url).find()) {
                map.put("/pinpai/", map.get("/pinpai/") + 1);
            } else if (p2.matcher(url).find()) {
                map.put("com/news/", map.get("com/news/") + 1);
                if (p9.matcher(url).find()) {
                    map.put("/news/tag/", map.get("/news/tag/") + 1);
                }
            } else if (p3.matcher(url).find()) {
                map.put("com/*/news/", map.get("com/*/news/") + 1);
            } else if (p4.matcher(url).find()) {
                map.put("/ask/v/", map.get("/ask/v/") + 1);
            } else if (p5.matcher(url).find()) {
                map.put("/*/ask/", map.get("/*/ask/") + 1);
            } else if (p6.matcher(url).find()) {
                map.put("/top/", map.get("/top/") + 1);
            } else if (p7.matcher(url).find()) {
                map.put("/liebiao/", map.get("/liebiao/") + 1);
            } else if (p8.matcher(url).find()) {
                map.put("/tuku/", map.get("/tuku/") + 1);
            } else if (p10.matcher(url).find()) {
                map.put("/resource/css/", map.get("/resource/css/") + 1);
            } else if (p11.matcher(url).find()) {
                map.put("/resource/js/", map.get("/resource/js/") + 1);
            } else if (p12.matcher(url).find()) {
                map.put("/ajax/", map.get("/ajax/") + 1);
            } else if (p14.matcher(url).find()) {
                map.put("/*/jiamengfei/", map.get("/*/jiamengfei/") + 1);
            } else if (p13.matcher(url).find()) {
                map.put("[项目主页]", map.get("[项目主页]") + 1);
            }
        }
        return map;
    }

    public static LinkedHashMap<String, Integer> urlClassifyMip(List<String> list) {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
        map.put("total", list.size());
        map.put("/pro/", 0);
        map.put("com/news/", 0);
        map.put("/liebiao/", 0);
        map.put("/news/hangye/", 0);
        map.put("com/*/news/*.htm", 0);

        String pro_REG = "^.*jm.com/pro/.*";
        String news_REG = "^.*jm.com/news/.*";
        String liebiao_REG = "^.*jm.com/liebiao/.*";
        String hangye_REG = "^.*jm.com/news/hangye/.*";
        String news_brand_dtl_REG = "^.*jm.com/.+/news/.*(.htm)$";

        Pattern p1 = Pattern.compile(pro_REG);
        Pattern p2 = Pattern.compile(news_REG);
        Pattern p3 = Pattern.compile(liebiao_REG);
        Pattern p4 = Pattern.compile(hangye_REG);
        Pattern p5 = Pattern.compile(news_brand_dtl_REG);

        for (String url : list) {
            String str = url.substring(url.lastIndexOf("/"));
            if (!url.endsWith("/") && !str.contains(".")) {
                url += "/";
            }
            if (p1.matcher(url).find()) {
                map.put("/pro/", map.get("/pro/") + 1);
            } else if (p2.matcher(url).find()) {
                map.put("com/news/", map.get("com/news/") + 1);
                if (p4.matcher(url).find()) {
                    map.put("/news/hangye/", map.get("/news/hangye/") + 1);
                }
            } else if (p3.matcher(url).find()) {
                map.put("/liebiao/", map.get("/liebiao/") + 1);
            } else if (p5.matcher(url).find()) {
                map.put("com/*/news/*.htm", map.get("com/*/news/*.htm") + 1);
            }
        }
        return map;
    }

    public static LinkedHashMap<String, Integer> urlClassifyPro(List<String> list) {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
        map.put("total", list.size());
        map.put("/news/", 0);
        map.put("/shop/", 0);
        map.put("/pic/", 0);
        map.put("/ask/", 0);
        map.put("/robots.txt", 0);
        map.put("/resource/js/", 0);
        map.put("/resource/css/", 0);
        map.put("/resource/images/", 0);
        map.put("[项目抓取]", 0);
        map.put("[唯一抓取]", 0);
        map.put("[抓取占比]", -1);
        map.put("/jiamengfei/", 0);
        map.put("/brand/jm-tongji", 0);
        map.put("/ajax/loginDetect.html", 0);
        map.put("#row_3", 0);
        map.put("#mesBoard", 0);

        String news_REG = "^.*91jm.com/news/.*";
        String shop_REG = "^.*91jm.com/shop/.*";
        String pic_REG = "^.*91jm.com/pic/.*";
        String ask_REG = "^.*91jm.com/ask/.*";
        String robots_REG = "^.*91jm.com/robots.txt$";
        String js_REG = "^.*91jm.com/resource/js/.*";
        String css_REG = "^.*91jm.com/resource/css/.*";
        String images_REG = "^.*91jm.com/resource/images/.*";
        String brand_REG = "^.*91jm.com/$";
        String jmf_REG = "^.*91jm.com/jiamengfei/$";
        String tongji_REG = "^.*91jm.com/brand/jm-tongji.*";
        String login_REG = "^.*91jm.com/ajax/loginDetect.html$";
        String row_REG = "^.*91jm.com/.*#row_3$";
        String mesBoard_REG = "^.*91jm.com/.*#mesBoard$";

        Pattern p1 = Pattern.compile(news_REG);
        Pattern p2 = Pattern.compile(shop_REG);
        Pattern p3 = Pattern.compile(pic_REG);
        Pattern p4 = Pattern.compile(ask_REG);
        Pattern p5 = Pattern.compile(robots_REG);
        Pattern p6 = Pattern.compile(js_REG);
        Pattern p7 = Pattern.compile(css_REG);
        Pattern p8 = Pattern.compile(images_REG);
        Pattern p9 = Pattern.compile(brand_REG);
        Pattern p10 = Pattern.compile(jmf_REG);
        Pattern p11 = Pattern.compile(tongji_REG);
        Pattern p12 = Pattern.compile(login_REG);
        Pattern p13 = Pattern.compile(row_REG);
        Pattern p14 = Pattern.compile(mesBoard_REG);

        Set<String> urlSet = new HashSet<String>();
        for (String url : list) {
            String str = url.substring(url.lastIndexOf("/"));
            if (!url.endsWith("/") && !str.contains(".")) {
                url += "/";
            }
            if (p1.matcher(url).find()) {
                map.put("/news/", map.get("/news/") + 1);
            } else if (p2.matcher(url).find()) {
                map.put("/shop/", map.get("/shop/") + 1);
            } else if (p3.matcher(url).find()) {
                map.put("/pic/", map.get("/pic/") + 1);
            } else if (p4.matcher(url).find()) {
                map.put("/ask/", map.get("/ask/") + 1);
            } else if (p5.matcher(url).find()) {
                map.put("/robots.txt", map.get("/robots.txt") + 1);
            } else if (p6.matcher(url).find()) {
                map.put("/resource/js/", map.get("/resource/js/") + 1);
            } else if (p7.matcher(url).find()) {
                map.put("/resource/css/", map.get("/resource/css/") + 1);
            } else if (p8.matcher(url).find()) {
                map.put("/resource/images/", map.get("/resource/images/") + 1);
            } else if (p9.matcher(url).find()) {
                map.put("[项目抓取]", map.get("[项目抓取]") + 1);
                urlSet.add(url);
            } else if (p10.matcher(url).find()) {
                map.put("/jiamengfei/", map.get("/jiamengfei/") + 1);
            } else if (p11.matcher(url).find()) {
                map.put("/brand/jm-tongji", map.get("/brand/jm-tongji") + 1);
            } else if (p12.matcher(url).find()) {
                map.put("/ajax/loginDetect.html", map.get("/ajax/loginDetect.html") + 1);
            } else if (p13.matcher(url).find()) {
                map.put("#row_3", map.get("#row_3") + 1);
            } else if (p14.matcher(url).find()) {
                map.put("#mesBoard", map.get("#mesBoard") + 1);
            }
        }
        map.put("[唯一抓取]", urlSet.size());
        return map;
    }

    public static String getNumberMonth(String englishMonth) {
        if (null == englishMonth || "".equals(englishMonth)) {
            return null;
        }
        String res = "";
        if ("Jan".equals(englishMonth)) {
            res = "1月";
        } else if ("Feb".equals(englishMonth)) {
            res = "2月";
        } else if ("Mar".equals(englishMonth)) {
            res = "3月";
        } else if ("Apr".equals(englishMonth)) {
            res = "4月";
        } else if ("May".equals(englishMonth)) {
            res = "5月";
        } else if ("Jun".equals(englishMonth)) {
            res = "6月";
        } else if ("Jul".equals(englishMonth)) {
            res = "7月";
        } else if ("Aug".equals(englishMonth)) {
            res = "8月";
        } else if ("Sept".equals(englishMonth)) {
            res = "9月";
        } else if ("Oct".equals(englishMonth)) {
            res = "10月";
        } else if ("Nov".equals(englishMonth)) {
            res = "11月";
        } else if ("Dec".equals(englishMonth)) {
            res = "12月";
        }
        return res;
    }

}
