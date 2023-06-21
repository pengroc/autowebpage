package org.utils;

import org.apache.log4j.Level;

import java.sql.Connection;
import java.sql.Statement;

public class DButil {

    // 创建静态全局变量
    private static Connection conn;

    private static Statement st;

    private static DButil dbutil;

    /**
     * 驱动
     */
    private String driver;

    /**
     * 连接URL
     */
    private String url;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String pwd;

    public static Connection getConnection91() {
        Connection con = null;
        try {
            con = registerDB(ClientProperties.getProperty("91mysql_driver"),
                    ClientProperties.getProperty("91mysql_url_ask"),
                    ClientProperties.getProperty("91mysql_username"),
                    ClientProperties.getProperty("91mysql_password"))
                    .getConnection();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return con;
    }

    public static DButil registerDB(String driver, String url, String userName,
                                    String pwd) {
        if (null == dbutil) {
            dbutil = new DButil();
        }
        dbutil.setDriver(driver);
        dbutil.setUrl(url);
        dbutil.setUserName(userName);
        dbutil.setPwd(pwd);
        return dbutil;
    }

    /* 获取数据库连接的函数 */
    public Connection getConnection() {
        Connection con = null; // 创建用于连接数据库的Connection对象
        try {

            Class.forName(this.driver);// 加载Mysql数据驱动

            con = java.sql.DriverManager.getConnection(this.url, this.userName,
                    this.pwd);

        } catch (Exception e) {
            LogFactory.getInstance(DButil.class).log(Level.ERROR, "DButil",
                    "getConnection", new String[]{}, new String[]{},
                    "数据库连接失败!", e);
            e.printStackTrace();
            System.exit(1);
        }
        return con; // 返回所建立的数据库连接
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

}
