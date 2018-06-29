package com.epam.coder.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:parser.properties")
public class AppConfig {

    @Value("${osm.filepath}")
    String osmFilePath;

    public String getOsmFilePath() {
       return osmFilePath;
    }


}