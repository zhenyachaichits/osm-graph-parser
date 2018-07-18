package com.epam.coder.util;

import com.epam.coder.chinesepostman.partition.KernighanLin;
import com.epam.coder.model.*;

import java.util.*;

public class SparseGraphBuilder {

    public static SparseGraph fromGraph(MapGraph graph) {
        SparseGraph sparseGraph = new SparseGraph();
        Map<Long, Node> nodesWithEdges = graph.getNodesWithEdges();
        nodesWithEdges.keySet().stream()
                .map(String::valueOf)
                .map(Vertex::new)
                .forEach(sparseGraph::addVertex);
        nodesWithEdges.entrySet().stream()
                .map(longNodeEntry -> longNodeEntry.getValue().getEdges())
                .flatMap(Set::stream)
                .forEach(sparseGraph::addEdge);
        return sparseGraph;
    }


    public static SparseGraph fromVertexGroup(KernighanLin.VertexGroup group, SparseGraph originalGraph) {
        SparseGraph sparseGraph = new SparseGraph();
        group.forEach(sparseGraph::addVertex);
        group.stream()
             .map(originalGraph::findEdges)
             .flatMap(List::stream)
             .forEach(sparseGraph::addEdge);
        return sparseGraph;
    }
}
