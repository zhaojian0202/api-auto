import com.autotest.qa.annotations.DataDriver;
import com.autotest.qa.common.ContextHolder;
import com.autotest.qa.common.ExcelDataHeleper;
import com.autotest.qa.common.HttpRequest;
import com.autotest.qa.common.KTA.KtaUtils;
import com.autotest.qa.common.rpc.FeignService;
import com.autotest.qa.common.rpc.KTAInvokeClient;
import com.autotest.qa.utils.KTAPropertiesUtil;
import com.finance.aggregation.api.UserLoginApi;
import com.finance.aggregation.req.user.PhoneSendCodeReq;
import com.rocket.common.def.api.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

@Component
public class TestCase extends ExcelDataHeleper {

    private static final String THIS_EXCEL_PATH="data/Boss.xlsx";
    @Autowired
    private static FeignService feignService;
    @Autowired
    private static UserLoginApi userLoginApi;

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

    @Test
    public void test(){
        PhoneSendCodeReq req=new PhoneSendCodeReq();
        String mobileNo=KtaUtils.buildRandomMobileNo(9);
        req.setSmsType(1);
        req.setMobileNo(mobileNo);
        String deviceInfo=KtaUtils.buildAndroidDeviceInfo(mobileNo);
        ContextHolder.setParma("deviceInfo",deviceInfo);
        Result<String> result=userLoginApi.commonPhoneSendCode(req);
    }



}
