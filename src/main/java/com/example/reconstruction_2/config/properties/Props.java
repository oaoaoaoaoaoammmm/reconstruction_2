package com.example.reconstruction_2.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt.secret")
public class Props {
    private String mum;
    private int validityAccess;
    private int validityRefresh;
}
