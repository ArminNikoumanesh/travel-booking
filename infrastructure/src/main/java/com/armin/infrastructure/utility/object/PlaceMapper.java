package com.armin.infrastructure.utility.object;

import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
public class PlaceMapper {

    public <T> Map<String, Object> mapPlaceAsOsmTags(T model) throws SystemException {
        Map<String, Object> result = new HashMap<>();
        ArrayList<Method> modelGetterMethods = new ArrayList<>();
        Class srcClass = model.getClass();
        Method[] fromMethods = srcClass.getMethods();
        ArrayList<Field> srcFieldsNames = new ArrayList(Arrays.asList(srcClass.getDeclaredFields()));
        Map<Field, Method> srcFieldToGetterMap = new HashMap<>();

        for (Method sourceMethod : fromMethods)
            if (sourceMethod.getName().contains("get"))
                modelGetterMethods.add(sourceMethod);

        for (Field field : srcFieldsNames)
            for (Method sourceMethod : modelGetterMethods)
                if (field.getName().equalsIgnoreCase(sourceMethod.getName().substring(3))) {
                    srcFieldToGetterMap.put(field, sourceMethod);
                }

        for (Field field : model.getClass().getDeclaredFields()) {
            MapTo attribute = field.getAnnotation(MapTo.class);
            if (attribute != null) {
                try {
                    field.setAccessible(true);
                    String key = attribute.target();
                    result.put(key, srcFieldToGetterMap.get(field).invoke(model));
                } catch (Exception e) {
                    throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "model", 1206);
                }
            }
        }
        return result;
    }
}
