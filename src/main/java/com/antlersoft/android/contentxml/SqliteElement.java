package com.antlersoft.android.contentxml;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Xml;
import com.antlersoft.util.xml.IElement;
import com.antlersoft.util.xml.IHandlerStack;
import com.antlersoft.util.xml.SimpleAttributes;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlSerializer;

public class SqliteElement implements IElement {
    static final String ROW_ELEMENT = "row";
    static final String[] TABLE_ARRAY = {"name"};
    static final String TABLE_ELEMENT = "table";
    static final String TABLE_NAME_ATTRIBUTE = "table_name";
    private String _databaseTag;
    SQLiteDatabase _db;
    private ReplaceStrategy _replaceStrategy;
    private ArrayList<String> _tableNames = new ArrayList<>();

    public enum ReplaceStrategy {
        REPLACE_ALL,
        REPLACE_EXISTING,
        REPLACE_NONE
    }

    /* access modifiers changed from: package-private */
    public class SqliteElementHandler extends DefaultHandler {
        private static final long INSERT_FAILED = -1;
        private String _currentTable;
        private ContentValues _lastRow;
        private IHandlerStack _stack;

        public SqliteElementHandler(IHandlerStack iHandlerStack) {
            this._stack = iHandlerStack;
        }

        private void saveLastRow() {
            if (this._lastRow != null) {
                try {
                    if (this._currentTable != null && SqliteElement.this._db.insert(this._currentTable, null, this._lastRow) == INSERT_FAILED) {
                        switch (SqliteElement.this.getReplaceStrategy()) {
                            case REPLACE_ALL:
                                throw new SQLException("Failed to insert row in " + this._currentTable + " after emptying");
                            case REPLACE_EXISTING:
                                SqliteElement.this._db.delete(this._currentTable, "_id = ?", new String[]{this._lastRow.getAsString("_id")});
                                SqliteElement.this._db.insertOrThrow(this._currentTable, null, this._lastRow);
                                break;
                        }
                    }
                } finally {
                    this._lastRow = null;
                }
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endElement(String str, String str2, String str3) throws SAXException {
            saveLastRow();
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
            saveLastRow();
            if (str2.equals(SqliteElement.TABLE_ELEMENT)) {
                this._currentTable = attributes.getValue(SqliteElement.TABLE_NAME_ATTRIBUTE);
                if (this._currentTable == null) {
                    throw new SAXException("table_name not found in table element.");
                } else if (!SqliteElement.this.getTableNames().contains(this._currentTable)) {
                    this._currentTable = null;
                } else if (SqliteElement.this.getReplaceStrategy() == ReplaceStrategy.REPLACE_ALL) {
                    SqliteElement.this._db.delete(this._currentTable, null, null);
                }
            } else if (str2.equals(SqliteElement.ROW_ELEMENT)) {
                this._lastRow = new ContentValues();
                this._stack.startWithHandler(new ContentValuesElement(this._lastRow, SqliteElement.ROW_ELEMENT).readFromXML(this._stack), str, str2, str3, attributes);
            }
        }
    }

    public static class StackContentHandler implements ContentHandler, IHandlerStack {
        private Stack<ContentHandler> _stack = new Stack<>();

        @Override // org.xml.sax.ContentHandler
        public void characters(char[] cArr, int i, int i2) throws SAXException {
            this._stack.peek().characters(cArr, i, i2);
        }

        @Override // org.xml.sax.ContentHandler
        public void endDocument() throws SAXException {
            this._stack.peek().endDocument();
        }

        @Override // org.xml.sax.ContentHandler
        public void endElement(String str, String str2, String str3) throws SAXException {
            this._stack.peek().endElement(str, str2, str3);
        }

        @Override // org.xml.sax.ContentHandler
        public void endPrefixMapping(String str) throws SAXException {
            this._stack.peek().endPrefixMapping(str);
        }

        @Override // org.xml.sax.ContentHandler
        public void ignorableWhitespace(char[] cArr, int i, int i2) throws SAXException {
            this._stack.peek().ignorableWhitespace(cArr, i, i2);
        }

        @Override // com.antlersoft.util.xml.IHandlerStack
        public void popHandlerStack() {
            this._stack.pop();
        }

        @Override // org.xml.sax.ContentHandler
        public void processingInstruction(String str, String str2) throws SAXException {
            this._stack.peek().processingInstruction(str, str2);
        }

        @Override // com.antlersoft.util.xml.IHandlerStack
        public void pushHandlerStack(DefaultHandler defaultHandler) {
            this._stack.push(defaultHandler);
        }

        public void setDocumentLocator(Locator locator) {
            this._stack.peek().setDocumentLocator(locator);
        }

        @Override // org.xml.sax.ContentHandler
        public void skippedEntity(String str) throws SAXException {
            this._stack.peek().skippedEntity(str);
        }

        @Override // org.xml.sax.ContentHandler
        public void startDocument() throws SAXException {
            this._stack.peek().startDocument();
        }

        @Override // org.xml.sax.ContentHandler
        public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
            this._stack.peek().startElement(str, str2, str3, attributes);
        }

        @Override // org.xml.sax.ContentHandler
        public void startPrefixMapping(String str, String str2) throws SAXException {
            this._stack.peek().startPrefixMapping(str, str2);
        }

        @Override // com.antlersoft.util.xml.IHandlerStack
        public void startWithHandler(DefaultHandler defaultHandler, String str, String str2, String str3, Attributes attributes) throws SAXException {
            this._stack.push(defaultHandler);
            defaultHandler.startElement(str, str2, str3, attributes);
        }
    }

