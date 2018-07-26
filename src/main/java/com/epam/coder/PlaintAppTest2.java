package com.epam.coder;

import com.epam.coder.chinesepostman.greedy.Greedy;
import com.epam.coder.csv.RouteCsvGenerator;
import com.epam.coder.model.MapGraph;
import com.epam.coder.model.SparseGraph;
import com.epam.coder.parser.GraphParser;
import com.epam.coder.parser.osm.OsmMapParser;
import com.epam.coder.util.SparseGraphBuilder;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class PlaintAppTest2 {

    private static final int GRAPH_COUNT = 128;

    public static void main(String[] args) throws JAXBException, IOException {
        GraphParser graphParser = new OsmMapParser();

        File file = new File("map_astana_1.osm");
        MapGraph mapGraph = graphParser.parseXml(file);
        SparseGraph sparseGraph = SparseGraphBuilder.fromGraph(mapGraph);

        System.out.println("Graph parsed from file");
        List<SparseGraph> subgraphs = sparseGraph.split(GRAPH_COUNT);
        System.out.println("Graph split into " + subgraphs.size() + "  parts");

        final int[] count = {0};
        subgraphs.parallelStream()
                .filter(Objects::nonNull)
                .map(Greedy::new)
                .peek(Greedy::perform)
                .forEach(route -> {
                    try {
                        String csvFile = "routeSm" + count[0] + ".csv";
                        FileWriter writer = new FileWriter(csvFile);
                        RouteCsvGenerator.writeRoute(writer, mapGraph, route, count[0]++);

                        writer.flush();
                        writer.close();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });


        System.exit(0);
    }

}
