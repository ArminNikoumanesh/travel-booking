package com.armin.webservice.shahkar.client;

import com.armin.webservice.shahkar.model.ShahkarIn;
import com.armin.webservice.shahkar.model.ShahkarOut;
import com.armin.webservice.shahkar.statics.constants.ShahkarRestApi;
import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "shahkarClient", url = ShahkarRestApi.VERIFY_SHAHKAR_CLIENT, configuration = {ErrorDecoder.Default.class})
public interface ShahkarClient {
    @PostMapping
    ShahkarOut verify(@RequestBody ShahkarIn model);
}
