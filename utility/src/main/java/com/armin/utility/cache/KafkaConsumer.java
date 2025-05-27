//package com.armin.utility.cache;
//
//import org.springframework.cache.Cache;
//import org.springframework.cache.CacheManager;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//import java.io.UnsupportedEncodingException;
//
//@Service
//public class KafkaConsumer {
//    private final CacheManager cacheManager;
//
//
//    public KafkaConsumer(CacheManager cacheManager) {
//        this.cacheManager = cacheManager;
//    }
//
//    @KafkaListener(topics = "${kafka.cache.topic}", groupId = "#{T(java.util.UUID).randomUUID().toString()}")
//    public void evictCache(byte[] key) throws UnsupportedEncodingException {
//        String keyString = new String(key, "UTF-8");
//        Cache cache = cacheManager.getCache(keyString.substring(1, keyString.length() - 1));
//        if (cache != null) {
//            cache.clear();
//        }
//    }
//}