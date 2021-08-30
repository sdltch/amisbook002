package com.example.amisbook002;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.catalina.loader.JdbcLeakPrevention;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import sun.net.www.http.HttpClient;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Test
public class Request {
    private String myCookie = "";
    public String Authorization = "Basic YW1paW50ZWxsZWN0OmFtaWludGVsbGVjdC0xMjM0NTY=";
    public static String getRsaPublicKey = "";
    private static Logger log = LoggerFactory.getLogger(Request.class);
    private final static String BOUNDARY = UUID.randomUUID().toString()
            .toLowerCase().replaceAll("-", "");// 边界标识
    private final static String PREFIX = "--";// 必须存在
    private final static String LINE_END = "\r\n";
    //

    public Request() throws Exception {
        //获取登陆URl
        String loginUrl = DataManipulation.myurlRpa + DataManipulation.loginUrl;
        System.out.println("登录urlLL："+loginUrl);
        //登录入参
        String postData = DataManipulation.loginData;
        String loginYype = DataManipulation.loginYype;
        System.out.println("登录dataLL："+ postData);
        //获取cookie
        Response doPostCookie;
        String loginBody="";
        if(loginYype.equals("POSTS")){
            loginBody = this.JdLoginTest(loginUrl);
        }
        doPostCookie = this.doPost(loginUrl, postData, loginYype);
        System.out.println("this.Authorization："+ this.Authorization);
        this.myCookie = doPostCookie.getCookie();
        //this.Authorization = doPostCookie.getCookie();
        System.out.println("this.Authorization："+ this.Authorization);
        String body = doPostCookie.getBody();
        //String loginBody = doPostCookie.getBody();
        System.out.println("this.loginBody："+ body);

        /*
        *获取heade中的：Authorization
        *
        * 将body转化成JSONObject
        * */
//        JSONObject jsonObject = JSONObject.parseObject(loginBody);
//        //通过【key】（data)获取value
//        String data = jsonObject.getString("data");
//        System.out.println("data:"+data);
//        JSONObject jsonObject1 = JSONObject.parseObject(data);
//        if(data != null){
//            this.Authorization = jsonObject1.getString("tokenHead")+jsonObject1.getString("token");
//        }
//        System.out.println("Authorization="+this.Authorization);
//        String body = doPostCookie.getBody();
//        System.out.println("登录响应1：" + body);
//        if (body.contains(DataManipulation.loginAssert)){
//            System.out.println("默认登录成功：" + body);
//        }
//        System.out.println("默认登录cookie：" + myCookie);
    }
    //Get请求
    public Response doGet(String getUrl, String getdata) {
        //GET请求
        Response response = null;
        System.out.println("get入参："+getdata);
        if (!getdata.equals("")){
            System.out.println("get入参不为空："+getdata);
            getUrl = getUrl + "?" + getdata;
        }
        try {
            HttpURLConnection openConnection = this.getConnection(getUrl);
            //接收响应
            response = new Response(openConnection);
            //关闭连接
            openConnection.disconnect();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return response;
    }
    /*
     * Post请求josn/from
     * postType:类型入参
     *
     * */
    public Response doPost(String postUrl, String postdata, String postType) {
        Response response = null;
        try {
            HttpURLConnection openConnection = this.postAll(postUrl,postType);
            //System.out.println("params======" + postdata);
            //发数据
            OutputStream outputStream = openConnection.getOutputStream();
            outputStream.write(postdata.getBytes());

            outputStream.flush();
            //收响应
            response = new Response(openConnection);
            //关闭
            openConnection.disconnect();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return response;
    }
    /*
     * Post请求josn/from
     * postType:类型入参
     *
     * */
    public Response doPostfrom(String postUrl, String postdata, String postType) {
        Response response = null;
        try {
            HttpURLConnection openConnection = this.postAll(postUrl,postType);
            //System.out.println("params======" + postdata);
            //发数据
            OutputStream outputStream = openConnection.getOutputStream();
            outputStream.write(postdata.getBytes());

            outputStream.flush();
            //收响应
            response = new Response(openConnection);
            //关闭
            openConnection.disconnect();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return response;
    }
    /**
     * 文件上传. <br/>
     */
    public Response sendpost(String requestUrl, Map<String, String> requestText,
                             Map<String, File> requestFile) throws Exception {
        Response response = null;
        InputStream input = null;
        BufferedReader br = null;
        StringBuffer buffer = null;
        HttpURLConnection sendURLConnection = this.sendPostConnection(requestUrl);
        // 往服务器端写内容 也就是发起http请求需要带的参数
        OutputStream sendoutputStream = sendURLConnection.getOutputStream();
        DataOutputStream senddataOutputStream = new DataOutputStream(sendoutputStream);
        // 请求参数部分
        writeParams(requestText, senddataOutputStream);
        // 请求上传文件部分
        writeFile(requestFile, senddataOutputStream);
        // 请求结束标志
        String endTarget = PREFIX + BOUNDARY + PREFIX + LINE_END;
        senddataOutputStream.write(endTarget.getBytes());
        senddataOutputStream.flush();
        response = new Response(sendURLConnection);
//        return response;
//        // 读取服务器端返回的内容
//        System.out.println("======================响应体=========================");
//        System.out.println("ResponseCode:" + sendURLConnection.getResponseCode()
//                + ",ResponseMessage:" + sendURLConnection.getResponseMessage());
//        if (sendURLConnection.getResponseCode() == 200) {
//            input = sendURLConnection.getInputStream();
//        } else {
//            input = sendURLConnection.getErrorStream();
//        }
//
//        br = new BufferedReader(new InputStreamReader(input, "UTF-8"));
//        buffer = new StringBuffer();
//        String line = null;
//        while ((line = br.readLine()) != null) {
//            buffer.append(line);
//        }
//        //......
//        System.out.println("返回报文:" + buffer.toString());
        return response;
    }
    /*
    * put
    *
    * */
    public Response doput(String postUrl, String postdata){
        Response response = null;
        try {
            HttpURLConnection openConnection = this.putConnection(postUrl);
            //System.out.println("params======" + postdata);
            //发数据
            OutputStream outputStream = openConnection.getOutputStream();
            outputStream.write(postdata.getBytes());
            outputStream.flush();
            //收响应
            response = new Response(openConnection);
            //关闭
            openConnection.disconnect();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return response;
    }
//    public Response dodelete(String getUrl,String data) {
//        //delect请求
//        Response response = null;
//        try {
//            HttpURLConnection openConnection = this.deleteConnection(getUrl,data);
//            //接收响应
//            response = new Response(openConnection);
//            //关闭连接
//            openConnection.disconnect();
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//        return response;
//    }
    private HttpURLConnection getConnection(String postUrl) throws Exception {

        //得到一个url对象  参数为发送的地址
        URL url = new URL(postUrl);
        HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
        //设置http属性
        openConnection.setRequestMethod("GET");
        openConnection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
        openConnection.setDoOutput(true);// http正文内，因此需要设为true, 默认情况下是false;
        openConnection.setRequestProperty(
                    "Content-Type",  "application/json;charset=utf-8");//json格式上传的模式
        //不缓存
        openConnection.setUseCaches(false);
        openConnection.setConnectTimeout(5000);
        openConnection.setReadTimeout(10000);
        openConnection.setRequestProperty("cookie", this.myCookie);
        openConnection.setRequestProperty("Authorization",this.Authorization);
        //建立连接
        openConnection.connect();
        return openConnection;
    }
    private HttpURLConnection postAll(String postUrl, String postType) throws Exception {
        //得到一个url对象  参数为发送的地址
        URL url = new URL(postUrl);
        HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
        if(postType.equals("POST") || postType.equals("POSTFROM")){
            openConnection.setRequestMethod("POST");
        } else if (postType.equals("PUT")){
            openConnection.setRequestMethod("put");
        }
        //openConnection.setDoInput(true);
        openConnection.setDoOutput(true);
        openConnection.setRequestProperty(
                "Accept","application/json, text/plain, */*");
        //根据请求方式判断入参类型
        if (postType.equals("POST")){
        openConnection.setRequestProperty(
                "Content-Type",  "application/json;charset=utf-8");//json格式上传的模式
            System.out.println("请求方式：json");
        }else if (postType.equals("POSTFROM")){
                    openConnection.setRequestProperty(
                "Content-Type",  "application/x-www-form-urlencoded");//表单上传的模式
            System.out.println("请求方式：表单");
        }else {
            System.out.println("请求方式错误："+postType);
        }
        //不缓存
        openConnection.setUseCaches(false);
        openConnection.setConnectTimeout(5000);
        openConnection.setReadTimeout(10000);
        openConnection.setRequestProperty("cookie", this.myCookie);
        openConnection.setRequestProperty("Authorization",this.Authorization);
        System.out.println("开始连接...");
        //建立连接
        openConnection.connect();
        System.out.println("成功连接");
        return openConnection;
    }
    //post
    @Test
    public String JdLoginTest(String url) throws IOException {
        Response response = null;
        String entityStr = null;
        //登录url
        CloseableHttpResponse responses = null;
        String entityStrs = null;
        // 获取连接客户端工具
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建POST请求对象
        HttpPost httpPost = new HttpPost(url);
        // httpPost.addHeader post请求 header
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        httpPost.addHeader("User-Agent:", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36");
//        List<Header> headerList= Lists.newArrayList();
//        headerList.add(new BasicHeader(HttpHeaders.CONTENT_TYPE,"application/x-www-form-urlencoded; charset=UTF-8"));
//        headerList.add(new BasicHeader(HttpHeaders.USER_AGENT,"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36"));

        //参数封装对象
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        try {
//            for(int i = 0; i < 9;i ++){
//                params.add(new BasicNameValuePair("uuid", "a25f6873-4dd9-4334-ad4c-e6f8992b8e3f"));
//
//            }
            params.add(new BasicNameValuePair("password", "superlucy@71"));
            params.add(new BasicNameValuePair("username", "admin"));
            params.add(new BasicNameValuePair("grant_type", "password"));
            params.add(new BasicNameValuePair("client_id", "amiintellect"));
            params.add(new BasicNameValuePair("client_secret", "amiintellect-123456"));

            // 使用URL实体转换工具
            UrlEncodedFormEntity entityParam = new UrlEncodedFormEntity(params, "UTF-8");

            httpPost.setEntity(entityParam);
            // 执行请求
            responses = httpClient.execute(httpPost);
            // 获得响应的实体对象
            HttpEntity entity = responses.getEntity();
            // 使用Apache提供的工具类进行转换成字符串
            int code = responses.getStatusLine().getStatusCode();
            System.out.println("StatusCode: " + code);
            Assert.assertEquals(200, code);
            entityStrs = EntityUtils.toString(entity, "UTF-8");
            System.out.println("接口返回结果是:=" + entityStrs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放资源
            if (httpClient != null) {
                httpClient.close();
            }
            if (responses != null) {
                responses.close();
            }
        }
        return entityStrs;
    }
    //POST请求数据表单from
//    private HttpURLConnection postFromConnection(String postUrl) throws Exception {
//        //得到一个url对象  参数为发送的地址
//        URL url = new URL(postUrl);
//        HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
//        openConnection.setRequestMethod("POST");
//        //openConnection.setDoInput(true);
//        openConnection.setDoOutput(true);
//        openConnection.setRequestProperty("Content-Type", "application/Json; charset=UTF-8");
//        openConnection.setRequestProperty("Accept","application/json, text/plain, */*");
//        //from表单
//        openConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//        //不缓存
//        openConnection.setUseCaches(false);
//        openConnection.setConnectTimeout(5000);
//        openConnection.setReadTimeout(10000);
//        openConnection.setRequestProperty("cookie", this.myCookie);
//        openConnection.setRequestProperty("Authorization",this.Authorization);
//        System.out.println("开始连接...");
//        //建立连接
//        openConnection.connect();
//        System.out.println("成功连接");
//        return openConnection;
//    }
//    //POST请求数据josn
//    private HttpURLConnection postConnection(String postUrl) throws Exception {
//        //得到一个url对象  参数为发送的地址
//        URL url = new URL(postUrl);
//        HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
//        openConnection.setRequestMethod("POST");
//        //openConnection.setDoInput(true);
//        openConnection.setDoOutput(true);
//        openConnection.setRequestProperty(
//                "Content-Type", "application/Json; charset=UTF-8");
//        openConnection.setRequestProperty(
//                "Accept","application/json, text/plain, */*");
////        openConnection.setRequestProperty(
////                "Content-Type",  "application/x-www-form-urlencoded");//表单上传的模式
////        openConnection.setRequestProperty(
////                "Content-Type",  "application/json;charset=utf-8");//json格式上传的模式
//        //不缓存
//        openConnection.setUseCaches(false);
//        openConnection.setConnectTimeout(5000);
//        openConnection.setReadTimeout(10000);
//        openConnection.setRequestProperty("cookie", this.myCookie);
//        openConnection.setRequestProperty("Authorization",this.Authorization);
//        System.out.println("开始连接...");
//        //建立连接
//        openConnection.connect();
//        System.out.println("成功连接");
//        return openConnection;
//    }
    //put请求josn
    private HttpURLConnection putConnection(String putUrl) throws Exception {
        //得到一个url对象  参数为发送的地址
        URL url = new URL(putUrl);
        HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
        openConnection.setRequestMethod("PUT");
        //openConnection.setDoInput(true);
        openConnection.setDoOutput(true);
        openConnection.setRequestProperty("Content-Type", "application/Json; charset=UTF-8");
        openConnection.setRequestProperty("Accept","application/json, text/plain, */*");
        //不缓存
        openConnection.setUseCaches(false);
        openConnection.setConnectTimeout(5000);
        openConnection.setReadTimeout(10000);
        openConnection.setRequestProperty("cookie", this.myCookie);
        openConnection.setRequestProperty("Authorization",this.Authorization);
        System.out.println("开始连接...");
        //建立连接
        openConnection.connect();
        System.out.println("成功连接");
        return openConnection;
    }
    /*
    *     delete：HttpClient
    * */

    public String deleteConnection(String deleteUrl,String data) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        if (!data.equals("")){
            System.out.println("delete入参不为空："+data);
            deleteUrl = deleteUrl + "?" + data;
        }
        System.out.println("delete入参不为空："+deleteUrl);
        HttpDelete httpDelete = new HttpDelete(deleteUrl);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).
                setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
        httpDelete.setConfig(requestConfig);
        httpDelete.setHeader("Content-type", "application/json");
        httpDelete.setHeader("DataEncoding", "UTF-8");
        //httpDelete.setHeader("token", this.Authorization);
        httpDelete.setHeader("cookie", this.myCookie);
        httpDelete.setHeader("Authorization",this.Authorization);
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpDelete);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            System.out.println("delete请求:"+result);
            return result;
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (httpResponse != null) {
                try {
                    httpResponse.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    //文件上传post
    private HttpURLConnection sendPostConnection(String postUrl) throws Exception {
        //得到一个url对象  参数为发送的地址
        URL url = new URL(postUrl);
        HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
        openConnection.setRequestMethod("POST");
        openConnection.setDoInput(true);
        openConnection.setDoOutput(true);

        //String BOUNDARY = "----WebKitFormBoundarylCCjai5boCV7F5g3";
        openConnection.setRequestProperty("Accept", "*/*");
        openConnection.setRequestProperty("Connection", "keep-alive");
        openConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        openConnection.setRequestProperty("Charset", "UTF-8");
        openConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
        openConnection.setRequestProperty("Authorization",this.Authorization);
        //不缓存
        openConnection.setUseCaches(false);
        openConnection.setConnectTimeout(5000);
        openConnection.setReadTimeout(10000);
        openConnection.setRequestProperty("cookie", this.myCookie);
        System.out.println("cookie="+this.myCookie);
        //建立连接
        try {
            openConnection.connect();
        } catch (Exception e){
            log.error("writeParams failed", e);
        }
        return openConnection;

    }
    /**
     * 对post参数进行编码处理并写入数据流中
     *
     * @throws Exception
     * @throws IOException
     */
    private static void writeParams(Map<String, String> requestText,
                                    OutputStream os) throws Exception {
        try {
            String msg = "请求参数部分:\n";
            if (requestText == null || requestText.isEmpty()) {
                msg += "空";
            } else {
                StringBuilder requestParams = new StringBuilder();
                Set<Map.Entry<String, String>> set = requestText.entrySet();
                Iterator<Map.Entry<String, String>> it = set.iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    requestParams.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    requestParams.append("Content-Disposition: form-data; name=\"")
                            .append(entry.getKey()).append("\"").append(LINE_END);
                    requestParams.append("Content-Type: text/plain; charset=utf-8")
                            .append(LINE_END);
                    requestParams.append("Content-Transfer-Encoding: 8bit").append(
                            LINE_END);
                    requestParams.append(LINE_END);// 参数头设置完以后需要两个换行，然后才是参数内容
                    requestParams.append(entry.getValue());
                    requestParams.append(LINE_END);
                }
                os.write(requestParams.toString().getBytes());
                os.flush();

                msg += requestParams.toString();
            }

            System.out.println(msg);
        } catch (Exception e) {
            log.error("writeParams failed", e);
            throw new Exception(e);
        }
    }

    /**
     * 对post上传的文件进行编码处理并写入数据流中
     *
     * @throws IOException
     */
    private static void writeFile(Map<String, File> requestFile,
                                  OutputStream os) throws Exception {
        InputStream is = null;
        try {
            String msg = "请求上传文件部分:\n";
            if (requestFile == null || requestFile.isEmpty()) {
                msg += "空";
            } else {
                StringBuilder requestParams = new StringBuilder();
                Set<Map.Entry<String, File>> set = requestFile.entrySet();
                Iterator<Map.Entry<String, File>> it = set.iterator();
                while (it.hasNext()) {
                    Map.Entry<String, File> entry = it.next();
                    requestParams.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    requestParams.append("Content-Disposition: form-data; name=\"")
                            .append(entry.getKey()).append("\"; filename=\"")
                            .append(entry.getValue().getName()).append("\"")
                            .append(LINE_END);
                    requestParams.append("Content-Type:")
                            .append(getContentType(entry.getValue()))
                            .append(LINE_END);
                    requestParams.append("Content-Transfer-Encoding: 8bit").append(
                            LINE_END);
                    requestParams.append(LINE_END);// 参数头设置完以后需要两个换行，然后才是参数内容

                    os.write(requestParams.toString().getBytes());

                    is = new FileInputStream(entry.getValue());

                    byte[] buffer = new byte[1024 * 1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        os.write(buffer, 0, len);
                    }
                    os.write(LINE_END.getBytes());
                    os.flush();

                    msg += requestParams.toString();
                }
            }
            System.out.println(msg);
        } catch (Exception e) {
            log.error("writeFile failed", e);
            throw new Exception(e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                log.error("writeFile FileInputStream close failed", e);
                throw new Exception(e);
            }
        }
    }
    /**
     * ContentType
     *
     * @param file
     * @return
     * @throws IOException
     * @Description:
     */
    public static String getContentType(File file) throws Exception {
        String streamContentType = "application/octet-stream";
        String imageContentType = "";
        ImageInputStream image = null;
        try {
            image = ImageIO.createImageInputStream(file);
            if (image == null) {
                return streamContentType;
            }
            Iterator<ImageReader> it = ImageIO.getImageReaders(image);
            if (it.hasNext()) {
                imageContentType = "image/" + it.next().getFormatName();
                return imageContentType;
            }
        } catch (IOException e) {
            log.error("method getContentType failed", e);
            throw new Exception(e);
        } finally {
            try {
                if (image != null) {
                    image.close();
                }
            } catch (IOException e) {
                log.error("ImageInputStream close failed", e);
                ;
                throw new Exception(e);
            }
        }
        return streamContentType;
    }
}


