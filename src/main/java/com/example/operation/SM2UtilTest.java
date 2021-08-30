package com.example.operation;

import java.util.Arrays;


public class SM2UtilTest {
    /** 元消息串 */

    private static String M="a=1|b=33|c=韩";
    public static void main(String[] args) {
        SM2Util sm2 = new SM2Util();
        SM2KeyPair keyPair = sm2.generateKeyPair();
        //加密
//        byte[] data = sm2.encrypt(M,keyPair.getPublicKey());
//        System.out.println("data is:"+Arrays.toString(data));
//        System.out.println("keyPair.getPublicKey() is:"+keyPair.getPublicKey());
//        System.out.println("keyPair.getPrivateKey() is:"+keyPair.getPrivateKey());
//        //解密
//        sm2.decrypt(data, keyPair.getPrivateKey());
        //
        String AmiPublicKey ="23F029602D80C31EEEFEB84F8B8516C93EF67E7C3092FECDBF593F7E3E0DE0E" +
                "AAEEEFEDD99DD398E2A6E8E52086722D4FE78DB17ABA434B5B1670B731FCA7468";
        String AmiPrivateKey = "000EFB046882628841AC4272D14020F37804A12F66150E45E07C343A5A73B60";
        byte[] data = sm2.encrypt(M,keyPair.getPublicKey());
        System.out.println("data is:"+Arrays.toString(data));
        System.out.println("keyPair.getPublicKey() is:"+keyPair.getPublicKey());
        System.out.println("keyPair.getPrivateKey() is:"+keyPair.getPrivateKey());
        //解密
        sm2.decrypt(data, keyPair.getPrivateKey());
    }

}