package com.example.operation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.amisbook002.DataManipulation;
import com.google.gson.internal.$Gson$Preconditions;
import sun.text.normalizer.Trie;

import java.util.Collections;
import java.util.HashMap;
/*
*将响应字段放入myjsonmap
* */
public class ImportResponse {
    //判断excel中response中是否有值，并对response中的值进行处理
    public HashMap<String, String> myresponse(String body,String myresponse,HashMap<String, String> myjsonmap) {
        //将body转化成JSONObject
        DataManipulation dataManipulation = new DataManipulation();
        JSONObject jsonObject = JSONObject.parseObject(body);
        if (myresponse.contains("=") && !myresponse.contains("[")) {
            System.out.println("myresponse有等号：");
            if (myresponse.contains(";")) {
                System.out.println("myresponse有分号：" + myresponse);
                String[] splitone = myresponse.split(";");
                for (int i = 0; i < splitone.length; i++) {
                    String myresponseone = splitone[i];
                    String[] splittwo = myresponseone.split("=");
                    responseGinseng(jsonObject, splittwo, myresponse,myjsonmap);
                }
            } else {
                System.out.println("myresponse没有分号：" + myresponse);
                String[] splitthree = myresponse.split("=");
                responseGinseng(jsonObject, splitthree, myresponse,myjsonmap);
            }
        }else{
            System.out.println("myresponse没有等号不进行处理！！");
        }
        return myjsonmap;
    }
    //将excel表中response数据用“；”和“=”分割后，将响应的某个字段存入map中
    public  HashMap<String, String>  responseGinseng(JSONObject jsonObject,String[] countsone,
                                                     String myrespones,HashMap<String, String> myjsonmap){
//        if (countsone)
        if (countsone.length == 2){
            String sponseone = countsone[0];
            String sponsetwo = countsone[1];
            //通过Key：s，获取value：值
            if(!sponseone.contains("[")){
                myjsonmap.put(sponsetwo, myConversion(jsonObject,sponseone));
                System.out.println("mapjih12:"+myjsonmap.get(sponsetwo));
            }else{
                String[] splitone = sponseone.split("]");
                String splitsone = splitone[0];
                String splitstwo = splitone[1];
                String splitsthree = splitone[2];
                JSONArray jsonArray1 = jsonObject.getJSONArray(splitsone);
                //Object o = (String)jsonArray1.get((splitstwo)
                JSONArray jsonArray=null;
                jsonArray = new JSONArray(Collections.singletonList(sponseone));
            }
        }else if(countsone.length == 3){
            String sponseone = countsone[0];
            String sponsetwo = countsone[1];
            String sponsethree = countsone[2];
            //通过Key：s，获取value：值
            String value = jsonObject.getString(sponseone);
            System.out.println("value1111111实际为=====" + value);
            //将value转化成JSONObject
            JSONObject jsonvalue = JSONObject.parseObject(value);
            //通过Key：s1，获取value：值,并附值
            System.out.println("jsonvalues实际为=====" + jsonvalue.getString(sponsetwo));
            String valuetwo = jsonvalue.getString(sponsetwo);
            myjsonmap.put(sponsethree, valuetwo);
            System.out.println("mapjih13:"+myjsonmap.get(sponsethree));
        }else if (countsone.length == 4) {
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
        }else if (countsone.length == 5){
                String sponseone = countsone[0];
                String sponsetwo = countsone[1];
                String sponsethree = countsone[2];
                String sponsefour = countsone[3];
                String sponsefive = countsone[4];
                String value = jsonObject.getString(sponseone);
                JSONObject jsonObjectone = JSONObject.parseObject(value);
                String valueone = jsonObjectone.getString(sponsetwo);
                JSONObject jsonObjecttwo = JSONObject.parseObject(valueone);
                String valuetwo = jsonObjectone.getString(sponsethree);
                JSONObject jsonObjectthree = JSONObject.parseObject(valuetwo);
                String valuethree = jsonObjectthree.getString(sponsefour);
                myjsonmap.put(sponsefive, valuethree);
        }else {
            System.out.println("excel中的Respones不支持改格式"+myrespones);
        }
        return myjsonmap;
    }
    public String myConversion(JSONObject jsonObject,String sponseone){

        return jsonObject.getString(sponseone);
    }
    //判断excel中response中是否有值，并对response中的值进行处理
//    public void myresponse(String body,String myresponse) {
//        //将body转化成JSONObject
//        JSONObject jsonObject = JSONObject.parseObject(body);
//        if (myresponse.contains("=")) {
//            System.out.println("myresponse有等号：");
//            if (myresponse.contains(";")) {
//                System.out.println("myresponse有分号：" + myresponse);
//                String[] splitone = myresponse.split(";");
//                for (int i = 0; i < splitone.length; i++) {
//                    String myresponseone = splitone[i];
//                    String[] splittwo = myresponseone.split("=");
//                    responseGinseng(jsonObject, splittwo, myresponse);
//                }
//            } else {
//                System.out.println("myresponse没有分号：" + myresponse);
//                String[] splitthree = myresponse.split("=");
//                responseGinseng(jsonObject, splitthree, myresponse);
//            }
//        }else if(myresponse.contains("&")){
//            System.out.println("myresponse有&号：");
//            if (myresponse.contains(";")) {
//                System.out.println("myresponse有;号：" + myresponse);
//                String[] splitones = myresponse.split(";");
//                for (int i = 0; i < splitones.length; i++) {
//                    String myresponseone = splitones[i];
//                    String[] splittwos = myresponseone.split("&");
//                    responseGinseng(jsonObject, splittwos, myresponse);
//                }
//            } else {
//                System.out.println("myresponse没有&号：" + myresponse);
//                String[] splitthrees = myresponse.split("&");
//                responseGinseng(jsonObject, splitthrees, myresponse);
//            }
//        }else{
//            System.out.println("myresponse没有等号&号不进行处理！！");
//        }
//    }
//    //将excel表中response数据用“；”和“=”分割后，将响应的某个字段存入map中
//    public void responseGinseng(JSONObject jsonObject,String[] countsone,String myrespones){
//        if (countsone.length == 2){
//            String sponseone = countsone[0];
//            String sponsetwo = countsone[1];
//            //通过Key：s，获取value：值
//            String valueone = jsonObject.getString(sponseone);
//            this.myjsonmap.put(sponsetwo, valueone);
//            System.out.println("mapjih12:"+this.myjsonmap.get(sponsetwo));
//        }else if(countsone.length == 3){
//            String sponseone = countsone[0];
//            String sponsetwo = countsone[1];
//            String sponsethree = countsone[2];
//            //通过Key：s，获取value：值
//            String value = jsonObject.getString(sponseone);
//            System.out.println("value实际为=====" + value);
//            //将value转化成JSONObject
//            JSONObject jsonvalue = JSONObject.parseObject(value);
//            //通过Key：s1，获取value：值,并附值
//            System.out.println("jsonvalues实际为=====" + jsonvalue.getString(sponsetwo));
//            String valuetwo = jsonvalue.getString(sponsetwo);
//            myjsonmap.put(sponsethree, valuetwo);
//            System.out.println("mapjih13:"+myjsonmap.get(sponsethree));
//        }else if (countsone.length == 4){
//            String sponseone = countsone[0];
//            String sponsetwo = countsone[1];
//            String sponsethree = countsone[2];
//            String sponsefour = countsone[3];
//            String value = jsonObject.getString(sponseone);
//            JSONObject jsonObjectone = JSONObject.parseObject(value);
//            String valueone = jsonObjectone.getString(sponsetwo);
//            JSONObject jsonObjecttwo = JSONObject.parseObject(valueone);
//            String valuetwo = jsonObjecttwo.getString(sponsethree);
//            myjsonmap.put(sponsefour, valuetwo);
//        }else {
//            System.out.println("excel中的Respones不支持改格式"+myrespones);
//        }
//    }

