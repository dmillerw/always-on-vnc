package com.antlersoft.util.xml;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

public class SimpleAttributes {
    private Attributes attr;
    public double defaultDouble;
    public int defaultInt;
    public String defaultString;

    public SimpleAttributes() {
        this.defaultInt = 0;
        this.defaultString = "";
        this.defaultDouble = 0.0d;
        this.attr = new AttributesImpl();
    }

    public SimpleAttributes(Attributes attributes) {
        this.defaultInt = 0;
        this.defaultString = "";
        this.defaultDouble = 0.0d;
        this.attr = attributes;
    }

    public void addValue(Object obj, Object obj2) {
        ((AttributesImpl) this.attr).addAttribute("", "", obj.toString(), "", obj2.toString());
    }

    public boolean booleanValue(Object obj) {
        return booleanValue(obj, false);
    }

    public boolean booleanValue(Object obj, boolean z) {
        String value = this.attr.getValue(obj.toString());
        return value != null ? Boolean.valueOf(value).booleanValue() : z;
    }

    public double doubleValue(Object obj) {
        return doubleValue(obj, this.defaultDouble);
    }

    public double doubleValue(Object obj, double d) {
        String value = this.attr.getValue(obj.toString());
        if (value == null) {
            return d;
        }
        try {
            return Double.valueOf(value).doubleValue();
        } catch (NumberFormatException e) {
            return d;
        }
    }

    public <E extends Enum<E>> E enumValue(Object obj, E e) {
        String value = this.attr.getValue(obj.toString());
        if (value == null) {
            return e;
        }
        try {
            return (E) Enum.valueOf(e.getClass(), value);
        } catch (IllegalArgumentException e2) {
            return e;
        }
    }

    public Attributes getAttributes() {
        return this.attr;
    }

    public int intValue(Object obj) {
        return intValue(obj, this.defaultInt);
    }

    public int intValue(Object obj, int i) {
        String value = this.attr.getValue(obj.toString());
        if (value == null) {
            return i;
        }
        try {
            return Integer.valueOf(value).intValue();
        } catch (NumberFormatException e) {
            return i;
        }
    }

    public long longValue(Object obj) {
        return longValue(obj, (long) this.defaultInt);
    }

    public long longValue(Object obj, long j) {
        String value = this.attr.getValue(obj.toString());
        if (value == null) {
            return j;
        }
        try {
            return Long.valueOf(value).longValue();
        } catch (NumberFormatException e) {
            return j;
        }
    }

    public void setDefaultInt(int i) {
        this.defaultInt = i;
    }

    public String stringValue(Object obj) {
        return stringValue(obj, this.defaultString);
    }

    public String stringValue(Object obj, String str) {
        String value = this.attr.getValue(obj.toString());
        return value == null ? str : value;
    }
}
