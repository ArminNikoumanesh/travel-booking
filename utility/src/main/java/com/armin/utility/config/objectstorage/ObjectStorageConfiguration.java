//package com.armin.utility.config.objectstorage;
//
//import com.armin.utility.config.cloud.BaseApplicationProperties;
//import io.minio.MinioClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
//public class ObjectStorageConfiguration {
//    private final BaseApplicationProperties applicationProperties;
//
//    @Autowired
//    public ObjectStorageConfiguration(BaseApplicationProperties applicationProperties) {
//        this.applicationProperties = applicationProperties;
//    }
//
//    @Bean
//    public MinioClient minioClient() {
//        return MinioClient.builder()
//                .endpoint(applicationProperties.getMinioConfig().getUrl())
//                .credentials(applicationProperties.getMinioConfig().getAccessKey(), applicationProperties.getMinioConfig().getSecretKey())
//                .build();
//    }
//}
