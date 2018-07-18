package com.epam.coder.model;

import java.util.LinkedList;

public class Vertex implements Comparable<Vertex> {

    private final String key;
    private final int id;
    public boolean white;
    public String father;
    public long distance;
    public int heuristic;
    public int finish;
    public LinkedList<String> fatherList;

    public Vertex(String key) {
        this.key = key;
        this.id = -1;
        fatherList = new LinkedList<>();
    }

    public Vertex(String key, int id){
        this.key = key;
        this.id = id;
        
        fatherList = new LinkedList<>();
    }

    public int getId() {
        return id;
    }
    
    public String getKey() {
        return this.key;
    }

    @Override
    public int compareTo(Vertex anotherVertex) {
        if (this.distance <= anotherVertex.distance) {
            return -1;
        }
        return 1;
    }
}
