package com.example.amisbook002;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

public class Response {
    private String cookie = "";
    private String body = "";
    private int code = 0;
    private HttpURLConnection connection = null;
    public Response(HttpURLConnection httpUrlConnection) {
        this.connection = httpUrlConnection;
    }
    public String getCookie() {
        Map<String, List<String>> headerFields = this.connection.getHeaderFields();
        List<String> list = headerFields.get("Set-Cookie");
        if (list != null) {
            for (String line : list) {
                this.cookie += line + ";";
            }
        }
        return this.cookie;
    }

    public String getBody() {
        //收发数据
        try {
            InputStream inputStream = this.connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                this.body += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.body;
    }

    public int getCode() {
        try {
            this.code = this.connection.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.code;

    }

}

