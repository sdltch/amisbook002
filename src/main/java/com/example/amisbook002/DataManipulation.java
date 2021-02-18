package com.example.amisbook002;
import com.alibaba.fastjson.JSONObject;
import com.example.operation.ImportResponse;
import com.example.operation.PutRequest;
import org.apache.commons.codec.binary.Base64;
import org.springframework.context.annotation.Import;

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
                ImportResponse importResponse = new ImportResponse();
                PutRequest putRequest = new PutRequest();
                //根据method判定调用方法
                if (method.equals("POST")) {
                    System.out.println("当前请求：post");
                    //myrequest不等于空
                    if (!myrequest.equals("null")) {
                        data = putRequest.myrequest(data, myrequest, this.myjsonmap);
                    }
                    System.out.println("转化后data：" + data);
                    //对登录接口的密码进行加密
//                    if (line[1].equals("login")){
//                        data = rsapassword(data);
//                        System.out.println("加密data："+data);
//                    }
                    response = ss.doPost(url, data);
                    System.out.println("当前请求：" + method);
                }else if(method.equals("POSTFROM")){
                    System.out.println("当前请求：postfrom" );
                    //myrequest不等于空
                    if (!myrequest.equals("")) {
                        data = putRequest.myrequest(data, myrequest, this.myjsonmap);
                    }
                    System.out.println("转化后data：" + data);
                    response = ss.doPostFrom(url, data);
                } else if (method.equals("GET")) {
                    System.out.println("当前请求：get" );
                    response = ss.doGet(url);
                } else if (method.equals("SENDPOST")){
                    System.out.println("当前请求：SENDPOST" );
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
                    System.out.println("(系统暂时只支持get,post,sendpost请求):当前请求：" + method);
                    break;
                }
                //System.out.println("response------"+response);
                String body = response.getBody();
                System.out.println(line[0]+"响应："+body);
                //获取响应字段，并放入map集合
                if(!myresponse.equals("") && !body.equals("")){
                    System.out.println("myresponse和body不为空：");
                    this.myjsonmap =importResponse.myresponse(body,myresponse,this.myjsonmap);
                }
                System.out.println("body实际为=====" + body);
                //调用断言
                MyAssert.assertIt(caseID, body, expected,interfaceUrl,noteCase);
            }
        }
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
