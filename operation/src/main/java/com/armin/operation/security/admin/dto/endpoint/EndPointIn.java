package com.armin.operation.security.admin.dto.endpoint;

import org.springframework.http.HttpMethod;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author imax on 5/16/19
 */
public class EndPointIn {

    @NotNull
    @Size(max = 260)
    String url;
    @NotNull
    HttpMethod httpMethod;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }


}
