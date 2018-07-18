package com.epam.coder;


import com.epam.coder.chinesepostman.algorithms.CPP;
import com.epam.coder.chinesepostman.greedy.Greedy;
import com.epam.coder.config.AppConfig;
import com.epam.coder.model.SparseGraph;
import com.epam.coder.parser.GraphParser;
import com.epam.coder.util.SparseGraphBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.List;
import java.util.Objects;

@SpringBootApplication
public class Application implements ApplicationRunner {

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private GraphParser graphParser;

    private static final int GRAPH_COUNT = 2;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        File file = new File(appConfig.getOsmFilePath());
        SparseGraph sparseGraph = SparseGraphBuilder.fromGraph(graphParser.parseXml(file));
        List<SparseGraph> subgraphs = sparseGraph.split(GRAPH_COUNT);
        subgraphs.stream()
                 .filter(Objects::nonNull)
                 .map(Greedy::new)
                 .peek(Greedy::perform)
                 .forEach(CPP::printOut);

        System.exit(0);
    }
}
