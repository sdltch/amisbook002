package com.example.amisbook002;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;

public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    @Resource
    private RestTemplate restTemplate;

    private static HttpUtil httpUtil;

    @PostConstruct
    public void init(){
        httpUtil = this;
        httpUtil.restTemplate = this.restTemplate;
    }

    public static <T> String httpRequest(String url, HttpMethod method, HttpEntity<T> entity){
        try {
            String s= "{\n" +
                    "  \"loginName\": \"0215测试002\",\n" +
                    "  \"password\": \"123456\",\n" +
                    "  \"returnUrl\": \"/home\"\n" +
                    "}";
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.add("key","testKey");
//            requestHeaders.add("intent", String.valueOf(intent));
//            requestHeaders.add("number", number);
//            requestHeaders.add("code", code);

            //发起一个POST请求
            ResponseEntity<String> result = httpUtil.restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            return result.getBody();
        } catch (Exception e) {
            logger.error("请求失败： " + e.getMessage());
        }
        return null;
    }
    private String getId(String id) {
        //String url = RemoteUrl + "/id";
        String url = "/id";
        //设置Http的Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        //设置访问参数
        HashMap<String, Object> params = new HashMap<>();
        params.put("loginName", "lo");
//        params.put("loginName", name);
//        params.put("loginName", name);
        //设置访问的Entity
        HttpEntity entity = new HttpEntity<>(params, headers);
        ResponseEntity<String> result = null;
        try {
            //发起一个POST请求
            result = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            JSONObject data = JSONObject.parseObject(result.getBody()).getJSONObject("data");
            return data.getString("id");
        } catch (Exception e) {
            logger.error("获取id失败： " + e.getMessage());
        }
        return null;
    }

}
