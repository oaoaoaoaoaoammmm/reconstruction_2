package com.example.reconstruction_2;

import com.example.reconstruction_2.config.properties.Props;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({Props.class})
public class Reconstruction2Application {
    public static void main(String[] args) {
        SpringApplication.run(Reconstruction2Application.class, args);
    }
}

