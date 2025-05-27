package com.armin.utility.config.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import java.util.Set;
import java.util.concurrent.Executor;

/**
 * Application Configuration
 * Like XML based Dispatcher Servlet
 *
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */

@EnableAsync
@Configuration
@EnableRedisRepositories(basePackages = "com.armin.*")
public class ApplicationConfig implements AsyncConfigurer {
    @Value("${spring.redis.sentinel.master}")
    private String masterName;
    @Value("${spring.redis.sentinel.nodes}")
    private Set<String> redisSentinelNodes;

//    @Bean
//    public AspectConfig appLogger() {
//        return new AspectConfig();
//    }

    @Bean
    public HttpFirewall allowUrlSemicolonHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowSemicolon(true);
        return firewall;
    }

//    @Bean
//    public MethodValidationPostProcessor methodValidationPostProcessor() {
//        return new MethodValidationPostProcessor();
//    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisSentinelConfiguration(masterName, redisSentinelNodes));
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }

    @Override
    public Executor getAsyncExecutor() {
        return null;
    }
}
