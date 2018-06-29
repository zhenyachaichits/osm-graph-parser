package com.epam.coder.model;

import java.util.HashMap;
import java.util.Map;

public class Edge {

    private long startPointId;
    private long endPointId;
    private long distance; //meters
    private boolean active;
    private Map<String, String> tags = new HashMap<>();

    public Edge(long startPoint, long endPointId, long distance) {
        this.startPointId = startPoint;
        this.endPointId = endPointId;
        this.distance = distance;
    }

    public Edge(long startPoint, long endPointId, long distance, Map<String, String> tags) {
        this.startPointId = startPoint;
        this.endPointId = endPointId;
        this.distance = distance;
        this.tags = tags;
    }


    public long getStartPointId() {
        return startPointId;
    }

    public long getEndPointId() {
        return endPointId;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        if (startPointId != edge.startPointId) return false;
        if (endPointId != edge.endPointId) return false;
        if (distance != edge.distance) return false;
        if (active != edge.active) return false;
        return tags != null ? tags.equals(edge.tags) : edge.tags == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (startPointId ^ (startPointId >>> 32));
        result = 31 * result + (int) (endPointId ^ (endPointId >>> 32));
        result = 31 * result + (int) (distance ^ (distance >>> 32));
        result = 31 * result + (active ? 1 : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "Edge{" +
                "startPointId=" + startPointId +
                ", endPointId=" + endPointId +
                ", distance=" + distance +
                ", active=" + active +
                '}';
    }
}
