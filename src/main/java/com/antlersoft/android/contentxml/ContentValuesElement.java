package com.antlersoft.android.contentxml;

import android.content.ContentValues;
import com.antlersoft.util.xml.IElement;
import com.antlersoft.util.xml.IHandlerStack;
import com.antlersoft.util.xml.ISimpleElement;
import com.antlersoft.util.xml.SimpleAttributes;
import com.antlersoft.util.xml.SimpleHandler;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ContentValuesElement implements IElement, ISimpleElement {
    private String _elementTag;
    private ContentValues _values;

    public ContentValuesElement(ContentValues contentValues, String str) {
        this._values = contentValues;
        this._elementTag = str;
    }

    @Override // com.antlersoft.util.xml.IElement
    public String getElementTag() {
        return this._elementTag;
    }

    @Override // com.antlersoft.util.xml.ISimpleElement
    public void gotElement(String str, String str2, Attributes attributes) throws SAXException {
        int length = attributes.getLength();
        for (int i = 0; i < length; i++) {
            this._values.put(attributes.getLocalName(i), attributes.getValue(i));
        }
    }

    @Override // com.antlersoft.util.xml.IElement
    public DefaultHandler readFromXML(IHandlerStack iHandlerStack) {
        return new SimpleHandler(iHandlerStack, this);
    }

    @Override // com.antlersoft.util.xml.IElement
    public void writeToXML(ContentHandler contentHandler) throws SAXException {
        SimpleAttributes simpleAttributes = new SimpleAttributes();
        for (Map.Entry<String, Object> entry : this._values.valueSet()) {
            if (!(entry.getKey() == null || entry.getValue() == null)) {
                simpleAttributes.addValue(entry.getKey(), entry.getValue());
            }
        }
        contentHandler.startElement("", "", getElementTag(), simpleAttributes.getAttributes());
        contentHandler.endElement("", "", getElementTag());
    }
}
