package com.antlersoft.util.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public interface ISimpleElement {
    void gotElement(String str, String str2, Attributes attributes) throws SAXException;
}
