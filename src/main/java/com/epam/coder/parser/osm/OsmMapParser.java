package com.epam.coder.parser.osm;

import com.epam.coder.model.MapGraph;
import com.epam.coder.model.osm.Osm;
import com.epam.coder.parser.GraphParser;
import com.epam.coder.util.GraphBuilder;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

@Component
public class OsmMapParser implements GraphParser {

    @Override
    public MapGraph parseXml(File file) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Osm.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Osm osm = (Osm) jaxbUnmarshaller.unmarshal(file);

        return new GraphBuilder(osm).buildRoadGraph();
    }
}
