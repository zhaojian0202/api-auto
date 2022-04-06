package com.autotest.qa.common.rpc;

import com.finance.aggregation.req.user.PhoneSendCodeReq;
import com.rocket.common.def.api.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="java-aggregation-app",url="http://192.168.12.115:8021/api/v1/user")
@Component
public interface FeignService {
    @RequestMapping(value = "/commonPhoneSendCode", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Result<String> commonPhoneSendCode(PhoneSendCodeReq req);
}
