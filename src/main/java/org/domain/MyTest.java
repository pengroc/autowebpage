package org.domain;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyTest {

    public static void main(String[] args) {
        /*int i = Double.valueOf("12.45").intValue();
        System.out.println(i);

        String time = "2018-06-08T10:34:56+08:00";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTime dateTime = new DateTime(time);
        long timeInMillis = dateTime.toCalendar(Locale.getDefault()).getTimeInMillis();
        Date date = new Date(timeInMillis);
        String payTime = df.format(date);
        System.out.println(payTime);*/

        try {
            String str = "https://www.baidu.com/link?url=2FdxEGAZFkOr_LDfZzpWKUK3KIk5gGB96ZL-7_0UfzhiFQKgnnXKF-Eaj9M4P6Ye&amp;wd=&amp;eqid=89af516b000202b20000000464142749";
            URL url = new URL(str);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.getResponseCode();
            String realUrl = conn.getURL().toString();
            conn.disconnect();
            System.out.println("真实URL:" + realUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

}
