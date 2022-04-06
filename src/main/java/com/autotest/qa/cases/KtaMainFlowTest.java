package com.autotest.qa.cases;

import com.autotest.qa.common.ContextHolder;
import com.autotest.qa.common.KTA.KtaUtils;
import com.autotest.qa.common.rpc.KTAInvokeClient;
import com.autotest.qa.utils.KTAPropertiesUtil;
import com.finance.aggregation.api.UserLoginApi;
import com.finance.aggregation.req.user.AccountVerifyReq;
import com.finance.aggregation.req.user.LoginReq;
import com.finance.aggregation.req.user.PhoneSendCodeReq;
import com.finance.aggregation.req.user.SetUserPasswordReq;
import com.finance.aggregation.resp.user.AccountVerifyStepOneResp;
import com.finance.aggregation.resp.user.LoginResp;
import com.rocket.common.def.api.Result;
import com.rocket.common.def.enums.CodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import java.math.BigDecimal;

@Listeners(TestListener.class)
public class KtaMainFlowTest {

    @Autowired
    private static UserLoginApi userLoginApi;
    @BeforeClass
    public static void init(){
        userLoginApi= KTAInvokeClient.buildApi(KTAPropertiesUtil.getEnv("api.domain"),UserLoginApi.class);
    }

    @Test
    public void register(){
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

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
