package com.example.amisbook002;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
public class decryptByPublicKey {
    //获取公钥
    static String RSA_PUB_KEY="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCEitzF+mZYpJonCKSRsdu5YFSFhbeB1MTzhswRmK8EocXivHK4eAOUgCRt+N9jKRAMzcmiLO1xJU5ZMNXbcW1qmPrFeRlEYppirQQuVqzFiKoh8HKkOgvCv2eDx1G2BIlLQbRSY9YLoLQWmje2xVyNxp8LaQv+Wym0euMDHNJbSwIDAQAB";
    static String KEY_ALGORITHM = "RSA";
    static String SIGNATURE_ALGORITHM = "MD5withRSA";
    static int MAX_ENCRYPT_BLOCK = 117;
    static int MAX_DECRYPT_BLOCK = 128;
    private static final String CIPHER_TYPE = "RSA/None/PKCS1Padding"; // Android端加密算法

    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    public  byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicK);
        int inputLen = encryptedData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }
    public  byte[] encryptByPublicKeys(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }
    public static byte[] encryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        byte[] keyBytes = Base64.decodeBase64(publicKey);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicK = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return encryptedData;
    }
    //需要加密的用户密码
//    public static void main(String[] args) throws Exception {
//        String str = "123456";
//        String result ="";
//        try {
//
//            result = Base64.encodeBase64String(encryptByPublicKey(str.getBytes(), RSA_PUB_KEY));
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        System.out.println("密码："+result);
//        Request ss = new Request();
//        String s= "{\n" +
//                "  \"loginName\": \"0215测试001\",\n" +
//                "  \"password\": \"{RSA}"+result+"\",\n" +
//                "  \"returnUrl\": \"/home\"\n" +
//                "}";
//        System.out.println("密码rsa："+s);
//        String url = "http://apis.develop.edge.customs.dev.amiintellect.com/api/connect/auth/authorize";
//        Response response = ss.doPost(url, s);
//        System.out.println("响应："+response.getBody());
//    }
//    public static byte[] encryptByPublicKeya(String data, String key) throws Exception {
//        // 对公钥解密
//        byte[] keyBytes = decryptBASE64(key);
//        // 取得公钥
//        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
//        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
//        Key publicKey = keyFactory.generatePublic(x509KeySpec);
//        // 对数据加密(分段)
//        Cipher cipher = Cipher.getInstance(CIPHER_TYPE);
//        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//        byte[] dataByteArray = data.getBytes();
//        int inputLen = dataByteArray.length;
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        int offSet = 0;
//        byte[] cache;
//        int i = 0;
//        // 对数据分段加密
//        while (inputLen - offSet > 0) {
//            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
//                cache = cipher.doFinal(dataByteArray, offSet, MAX_ENCRYPT_BLOCK);
//            } else {
//                cache = cipher.doFinal(dataByteArray, offSet, inputLen - offSet);
//            }
//            out.write(cache, 0, cache.length);
//            i++;
//            offSet = i * MAX_ENCRYPT_BLOCK;
//        }
//        byte[] encryptedData = out.toByteArray();
//        out.close();
//        return encryptedData;
//    }

}
