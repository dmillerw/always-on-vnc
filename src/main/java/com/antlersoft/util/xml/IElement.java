package com.antlersoft.util.xml;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public interface IElement {
    String getElementTag();

    DefaultHandler readFromXML(IHandlerStack iHandlerStack);

    void writeToXML(ContentHandler contentHandler) throws SAXException;
}
