package com.epam.coder;


import com.epam.coder.chinesepostman.partition.KernighanLin;
import com.epam.coder.config.AppConfig;
import com.epam.coder.model.*;
import com.epam.coder.parser.GraphParser;
import com.epam.coder.util.SparseGraphBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.*;
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
        SparseGraph sparseGraph = SparseGraphBuilder.fromGraph(graph);

        KernighanLin k = KernighanLin.process(sparseGraph);

        System.out.print("Group A: ");
        for (Vertex x : k.getGroupA())
            System.out.print(x);
        System.out.print("\nGroup B: ");
        for (Vertex x : k.getGroupB())
            System.out.print(x);
        System.out.println("");
        System.out.println("Cut cost: " + k.getCutCost());

        /*Greedy alg = new Greedy(sparseGraph);
        alg.perform();
        alg.printOut();*/
        System.exit(0);
    }
}
