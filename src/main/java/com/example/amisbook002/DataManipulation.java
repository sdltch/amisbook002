package com.example.amisbook002;
import com.alibaba.fastjson.JSONObject;
import com.example.operation.ImportResponse;
import com.example.operation.PutRequest;
import org.apache.commons.codec.binary.Base64;
import org.springframework.context.annotation.Import;
import org.testng.annotations.Test;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
@Test
public class DataManipulation {
    /**
     *  循环获取数据，并根据获取的get、post、sendpost调用不同的方法；
     * 对接口返回结果进行断言
     */
    HashMap<String, String> myjsonmap = new HashMap<String, String>();
    static ArrayList<String[]>  readExcels = null;
    //域名/url/入参/响应
    static String loginId = "";
    static String myurlRpa = "";
    static String loginUrl = "";
    static String loginData = "";
    static String loginAssert = "";
    static String loginYype = "";
    static String RSA_PUB_KEY = "";

    public void start() throws Exception {
        Response response = null;
        String body = "";
        readExcels = Excel.judgeExcel();
        //ArrayList<String[]> readExcels = Excel.readExcelx();
        if(readExcels != null){
            //取第二行域名
            String[] readexcelone = readExcels.get(1);
            myurlRpa = readexcelone[1];
            System.out.println("域名："+myurlRpa);
            //取第五行登录数据
            String[] readexcelfive = readExcels.get(4);
            loginId = readexcelfive[0];
            loginYype = readexcelfive[2];
            loginUrl = readexcelfive[3];
            loginData = readexcelfive[4];
            loginAssert = readexcelfive[5];
            //删除前第几行
            int tag = 3;
            for(int i=0;i < tag; i++){
                readExcels.remove(0);
            }
            //实例化对象		Request
            Request ss = new Request();
            //循环取值
            mytest(readExcels);
//            for (String[] line : readExcels) {
//                //编号
//                String caseID = line[0];
//                System.out.println("开始----------------------------------------------------------");
//                System.out.println("编号："+line[0]);
//                //方法
//                String method = line[2];
//                //地址
//                String interfaceUrl = line[3];
//                String url = myurlRpa + interfaceUrl;
//                //String url = line[3];
//                System.out.println("：url路径："+url);
//                //参数
//                String data = line[4];
//                //断言
//                String expected = line[5];
//                //文件路径
//                String refile = line[6];
//                System.out.println("：文件路径："+refile);
//                //根据给定的值，更改请求参数
//                String myrequest = line[7];
//                System.out.println("myrequest------"+myrequest);
//                //根据给定的值，取响应值
//                String myresponse = line[8];
//                System.out.println("myresponse------"+myresponse);
//                //备注
//                String noteCase = line[9];
//                //获取响应值
//                Map<String, File> requestFiles = new HashMap<String, File>();
//                JosnConversionMap josnConversionMap = new JosnConversionMap();
//                ImportResponse importResponse = new ImportResponse();
//                PutRequest putRequest = new PutRequest();
//                //根据method判定调用方法
//                if (method.equals("POST")) {
//                    System.out.println("当前请求："+method);
//                    //myrequest不等于空
//                    if (!myrequest.equals("")) {
//                        data = putRequest.myrequest(data, myrequest, this.myjsonmap);
//                    }
////                    //对登录接口的密码进行加密
////                    if (line[1].equals("login")){
////                        data = rsaType(data, method);
////                        System.out.println("加密data："+data);
////                    }
//                    System.out.println("转化后data：" + data);
//                    response = ss.doPost(url, data, method);
//                    System.out.println("当前请求：" + method);
//                    body = response.getBody();
//                }else if(method.equals("POSTFROM")){
//                    System.out.println("当前请求："+method);
//                    //myrequest不等于空
//                    if (!myrequest.equals("")) {
//                        data = putRequest.myrequest(data, myrequest, this.myjsonmap);
//                        System.out.println("转化后data：" + data);
//                    }else{
//                        System.out.println("未转化data：" + data);
//                    }
//                    //对登录接口的密码进行加密
////                    if (line[1].equals("login")){
////                        System.out.println("加密data：");
////                        data = rsaType(data,method);
////                        System.out.println("加密后data："+data);
////                    }
//                    response = ss.doPost(url, data, method);
//                    body = response.getBody();
//                } else if (method.contains("GET")) {
//                    System.out.println("当前请求："+method);
//                    response = ss.doGet(url,data);
//                    body = response.getBody();
//                } else if (method.equals("SENDPOST")) {
//                    System.out.println("当前请求："+method);
//                    //文件file，将文件路径转化成可识别文件路径
//                    String location = refile.replaceAll("\\\\", "//");
//                    System.out.println("：location文件路径：" + location);
//                    requestFiles.put("file", new File(location));
//                    //参数转化Map<String,String>
//                    Map<String, String> jsonmap = josnConversionMap.jsonmap(data);
//                    //调用Response的sendpost方法
//                    System.out.println("当前请求：" + method);
//                    response = ss.sendpost(url, jsonmap, requestFiles);
//                    body = response.getBody();
//                } else if(method.equals("PUT")){
//                    System.out.println("当前请求："+method);
//                    //myrequest不等于空
//                    if (!myrequest.equals("")) {
//                        data = putRequest.myrequest(data, myrequest, this.myjsonmap);
//                    }
//                    System.out.println("转化后data：" + data);
//                    response = ss.doput(url, data);
//                    body = response.getBody();
//                } else if(method.equals("DELETE")){
//                    System.out.println("当前请求："+method);
//                    if (!myrequest.equals("")) {
//                        //替换
//                        data = setRequest(data, myrequest, this.myjsonmap);
//                        System.out.println("替换后data：" + data);
//                    }
//                    /**读取服务器返回过来的json字符串数据**/
//                    body = ss.deleteConnection(url,data);
//                    System.out.println("delete响应：" + body);
//                } else {
//                    System.out.println("(系统暂时只支持get,post,sendpost,put,delete请求):当前请求：" + method);
//                    break;
//                }
//                System.out.println("response------"+response.toString());
//                try {
//                    System.out.println(line[0]+"响应："+body);
//                    System.out.println(line[0]+"code："+response.getCode());
//                    //获取响应字段，并放入map集合
//                    if(!myresponse.equals("") && !body.contains("false")){
//                        System.out.println("myresponse和body不为空：");
//                        this.myjsonmap =importResponse.myresponse(body,myresponse,this.myjsonmap);
//                        if (myresponse.contains("getRsaPublicKey")){
//                            RSA_PUB_KEY=this.myjsonmap.get("getRsaPublicKey");
//                        }
//
//                    }
//                    System.out.println("body实际为=====" + body);
//                    //调用断言
////                    body = new String(body.getBytes("gbk"),"utf-8");
////                    noteCase = new String(noteCase.getBytes("gbk"),"utf-8");
//                    MyAssert.assertIt(caseID, body, expected,interfaceUrl,noteCase);
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
        }
    }

