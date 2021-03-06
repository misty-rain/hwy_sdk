package com.bojoy.bjsdk_mainland_new.support.fastjson.parser.deserializer;

import com.bojoy.bjsdk_mainland_new.support.fastjson.parser.DefaultJSONParser;
import com.bojoy.bjsdk_mainland_new.support.fastjson.parser.JSONToken;
import com.bojoy.bjsdk_mainland_new.support.fastjson.util.TypeUtils;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;



public class JavaObjectDeserializer implements ObjectDeserializer {

    public final static JavaObjectDeserializer instance = new JavaObjectDeserializer();

    @SuppressWarnings("unchecked")
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            if (componentType instanceof TypeVariable) {
                TypeVariable<?> componentVar = (TypeVariable<?>) componentType;
                componentType = componentVar.getBounds()[0];
            }

            List<Object> list = new ArrayList<Object>();
            parser.parseArray(componentType, list);
            Class<?> componentClass;
            if (componentType instanceof Class) {
                componentClass = (Class<?>) componentType;
                if (componentClass == boolean.class) {
                    return (T) TypeUtils.cast(list, boolean[].class, parser.getConfig());
                } else if (componentClass == short.class) {
                    return (T) TypeUtils.cast(list, short[].class, parser.getConfig());
                } else if (componentClass == int.class) {
                    return (T) TypeUtils.cast(list, int[].class, parser.getConfig());
                } else if (componentClass == long.class) {
                    return (T) TypeUtils.cast(list, long[].class, parser.getConfig());
                } else if (componentClass == float.class) {
                    return (T) TypeUtils.cast(list, float[].class, parser.getConfig());
                } else if (componentClass == double.class) {
                    return (T) TypeUtils.cast(list, double[].class, parser.getConfig());
                }

                Object[] array = (Object[]) Array.newInstance(componentClass, list.size());
                list.toArray(array);
                return (T) array;
            } else {
                return (T) list.toArray();
            }

        }

        return (T) parser.parse(fieldName);
    }

    public int getFastMatchToken() {
        return JSONToken.LBRACE;
    }
}
