package com.epam.coder.chinesepostman.algorithms;

import com.epam.coder.model.*;

import java.util.PriorityQueue;

public class Dijkstra extends Relaxation {

    private final PriorityQueue<Vertex> pq;

    public Dijkstra(SparseGraph wegraph) {
        sg = wegraph;
        pq = new PriorityQueue<>();
    }

    private void initPQ() {
        pq.addAll(sg.getVertexSet());
    }

    @Override
    public void relax(Vertex u, Vertex v, Edge e) {
        long sum;
        sum = u.distance + e.getDistance();
        if (v!= null && u.distance != Integer.MAX_VALUE && v.distance > sum) {
            v.distance = sum;
            v.father = u.getKey();
            pq.add(v);
        }
    }

    public void perform() {
        Vertex u, v;
        init();
        initPQ();
        while (!pq.isEmpty()) {
            u = pq.poll();
            if (u.distance == sg.getVertex(u.getKey()).distance) {
                for (Edge e : sg.getAdj(u.getKey())) {
                    v = sg.getVertex(String.valueOf(e.getEndPointId()));
                    if (v != null) {
                        relax(u, v, e);
                        printOutVisit(v);
                    }
                }
            }
        }
    }
}
