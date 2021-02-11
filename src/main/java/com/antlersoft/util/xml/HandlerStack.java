package com.antlersoft.util.xml;

import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class HandlerStack implements IHandlerStack {
    private Stack m_handler_stack = new Stack();
    private XMLReader m_reader;

    public HandlerStack(XMLReader xMLReader) {
        this.m_reader = xMLReader;
    }

    @Override // com.antlersoft.util.xml.IHandlerStack
    public void popHandlerStack() {
        this.m_handler_stack.pop();
        if (this.m_reader != null && this.m_handler_stack.size() > 0) {
            DefaultHandler defaultHandler = (DefaultHandler) this.m_handler_stack.peek();
            this.m_reader.setContentHandler(defaultHandler);
            this.m_reader.setErrorHandler(defaultHandler);
        }
    }

    @Override // com.antlersoft.util.xml.IHandlerStack
    public void pushHandlerStack(DefaultHandler defaultHandler) {
        this.m_handler_stack.push(defaultHandler);
        if (this.m_reader != null) {
            this.m_reader.setContentHandler(defaultHandler);
            this.m_reader.setErrorHandler(defaultHandler);
        }
    }

    @Override // com.antlersoft.util.xml.IHandlerStack
    public void startWithHandler(DefaultHandler defaultHandler, String str, String str2, String str3, Attributes attributes) throws SAXException {
        pushHandlerStack(defaultHandler);
        defaultHandler.startElement(str, str2, str3, attributes);
    }
}
