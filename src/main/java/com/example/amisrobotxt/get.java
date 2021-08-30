package com.example.amisrobotxt;
import java.util.Arrays;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class get {
    public static void main(String[] args){
        get gets = new get();
        String myurl = "http://synergy.k8s.superlucy.net";
        String interfaceUrl = "/api/auth/oauth/pubKey";
        String myxturl = myurl+interfaceUrl;
        gets.doget(myxturl);
    }
    public void doget(String myxturl) {
        // get请求的方式，https://域名/路径？k1=v1&k2=v2
        //String url = "https://xxx.xxx.com/xxx" + "?" + "k1=v1&k2=v2";

        System.out.println("url:"+myxturl);
        try {
            // 创建httpGet请求
            HttpGet httpGet = new HttpGet(myxturl);
            CloseableHttpClient httpClient = HttpClients.createDefault();

            // 准备请求头 （这一步看需要，若接口有添加请求头信息则直接添加，若不需要 可直接略过）
            HttpRequest httpRequest = httpGet;
//            httpRequest.addHeader("Authorization", "Basic YW1paW50ZWxsZWN0OmFtaWludGVsbGVjdC0xMjM0NTY=");
            httpRequest.addHeader("token2", "xxxxx2");

            // 发送请求
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            int httpCode = httpResponse.getStatusLine().getStatusCode();
            Header[] header = httpResponse.getAllHeaders();
            String result = EntityUtils.toString(httpResponse.getEntity());

            System.out.println("接口的响应状态码" + httpCode);
            System.out.println("接口的响应头" + Arrays.toString(header));
            System.out.println("接口的响应报文" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
