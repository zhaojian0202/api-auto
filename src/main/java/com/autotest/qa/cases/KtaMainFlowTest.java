package com.autotest.qa.cases;

import com.autotest.qa.common.ContextHolder;
import com.autotest.qa.kta.KtaUtils;
import com.autotest.qa.common.rpc.KTAInvokeClient;
import com.autotest.qa.dao.StepType;
import com.autotest.qa.log.StepLogger;
import com.autotest.qa.utils.PropertiesUtil;
import com.finance.aggregation.api.*;
import com.finance.aggregation.req.user.*;
import com.finance.aggregation.resp.user.*;
import com.rocket.common.def.api.Result;
import com.rocket.common.def.enums.CodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

import java.math.BigDecimal;

public class KtaMainFlowTest {

    @Autowired
    private static UserLoginApi userLoginApi;

    @Autowired
    private static UserProfileApi userProfileApi;

    private static UserBankCardApi userBankCardApi;

    private static UserIdentityApi userIdentityApi;

    private static UserContactApi userContactApi;

    private static UserJobInfoApi userJobInfoApi;

    private static HttpHeaders headers = new HttpHeaders();


    static {
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Tenant","{\"timeZone\":\"GMT+8\",\"appName\":\"KtaKilat\",\"countryCode\":\"ID\",\"datasource\":\"kta1\",\"countryName\":\"Indonesia\",\"tenant\":\"KTAID\",\"currency\":\"IDR\",\"dateFormat\":\"yyyy/MM/dd\",\"dateTimeFormat\":\"yyyy-MM-dd hh:mm:ss\"}");
    }


    @BeforeTest
    public static void init(){
        userLoginApi= KTAInvokeClient.buildApi(PropertiesUtil.getEnv("api.domain"),UserLoginApi.class);
        userBankCardApi = KTAInvokeClient.buildApi(PropertiesUtil.getEnv("api.domain"),UserBankCardApi.class);
        userProfileApi = KTAInvokeClient.buildApi(PropertiesUtil.getEnv("api.domain"),UserProfileApi.class);
        userIdentityApi= KTAInvokeClient.buildApi(PropertiesUtil.getEnv("api.domain"),UserIdentityApi.class);
        userContactApi = KTAInvokeClient.buildApi(PropertiesUtil.getEnv("api.domain"),UserContactApi.class);
        userJobInfoApi = KTAInvokeClient.buildApi(PropertiesUtil.getEnv("api.domain"),UserJobInfoApi.class);
    }


