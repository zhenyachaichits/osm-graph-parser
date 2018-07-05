package com.epam.coder.util;

import com.epam.coder.model.SparseGraph;
import com.epam.coder.model.Vertex;
import com.epam.coder.model.MapGraph;
import com.epam.coder.model.Node;

import java.util.Map;
import java.util.Set;

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
}
