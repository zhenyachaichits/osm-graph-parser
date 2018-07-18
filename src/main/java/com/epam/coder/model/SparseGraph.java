package com.epam.coder.model;

import com.epam.coder.chinesepostman.partition.KernighanLin;
import com.epam.coder.chinesepostman.partition.Pair;
import com.epam.coder.util.SparseGraphBuilder;

import java.util.*;
import java.util.stream.Collectors;

public class SparseGraph {

    private final HashMap<String, ArrayList<Edge>> adj;
    private final Set<Pair<Vertex>> neighbours;
    private HashMap<String, Vertex> vertexSet;
    private ArrayList<Edge> edgeSet;
    private String startKey;

    public SparseGraph() {
        adj = new HashMap<>();
        vertexSet = new HashMap<>();
        edgeSet = new ArrayList<>();
        neighbours = new HashSet<>();
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
        String first = String.valueOf(e.getStartPointId());
        String second = String.valueOf(e.getEndPointId());
        neighbours.add(new Pair<>(vertexSet.get(first), vertexSet.get(second)));
        neighbours.add(new Pair<>(vertexSet.get(second), vertexSet.get(first)));
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
        if (neighbours.contains(new Pair<>(v1, v2))) {
            ArrayList<Edge> edges = this.getAdj(v1.getKey());
            return edges.stream()
                    .filter(edge -> String.valueOf(edge.getEndPointId()).equals(v2.getKey()))
                    .findFirst()
                    .orElseGet(() -> {
                        this.getAdj(v2.getKey());
                        return edges.stream()
                                .filter(edge -> String.valueOf(edge.getEndPointId()).equals(v1.getKey()))
                                .findFirst().orElse(null);
                    });
        } else {
            return null;
        }
    }



    public List<Edge> findEdges(Vertex v1) {
        return this.getNeighbors(v1)
             .stream()
             .map(vertex -> this.findEdge(v1, vertex))
             .collect(Collectors.toList());
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
        return neighbours.stream()
               .filter(vertexPair -> vertexPair.first.getKey().equalsIgnoreCase(v.getKey()))
               .map(vertexPair -> vertexPair.second)
               .collect(Collectors.toList());
    }

    public List<SparseGraph> split(int subgraphsCount) {
        List<SparseGraph> result = split(this);
        do {
            result = result.stream()
                    .map(this::split)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        } while (result.size() < subgraphsCount);
        return result;
    }

    private List<SparseGraph> split(SparseGraph graph) {
        KernighanLin k = KernighanLin.process(graph);
        return k.getGroups().stream()
                     .map(group -> SparseGraphBuilder.fromVertexGroup(group, this))
                     .collect(Collectors.toList());
    }
}
