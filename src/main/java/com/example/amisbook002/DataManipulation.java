package com.example.amisbook002;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    public void start() throws Exception {
        Response response = null;
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
            for (String[] line : readExcels) {
                //编号
                String caseID = line[0];
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
                //根据method判定调用方法
                if (method.equals("POST")) {
                    data = myrequest(data, myrequest);
                    System.out.println("转化后data："+data);
                    //对登录接口的密码进行加密
                    if (line[1].equals("login")){
                        data = rsapassword(data);
                        System.out.println("加密data："+data);
                    }
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
                    //continue;
                }
                //System.out.println("response------"+response);
                String body = response.getBody();
                System.out.println(line[0]+"响应："+body);
                //获取响应字段，并放入map集合
                if(!myresponse.equals("") && !body.equals("")){
                    System.out.println("myresponse和body不为空：");
                    this.myresponse(body,myresponse);
                }
                System.out.println("body实际为=====" + body);
                //调用断言
                MyAssert.assertIt(caseID, body, expected,interfaceUrl,noteCase);
            }
        }
    }
    //判断excel中response中是否有值，并对response中的值进行处理
    public void myresponse(String body,String myresponse) {
        //将body转化成JSONObject
        JSONObject jsonObject = JSONObject.parseObject(body);
        if (myresponse.contains("=")) {
            System.out.println("myresponse有等号：");
            if(myresponse.contains(";")){
                System.out.println("myresponse有分号：" +myresponse);
                String[] splitone = myresponse.split(";");
                for(int i = 0;i<splitone.length;i++){
                    String myresponseone = splitone[i];
                    String[] splittwo = myresponseone.split("=");
                    responseGinseng(jsonObject,splittwo,myresponse);
                }
            }else {
                System.out.println("myresponse没有分号："+myresponse);
                String[] splitthree = myresponse.split("=");
                responseGinseng(jsonObject,splitthree,myresponse);
            }
        }
    }
    //将excel表中response数据用“；”和“=”分割后，将响应的某个字段存入map中
    public void responseGinseng(JSONObject jsonObject,String[] countsone,String myrespones){
        if (countsone.length == 2){
            String sponseone = countsone[0];
            String sponsetwo = countsone[1];
            //通过Key：s，获取value：值
            String valueone = jsonObject.getString(sponseone);
            this.myjsonmap.put(sponsetwo, valueone);
            System.out.println("mapjih12:"+this.myjsonmap.get(sponsetwo));
        }else if(countsone.length == 3){
            String sponseone = countsone[0];
            String sponsetwo = countsone[1];
            String sponsethree = countsone[2];
            //通过Key：s，获取value：值
            String value = jsonObject.getString(sponseone);
            System.out.println("value实际为=====" + value);
            //将value转化成JSONObject
            JSONObject jsonvalue = JSONObject.parseObject(value);
            //通过Key：s1，获取value：值,并附值
            System.out.println("jsonvalues实际为=====" + jsonvalue.getString(sponsetwo));
            String valuetwo = jsonvalue.getString(sponsetwo);
            myjsonmap.put(sponsethree, valuetwo);
            System.out.println("mapjih13:"+myjsonmap.get(sponsethree));
        }else if (countsone.length == 4){
            String sponseone = countsone[0];
            String sponsetwo = countsone[1];
            String sponsethree = countsone[2];
            String sponsefour = countsone[3];
            String value = jsonObject.getString(sponseone);
            JSONObject jsonObjectone = JSONObject.parseObject(value);
            String valueone = jsonObjectone.getString(sponsetwo);
            JSONObject jsonObjecttwo = JSONObject.parseObject(valueone);
            String valuetwo = jsonObjecttwo.getString(sponsethree);
            myjsonmap.put(sponsefour, valuetwo);
        }else {
            System.out.println("excel中的Respones不支持改格式"+myrespones);
        }
    }

    //判断request中是否有值，并通过条件对data中的值进行处理
    public String myrequest(String data,String myrequest) {
        String ss = data;
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
                    String[] splitones = s.split("=");
                    System.out.println("request长度:" + splitones.length);
                    jsonObject = requestGinseng(splitones, jsonObject,myrequest);
                }
            } else {
                String[] requestspone = myrequest.split("=");
                System.out.println("request长度:" + requestspone.length);
                jsonObject = requestGinseng(requestspone, jsonObject,myrequest);
            }
            ss=JSONObject.toJSONString(jsonObject);
        }
        return ss;
    }
    //通过传入的counts数组，判断长度，
    public JSONObject requestGinseng(String[] counts,JSONObject jsonObject,String myrequest){
        String myrequestsss = null;
        if (counts.length == 2){
            String questone = counts[0];
            String questtwo = counts[1];
            System.out.println("questone:" + questone);
            System.out.println("questtwo:" + questtwo);
            System.out.println("myjsonmap.get(questtwo):" + myjsonmap.get(questtwo));
            //更换tonken
            jsonObject.put(questone, myjsonmap.get(questtwo));
            //转化成Map<String, String>
            //Map<String, String> jsonMap = JSONObject.toJavaObject(jsonObject, Map.class);
            //转化成String
//            ss = JSONObject.toJSONString(jsonObject);
//            System.out.println("data转1222化后："+ss);
        }else if (counts.length == 3) {
            String requestsone = counts[0];
            String requeststwo = counts[1];
            String requeststhree = counts[2];
            //通过第一个数据获取data中对应的数据
            String requestObj = jsonObject.getString(requestsone);
            //转化成JSONObject类型
            JSONObject jsonvalue = JSONObject.parseObject(requestObj);
            //更改requesttwo的数据
            jsonvalue.put(requeststwo, myjsonmap.get(requeststhree));
            //转化成String
            myrequestsss = JSONObject.toJSONString(jsonvalue);
            //将data中的requesttwo数据更改为myrequestsss
            jsonObject.put(requestsone, myrequestsss);
        }else if (counts.length == 4){
            String requestsone = counts[0];
            String requeststwo = counts[1];
            String requeststhree = counts[2];
            String requestsfour = counts[3];
            //通过第一个数据获取data中对应的数据
            String requestObjone = jsonObject.getString(requestsone);
            JSONObject jsonObjectone = JSONObject.parseObject(requestObjone);
            String requestObjtwo = jsonObjectone.getString(requeststwo);
            JSONObject jsonObjecttwo = JSONObject.parseObject(requestObjtwo);
            //更改requesttwo的数据
            jsonObjecttwo.put(requeststhree, myjsonmap.get(requestsfour));
            String s = JSONObject.toJSONString(jsonObjecttwo);
            jsonObjectone.put(requeststwo,s);
            String s1 = JSONObject.toJSONString(jsonObjectone);
            jsonObject.put(requestsone,s1);
        }else{
            System.out.println("excel中的Request不支持改格式"+myrequest);
        }
        return jsonObject;
    }
    //密码加密
    public String rsapassword(String data){
        JSONObject jsonObject = JSONObject.parseObject(data);
        //通过key获取密码
        String passwordone = jsonObject.getString("password");
        //将密码加密
        decryptByPublicKey decryptByPublicKey = new decryptByPublicKey();
        String result ="";
        //获取密钥
        try {
            decryptByPublicKey decryp = new decryptByPublicKey();
            String results = Base64.encodeBase64String(decryp.encryptByPublicKey(
                    passwordone.getBytes(), myjsonmap.get("getRsaPublicKey")));
            result = "{RSA}" +results;
            System.out.println("rsa加密后："+result);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        jsonObject.put("password", result);
        String rsapassword = JSONObject.toJSONString(jsonObject);
        return rsapassword;
    }

}
