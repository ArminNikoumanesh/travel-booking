package com.armin.operation.admin.client;

import com.armin.operation.admin.model.dto.account.ReCaptchaOut;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "reCaptchaClient", url = "${recaptcha.base.url}", configuration = {ErrorDecoder.Default.class})
public interface ReCaptchaClient {
    @PostMapping
    ReCaptchaOut reCaptchaAssessment(@RequestParam("secret") String secret, @RequestParam("response") String response,
                                     @RequestBody Object emptyJson);
}
