package com.epam.coder.model.osm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class NodeReference {

    @XmlAttribute
    private long ref;

    public long getRef() {
        return ref;
    }

    public void setRef(long ref) {
        this.ref = ref;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeReference that = (NodeReference) o;

        return ref == that.ref;
    }

    @Override
    public int hashCode() {
        return (int) (ref ^ (ref >>> 32));
    }

    public String toString() {
        return "NodeReference{" +
                "ref=" + ref +
                '}';
    }
}