    public  static String register(){
        userLoginApi= KTAInvokeClient.buildApi(PropertiesUtil.getEnv("api.domain"),UserLoginApi.class);
        userBankCardApi = KTAInvokeClient.buildApi(PropertiesUtil.getEnv("api.domain"),UserBankCardApi.class);
        userProfileApi = KTAInvokeClient.buildApi(PropertiesUtil.getEnv("api.domain"),UserProfileApi.class);
        userIdentityApi= KTAInvokeClient.buildApi(PropertiesUtil.getEnv("api.domain"),UserIdentityApi.class);
        userContactApi = KTAInvokeClient.buildApi(PropertiesUtil.getEnv("api.domain"),UserContactApi.class);
        userJobInfoApi = KTAInvokeClient.buildApi(PropertiesUtil.getEnv("api.domain"),UserJobInfoApi.class);
        try {
            String commonPassword = "a123456";
            //生成用户手机号码
            String mobileNo = KtaUtils.buildRandomMobileNo(9) + 01;

            //生成deviceInfo
            String deviceInfo = KtaUtils.buildAndroidDeviceInfo(mobileNo);
            ContextHolder.setParma("deviceInfo", deviceInfo);
            ContextHolder.setParma("mobileNo", mobileNo);
            //发送短信验证码
            PhoneSendCodeReq sendCodeReq = new PhoneSendCodeReq();
            sendCodeReq.setMobileNo(mobileNo);
            sendCodeReq.setSmsType(1);
            Result<String> result = userLoginApi.commonPhoneSendCode(sendCodeReq);
            Assert.assertEquals(result.getCode(), CodeEnum.S_000000.getCode(), "校验发送验证码接口返回结果:"+result.getMsg());
            //注册
            AccountVerifyReq verifyReq = new AccountVerifyReq();
            verifyReq.setRegChannel("KTA Autotest");
            verifyReq.setToken(result.getData());
            verifyReq.setLoginLat(BigDecimal.valueOf(12.12313));
            verifyReq.setLoginLng(BigDecimal.valueOf(112.9771));
            verifyReq.setChannel("Google Play");
            verifyReq.setCode("123456");
            Result<AccountVerifyStepOneResp> verifyResult = userLoginApi.accountVerify(verifyReq);
            Assert.assertEquals(verifyResult.getCode(), CodeEnum.S_000000.getCode(), "校验注册接口返回结果");
            //设置密码
            String authToken = verifyResult.getData().getToken();
            ContextHolder.setParma("authorization", authToken);
            SetUserPasswordReq userPasswordReq = new SetUserPasswordReq();
            userPasswordReq.setPassword(commonPassword);
            Result<Boolean> passwordResult = userLoginApi.setPassword(userPasswordReq);
            Assert.assertEquals(passwordResult.getCode(), CodeEnum.S_000000.getCode(), "校验设置密码接口返回结果");
            //用户登录
            LoginReq loginReq = new LoginReq();
            loginReq.setMobileNo(mobileNo);
            loginReq.setPassword(commonPassword);
            loginReq.setLoginLat(BigDecimal.valueOf(12.12313));
            loginReq.setLoginLng(BigDecimal.valueOf(112.9771));
            Result<LoginResp> loginResult = userLoginApi.login(loginReq);
            Assert.assertEquals(loginResult.getCode(), CodeEnum.S_000000.getCode(), "校验登录接口返回结果");
            return "注册成功";
        }catch(Exception e){
            e.printStackTrace();
            return e.toString();
        }
    }

