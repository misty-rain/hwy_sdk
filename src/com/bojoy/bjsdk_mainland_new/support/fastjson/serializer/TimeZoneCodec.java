package com.bojoy.bjsdk_mainland_new.support.fastjson.serializer;

import com.bojoy.bjsdk_mainland_new.support.fastjson.parser.DefaultJSONParser;
import com.bojoy.bjsdk_mainland_new.support.fastjson.parser.JSONToken;
import com.bojoy.bjsdk_mainland_new.support.fastjson.parser.deserializer.ObjectDeserializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.TimeZone;



public class TimeZoneCodec implements ObjectSerializer, ObjectDeserializer {

    public final static TimeZoneCodec instance = new TimeZoneCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        if (object == null) {
            serializer.writeNull();
            return;
        }

        TimeZone timeZone = (TimeZone) object;
        serializer.write(timeZone.getID());
    }
    
    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        
        String id = (String) parser.parse();
        
        if (id == null) {
            return null;
        }
        
        return (T) TimeZone.getTimeZone(id);
    }

    public int getFastMatchToken() {
        return JSONToken.LITERAL_STRING;
    }

}
