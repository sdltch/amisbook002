package com.example.amisbook002;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
@Test
public class Responseclient {
    private String cookie ="";
    private String body = "";
    private int code = 0;
    private CloseableHttpResponse connection = null;
    public Responseclient(CloseableHttpResponse httpUrlConnection) {
        this.connection = httpUrlConnection;
    }
    //获取code
    public int getCode() {
        this.code = this.connection.getStatusLine().getStatusCode();
        Assert.assertEquals(200, this.code);
        return this.code;

    }
    //获取body
    public String getBody() throws IOException {
        // 获得响应的实体对象
        HttpEntity entity = this.connection.getEntity();
        this.body = EntityUtils.toString(entity, "UTF-8");
        System.out.println("接口返回结果是:=" + body);
        return this.body;
    }
}
