package com.example.amisbook002;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Test
public class Request {
    private String myCookie = "";
    private String Authorization = "";
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
        System.out.println("登录url11："+loginUrl);
        //登录入参
        String postData = DataManipulation.loginData;
        System.out.println("登录data："+ postData);
        //获取cookie
        Response doPostCookie = this.doPost(loginUrl, postData);
        this.myCookie = doPostCookie.getCookie();
        this.Authorization = doPostCookie.getCookie();
        String loginBody = doPostCookie.getBody();
        /*
        *获取heade中的：Authorization
        *
        * 将body转化成JSONObject
        * */
        JSONObject jsonObject = JSONObject.parseObject(loginBody);
        String data = jsonObject.getString("data");
        JSONObject jsonObject1 = JSONObject.parseObject(data);
        this.Authorization = jsonObject1.getString("id");
        System.out.println("Authorization="+this.Authorization);
        String body = doPostCookie.getBody();
        System.out.println("登录响应1：" + body);
        if (body.contains(DataManipulation.loginAssert)){
            System.out.println("默认登录成功：" + body);
        }
        System.out.println("默认登录cookie：" + myCookie);
    }

    //Get请求
    public Response doGet(String getUrl) {
        //GET请求
        Response response = null;
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

    //Post请求
    public Response doPost(String postUrl, String postdata) {
        Response response = null;
        try {
//            List<TestBean> testBeanList = new ArrayList<>();
//            formatProcessing(testBeanList,postdata);
//            Map<String,String> map = new HashMap();
//            formatMap(map,postdata);
//            String params = JSON.toJSONString(map);
            HttpURLConnection openConnection = this.postConnection(postUrl);
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
            HttpURLConnection openConnection = this.postConnection(postUrl);
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

    private HttpURLConnection getConnection(String postUrl) throws Exception {
        //得到一个url对象  参数为发送的地址
        URL url = new URL(postUrl);
        HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
        //设置http属性
        openConnection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
        openConnection.setDoOutput(true);// http正文内，因此需要设为true, 默认情况下是false;
        openConnection.setRequestProperty("Content-Type", "application/Json; charset=UTF-8");
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

    private HttpURLConnection postConnection(String postUrl) throws Exception {
        //得到一个url对象  参数为发送的地址
        URL url = new URL(postUrl);
        HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
        openConnection.setRequestMethod("POST");
        //openConnection.setDoInput(true);
        openConnection.setDoOutput(true);
        openConnection.setRequestProperty("Content-Type", "application/Json; charset=UTF-8");
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

    /**
     *      传参处理
     *      param为key=value？key=value形式，转化成list
     */
//    public void formatProcessing(List<TestBean> testBeanList, String param) {
//        String[] nameValue = param.split("&");
//        for (int i = 0; i < nameValue.length; i++) {
//            String key = nameValue[i].split("=")[0];
//            String value = nameValue[i].split("=")[1];
//            TestBean testBean = new TestBean(key, value);
//            testBeanList.add(testBean);
//        }
//    }

//    public void formatMap(Map<String, String> map, String param) {
//        String[] nameValue = param.split("&");
//        for (int i = 0; i < nameValue.length; i++) {
//            String key = nameValue[i].split("=")[0];
//            String value = nameValue[i].split("=")[1];
//            map.put(key, value);
//        }
//    }

}