    //判断excel中request是否有值，并通过条件对入参data中的值进行处理
//    public String myrequest(String data,String myrequest) {
//        String ss = data;
//        //将data转化成JSONObject
//        System.out.println("data转化前："+ss);
//        JSONObject jsonObject = JSONObject.parseObject(data);
//        //如果有值，将替换request中某个key的值
//        ArrayList<String> list = new ArrayList<String>();
//        //判断是否存在=
//        if (myrequest.contains("=")) {
//            //判断是否存在；
//            if (myrequest.contains(";")) {
//                //以；进行分割
//                String[] requestsp = myrequest.split(";");
//                for (int i = 0; i < requestsp.length; i++) {
//                    String s = requestsp[i];
//                    //以=进行分割
//                    String[] splitones = s.split("=");
//                    System.out.println("request长度:" + splitones.length);
//                    jsonObject = requestGinseng(splitones, jsonObject,myrequest);
//                }
//            } else {
//                String[] requestspone = myrequest.split("=");
//                System.out.println("request长度:" + requestspone.length);
//                jsonObject = requestGinseng(requestspone, jsonObject,myrequest);
//            }
//            ss=JSONObject.toJSONString(jsonObject);
//        }else if(myrequest.contains("&")){
//
//        }
//        return ss;
//    }
//    //通过传入的counts数组，判断长度，
//    public JSONObject requestGinseng(String[] counts,JSONObject jsonObject,String myrequest){
//        String myrequestsss = null;
//        if (counts.length == 2){
//            String questone = counts[0];
//            String questtwo = counts[1];
//            System.out.println("questone:" + questone);
//            System.out.println("questtwo:" + questtwo);
//            System.out.println("myjsonmap.get(questtwo):" + myjsonmap.get(questtwo));
//            //更换tonken
//            jsonObject.put(questone, myjsonmap.get(questtwo));
//            //转化成Map<String, String>
//            //Map<String, String> jsonMap = JSONObject.toJavaObject(jsonObject, Map.class);
//            //转化成String
////            ss = JSONObject.toJSONString(jsonObject);
////            System.out.println("data转1222化后："+ss);
//        }else if (counts.length == 3) {
//            String requestsone = counts[0];
//            String requeststwo = counts[1];
//            String requeststhree = counts[2];
//            //通过第一个数据获取data中对应的数据
//            String requestObj = jsonObject.getString(requestsone);
//            //转化成JSONObject类型
//            JSONObject jsonvalue = JSONObject.parseObject(requestObj);
//            //更改requesttwo的数据
//            jsonvalue.put(requeststwo, myjsonmap.get(requeststhree));
//            //转化成String
//            myrequestsss = JSONObject.toJSONString(jsonvalue);
//            //将data中的requesttwo数据更改为myrequestsss
//            jsonObject.put(requestsone, myrequestsss);
//        }else if (counts.length == 4){
//            String requestsone = counts[0];
//            String requeststwo = counts[1];
//            String requeststhree = counts[2];
//            String requestsfour = counts[3];
//            //通过第一个数据获取data中对应的数据
//            String requestObjone = jsonObject.getString(requestsone);
//            JSONObject jsonObjectone = JSONObject.parseObject(requestObjone);
//            String requestObjtwo = jsonObjectone.getString(requeststwo);
//            JSONObject jsonObjecttwo = JSONObject.parseObject(requestObjtwo);
//            //更改requesttwo的数据
//            jsonObjecttwo.put(requeststhree, myjsonmap.get(requestsfour));
//            String s = JSONObject.toJSONString(jsonObjecttwo);
//            jsonObjectone.put(requeststwo,s);
//            String s1 = JSONObject.toJSONString(jsonObjectone);
//            jsonObject.put(requestsone,s1);
//        }else{
//            System.out.println("excel中的Request不支持改格式"+myrequest);
//        }
//
//        return jsonObject;
//    }
}


