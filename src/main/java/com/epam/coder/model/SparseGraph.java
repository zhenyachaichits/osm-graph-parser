package com.epam.coder.model;

import com.epam.coder.chinesepostman.partition.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class SparseGraph {

    private final HashMap<String, ArrayList<Edge>> adj;
    private HashMap<String, Vertex> vertexSet;
    private ArrayList<Edge> edgeSet;
    private String startKey;

    public SparseGraph() {
        adj = new HashMap<>();
        vertexSet = new HashMap<>();
        edgeSet = new ArrayList<>();
        startKey = null;
    }



    public Collection<Vertex> getVertexSet() {
        return vertexSet.values();
    }

    public void setVertexSet(HashMap<String, Vertex> vertexSet) {
        this.vertexSet = vertexSet;
    }

    public Collection<String> getKeys() {
        return vertexSet.keySet();
    }

    public Vertex getVertex(String key) {
        return vertexSet.get(key);
    }

    public Vertex getStartVertex() {
        return vertexSet.get(this.startKey);
    }

    public ArrayList<Edge> getAdj(String key) {
        return adj.get(key);
    }

    public void addVertex(Vertex v) {
        vertexSet.put(v.getKey(), v);
        adj.put(v.getKey(), new ArrayList<>());
    }

    public void addEdge(Edge e) {
        edgeSet.add(e);
        adj.get(String.valueOf(e.getStartPointId())).add(e);
    }

    public Edge edgeAdj(String a, String b) {
        for (Edge edge : this.getAdj(a)) {
            if (String.valueOf(edge.getEndPointId()).equals(b)) {
                return edge;
            }
        }
        return null;
    }

    public void invalidate(String beginKey, String endKey) {
        Edge ab = null;
        for (Edge edge : this.getAdj(beginKey)) {
            if (String.valueOf(edge.getEndPointId()).equals(endKey)) {
                ab = edge;
            }
        }
        if (ab != null && ab.isActive()) {
            ab.setActive(true);
            for (Edge ba : this.getAdj(String.valueOf(ab.getEndPointId()))) {
                if (ba.getEndPointId() == (ab.getStartPointId())) {
                    ba.setActive(false);
                }
            }
        }
    }

    public void decreaseDegree(Edge ab) {
        if (!ab.isActive()) {
            Vertex v = this.getVertex(String.valueOf(ab.getStartPointId()));
            v.heuristic--;
            v = this.getVertex(String.valueOf(ab.getEndPointId()));
            v.heuristic--;
        }
    }

    private void validateAllEdges() {
        for (Edge edge : this.edgeSet) {
            edge.setActive(true);
        }
    }

    public int calculateWayCost(ArrayList<String> circuit) {
        String a, b;
        Edge e;
        int way = 0;

        this.validateAllEdges();

        for (int i = 0; i < circuit.size() - 1; i++) {
            a = circuit.get(i);
            b = circuit.get(i + 1);
            e = this.edgeAdj(a, b);
            this.invalidate(a, b);
            way += e.getDistance();
        }
        return way;
    }

    public Edge findEdge(Vertex v1, Vertex v2) {
        Vertex vertex = vertexSet.get(v1.getKey());
        Edge edge = this.getAdj(v1.getKey()).stream()
                .filter(item -> item.getEndPointId() == v2.getId())
                .findFirst()
                .orElse(null);
        if (edge != null ) {
            return edge;
        }
        edge = this.getAdj(v2.getKey()).stream()
                .filter(item -> item.getEndPointId() == v1.getId())
                .findFirst()
                .orElse(null);
        return edge;
    }

    public String getStartKey() {
        return startKey;
    }

    public void setStartKey(String startKey) {
        this.startKey = startKey;
    }

    public HashMap<String, ArrayList<Edge>> getAdj() {
        return adj;
    }

    public ArrayList<Edge> getEdgeSet() {
        return edgeSet;
    }

    public void setEdgeSet(ArrayList<Edge> edgeSet) {
        this.edgeSet = edgeSet;
    }

    public Pair<Vertex> getEndpoints(Edge edge) {
        return this.edgeSet
                .stream()
                .filter(edge::equals)
                .findFirst()
                .map(edge1 -> new Pair<>(getVertex(String.valueOf(edge.getStartPointId()))
                                        , getVertex(String.valueOf(edge.getEndPointId())))
                ).orElse(null);

    }

    public List<Vertex> getNeighbors(Vertex v) {
        if (!vertexSet.containsKey(v.getKey())) {
            return null;
        }
        Set<Vertex> start = this.getAdj(v.getKey()).stream()
                .map(Edge::getStartPointId)
                .map(String::valueOf)
                .map(vertexSet::get)
                .collect(Collectors.toSet());
        Set<Vertex> end = this.getAdj(v.getKey()).stream()
                .map(Edge::getEndPointId)
                .map(String::valueOf)
                .map(vertexSet::get)
                .collect(Collectors.toSet());
        start.addAll(end);
        return new ArrayList<>(start);
    }
}
