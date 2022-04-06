package com.autotest.qa.cases;


import com.alibaba.fastjson.JSON;
import com.autotest.qa.annotations.DataDriver;
import com.autotest.qa.common.ContextHolder;
import com.autotest.qa.common.ExcelDataHeleper;
import com.autotest.qa.common.HttpRequest;
import com.autotest.qa.common.KTA.KtaUtils;
import com.autotest.qa.common.rpc.FeignService;
import com.autotest.qa.common.rpc.KTAInvokeClient;
import com.autotest.qa.dao.StepType;
import com.autotest.qa.log.StepLogger;
import com.autotest.qa.utils.KTAPropertiesUtil;
import com.finance.aggregation.api.UserLoginApi;
import com.finance.aggregation.req.user.AccountVerifyReq;
import com.finance.aggregation.req.user.PhoneSendCodeReq;
import com.finance.aggregation.resp.user.AccountVerifyStepOneResp;
import com.rocket.common.def.api.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

@Listeners(TestListener.class)
public class TestCase extends ExcelDataHeleper {

    private static final String THIS_EXCEL_PATH="data/Boss.xlsx";
    @Autowired
    private static FeignService feignService;
    @Autowired
    private static UserLoginApi userLoginApi;

    private static final String mobileNo="876532625101";

    private static final String password="a1234556";
    @BeforeClass
    public void init(){
        userLoginApi= KTAInvokeClient.buildApi(KTAPropertiesUtil.getEnv("api.domain"),UserLoginApi.class);
    }

    @Test(dataProvider = "excel")
    @DataDriver(filePath = THIS_EXCEL_PATH,sheetName = "commonPhoneSendCode")
    public void testM(Map param){
        String url= KTAPropertiesUtil.getEnv("api.domain")+ param.get("uri");
        HttpHeaders httpHeaders=new HttpHeaders();
        System.out.println(param.get("mobileNo"));
        String mobileNo=param.get("mobileNo").toString();
        String deviceInfo= KtaUtils.buildAndroidDeviceInfo(mobileNo);
        ContextHolder.setParma("deviceInfo",deviceInfo);
        httpHeaders.set("Device-Info",deviceInfo);
        Map<String,Object> map=new HashMap<>();
        map.put("mobileNo",mobileNo);
        map.put("smsType",1);
        ResponseEntity<String> response= HttpRequest.senRequest(url,httpHeaders,map);
        System.out.println(response);
    }

    @Test(dataProvider = "excel")
    @DataDriver(filePath = THIS_EXCEL_PATH,sheetName = "commonPhoneSendCode")
    public void test(Map param){
        //发送验证码
        PhoneSendCodeReq req=new PhoneSendCodeReq();
        String mobileNo=param.get("mobileNo").toString();
        req.setSmsType(1);
        req.setMobileNo(mobileNo);
        String deviceInfo=KtaUtils.buildAndroidDeviceInfo(mobileNo);
        ContextHolder.setParma("deviceInfo",deviceInfo);
        Result<String> result=userLoginApi.commonPhoneSendCode(req);


    }

    @Test(dataProvider = "excel")
    @DataDriver(filePath = THIS_EXCEL_PATH,sheetName = "accountVerify")
    public void register(Map param){
        try {
            PhoneSendCodeReq req = new PhoneSendCodeReq();
            String mobileNo = KtaUtils.buildRandomMobileNo(9);
            req.setSmsType(1);
            req.setMobileNo(mobileNo);
            String deviceInfo = KtaUtils.buildAndroidDeviceInfo(mobileNo);
            ContextHolder.setParma("deviceInfo", deviceInfo);
            Result<String> result = userLoginApi.commonPhoneSendCode(req);

            AccountVerifyReq req1 = JSON.parseObject(param.get("paramJson").toString(), AccountVerifyReq.class);
            if (!"N".equals(param.get("needToken"))) {
                req1.setToken(result.getData());
            }
            Result<AccountVerifyStepOneResp> result1 = userLoginApi.accountVerify(req1);
            Assert.assertEquals(result1.getMsg().toString(), param.get("expectedResult").toString());
        }catch (Exception e){
            e.printStackTrace();
            StepLogger.logStepFail(StepType.ACTION,"注册用户账户","注册用户失败,信息为"+e.getMessage());
        }finally {
            ContextHolder.removeKey("deviceInfo");
        }
    }



}
