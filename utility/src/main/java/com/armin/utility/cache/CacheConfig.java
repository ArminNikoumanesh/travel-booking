package com.armin.utility.cache;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 **/
@Configuration
public class CacheConfig extends CachingConfigurerSupport {

    @Bean("customKeyGenerator")
    public KeyGenerator keyGenerator() {
        return new CustomKeyGenerator();
    }
}
