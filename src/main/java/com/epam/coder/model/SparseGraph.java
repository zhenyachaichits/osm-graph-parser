package com.epam.coder.model;

import com.epam.coder.chinesepostman.partition.KernighanLin;
import com.epam.coder.chinesepostman.partition.Pair;
import com.epam.coder.util.SparseGraphBuilder;

import java.util.*;
import java.util.stream.Collectors;

public class SparseGraph {

    private final HashMap<String, ArrayList<Edge>> adj;
    private final HashMap<Vertex, List<Vertex>> neighbours;
    private HashMap<String, Vertex> vertexSet;
    private HashMap<Pair<Vertex>, Edge> vertexToEdgeMapping;
    private ArrayList<Edge> edgeSet;
    private String startKey;

    public SparseGraph() {
        adj = new HashMap<>();
        vertexSet = new HashMap<>();
        edgeSet = new ArrayList<>();
        neighbours = new HashMap<>();
        startKey = null;
        vertexToEdgeMapping = new HashMap<>();
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
        String key = String.valueOf(e.getStartPointId());
        ArrayList<Edge> edges = adj.get(key);
        if (edges != null) {
            edges.add(e);
        } else {
            edges = new ArrayList<>();
        }
        adj.put(key, edges);
        String first = String.valueOf(e.getStartPointId());
        String second = String.valueOf(e.getEndPointId());

        Vertex firstVertex = vertexSet.get(first);
        List<Vertex> firstNeighbours = neighbours.get(firstVertex);
        if (firstNeighbours == null) {
            firstNeighbours = new ArrayList<>();
        }
        firstNeighbours.add(vertexSet.get(first));
        neighbours.put(firstVertex, firstNeighbours);

        Vertex secondVertex = vertexSet.get(second);
        List<Vertex> secondNeighbours = neighbours.get(secondVertex);
        if (secondNeighbours == null) {
            secondNeighbours = new ArrayList<>();
        }
        secondNeighbours.add(vertexSet.get(first));
        neighbours.put(secondVertex, secondNeighbours);

        vertexToEdgeMapping.put(new Pair<>(vertexSet.get(second), vertexSet.get(first)), e);
        vertexToEdgeMapping.put(new Pair<>(vertexSet.get(first), vertexSet.get(second)), e);
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
        return vertexToEdgeMapping.get(new Pair<>(v1, v2));
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
        return neighbours.get(v);
    }


    public List<SparseGraph> split(int subgraphsCount) {
        List<SparseGraph> result = split(this);
        while (result.size() < subgraphsCount) {
            result = result.parallelStream()
                    .map(this::split)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        }
        return result;
    }

    private List<SparseGraph> split(SparseGraph graph) {
        KernighanLin k = KernighanLin.process(graph);
        return k.getGroups().stream()
                     .map(group -> SparseGraphBuilder.fromVertexGroup(group, this))
                     .collect(Collectors.toList());
    }
}
