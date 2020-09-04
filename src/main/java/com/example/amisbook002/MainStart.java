package com.example.amisbook002;

import com.alibaba.fastjson.JSONObject;
import org.testng.annotations.Test;

import java.io.File;
import java.util.*;

public class MainStart {
    String mytonken=null;
    public static void main(String[] args) throws Exception {
        String startTime = Report.getCurrentTime();
        MainStart mainStart = new MainStart();
        mainStart.start();
        String endTime = Report.getCurrentTime();
        Report.generateReport(startTime, endTime);
//        while (true) {//获取当前时间
//            String currentTime = Report.getCurrentTime();
//            System.out.println("北京时间：" + currentTime);
//            //判断当前时间是否包含设定时间
//            if (currentTime.contains("14:31:")) {
//                String startTime = Report.getCurrentTime();
//                MainStart mainStart = new MainStart();
//                mainStart.start();
//                String endTime = Report.getCurrentTime();
//                Report.generateReport(startTime, endTime);
//                break;
//            }
//            Thread.sleep(1000);
//        }
    }

    //循环获取数据，并根据获取的get、post、sendpost调用不同的方法；
    //对接口返回结果进行断言
@Test
    public void start() throws Exception {

        Response response = null;

        ArrayList<String[]> readExcel = Excel.judgeExcel();
        //ArrayList<String[]> readExcel = Excel.readExcelx();
        if(readExcel != null){
            //去掉第1\2行
            readExcel.remove(0);
            readExcel.remove(0);
            //域名
            String[] readexcel = readExcel.get(0);
            String myurl = readexcel[7];
            System.out.println("域名："+myurl);
            //循环取值
            for (String[] line : readExcel) {
                //编号
                String caseID = line[0];
                System.out.println("编号："+line[0]);
                //方法
                String method = line[2];
                //地址
                String interfaceUrl = line[3];
                String url = myurl+interfaceUrl;
                //String url = line[3];
                System.out.println("：url路径："+url);
                //参数
                String data = line[4];
                //断言
                String expected = line[5];
                //文件路径
                String refile = line[6];
                System.out.println("：文件路径："+refile);
                //
                String myrequest = line[8];
                System.out.println("myrequest------"+myrequest);
                //获取响应数据
                String myresponse = line[9];
                System.out.println("myresponse------"+myresponse);
                //获取响应值
                //实例化对象		Request
                Request ss = new Request();
                Map<String, File> requestFiles = new HashMap<String, File>();
                JosnConversionMap josnConversionMap = new JosnConversionMap();
                //根据method判定调用方法
                if (method.equals("POST")) {
                    data = myrequest(data, myrequest);
                    System.out.println("转化后data："+data);
                    response = ss.doPost(url, data);
                    System.out.println("当前请求：" + method);
                } else if (method.equals("GET")) {
                    response = ss.doGet(url);
                    System.out.println("当前请求：" + method);
                } else if (method.equals("SENDPOST")){
                    //文件file，将文件路径转化成可识别文件路径
                    String location = refile.replaceAll("\\\\","//");
                    System.out.println("：location文件路径："+location);
                    requestFiles.put("file", new File(location));
                    //参数转化Map<String,String>
                    Map<String, String> jsonmap = josnConversionMap.jsonmap(data);
                    //调用Response的sendpost方法
                    System.out.println("当前请求：" + method);
                    response = ss.sendpost(url,jsonmap,requestFiles);
                } else {
                    System.out.println("请求错误(系统暂时只支持get,post,sendpost请求):当前请求：" + method);
                    break;
                }
                //System.out.println("response------"+response);
                String bodys = response.getBody();
                String body = new String(bodys.getBytes("utf-8"),"utf-8");
                //将body通过“=”进行分割，取出响应值（获取tonken)
                if(myresponse.contains("=")){
                    String[] responsesp = myresponse.split("=");
                    System.out.println("resoonp长:"+responsesp.length);
                    String sponseone = responsesp[0];
                    String sponsetwo = responsesp[1];
                    System.out.println("response-s-----"+sponseone);
                    System.out.println("response-s1-----"+sponsetwo);
                    //将body转化成JSONObject
                    JSONObject jsonObject = JSONObject.parseObject(body);
                    System.out.println("jsonObject实际为=====" + jsonObject.toString());
                    //通过Key：s，获取value：值
                    String value= jsonObject.getString(sponseone);
                    System.out.println("value实际为=====" + value);
                    //将value转化成JSONObject
                    JSONObject jsonvalue = JSONObject.parseObject(value);
                    //通过Key：s1，获取value：值,并附值
                    this.mytonken = jsonvalue.getString(sponsetwo);
                    System.out.println("实际为tonken=====" +this.mytonken);
                }
                System.out.println("body实际为=====" + body);
                //调用断言
                MyAssert.assertIt(caseID, body, expected,interfaceUrl);

            }

        }
    }
    //判断request中是否有值，并通过条件对data中的值进行处理
    public String  myrequest(String data,String myrequest) {
        String ss = data;
        String s1 = null;
        //将data转化成JSONObject
        System.out.println("data转化前："+ss);
        JSONObject jsonObject = JSONObject.parseObject(data);
        //如果有值，将替换request中某个key的值
        ArrayList<String> list = new ArrayList<String>();
        //判断是否存在=
        if (myrequest.contains("=")) {
            //判断是否存在；
            if (myrequest.contains(";")) {
                //以；进行分割
                String[] requestsp = myrequest.split(";");
                for (int i = 0; i < requestsp.length; i++) {
                    String s = requestsp[i];
                    //以=进行分割
                    String[] splitone = s.split("=");
                    if (i == 0) {
                        //将第一行单独处理
                        s1 = splitone[0];
                        System.out.println("下标为:" + i + "，实际为requestkey=====" + s1);
                    } else {
                        String s2 = splitone[0];
                        //将第二行及以后数据放入list集合
                        list.add(s2);
                        System.out.println("下标为:" + i + "，实际为requestkey=====" + s2);
                    }
                }
                //输出list
                for (int i = 0; i < list.size(); i++) {
                    System.out.println("list:" + i + ":" + list.get(i));
                }

            } else {
                String[] requestsp = myrequest.split("=");
                System.out.println("request长度:" + requestsp.length);
                String questone = requestsp[0];
                String questtwo = requestsp[1];
                //更换tonken
                jsonObject.put(questone,this.mytonken);
                //转化成Map<String, String>
                //Map<String, String> jsonMap = JSONObject.toJavaObject(jsonObject, Map.class);
                //转化成String
                ss = JSONObject.toJSONString(jsonObject);
                System.out.println("data转化后："+ss);
            }

        }
        return ss;
    }
}
