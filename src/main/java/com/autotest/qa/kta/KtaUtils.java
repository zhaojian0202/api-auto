package com.autotest.qa.kta;


import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.autotest.qa.common.ContextHolder;
import com.autotest.qa.kta.KTAConstant;
import com.finance.aggregation.resp.file.AttachmentResp;
import com.rocket.common.def.enums.CodeEnum;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
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
     * 上传文件接口
     * @param url
     * @param filePath
     * @return
     */
    public static String uploadFile(String url, String filePath, String type) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(filePath);
            InputStream inputStream = classPathResource.getInputStream();
            File file = File.createTempFile("temp", "jpg");
            try {
                FileUtils.copyInputStreamToFile(inputStream, file);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
            MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
            bodyMap.add("attachment", new FileSystemResource(file));
            bodyMap.add("type", type);
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            headers.add("connection", "Keep-Alive");
            headers.add("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            headers.add("Accept-Charset", "utf-8");
            headers.add(KTAConstant.HEADER_AUTHORIZATION, ContextHolder.getParmaByKey("authorization").toString());
            headers.add(KTAConstant.HEADER_DEVICE_INFO, ContextHolder.getParmaByKey("deviceInfo").toString());
            headers.add(KTAConstant.HEADER_CONTENT_LANGUAGE, "zh_CN");
            headers.add("Content-Type", MediaType.MULTIPART_FORM_DATA_VALUE + ";boundary=" + UUID.randomUUID().toString());
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            String body = response.getBody();
            JSONObject jsonObject = JSON.parseObject(body);
            if (CodeEnum.S_000000.getCode().equals(jsonObject.getString("code"))) {
                AttachmentResp attachmentResp = jsonObject.getObject("data", AttachmentResp.class);
                return attachmentResp.getFilePath();
            }
            System.err.println("上传图片失败,返回结果==>" + body);
            return null;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args){
        String sessionId = "session-id=7ddfb8c2517b4bfbb37201d2495a21c7; Domain=pendanaan.net; Path=/";
        sessionId = sessionId.split(";")[0].split("=")[1];
        System.out.println(sessionId);
    }

}
