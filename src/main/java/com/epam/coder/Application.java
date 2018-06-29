package com.epam.coder;


import com.epam.coder.config.AppConfig;
import com.epam.coder.model.MapGraph;
import com.epam.coder.parser.GraphParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class Application implements ApplicationRunner {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private GraphParser graphParser;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        File file = new File(appConfig.getOsmFilePath());
        MapGraph graph = graphParser.parseXml(file);
        System.out.println(graph);
    }
}
