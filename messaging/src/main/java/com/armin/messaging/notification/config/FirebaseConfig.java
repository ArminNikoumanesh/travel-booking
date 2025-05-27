package com.armin.messaging.notification.config;//package com.armin.messaging.notification.config;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import com.google.firebase.messaging.FirebaseMessaging;
//import org.springframework.context.annotation.Bean;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class FirebaseConfig {
//
//    @Bean
//    FirebaseMessaging firebaseMessaging() throws IOException {
//        GoogleCredentials googleCredentials = GoogleCredentials
//                .fromStream(new ClassPathResource("google-services.json").getInputStream());
//        FirebaseOptions firebaseOptions = FirebaseOptions
//                .builder()
//                .setCredentials(googleCredentials)
//                .build();
//        FirebaseApp app = FirebaseApp.initializeApp(firebaseOptions, "BRZ");
//        return FirebaseMessaging.getInstance(app);
//    }
//}
