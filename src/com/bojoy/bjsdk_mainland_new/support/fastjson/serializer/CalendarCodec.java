package com.bojoy.bjsdk_mainland_new.support.fastjson.serializer;

import com.bojoy.bjsdk_mainland_new.support.fastjson.parser.DefaultJSONParser;
import com.bojoy.bjsdk_mainland_new.support.fastjson.parser.JSONToken;
import com.bojoy.bjsdk_mainland_new.support.fastjson.parser.deserializer.DateDeserializer;
import com.bojoy.bjsdk_mainland_new.support.fastjson.parser.deserializer.ObjectDeserializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Date;



public class CalendarCodec implements ObjectSerializer, ObjectDeserializer {

    public final static CalendarCodec instance = new CalendarCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        Calendar calendar = (Calendar) object;
        Date date = calendar.getTime();
        serializer.write(date);
    }

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Object value = DateDeserializer.instance.deserialze(parser, type, fieldName);
        
        if (value instanceof Calendar) {
            return (T) value;
        }
        
        Date date = (Date) value;
        if (date == null) {
            return null;
        }
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        return (T) calendar;
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }
}
