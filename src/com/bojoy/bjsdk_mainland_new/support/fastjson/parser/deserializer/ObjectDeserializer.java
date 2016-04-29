package com.bojoy.bjsdk_mainland_new.support.fastjson.parser.deserializer;

import com.bojoy.bjsdk_mainland_new.support.fastjson.parser.DefaultJSONParser;

import java.lang.reflect.Type;


public interface ObjectDeserializer {
    <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName);
    
    int getFastMatchToken();
}
