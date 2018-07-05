package com.epam.coder.model;

import java.util.HashMap;
import java.util.Map;

public class MapGraph {

    /*
        Streets graph map.
        Key - OsmNode ID (from OSM)
        Value - OsmNode
     */
    private final Map<Long, Node> graph;

    public MapGraph() {
        this.graph = new HashMap<>();
    }

    public MapGraph(Map<Long, Node> graph) {
        this.graph = graph;
    }

    public void addNode(Node node) {
        graph.put(node.getId(), node);
    }

    public void addEdge(long startPointId, long endPointId, Map<String, String> tags) {
        Node startNode = graph.get(startPointId);
        Node endNode = graph.get(endPointId);

        if (startNode != null && endNode != null) {
            long leftLimit = 1L;
            long rightLimit = 10L;
            long generatedLong = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
            long distance = generatedLong;//GoogleMapsUtils.measureDistance(new LatLng(startNode.getLatitude(), startNode.getLongitude()),new LatLng(endNode.getLatitude(), endNode.getLongitude()));

            startNode.getEdges().add(new Edge(startPointId, endPointId, distance, tags));
        }
    }

    public Map<Long, Node> getNodes() {
        return graph;
    }

    public Map<Long, Node> getNodesWithEdges() {
        Map<Long, Node> map = new HashMap<>();
        for (Map.Entry<Long, Node> entry : graph.entrySet()) {
            if (!entry.getValue().getEdges().isEmpty()) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map;
    }
}
