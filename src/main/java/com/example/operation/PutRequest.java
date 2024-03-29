package com.example.operation;

import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class PutRequest {
    //判断excel中request是否有值，并通过条件对入参data中的值进行处理
    public String myrequest(String data, String myrequest, HashMap<String, String> myjsonmap) {
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
                    jsonObject = requestGinseng(splitones, jsonObject, myrequest, myjsonmap);
                }
            } else {
                String[] requestspone = myrequest.split("=");
                System.out.println("request长度:" + requestspone.length);
                jsonObject = requestGinseng(requestspone, jsonObject, myrequest, myjsonmap);
            }
            ss=JSONObject.toJSONString(jsonObject);
        }
        return ss;
    }
    //通过传入的counts数组，判断长度，
    public JSONObject requestGinseng(String[] counts,JSONObject jsonObject,
                                     String myrequest,HashMap<String, String> myjsonmap){
        String myrequestsss = null;
        if (counts.length == 2){
            String questone = counts[0];
            String questtwo = counts[1];
            System.out.println("questone:" + questone);
            System.out.println("questtwo:" + questtwo);
            System.out.println("myjsonmap.get(questtwo):" + myjsonmap.get(questtwo));
            //更换
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
            System.out.println("excel中的Request不支持该格式"+myrequest);
        }
        return jsonObject;
    }
}
