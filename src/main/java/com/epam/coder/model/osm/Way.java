package com.epam.coder.model.osm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Way {

    @XmlAttribute
    private long id;
    @XmlElement(name = "nd")
    private List<NodeReference> nodeReferences;
    @XmlElement(name = "tag")
    private List<Tag> tags;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<NodeReference> getNodeReferences() {
        return nodeReferences;
    }

    public void setNodeReferences(List<NodeReference> nodeReferences) {
        this.nodeReferences = nodeReferences;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Way way = (Way) o;

        if (id != way.id) return false;
        if (nodeReferences != null ? !nodeReferences.equals(way.nodeReferences) : way.nodeReferences != null)
            return false;
        return tags != null ? tags.equals(way.tags) : way.tags == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (nodeReferences != null ? nodeReferences.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "Way{" +
                "id=" + id +
                ", nodeReferences=" + nodeReferences +
                ", tags=" + tags +
                '}';
    }
}
