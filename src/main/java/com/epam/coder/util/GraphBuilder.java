package com.epam.coder.util;

import com.epam.coder.model.MapGraph;
import com.epam.coder.model.Node;
import com.epam.coder.model.osm.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GraphBuilder {

    private Osm osmMap;

    public GraphBuilder(Osm osmMap) {
        this.osmMap = osmMap;
    }

    public MapGraph buildFullGraph() {
        MapGraph mapGraph = new MapGraph();

        osmMap.getNodes().stream()
                .map(this::transformNode)
                .forEach(mapGraph::addNode);

        osmMap.getWays().forEach(way -> addEdgesToGraph(way.getNodeReferences(),
                transformTags(way.getTags()), mapGraph));

        return mapGraph;
    }

    public MapGraph buildRoadGraph() {
        MapGraph mapGraph = new MapGraph();

        List<Way> roadWays = osmMap.getWays().stream()
                .filter(this::isHighway)
                .collect(Collectors.toList());

        osmMap.getNodes().stream()
                .filter(node -> isNodeInWay(node, roadWays))
                .map(this::transformNode)
                .forEach(mapGraph::addNode);

        roadWays.forEach(way -> addEdgesToGraph(way.getNodeReferences(), transformTags(way.getTags()), mapGraph));

        return mapGraph;

    }

    private void addEdgesToGraph(List<NodeReference> nodeReferences, Map<String, String> tags, MapGraph mapGraph) {
        long size = nodeReferences.size();

        for (int i = 0; i < size; i++) {
            if (i == 0 && size > 1) {
                mapGraph.addEdge(nodeReferences.get(i).getRef(), nodeReferences.get(i + 1).getRef(), tags);
            } else if (i > 0 && i < size - 1) {
                mapGraph.addEdge(nodeReferences.get(i).getRef(), nodeReferences.get(i - 1).getRef(), tags);
                mapGraph.addEdge(nodeReferences.get(i).getRef(), nodeReferences.get(i + 1).getRef(), tags);
            } else if (i == size - 1 && size > 1) {
                mapGraph.addEdge(nodeReferences.get(i).getRef(), nodeReferences.get(i - 1).getRef(), tags);
            }
        }
    }

    private Map<String, String> transformTags(List<Tag> tags) {
        if (tags == null) {
            return new HashMap<>();
        }

        return tags.stream().collect(Collectors.toMap(Tag::getKey, Tag::getValue));
    }

    private Node transformNode(OsmNode osmNode) {
        return new Node(osmNode.getId(), osmNode.getLat(), osmNode.getLon());
    }

    private boolean isNodeInWay(OsmNode osmNode, List<Way> ways) {
        return ways.stream()
                .flatMap(way -> way.getNodeReferences().stream())
                .anyMatch(nodeReference -> nodeReference.getRef() == osmNode.getId());
    }

    private boolean isHighway(Way way) {
        if (way.getTags() == null) {
            return false;
        }

        return way.getTags().stream()
                .anyMatch(tag -> "highway".equals(tag.getKey()));
    }

}
