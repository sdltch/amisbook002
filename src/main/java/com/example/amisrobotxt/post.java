package com.example.amisrobotxt;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.SystemDefaultCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class post {

    public static void main(String[] args) {

        //请求的url
        String url ="http://permission.synergy.c.superlucy.net/api/auth/oauth/token";
        //接口请求的数据，拼接成json字符串
        //String jsonStr ="{\"xx\":\"xx\",\"xx\":\"xx\"}";
        String jsonStr ="{\"password\":\"sdl@123\",\"username\":\"sdltest\",\"grant_type\":\"password\"}";
        //接口请求的类型：json，form
        String submitType = "form";
        String myhead = "Authorization:Basic YW1paW50ZWxsZWN0OmFtaWludGVsbGVjdC0xMjM0NTY=";
        try {
            HttpPost httpPost = new HttpPost(url);
                if(myhead.toString().contains(":")){
                    String[] splittwo = myhead.split(":");
                    httpPost.addHeader(new BasicHeader(splittwo[0], splittwo[1]));
            }
            if ("form".equalsIgnoreCase(submitType)) {
                httpPost.addHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"));
                //httpPost.addHeader(new BasicHeader("Authorization", "Basic YW1paW50ZWxsZWN0OmFtaWludGVsbGVjdC0xMjM0NTY="));
                String params =jointBodyParam(jsonStr);
                System.out.println("from入参"+params);
                httpPost.setEntity(new StringEntity(params, "UTF-8"));
            }else if("json".equalsIgnoreCase(submitType)){
                httpPost.addHeader(new BasicHeader("Content-Type", "application/json;charset=UTF-8"));
                //httpPost.addHeader("Authorization", "Basic YW1paW50ZWxsZWN0OmFtaWludGVsbGVjdC0xMjM0NTY=");
                httpPost.setEntity(new StringEntity(jsonStr,"UTF-8"));
            }
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            int httpCode =  httpResponse.getStatusLine().getStatusCode();
            Header[] header = httpResponse.getAllHeaders();
            String result = EntityUtils.toString(httpResponse.getEntity());

            System.out.println("接口的响应状态码："+httpCode);
            System.out.println("接口的响应头："+Arrays.toString(header));
            System.out.println("接口的响应报文"+result);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static String jointBodyParam(String jsonStr) {
        Map<String, Object> map = (Map<String, Object>) JSONObject.parse(jsonStr);
        Set<String> keySet = map.keySet();
        StringBuffer sBuffer = new StringBuffer();
        String[] keyArr = new String[keySet.size()];
        keySet.toArray(keyArr);
        for (int i = 0; i < keyArr.length; i++) {
            String paramName = keyArr[i];
            // 第一个参数前用  ？ 来拼接
            sBuffer.append(paramName).append("=").append(map.get(paramName)).append("&");
        }
        // 截取字符串， 从0开始，到（最后一个&出现的位置）； 是为了截掉最后一个&
        return sBuffer.substring(0, sBuffer.lastIndexOf("&"));
    }
}