    @Test
    public void uploadUserInfo(){
        //上传基本资料
        UserProfileReq profileReq = new UserProfileReq();
        profileReq.setAddress("KTA Autotest User Profile Address #12.31");
        profileReq.setPassword("a123456");
        profileReq.setProvinceId(24);
        profileReq.setCityId(401);
        profileReq.setDistrictId(5648);
        profileReq.setWhatsApp("816766780");
        profileReq.setResidenceTime(2);
        profileReq.setResidenceType(2);
        profileReq.setMarriage(2);
        //不校验ws号码
//        profileReq.setIsValidWhatsApp(0);
        profileReq.setAreaName("Ahoad jih31");
        Result<UserProfileUpdateResp> profileResult = userProfileApi.createOrUpdate(profileReq);
        Assert.assertEquals(profileResult.getCode(), CodeEnum.S_000000.getCode(),"校验上传用户资料返回结果");
        //上传工作信息
        UserJobReq userJobReq = new UserJobReq();
        userJobReq.setAddress("KTA Autotest User Job Address #12.31");
        userJobReq.setIndustry(1);
        userJobReq.setUserEmail("Autotest@pendanaan.com");
        userJobReq.setIncome("16666666");
        userJobReq.setName("Pendanaan.com");
        userJobReq.setAreaName("ces1");
        userJobReq.setPayday(12);
        userJobReq.setProvinceId(24);
        userJobReq.setCityId(401);
        userJobReq.setDistrictId(5648);
        Result<Integer> result = userJobInfoApi.createOrUpdate(userJobReq);
        Assert.assertEquals(result.getCode(), CodeEnum.S_000000.getCode(),"上传工作信息返回结果");
        //上传联系人信息
        UserContactReq userContactReq = new UserContactReq();
        UserContactDto user1 = new UserContactDto();
        user1.setMobileNo("877777771");
        user1.setName("contact1");
        user1.setRelationship(1);
        user1.setType(1);
        UserContactDto user2 = new UserContactDto();
        user2.setMobileNo("877777772");
        user2.setName("contact2");
        user2.setRelationship(12);
        user2.setType(2);
        userContactReq.setContacts(Lists.newArrayList(user1,user2));
        Result<Integer> contactResult = userContactApi.createOrUpdate(userContactReq);
        Assert.assertEquals(contactResult.getCode(), CodeEnum.S_000000.getCode(),"上传联系人信息返回结果");
        //上传银行卡信息
        UserBankCardReq bankCardReq = new UserBankCardReq();
        bankCardReq.setBankNo("66789987654567123");
        bankCardReq.setBankCode("SBS");
        bankCardReq.setHolder("Ajk Simo");
        bankCardReq.setNo("66789987654567121");
        bankCardReq.setIsHolderMatched(true);
        bankCardReq.setVerifiedHolder("Ajk Slimoo");
        Result<Integer> bankResult = userBankCardApi.createOrUpdate(bankCardReq);
        Assert.assertEquals(bankResult.getCode(), CodeEnum.S_000000.getCode(),"上传银行卡信息返回结果");
        //上传EKTP身份信息
        int loop = 0 ;
        String url = PropertiesUtil.getEnv("api.domain")+"/api/v1/attachment/upload";
        String ektpFileUrl = KtaUtils.uploadFile(url,"images/ektp.jpg","one");
        while (StringUtils.isEmpty(ektpFileUrl) && loop < 5){
            ektpFileUrl = KtaUtils.uploadFile(url,"images/ektp.jpg","one");
            loop ++ ;
        }
        Assert.assertFalse(StringUtils.isEmpty(ektpFileUrl),"校验ektp上传结果是否为空");
        if (loop >= 5){
            //终止测试
            StepLogger.logStepFail(StepType.ACTION,"上传EKTP图片","图片上传重试了5次还没有成功");
            ContextHolder.interruptTest();
            return;
        }
        //开始上传人脸图片
        loop = 0;
        String faceFileUrl = KtaUtils.uploadFile(url,"images/face1.jpg","two");
        while (StringUtils.isEmpty(faceFileUrl) && loop < 5){
            faceFileUrl = KtaUtils.uploadFile(url,"images/face1.jpg","two");
            loop ++;
        }
        Assert.assertFalse(StringUtils.isEmpty(faceFileUrl),"校验face图片上传结果是否为空");
        if (loop >= 5){
            //终止测试
            StepLogger.logStepFail(StepType.ACTION,"上传人脸图片","图片上传重试了5次还没有成功");
            ContextHolder.interruptTest();
            return;
        }
        IdCardOcrImgReq faceReq = new IdCardOcrImgReq();
        faceReq.setUrlPath(faceFileUrl);
        Result<UploadSelfieImgResp> faceResult = userIdentityApi.uploadSelfieImg(faceReq);
        Assert.assertEquals(faceResult.getCode(), CodeEnum.S_000000.getCode(),"上传人脸照片");
        IdCardOcrImgReq ektpReq = new IdCardOcrImgReq();
        ektpReq.setUrlPath(ektpFileUrl);
        Result<UserIdentityImgeResp> result1 = userIdentityApi.idCardOcr(ektpReq);
        Assert.assertEquals(result1.getCode(), CodeEnum.S_000000.getCode(),"EKTP OCR识别结果");
        //上传用户信息
        UserIdentityReq identityReq = new UserIdentityReq();
        identityReq.setName(result1.getData().getOcrName());
        identityReq.setIdNo(result1.getData().getOcrIdNo());
        identityReq.setOcrId(result1.getData().getOcrId());
        identityReq.setFaceId(faceResult.getData().getFaceId());
        Result<IdentitySubmitResp> result2 = userIdentityApi.identitySubmit(identityReq);
        Assert.assertEquals(result2.getCode(), CodeEnum.S_000000.getCode(),"上传用户身份信息返回结果");
    }


    @Test
    public void registerAndUploadUserInfo(){
        register();
        uploadUserInfo();

    }
}
