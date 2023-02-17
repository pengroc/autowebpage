package org.webpage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class WebPageDomain {

    private static final String IMG_JM = "img0.jiameng.com";

    private static final String IMG_BG = "b_01.jpg";

    public static void main(String[] args) throws Exception {

        File directory = new File(".");
        String itemPath = (new StringBuilder(directory.getCanonicalPath())).append("/item/").toString();
        String rollOnePath = (new StringBuilder(directory.getCanonicalPath())).append("/roll_one/").toString();
        String rollTwoPath = (new StringBuilder(directory.getCanonicalPath())).append("/roll_two/").toString();
        File itemFile = new File(itemPath);
        if (!itemFile.exists()) {
            exitSystem("item文件夹丢失!");
        }
        File[] itemFiles = itemFile.listFiles();
        if (1 != itemFiles.length || !itemFiles[0].isDirectory()) {
            exitSystem("打开item检查（有且只能有一个文件夹）...");
        }
        File webFile = itemFiles[0];
        File[] _files = webFile.listFiles();
        if (1 != _files.length || !_files[0].isDirectory()) {
            exitSystem("检查" + webFile.getName() + "（有且只能有一个存放图片文件夹）...");
        }
        if (!IMG_JM.equals(_files[0].getName())) {
            _files[0].renameTo(new File(webFile.getPath() + "/" + IMG_JM));
        }
        File imgFile = new File(webFile.getPath() + "/" + IMG_JM);
        File[] imgFiles = imgFile.listFiles();
        int imgNum = imgFiles.length;// 图片总数
        int width;// 页面宽度
        boolean bgFlag = true;// 背景标识
        if (!IMG_BG.equals(imgFiles[0].getName())) {
            BufferedImage img = ImageIO.read(imgFiles[0]);
            width = img.getWidth();
            bgFlag = false;
        } else {
            if (imgNum % 2 != 0) {
                exitSystem("背景页和前景页数量不一致!");
            }
            File pgFile = new File(webFile.getPath() + "/img0.jiameng.com/p_01.jpg");
            BufferedImage bfImg = ImageIO.read(pgFile);
            width = bfImg.getWidth();
            imgNum /= 2;
        }

        Scanner scan = new Scanner(System.in);
        System.out.println("生成100%页面？\n（y:是/n:否）");
        String hundred = scan.nextLine();
        while (!hundred.equals("y") && !hundred.equals("n")) {
            System.out.println("非法输入，请重新输入：");
            hundred = scan.next();
        }

        File rollOneFile = new File(rollOnePath);
        int rollOneInt = 0;
        if (rollOneFile.listFiles().length > 0) {
            scan = new Scanner(System.in);//控制台输入控件
            System.out.println("检测到roll_one含有滚动图片...\n请输入（滚动一）位置：");
            while (!scan.hasNextInt()) {
                System.out.println("非法输入，请重新输入：");
                scan.next();
            }
            rollOneInt = scan.nextInt();
        }
        File rollTwoFile = new File(rollTwoPath);
        int rollTwoInt = 0;
        if (rollTwoFile.listFiles().length > 0) {
            scan = new Scanner(System.in);//控制台输入控件
            System.out.println("检测到roll_two含有滚动图片...\n请输入（滚动二）位置：");
            while (!scan.hasNextInt()) {
                System.out.println("非法输入，请重新输入：");
                scan.next();
            }
            rollTwoInt = scan.nextInt();
        }
        scan.close();

        PrintStream printStream;
        StringBuilder sb = new StringBuilder();
        sb.append("<style>\n");
        sb.append("* { padding: 0; margin: 0; }\n");
        sb.append(".wrapper { text-align: left; font-family: Microsoft Yahei; }\n");
        sb.append(".wrapper .clearfix { overflow: auto; _height: 1% }\n");
        sb.append(".wrapper>div { background-position: top center; background-repeat: no-repeat; }\n");
        if ("y".equals(hundred)) {
            sb.append(".wrapper>div>div, .wrapper>div>img { display: block; width: 100%; max-width: 1920px; margin: 0 auto; background-position: top center; background-repeat: no-repeat; }\n");
        } else {
            sb.append(".wrapper>div>div, .wrapper>div>img { display: block; width: " + width + "px; margin: 0 auto; background-position: top center; background-repeat: no-repeat; }\n");
        }
        // 滚动1 css及html
        if (rollOneInt > 0) {
            // 转移图片至roll_one
            File rollDir = new File(webFile.getPath() + "/img0.jiameng.com/roll_one/");
            copyFolder(rollOneFile.getPath(), rollDir.getPath());
            deleteAll(rollOneFile);
            // 背景图名称
            String rollStr = rollOneInt < 10 ? "0" + rollOneInt : "" + rollOneInt;
            String prefix = "p";
            if (!bgFlag) {
                prefix = imgFiles[0].getName().split("_")[0];
            }
            String picStr = prefix + "_" + rollStr + ".jpg";
            // 背景图高度
            File bgFile = new File(webFile.getPath() + "/img0.jiameng.com/" + picStr);
            BufferedImage buffer = ImageIO.read(bgFile);
            int bgHeight = buffer.getHeight();
            // 滚动图高度
            File[] rollImages = rollDir.listFiles();
            File rollImg = rollImages[0];
            buffer = ImageIO.read(rollImg);
            int rollHeight = buffer.getHeight();
            // 添加htm样式
            if ("y".equals(hundred)) {
                sb.append("#con-" + rollOneInt + "{ background-image: url(img0.jiameng.com/" + picStr + "); height: " + bgHeight + "px; background-size:100% 100%; }\n");
                sb.append("#con-" + rollOneInt + " iframe { width: 90.5%; height: " + rollHeight + "px; padding-top: 50px; margin-left: 10.5%; }\n");
            } else {
                sb.append("#con-" + rollOneInt + "{ background-image: url(img0.jiameng.com/" + picStr + "); height: " + bgHeight + "px; }\n");
                sb.append("#con-" + rollOneInt + " iframe { width: 1820px; height: " + rollHeight + "px; padding-top: 50px; margin-left: 20px; }\n");
            }
            // 生成滚动html
            StringBuilder rollSb = new StringBuilder();
            rollSb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
            rollSb.append("<head>\n");
            rollSb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n");
            rollSb.append("<title>无标题文档</title>\n");
            rollSb.append("<style>\n");
            rollSb.append("* {border: 0; padding: 0; margin: 0;}\n");
            rollSb.append("table{border-spacing:0;}\n");
            rollSb.append("img {display: block; margin: 0 3px; height: " + rollHeight + "px;}\n");
            rollSb.append("</style>\n");
            rollSb.append("</head>\n");
            rollSb.append("<body>\n");
            rollSb.append("<div id=\"demo\" style=\"width:1920px; height:" + rollHeight + "px; overflow:hidden\">\n");
            rollSb.append("<table cellpadding=\"0\" width=\"100%\" align=\"left\" cellspacing=\"0\" border=\"0\">\n");
            rollSb.append("<tbody><tr><td id=\"demo1\" valign=\"top\"><table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" align=\"center\" border=\"0\">\n");
            rollSb.append("<tbody><tr>\n");
            for (File f : rollImages) {
                rollSb.append("<td align=\"middle\"><img src=\"" + f.getName() + "\" alt=\"\"/></td>\n");
            }
            rollSb.append("</tr></tbody></table></td><td id=\"demo2\" valign=\"center\"></td></tr></tbody></table>\n");
            rollSb.append("</div>\n");
            rollSb.append("<script>\n");
            rollSb.append("var speed=15\n");
            rollSb.append("demo2.innerHTML=demo1.innerHTML\n");
            rollSb.append("function Marquee110(){\n");
            rollSb.append("if(demo2.offsetWidth-demo.scrollLeft<=0)\n");
            rollSb.append("demo.scrollLeft-=demo1.offsetWidth\n");
            rollSb.append("else{demo.scrollLeft++}}\n");
            rollSb.append("var MyMar10=setInterval(Marquee110,speed)\n");
            rollSb.append("demo.onmouseover=function() {clearInterval(MyMar10)}\n");
            rollSb.append("demo.onmouseout=function() {MyMar10=setInterval(Marquee110,speed)}\n");
            rollSb.append("</script>\n");
            rollSb.append("</body>\n");
            rollSb.append("</html>\n");
            printStream = new PrintStream(new FileOutputStream(webFile.getAbsolutePath() + "/img0.jiameng.com/roll_one/scroll.html"));
            printStream.println(rollSb);
        }
        // 滚动2 css及html
        if (rollTwoInt > 0) {
            // 转移图片至roll_two
            File rollDir = new File(webFile.getPath() + "/img0.jiameng.com/roll_two/");
            copyFolder(rollTwoFile.getPath(), rollDir.getPath());
            deleteAll(rollTwoFile);
            // 背景图名称
            String rollStr = rollTwoInt < 10 ? "0" + rollTwoInt : "" + rollTwoInt;
            String prefix = "p";
            if (!bgFlag) {
                prefix = imgFiles[0].getName().split("_")[0];
            }
            String picStr = prefix + "_" + rollStr + ".jpg";
            // 背景图高度
            File bgFile = new File(webFile.getPath() + "/img0.jiameng.com/" + picStr);
            BufferedImage buffer = ImageIO.read(bgFile);
            int bgHeight = buffer.getHeight();
            // 滚动图高度
            File[] rollImages = rollDir.listFiles();
            File rollImg = rollImages[0];
            buffer = ImageIO.read(rollImg);
            int rollHeight = buffer.getHeight();
            // 添加htm样式
            if ("y".equals(hundred)) {
                sb.append("#con-" + rollTwoInt + "{ background-image: url(img0.jiameng.com/" + picStr + "); height: " + bgHeight + "px; background-size:100% 100%; }\n");
                sb.append("#con-" + rollTwoInt + " iframe { width: 90.5%; height: " + rollHeight + "px; padding-top: 50px; margin-left: 10.5%; }\n");
            } else {
                sb.append("#con-" + rollTwoInt + "{ background-image: url(img0.jiameng.com/" + picStr + "); height: " + bgHeight + "px; }\n");
                sb.append("#con-" + rollTwoInt + " iframe { width: 1820px; height: " + rollHeight + "px; padding-top: 50px; margin-left: 20px; }\n");
            }
            // 生成滚动html
            StringBuilder rollSb = new StringBuilder();
            rollSb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
            rollSb.append("<head>\n");
            rollSb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n");
            rollSb.append("<title>无标题文档</title>\n");
            rollSb.append("<style>\n");
            rollSb.append("* {border: 0; padding: 0; margin: 0;}\n");
            rollSb.append("table{border-spacing:0;}\n");
            rollSb.append("img {display: block; margin: 0 3px; height: " + rollHeight + "px;}\n");
            rollSb.append("</style>\n");
            rollSb.append("</head>\n");
            rollSb.append("<body>\n");
            rollSb.append("<div id=\"demo\" style=\"width:1920px; height:" + rollHeight + "px; overflow:hidden\">\n");
            rollSb.append("<table cellpadding=\"0\" width=\"100%\" align=\"left\" cellspacing=\"0\" border=\"0\">\n");
            rollSb.append("<tbody><tr><td id=\"demo1\" valign=\"top\"><table cellspacing=\"0\" cellpadding=\"0\" width=\"100%\" align=\"center\" border=\"0\">\n");
            rollSb.append("<tbody><tr>\n");
            for (File f : rollImages) {
                rollSb.append("<td align=\"middle\"><img src=\"" + f.getName() + "\" alt=\"\"/></td>\n");
            }
            rollSb.append("</tr></tbody></table></td><td id=\"demo2\" valign=\"center\"></td></tr></tbody></table>\n");
            rollSb.append("</div>\n");
            rollSb.append("<script>\n");
            rollSb.append("var speed=15\n");
            rollSb.append("demo2.innerHTML=demo1.innerHTML\n");
            rollSb.append("function Marquee110(){\n");
            rollSb.append("if(demo2.offsetWidth-demo.scrollLeft<=0)\n");
            rollSb.append("demo.scrollLeft-=demo1.offsetWidth\n");
            rollSb.append("else{demo.scrollLeft++}}\n");
            rollSb.append("var MyMar10=setInterval(Marquee110,speed)\n");
            rollSb.append("demo.onmouseover=function() {clearInterval(MyMar10)}\n");
            rollSb.append("demo.onmouseout=function() {MyMar10=setInterval(Marquee110,speed)}\n");
            rollSb.append("</script>\n");
            rollSb.append("</body>\n");
            rollSb.append("</html>\n");
            printStream = new PrintStream(new FileOutputStream(webFile.getAbsolutePath() + "/img0.jiameng.com/roll_two/scroll.html"));
            printStream.println(rollSb);
        }
        sb.append("</style>\n");
        sb.append("<div class=\"wrapper\">\n");
        String index;
        for (int i = 1; i <= imgNum; ++i) {
            index = i < 10 ? "0" + i : "" + i;
            if (bgFlag) {
                sb.append("<div style=\"background-image:url(img0.jiameng.com/b_" + index + ".jpg);\">");
                if (rollOneInt > 0 && rollOneInt == i) {
                    sb.append("\n<div id=\"con-" + rollOneInt + "\">\n");
                    sb.append("<iframe scrolling=\"no\" frameborder=\"0\" allowtransparency=\"true\" src=\"img0.jiameng.com/roll_one/scroll.html\"></iframe>\n");
                    sb.append("</div>\n");
                } else if (rollTwoInt > 0 && rollTwoInt == i) {
                    sb.append("\n<div id=\"con-" + rollTwoInt + "\">\n");
                    sb.append("<iframe scrolling=\"no\" frameborder=\"0\" allowtransparency=\"true\" src=\"img0.jiameng.com/roll_two/scroll.html\"></iframe>\n");
                    sb.append("</div>\n");
                } else {
                    sb.append("<img src=\"img0.jiameng.com/p_" + index + ".jpg\" alt=\"\">");
                }
                sb.append("</div>\n");
            } else {
                sb.append("<div>");
                if (rollOneInt > 0 && rollOneInt == i) {
                    sb.append("\n<div id=\"con-" + rollOneInt + "\">\n");
                    sb.append("<iframe scrolling=\"no\" frameborder=\"0\" allowtransparency=\"true\" src=\"img0.jiameng.com/roll_one/scroll.html\"></iframe>\n");
                    sb.append("</div>\n");
                } else if (rollTwoInt > 0 && rollTwoInt == i) {
                    sb.append("\n<div id=\"con-" + rollTwoInt + "\">\n");
                    sb.append("<iframe scrolling=\"no\" frameborder=\"0\" allowtransparency=\"true\" src=\"img0.jiameng.com/roll_two/scroll.html\"></iframe>\n");
                    sb.append("</div>\n");
                } else {
                    sb.append("<img src=\"img0.jiameng.com/" + imgFiles[0].getName().split("_")[0] + "_" + index + ".jpg\">");
                }
                sb.append("</div>\n");
            }
        }
        sb.append("</div>\n");
        printStream = new PrintStream(new FileOutputStream(webFile.getAbsolutePath() + "/vip.htm"));
        printStream.println(sb);
        printStream.close();
        if (rollOneInt > 0) {
            System.out.println("滚动位置需手动调整!");
            System.out.println("滚动位置需手动调整!");
            System.out.println("滚动位置需手动调整!");
        }
        exitSystem("##### 谢谢使用! #####");
    }


    private static void exitSystem(String message) throws InterruptedException {
        System.out.println(message);
        System.out.println("3秒后自动退出程序!");
        Thread.sleep(3000L);
        System.exit(0);
    }

    /**
     * 删除指定文件夹下的全部内容
     *
     * @param file
     */
    public static void deleteAll(File file) {
        File[] files = file.listFiles();//将file子目录及子文件放进文件数组
        if (files != null) {//如果包含文件进行删除操作
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {//删除子文件
                    files[i].delete();
                } else if (files[i].isDirectory()) {//通过递归方法删除子目录的文件
                    deleteAll(files[i]);
                }
                files[i].delete();//删除子目录
            }
        }
    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public static void copyFolder(String oldPath, String newPath) {
        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }
                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();
        }
    }


}