    /* access modifiers changed from: package-private */
    public static class XmlSerializerHandler extends DefaultHandler {
        boolean _first = true;
        XmlSerializer _serializer;

        XmlSerializerHandler(XmlSerializer xmlSerializer) {
            this._serializer = xmlSerializer;
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void characters(char[] cArr, int i, int i2) throws SAXException {
            try {
                this._serializer.text(cArr, i, i2);
            } catch (IOException e) {
                throw new SAXException(e.getMessage(), e);
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endElement(String str, String str2, String str3) throws SAXException {
            try {
                this._serializer.endTag(str, str3);
            } catch (IOException e) {
                throw new SAXException(e.getMessage(), e);
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void ignorableWhitespace(char[] cArr, int i, int i2) throws SAXException {
            try {
                this._serializer.ignorableWhitespace(new String(cArr, i, i2));
            } catch (IOException e) {
                throw new SAXException(e.getMessage(), e);
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
            try {
                if (this._first) {
                    this._first = false;
                } else {
                    this._serializer.ignorableWhitespace("\r\n");
                }
                this._serializer.startTag(str, str3);
                if (attributes != null) {
                    int length = attributes.getLength();
                    for (int i = 0; i < length; i++) {
                        this._serializer.attribute(attributes.getURI(i), attributes.getQName(i), attributes.getValue(i));
                    }
                }
            } catch (IOException e) {
                throw new SAXException(e.getMessage(), e);
            }
        }
    }

    public SqliteElement(SQLiteDatabase sQLiteDatabase, String str) {
        this._db = sQLiteDatabase;
        this._databaseTag = str;
        this._replaceStrategy = ReplaceStrategy.REPLACE_EXISTING;
    }

    public static void exportDbAsXmlToStream(SQLiteDatabase sQLiteDatabase, Writer writer) throws SAXException, IOException {
        XmlSerializer newSerializer = Xml.newSerializer();
        newSerializer.setOutput(writer);
        new SqliteElement(sQLiteDatabase, "database").writeToXML(new XmlSerializerHandler(newSerializer));
        newSerializer.flush();
    }

    public static void importXmlStreamToDb(SQLiteDatabase sQLiteDatabase, Reader reader, ReplaceStrategy replaceStrategy) throws SAXException, IOException {
        SqliteElement sqliteElement = new SqliteElement(sQLiteDatabase, "database");
        sqliteElement.setReplaceStrategy(replaceStrategy);
        StackContentHandler stackContentHandler = new StackContentHandler();
        stackContentHandler.pushHandlerStack(sqliteElement.readFromXML(stackContentHandler));
        Xml.parse(reader, stackContentHandler);
    }

    public void addTable(String str) {
        if (!this._tableNames.contains(str)) {
            this._tableNames.add(str);
        }
    }

    @Override // com.antlersoft.util.xml.IElement
    public String getElementTag() {
        return this._databaseTag;
    }

    public ReplaceStrategy getReplaceStrategy() {
        return this._replaceStrategy;
    }

    /* access modifiers changed from: package-private */
    public ArrayList<String> getTableNames() {
        if (this._tableNames.size() == 0) {
            Cursor query = this._db.query("sqlite_master", TABLE_ARRAY, "type = 'table'", null, null, null, null);
            while (query.moveToNext()) {
                try {
                    String string = query.getString(0);
                    String lowerCase = string.toLowerCase();
                    if (!lowerCase.equals("android_metadata") && !lowerCase.equals("sqlite_sequence")) {
                        this._tableNames.add(string);
                    }
                } finally {
                    query.close();
                }
            }
        }
        return this._tableNames;
    }

    @Override // com.antlersoft.util.xml.IElement
    public DefaultHandler readFromXML(IHandlerStack iHandlerStack) {
        return new SqliteElementHandler(iHandlerStack);
    }

    public void removeTable(String str) {
        getTableNames().remove(str);
    }

    public void setReplaceStrategy(ReplaceStrategy replaceStrategy) {
        this._replaceStrategy = replaceStrategy;
    }

    /* JADX INFO: finally extract failed */
    @Override // com.antlersoft.util.xml.IElement
    public void writeToXML(ContentHandler contentHandler) throws SAXException {
        contentHandler.startElement("", "", getElementTag(), null);
        Iterator<String> it = getTableNames().iterator();
        while (it.hasNext()) {
            String next = it.next();
            SimpleAttributes simpleAttributes = new SimpleAttributes();
            simpleAttributes.addValue(TABLE_NAME_ATTRIBUTE, next);
            contentHandler.startElement("", "", TABLE_ELEMENT, simpleAttributes.getAttributes());
            Cursor query = this._db.query(next, null, null, null, null, null, null);
            try {
                if (query.moveToFirst()) {
                    ContentValues contentValues = new ContentValues();
                    do {
                        DatabaseUtils.cursorRowToContentValues(query, contentValues);
                        new ContentValuesElement(contentValues, ROW_ELEMENT).writeToXML(contentHandler);
                    } while (query.moveToNext());
                }
                query.close();
                contentHandler.endElement("", "", TABLE_ELEMENT);
            } catch (Throwable th) {
                query.close();
                throw th;
            }
        }
        contentHandler.endElement("", "", getElementTag());
    }
}
