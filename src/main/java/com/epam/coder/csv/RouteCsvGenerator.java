package com.epam.coder.csv;

import com.epam.coder.chinesepostman.algorithms.CPP;
import com.epam.coder.chinesepostman.greedy.Greedy;
import com.epam.coder.model.MapGraph;
import com.epam.coder.model.Node;
import com.epam.coder.model.SparseGraph;
import com.epam.coder.util.SparseGraphBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RouteCsvGenerator {
    public static void writeRoute(FileWriter writer, MapGraph mapGraph, CPP cpp, int count) {
        List<String> stringCoordinatesList = cpp.getOutput().stream()
                .map(id -> {
                    Node node = mapGraph.getNodes().get(Long.valueOf(id));
                    return node.getLatitude() + "," + node.getLongitude();
                }).collect(Collectors.toList());

        try {
            CsvUtils.writeLine(writer, Arrays.asList(String.valueOf(count), String.join(";", stringCoordinatesList)),
                    ',', '"');
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
