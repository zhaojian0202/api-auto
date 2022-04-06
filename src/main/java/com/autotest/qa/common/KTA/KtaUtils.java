package com.autotest.qa.common.KTA;


import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;



/**
 * KTA 业务相关的工具方法
 * 如：生成请求的Device-Info
 */
public class KtaUtils {


    /**
     * 随机生产安卓的设备信息
     * @return
     */
    public  static  String buildAndroidDeviceInfo(String mobileNo){
        String originStr = "{\"adChannel\":\"GooglePlay\",\"deviceNo\":\"\",\"androidId\":\"\",\"appName\":\"KtaKilat\",\"packageName\":\"com.ktakilat.loan\",\"appVersion\":\"4.0.0\",\"countryCode\":\"ID\",\"countryName\":\"Indonesia\",\"deliveryPlatform\":\"google play\",\"adId\":\"\",\"imei\":\"\",\"imsi\":\"\",\"mac\":\"48:2c:a0:50:13:d1\",\"phoneBrand\":\"Xiaomi\",\"phoneBrandModel\":\"MI 8 SE\",\"systemPlatform\":\"android\",\"systemVersion\":\"10\",\"uuid\":\"\"}";
        //根据手机号生成deviceNo
        String androidId = MD5.create().digestHex16(mobileNo.getBytes(StandardCharsets.UTF_8));
        JSONObject deviceJson = JSONObject.parseObject(originStr);
        deviceJson.put("deviceNo",androidId);
        deviceJson.put("androidId",androidId);
        deviceJson.put("uuid",androidId+"_"+androidId);
        //System.out.println("当前获取到的deviceJson==>"+deviceJson.toJSONString());
        //转为Base64
        String deviceInfoBase64Str = Base64.getEncoder().encodeToString(deviceJson.toJSONString().getBytes());
        return deviceInfoBase64Str;
    }


    /**
     * 随机获取一个x位的印尼手机号码
     * @return
     */
    public static String buildRandomMobileNo(int length){
        if (7 > length || 10 < length){
            throw new IllegalArgumentException("手机号码7-10位");
        }
        String randomStr = System.currentTimeMillis() + "";
        randomStr = randomStr.substring(randomStr.length()-(length-2));
        return 8 + randomStr;
    }

    /**
     * 随机生产一个密码
     * @return
     */
    public static String buildRandomPassWord(int passLength){
        if (6 > passLength || 32 < passLength){
            throw new IllegalArgumentException("密码长度为8-32位");
        }
        String randomStr = System.currentTimeMillis() + "";
        randomStr =  UUID.randomUUID().toString().replace("-",randomStr);
        return randomStr.substring(randomStr.length() - passLength);
    }


    /**
     * 线程休息一段时间
     * @param seconds
     */
    public static void sleep(int seconds){
        try {
            Thread.sleep(seconds * 1000L);
        }catch (Exception e){
            System.err.println("sleep error");
        }
    }


    /**
     * 获取后面一天的时间
     * @param nowTime
     * @return
     */
    public static Long getNextDay(Long nowTime){
        return getFewDays(nowTime,1);
    }

    /**
     * 获取几天后的时候
     * @param nowTime
     * @param number
     * @return
     */
    public static Long getFewDays(Long nowTime,Integer number){
        Long one = 1000L*60*60*24;
        return  nowTime + one * number;
    }

    /**
     * 接收处理传过来的文件
     * @param file MultipartFile 类型的文件
     * @return
     */
    public static File convert(MultipartFile file) {
        File convFile = new File(file.getOriginalFilename());
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convFile;
    }


    /**
     * 将file转换成fileItem
     * @param file
     * @param fieldName
     * @return
     */
    private static FileItem getMultipartFile(File file, String fieldName){
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem(fieldName, "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }


    public static void main(String[] args){
        String sessionId = "session-id=7ddfb8c2517b4bfbb37201d2495a21c7; Domain=pendanaan.net; Path=/";
        sessionId = sessionId.split(";")[0].split("=")[1];
        System.out.println(sessionId);
    }

}
