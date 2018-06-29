package com.epam.coder.parser;

import com.epam.coder.model.MapGraph;

import javax.xml.bind.JAXBException;
import java.io.File;

public interface GraphParser {
    MapGraph parseXml(File file) throws JAXBException;
}
