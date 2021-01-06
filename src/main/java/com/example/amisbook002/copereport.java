package com.example.amisbook002;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//本类用于Selenium在运行过程中产生HTML报表输出，用于快速浏览程序运行后的结果

/*
 * 用户其实最终就到两个方法:
 * recordResult(Object[] caseID, boolean pass, String strDescrip) - 每次执行测试后生成的记录，并临时记录
 * generateReport(String startTime, String endTime) - 最终产生的报告
 */
public class copereport {

    //public static final String reportFileName = "report.html";
    //加上文件名称12
    public static String reportFileName = "report.html";
    public static String myreport = "\\report\\";
    public static String ROOT_DIR = "D:\\testdata\\amisbook002\\amisrobot";
    private static int totalCase = 0;
    private static int passedCase = 0;
    private static int failedCase = 0;
    private static String caseStatus = "";
    private static final File report = createReport();
    private static final File reportTmp = createReportTmp();

    public static String getDuration(String startTime, String endTime) {
        String duration = "0";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date start = formatter.parse(startTime);
            Date end = formatter.parse(endTime);
            duration = String.valueOf((end.getTime() - start.getTime()) / 1000);
        } catch (Exception e) {

        }
        return duration + " Seconds";
    }

    public static String getCurrentTime() {
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String datetime = formatter.format(date.getTime());
        return datetime;
    }

    private static File createReport() {
        //获取当前系统名称12424
        String mySystem = System.getProperties().getProperty("os.name");
        System.out.println("===========os.name:" + mySystem);
        if (mySystem.contains("Windows")) {
            reportFileName = "report.html";
            myreport = "\\report\\";
            ROOT_DIR = "D:\\testdata\\github\\amisbook\\amisrobot";
            System.out.println("当前在121:" + mySystem + " 系统操作");
            //源文件路径
            File startFile = new File("D:\\testdata\\github\\amisbook\\amisrobot\\report\\report.html");

            //目的目录路径
            File endDirection = new File("D:\\testdata\\github\\amisbook\\amisrobot\\report\\reporthistory");
            //如果目的目录路径不存在，则进行创建
            if (!endDirection.exists()) {
                endDirection.mkdirs();
            }
            String ss = startFile.getName();
            //目的文件路径=目的目录路径+源文件名称
            File endFile = new File(endDirection + File.separator + startFile.getName());

            try {
                //调用File类的核心方法renameTo
                if (startFile.renameTo(endFile)) {
                    System.out.println("文件移动成功！目标路径：{" + endFile.getAbsolutePath() + "}");
                } else {
                    System.out.println("文件移动失败！起始路径：{" + startFile.getAbsolutePath() + "}");
                }
            } catch (Exception e) {
                System.out.println("文件移动出现异常！起始路径：{" + startFile.getAbsolutePath() + "}");
            }
        } else if (mySystem.contains("Linux")) {
            reportFileName = "linuxreport.html";
            myreport = "/linuxreport/";
            //ROOT_DIR = "/amisbook/amis/amisrobot";
            ROOT_DIR = "/root/.jenkins/workspace/amisbook002/amisrobot";
            System.out.println("当前在121:" + mySystem + " 系统操作");
        } else {
            System.out.println("当前在121:" + mySystem + " 系统操作；且当前不支持该操作系统");
        }
        File report;
        //String path = ROOT_DIR +"\\report\\";
        String path = ROOT_DIR + myreport;
        String file = reportFileName;
        report = new File(path + file);
        if (!report.exists()) {
            try {
                report.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return report;
    }

    private static File createReportTmp() {
        File reportTmp;
        //String path = ROOT_DIR + "\\report\\";
        String path = ROOT_DIR + myreport;
        String file = reportFileName + ".tmp";
        //String file = reportFileName;
        reportTmp = new File(path + file);
        if (!reportTmp.exists()) {
            try {
                reportTmp.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return reportTmp;
    }

    public static File getReport() {
        return report;
    }

    private static File getReportTmp() {
        return reportTmp;
    }

    private static String tagHtml(String str) {
        String s, tagBegin, tagEnd;
        tagBegin = "<HTML>" + "\n";
        tagEnd = "</HTML>" + "\n";
        s = tagBegin + str + tagEnd;
        return s;
    }

    private static String tagHead(String strTag) {
        String s, tagBegin, tagEnd;
        tagBegin = "<HEAD language=\"java\" pageEncoding=\"UTF-8\" charset='utf-8'>" + "\n";
        //<%@ page language="java" pageEncoding="UTF-8"%>
        tagEnd = "</HEAD>" + "\n";
        s = tagBegin + strTag + tagEnd;
        return s;
    }

    private static String tagTitle(String strTitle) {
        return "<TITLE>" + strTitle + "</TITLE>" + "\n";
    }

    private static String tagBody(String strBody) {
        String s, tagBegin, tagEnd;
        tagBegin = "<BODY>" + "\n";
        tagEnd = "</BODY>" + "\n";
        s = tagBegin + strBody + tagEnd;
        return s;
    }

    private static String tagTable(String strTag, int width) {
        String s, tagBegin, tagEnd;
        tagBegin = "<TABLE align= center style='table-layout:fixed;WORD-BREAK:break-all;WORD-WRAP:break-word' width=" + width + " border=0 cellpadding=4 cellspacing=1 bgcolor=#000000>\n";
        tagEnd = "</TABLE>" + "\n";
        s = tagBegin + strTag + tagEnd;
        return s;
    }

    private static String tagTr(String strTag, String bgColor) {
        String s, tagBegin, tagEnd;
        tagBegin = "<TR bgcolor=\"" + bgColor + "\">" + "\n";
        tagEnd = "</TR>" + "\n";
        s = tagBegin + strTag + tagEnd;
        return s;
    }

    private static String tagTd(String strTag, String align, String bgColor, String width) {
        String s, tagBegin, tagEnd;
        tagBegin = "<TD charset='utf-8' align=\"" + align + "\" bgcolor=\"" + bgColor + "\" width=" + width + ">" + "\n";
        tagEnd = "</TD>" + "\n";
        s = tagBegin + strTag + tagEnd;
        return s;
    }

    private static String tagFont(String strTag, String color, int size, String face) throws UnsupportedEncodingException {
        String s, tagBegin, tagEnd;
        tagBegin = "<FONT color=\"" + color + "\" size=" + size + " face=\"" + face + "\">";
        tagEnd = "</FONT>" + "\n";

        String strTags = new String(strTag.getBytes("UTF-8"), "UTF-8");
        s = tagBegin + strTags + tagEnd;
        return s;
    }

    private static void writeFile(String strAdd, File file) {
        try {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(strAdd + "\n");
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String insertResultTable() throws UnsupportedEncodingException {
        File reportTmp = getReportTmp();
        String title, results, resultTable;
        String[] strArray = {"开始时间", "用例编号", "是否通过", "响应"};
        StringBuffer buffer = new StringBuffer();
        BufferedReader br;

        for (int i = 0; i < 4; i++) {
            String width = "";
            // Set the width for each column in the table
            switch (i) {
                case 0:
                    width = "20%";
                    break;
                case 1:
                    width = "10%";
                    break;
                case 2:
                    width = "10%";
                    break;
                case 3:
                    width = "60%";
                    break;
            }
            strArray[i] = tagFont(strArray[i], "white", 3, "Arial");
            strArray[i] = tagTd(strArray[i], "center", "navy", width);
        }
        title = tagTr(strArray[0] + strArray[1] + strArray[2] + strArray[3], "navy");
        //System.out.println("title1 == ="+title);
        try {
            // Read all the records to a StringBuffer object from report temp file
            br = new BufferedReader(new FileReader(reportTmp));
            while ((results = br.readLine()) != null) {
                buffer.append(results);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        results = new String(buffer);

        resultTable = tagTable(title + results, 960) + "<BR>";
        return resultTable;
    }

    //    /**
//     * Insert the statistic table to the runtime report
//     *
//     * @param int caseNum - The actual number of TA test cases which are needed to run
//     * @param String start - Start-time
//     * @param long startTime - - Start-time for accounting "execute time"
//     * @param String end - End-time
//     * @param long endTime - - Start-time for accounting "execute time"
//     * @return String
//     */
    private static String insertStatisticTable(int caseNum, String startTime, String endTime) throws UnsupportedEncodingException {
        String testingResult, title, result;
        String[] titleArray = {"开始时间", "结束时间", "持续时间", "状态", "所有用例", "成功用例", "失败用例"};
        String[] resultArray = new String[7];

        for (int i = 0; i < 7; i++) {
            String width;
            if (i < 3) {
                width = "18%";
            } else {
                width = "11.5%";
            }
            titleArray[i] = tagFont("" + titleArray[i], "white", 3, "Arial");
            titleArray[i] = tagTd(titleArray[i], "center", "navy", width);
        }
        title = titleArray[0] + titleArray[1] + titleArray[2] +
                titleArray[3] + titleArray[4] + titleArray[5] + titleArray[6];
        //System.out.println("title2 == ="+title);
        for (int i = 0; i < 7; i++) {
            String width;
            String color;
            if (i < 3) {
                width = "18%";
            } else {
                width = "11.5%";
            }
            if (i == 6) {
                color = "red";
            } else {
                color = "black";
            }
            // Insert statistic results
            switch (i) {
                case 0:
                    resultArray[i] = startTime;
                    break;
                case 1:
                    resultArray[i] = endTime;
                    break;
                case 2:
                    resultArray[i] = getDuration(startTime, endTime);
                    break;
                case 3:
                    resultArray[i] = caseStatus;
                    break;
                case 4:
                    resultArray[i] = String.valueOf(caseNum);//String.valueOf(totalCase);
                    break;
                case 5:
                    resultArray[i] = String.valueOf(passedCase);
                    break;
                case 6:
                    resultArray[i] = String.valueOf(failedCase);
                    break;
                default:
                    break;
            }
            resultArray[i] = tagFont(resultArray[i], color, 2, "Arial");
            resultArray[i] = tagTd(resultArray[i], "center", "white", width);
        }
        result = resultArray[0] + resultArray[1] + resultArray[2] +
                resultArray[3] + resultArray[4] + resultArray[5] + resultArray[6];
        //System.out.println("result == ="+result);
        testingResult = tagTable(tagTr(title, "navy") + tagTr(result, "white"), 960) + "<BR>";
        return testingResult;
    }


    //    /**
//     * Record a row of results to the temparory file.
//     *
//     * @param String[] caseID - caseID[0] is TA test case ID and caseID[1] is Manual test cases ID
//     * @param boolean pass - the testing result. true for "Pass" and false for "Fail".
//     * @param String strDescrip - the descriptions for the testing results
//     * @return null
//     */
    //记录结果
    public static void recordResult(Object[] caseID, boolean pass, String strDescrip) throws UnsupportedEncodingException {
        File reportTmp = getReportTmp();
        String record;
        String time = Calendar.getInstance().getTime().toLocaleString();
        //System.out.println("html响应："+strDescrip);
        //String times = new String(time.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        String result, color, bgColor, colorResult;
        String font = "Arial";
        int size = 2;
        //long totalCase, passedCase, failedCase;
        String[] strArray = new String[4];

        color = "black";

        if (pass) {
            result = "Pass";
            colorResult = "black";
            bgColor = "white";
            passedCase++;
        } else {
            result = "Fail";
            colorResult = "red";
            bgColor = "white";
            failedCase++;
        }
        strArray[0] = tagFont(time, color, size, font);
        strArray[1] = tagFont(caseID[0].toString(), color, size, font);
        strArray[2] = tagFont(result, colorResult, size, font);
        strArray[3] = tagFont(strDescrip, color, size, font);
        for (int i = 0; i < 4; i++) {
            String align = "";
            String width = "";
            // Set align style for each column
            switch (i) {
                case 0:
                    align = "center";
                    width = "18%";
                    break;
                case 1:
                    align = "left";
                    width = "25%";
                    break;
                case 2:
                    align = "center";
                    width = "11%";
                    break;
                case 3:
                    align = "left";
                    width = "46%";
                    break;
            }
            // Set width for each column

            strArray[i] = tagTd(strArray[i], align, bgColor, width);
        }
        record = tagTr(strArray[0] + strArray[1] + strArray[2] + strArray[3], bgColor);
        writeFile(record, reportTmp); // Add the record to the runtime report temp file

        totalCase = passedCase + failedCase;
    }

    //    /**
//     * Generate a HTML runtime report
//     *
//     * @param int caseNum - The actual number of TA test cases which are needed to run
//     * @param String start - Start-time
//     * @param long startTime - - Start-time for accounting "execute time"
//     * @param String end - End-time
//     * @param long endTime - - Start-time for accounting "execute time"
//     * @return null
//     */
    //生成报告
    public static void generateReport(String startTime, String endTime) throws UnsupportedEncodingException {
        File report = getReport();
        String title = tagTitle("MEAutomation Report");
        String resultTable, statisticTable, body, head, record;
        caseStatus = "Finished";

        resultTable = insertResultTable();
        statisticTable = insertStatisticTable(totalCase, startTime, endTime);

        head = tagHead(title);
        body = tagBody(resultTable + statisticTable);
        record = tagHtml(head + body);

        writeFile(record, report); // Generate HTML runtime report
        reportTmp.deleteOnExit(); // Delete report temp file
    }
}


