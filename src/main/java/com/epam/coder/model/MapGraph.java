package com.epam.coder.model;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.*;

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
            double dis = 6371000 * acos(
                    sin(startNode.getLatitude() * PI / 180) * sin(endNode.getLatitude() * PI / 180) +
                    cos(startNode.getLatitude() * PI / 180) * cos(endNode.getLatitude() * PI / 180) *
                            cos(startNode.getLongitude() * PI / 180 - endNode.getLongitude() * PI / 180  )
            );
            //long distance = GoogleMapsUtils.measureDistance(new LatLng(startNode.getLatitude(), startNode.getLongitude()),new LatLng(endNode.getLatitude(), endNode.getLongitude()));
            //System.out.println("dis - " + dis + " Google dis - " + distance + " dif - " + abs(dis - distance));
            startNode.getEdges().add(new Edge(startPointId, endPointId, round(dis), tags));
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
