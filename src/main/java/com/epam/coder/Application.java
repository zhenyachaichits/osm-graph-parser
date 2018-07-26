package com.epam.coder;


import com.epam.coder.chinesepostman.greedy.Greedy;
import com.epam.coder.config.AppConfig;
import com.epam.coder.csv.RouteCsvGenerator;
import com.epam.coder.model.MapGraph;
import com.epam.coder.model.SparseGraph;
import com.epam.coder.parser.GraphParser;
import com.epam.coder.util.SparseGraphBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Objects;

@SpringBootApplication
public class Application implements ApplicationRunner {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private GraphParser graphParser;

    private static final int GRAPH_COUNT = 128;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        File file = new File(appConfig.getOsmFilePath());
        MapGraph mapGraph = graphParser.parseXml(file);
        SparseGraph sparseGraph = SparseGraphBuilder.fromGraph(mapGraph);
        List<SparseGraph> subgraphs = sparseGraph.split(GRAPH_COUNT);

        String csvFile = "routes.csv";
        FileWriter writer = new FileWriter(csvFile);


        final int[] count = {0};
        subgraphs.stream()
                .filter(Objects::nonNull)
                .map(Greedy::new)
                .peek(Greedy::perform)
                .forEach(route -> RouteCsvGenerator.writeRoute(writer, mapGraph, route, count[0]++));

        writer.flush();
        writer.close();

        System.exit(0);
    }
}
