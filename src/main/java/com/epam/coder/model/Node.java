package com.epam.coder.model;

import java.util.HashSet;
import java.util.Set;

public class Node implements Comparable {

    private long id;
    private double latitude;
    private double longitude;
    private Set<Edge> edges;

    public Node(long id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.edges = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (id != node.id) return false;
        if (Double.compare(node.latitude, latitude) != 0) return false;
        return Double.compare(node.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

    public String toString() {
        return "OsmNode{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", edges=" + edges +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return 0;
        }
        Node n = (Node) o;

        return (int) (this.getId() - n.getId());
    }
}
