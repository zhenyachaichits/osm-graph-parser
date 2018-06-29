package com.epam.coder.model.osm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "osm")
@XmlAccessorType(XmlAccessType.FIELD)
public class Osm {

    @XmlElement(name = "node")
    private List<OsmNode> nodes;
    @XmlElement(name = "way")
    private List<Way> ways;

    public List<OsmNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<OsmNode> nodes) {
        this.nodes = nodes;
    }

    public List<Way> getWays() {
        return ways;
    }

    public void setWays(List<Way> ways) {
        this.ways = ways;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Osm osm = (Osm) o;

        if (nodes != null ? !nodes.equals(osm.nodes) : osm.nodes != null) return false;
        return ways != null ? ways.equals(osm.ways) : osm.ways == null;
    }

    @Override
    public int hashCode() {
        int result = nodes != null ? nodes.hashCode() : 0;
        result = 31 * result + (ways != null ? ways.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "Osm{" +
                "nodes=" + nodes +
                ", ways=" + ways +
                '}';
    }
}
