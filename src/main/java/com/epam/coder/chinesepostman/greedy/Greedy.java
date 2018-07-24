package com.epam.coder.chinesepostman.greedy;

import com.epam.coder.chinesepostman.algorithms.CPP;
import com.epam.coder.chinesepostman.algorithms.Dijkstra;
import com.epam.coder.model.*;

public class Greedy extends CPP {

    private String endKey;

    public Greedy(SparseGraph sg) {
        super(sg);
    }

    private String heuristic() {
        int heuristicWeight;
        String key = "";
        int max = 0;
        for (Vertex v : sg.getVertexSet()) {
            heuristicWeight = 0;
            for (Edge e : sg.getAdj(v.getKey())) {
                if (e.isActive()) {
                    heuristicWeight++;
                }
            }
            v.heuristic = heuristicWeight;
            if (max < v.heuristic) {
                key = v.getKey();
                max = v.heuristic;
            }
        }
        return key;
    }

    private void visit(String startKey) {
        boolean stop = false;
        String key = startKey;
        while (!stop) {
            output.add(key);
            int max = 0;
            Edge edgeMax = null;
            for (Edge e : sg.getAdj(key)) {
                if (e.isActive()) {
                    Vertex v = sg.getVertex(String.valueOf(e.getStartPointId()));
                    if (max < v.heuristic) {
                        max = v.heuristic;
                        edgeMax = e;
                    }
                }
            }
            if (edgeMax != null) {
                sg.decreaseDegree(edgeMax);
                sg.invalidate(String.valueOf(edgeMax.getStartPointId()), String.valueOf(edgeMax.getEndPointId()));
                key = String.valueOf(edgeMax.getEndPointId());
            } else if (!isObjective(key)) {
                sg.setStartKey(key);
                new Dijkstra(sg).perform();
                key = getMin();
            } else {
                stop = true;
            }
        }
    }

    @Override
    public void perform() {
        this.endKey = this.heuristic();
        visit(this.endKey);

    }

    private boolean isObjective(String key) {
        if (!key.equals(endKey)) {
            return false;
        }
        for (Vertex v : sg.getVertexSet()) {
            if (v.heuristic != 0) {
                return false;
            }
        }
        return true;
    }

    private String getMin() {
        long min = Integer.MAX_VALUE;
        Vertex vMin = null;
        for (Vertex v : sg.getVertexSet()) {
            if (v.distance != Integer.MAX_VALUE
                    && v.heuristic != 0
                    && min > v.distance) {
                min = v.distance;
                vMin = v;
            }
        }
        if (vMin == null) {
            vMin = sg.getVertex(endKey);
        }
        pathVisit(sg.getVertex(vMin.father));
        return vMin.getKey();
    }

    private void pathVisit(Vertex v) {
        if (v!= null && v.father != null) {
            pathVisit(sg.getVertex(v.father));
            output.add(v.getKey());
            sg.decreaseDegree(sg.edgeAdj(v.father, v.getKey()));
            sg.invalidate(v.father, v.getKey());
        }
    }

}
