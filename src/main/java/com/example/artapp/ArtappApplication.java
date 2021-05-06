package com.example.artapp;

import com.example.artapp.service.notification.EmailObserver;
import com.example.artapp.service.notification.LogObserver;
import com.example.artapp.service.notification.MainPostSubject;
import com.example.artapp.service.storage.StorageProperties;
import com.example.artapp.service.storage.StorageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class ArtappApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtappApplication.class, args);
    }

    @Bean
    CommandLineRunner init(StorageService storageService,
                           MainPostSubject mainPostSubject,
                           EmailObserver emailObserver,
                           LogObserver logObserver
                           ) {
        return (args) -> {
            //storageService.deleteAll();
            storageService.init();
            emailObserver.setSubject(mainPostSubject);
            logObserver.setSubject(mainPostSubject);
        };
    }


}
