package com.steps.thread;

import java.io.*;

/**
 * @author fx
 * @date 2021-10-24 22:01
 */
public class Encryption {
    private static final int key = 0x99; //加密解密秘钥
    public static void main(String[] args) {

        File srcFile = new File("C:\\json.json");    //初始文件
        File encFile = new File("C:\\encJson.json"); //加密文件
        File decFile = new File("C:\\decJson.json"); //解密文件

        try {
            //EncFile(srcFile, encFile);
            DecFile(encFile, decFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密文件
     * @param srcFile
     * @param encFile
     * @throws Exception
     */
    private static void EncFile(File srcFile, File encFile) throws Exception {
        if (!srcFile.exists()) {
            System.out.println("source file not exixt");
            return;
        }

        if (!encFile.exists()) {
            System.out.println("encrypt file created");
            encFile.createNewFile();
        }
        try (InputStream fis = new FileInputStream(srcFile);
             OutputStream fos = new FileOutputStream(encFile)) {
            int buf = 0;
            while ((buf = fis.read()) > -1) {
                fos.write(buf ^ key);
            }
        }
    }

    /**
     * 文件解密
     * @param encFile   加密文件
     * @param decFile   解密的文件
     * @throws Exception
     */
    private static void DecFile(File encFile, File decFile) throws Exception {
        if (!encFile.exists()) {
            System.out.println("encrypt file not exixt");
            return;
        }

        if (!decFile.exists()) {
            System.out.println("decrypt file created");
            decFile.createNewFile();
        }

        try (InputStream fis = new FileInputStream(encFile);
             OutputStream fos = new FileOutputStream(decFile)) {
            int buf = 0;
            while ((buf = fis.read()) > -1) {
                fos.write(buf ^ key);
            }
        }

    }
}
