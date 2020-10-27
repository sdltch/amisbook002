package com.example.amisbook002;


import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;

public class MyAssert {
    //断言方法
    public static void assertIt(String caseID, String actual, String expected,
                 String interfaceUrl,String notecase) throws UnsupportedEncodingException {
        String[] split = expected.split("=");
        String key = split[0];
        String myExpected = split[1];
        String[] a = {caseID};
        //断言
        if (key.toUpperCase().equals("STRING")) {
            if (actual.contains(myExpected)) {
                System.out.println("STRING ++++++ Test Case Pass; 用例编号：" + caseID);
                Report.recordResult(a, true, actual,interfaceUrl,notecase);
            } else {
                System.out.println("STRING ++++++ Test Case Fail; 用例编号：" + caseID);
                Report.recordResult(a, false, actual,interfaceUrl,notecase);
            }
        } else if (key.toUpperCase().equals("TYPE")) {
            if (myExpected.equals("ISNUMBER")) {
                System.out.println("#######################" + actual);
                if (actual.matches("^[1-9][0-9]*$")) {
                    System.out.println("TYPE  @@@@@@@@Test Case Pass; 用例编号：" + caseID);
                    Report.recordResult(a, true, actual,interfaceUrl,notecase);
                } else {
                    System.out.println("TYPE  @@@@@@@@Test Case Fail; 用例编号：" + caseID);
                    Report.recordResult(a, false, actual,interfaceUrl,notecase);
                }
            } else if (myExpected.toUpperCase().equals("NOTNUMBER")) {
                if (!actual.matches("^[1-9][0-9]*$")) {
                    System.out.println("TYPE  ------Test Case Pass; 用例编号：" + caseID);
                    Report.recordResult(a, true, actual,interfaceUrl,notecase);
                } else {
                    System.out.println("TYPE  ------Test Case Fail; 用例编号：" + caseID);
                    Report.recordResult(a, false, actual,interfaceUrl,notecase);
                }
            }
        }
    }
}
