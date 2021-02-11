package com.antlersoft.util.xml;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

public class SimpleHandler extends DefaultHandler {
    public static AttributesImpl m_empty = new AttributesImpl();
    private Attributes m_current_attributes;
    private StringBuffer m_current_element_contents = new StringBuffer();
    private String m_current_element_name;
    private ISimpleElement m_element;
    private IHandlerStack m_impl;

    public SimpleHandler(IHandlerStack iHandlerStack, ISimpleElement iSimpleElement) {
        this.m_impl = iHandlerStack;
        this.m_element = iSimpleElement;
    }

    public static void writeElement(ContentHandler contentHandler, String str, String str2) throws SAXException {
        contentHandler.startElement("", str, "", m_empty);
        char[] charArray = str2.toCharArray();
        contentHandler.characters(charArray, 0, charArray.length);
        contentHandler.endElement("", str, "");
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] cArr, int i, int i2) {
        this.m_current_element_contents.append(cArr, i, i2);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String str, String str2, String str3) throws SAXException {
        this.m_element.gotElement(this.m_current_element_name, this.m_current_element_contents.toString(), this.m_current_attributes);
        if (this.m_impl != null) {
            this.m_impl.popHandlerStack();
        }
    }

    /* access modifiers changed from: package-private */
    public Attributes getAttributes() {
        return this.m_current_attributes;
    }

    /* access modifiers changed from: package-private */
    public StringBuffer getContents() {
        return this.m_current_element_contents;
    }

    /* access modifiers changed from: package-private */
    public String getCurrentElementName() {
        return this.m_current_element_name;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] cArr, int i, int i2) {
        this.m_current_element_contents.append(cArr, i, i2);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
        this.m_current_element_name = str2;
        this.m_current_element_contents.setLength(0);
        AttributesImpl attributesImpl = new AttributesImpl();
        this.m_current_attributes = attributesImpl;
        int length = attributes.getLength();
        for (int i = 0; i < length; i++) {
            attributesImpl.addAttribute(attributes.getURI(i), attributes.getLocalName(i), attributes.getQName(i), attributes.getType(i), attributes.getValue(i));
        }
    }
}