    public void mytest(ArrayList<String[]> readExcels) throws Exception {
        Response response = null;
        String body = "";
        //实例化对象		Request
        Request ss = new Request();
        for (String[] line : readExcels) {
            //编号
            String caseID = line[0];
            System.out.println("开始----------------------------------------------------------");
            System.out.println("编号："+line[0]);
            //方法
            String method = line[2];
            //地址
            String interfaceUrl = line[3];
            String url = myurlRpa + interfaceUrl;
            //String url = line[3];
            System.out.println("：url路径："+url);
            //参数
            String data = line[4];
            //断言
            String expected = line[5];
            //文件路径
            String refile = line[6];
            System.out.println("：文件路径："+refile);
            //根据给定的值，更改请求参数
            String myrequest = line[7];
            System.out.println("myrequest------"+myrequest);
            //根据给定的值，取响应值
            String myresponse = line[8];
            System.out.println("myresponse------"+myresponse);
            //备注
            String noteCase = line[9];
            //获取响应值
            Map<String, File> requestFiles = new HashMap<String, File>();
            JosnConversionMap josnConversionMap = new JosnConversionMap();
            ImportResponse importResponse = new ImportResponse();
            PutRequest putRequest = new PutRequest();
            //根据method判定调用方法
            if (method.equals("POST")) {
                System.out.println("当前请求："+method);
                //myrequest不等于空
                if (!myrequest.equals("")) {
                    data = putRequest.myrequest(data, myrequest, this.myjsonmap);
                }
//                    //对登录接口的密码进行加密
//                    if (line[1].equals("login")){
//                        data = rsaType(data, method);
//                        System.out.println("加密data："+data);
//                    }
                System.out.println("转化后data：" + data);
                response = ss.doPost(url, data, method);
                System.out.println("当前请求：" + method);
                body = response.getBody();
            }else if(method.equals("POSTFROM")){
                System.out.println("当前请求："+method);
                //myrequest不等于空
                if (!myrequest.equals("")) {
                    data = putRequest.myrequest(data, myrequest, this.myjsonmap);
                    System.out.println("转化后data：" + data);
                }else{
                    System.out.println("未转化data：" + data);
                }
                //对登录接口的密码进行加密
//                    if (line[1].equals("login")){
//                        System.out.println("加密data：");
//                        data = rsaType(data,method);
//                        System.out.println("加密后data："+data);
//                    }
                response = ss.doPost(url, data, method);
                body = response.getBody();
            } else if (method.contains("GET")) {
                System.out.println("当前请求："+method);
                response = ss.doGet(url,data);
                body = response.getBody();
            } else if (method.equals("SENDPOST")) {
                System.out.println("当前请求："+method);
                //文件file，将文件路径转化成可识别文件路径
                String location = refile.replaceAll("\\\\", "//");
                System.out.println("：location文件路径：" + location);
                requestFiles.put("file", new File(location));
                //参数转化Map<String,String>
                Map<String, String> jsonmap = josnConversionMap.jsonmap(data);
                //调用Response的sendpost方法
                System.out.println("当前请求：" + method);
                response = ss.sendpost(url, jsonmap, requestFiles);
                body = response.getBody();
            } else if(method.equals("PUT")){
                System.out.println("当前请求："+method);
                //myrequest不等于空
                if (!myrequest.equals("")) {
                    data = putRequest.myrequest(data, myrequest, this.myjsonmap);
                }
                System.out.println("转化后data：" + data);
                response = ss.doput(url, data);
                body = response.getBody();
            } else if(method.equals("DELETE")){
                System.out.println("当前请求："+method);
                if (!myrequest.equals("")) {
                    //替换
                    data = setRequest(data, myrequest, this.myjsonmap);
                    System.out.println("替换后data：" + data);
                }
                /**读取服务器返回过来的json字符串数据**/
                body = ss.deleteConnection(url,data);
                System.out.println("delete响应：" + body);
            } else {
                System.out.println("(系统暂时只支持get,post,sendpost,put,delete请求):当前请求：" + method);
                break;
            }
            System.out.println("response------"+response.toString());
            try {
                System.out.println(line[0]+"响应："+body);
                System.out.println(line[0]+"code："+response.getCode());
                //获取响应字段，并放入map集合
                if(!myresponse.equals("") && !body.contains("false")){
                    System.out.println("myresponse和body不为空：");
                    this.myjsonmap =importResponse.myresponse(body,myresponse,this.myjsonmap);
                    if (myresponse.contains("getRsaPublicKey")){
                        RSA_PUB_KEY=this.myjsonmap.get("getRsaPublicKey");
                    }

                }
                System.out.println("body实际为=====" + body);
                //调用断言
//                    body = new String(body.getBytes("gbk"),"utf-8");
//                    noteCase = new String(noteCase.getBytes("gbk"),"utf-8");
                MyAssert.assertIt(caseID, body, expected,interfaceUrl,noteCase);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    //判断请求方式
    public String rsaType(String data ,String myType){
        System.out.println("密码"+data);
        if (myType.equals("POST")){
            JSONObject jsonObject = JSONObject.parseObject(data);
            String passwordone = (String)jsonObject.get("password");
            System.out.println("密码"+passwordone);
            //加密密码
            String rsapasswords = rsapassword(passwordone);
            System.out.println("加密密码"+rsapasswords);
            //更改requesttwo的数据
            jsonObject.put("password", rsapasswords);
            //转化成String
            data = JSONObject.toJSONString(jsonObject);
        }else if (myType.equals("POSTFROM")){
            /* 找出指定的2个字符在 该字符串里面的 位置 */
            int strStartIndex = data.indexOf("&password=");
            int strEndIndex = data.indexOf("&grant_type=");

            /* index 为负数 即表示该字符串中 没有该字符 */
            if (strStartIndex < 0) {
                return "字符串 :---->" + data + "<---- 中不存在 " + "&password=" + ", 无法截取目标字符串";
            }
            if (strEndIndex < 0) {
                return "字符串 :---->" + data + "<---- 中不存在 " + "&grant_type=" + ", 无法截取目标字符串";
            }
            /* 开始截取 */
            String result = data.substring(strStartIndex, strEndIndex).substring("&password=".length());
            //加密密码
            String rsapasswords = rsapassword(result);
            System.out.println("加密密码"+rsapasswords);
            //替换加密后的密码
            data=data.replace(result,rsapasswords);
        }
        return data;
    }
    public String setRequest(String data,String myrequest,HashMap<String, String> myjsonmap) {
        //判断是否存在=
        if (myrequest.contains("=")) {
            //判断是否存在；
            if (myrequest.contains(";")) {
                //以；进行分割
                String[] requestsp = myrequest.split(";");
                for (int i = 0; i < requestsp.length; i++) {
                    String[] requestspone = requestsp[i].split("=");
                    System.out.println("request长度:" + requestspone.length);
                    System.out.println("requestid:" + myjsonmap.get(requestspone[1]));
                    if (data.contains(";")){
                        String[] datas = data.split(";");
                        for (int j = 0; j < datas.length; j++){
                            data=data.replace(datas[j].split("=")[1],myjsonmap.get(requestspone[1]));
                        }
                    }
                }
            } else {
                String[] requestspone = myrequest.split("=");
                System.out.println("request长度:" + requestspone.length);
                System.out.println("requestid:" + myjsonmap.get(requestspone[1]));
                data=data.replace(data.split("=")[1],myjsonmap.get(requestspone[1]));
            }
        }
        return data;
    }
    
    //密码加密
    public String rsapassword(String passwordone){
        //将密码加密
        decryptByPublicKey decryptByPublicKey = new decryptByPublicKey();
        System.out.println("密码"+passwordone);
        System.out.println("密码密钥"+myjsonmap.get("getRsaPublicKey"));
        String result ="";
        //获取密钥
        try {
            decryptByPublicKey decryp = new decryptByPublicKey();
            String results = Base64.encodeBase64String(decryp.encryptByPublicKey(
                    passwordone.getBytes(), myjsonmap.get("getRsaPublicKey")));
            result = URLEncoder.encode(results, "utf-8");//编码
            System.out.println("rsa加密后："+result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

}
