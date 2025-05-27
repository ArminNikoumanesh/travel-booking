package com.armin.utility.config.common;

import com.armin.utility.repository.orm.service.FilterBase;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class NoContentControllerAdvice implements ResponseBodyAdvice<Void> {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        if (binder.getTarget() instanceof FilterBase) {
            StringTrimmerEditor stringTrimmer = new StringTrimmerEditor(true);
            binder.registerCustomEditor(String.class, stringTrimmer);
        }
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getParameterType().isAssignableFrom(void.class);
    }

    @Override
    public Void beforeBodyWrite(Void body, MethodParameter returnType, MediaType mediaType,
                                Class<? extends HttpMessageConverter<?>> converterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (returnType.getParameterType().isAssignableFrom(void.class)) {
            response.setStatusCode(HttpStatus.NO_CONTENT);
        }

        return body;
    }
}
