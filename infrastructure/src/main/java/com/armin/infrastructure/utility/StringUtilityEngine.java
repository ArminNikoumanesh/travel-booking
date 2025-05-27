package com.armin.infrastructure.utility;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Service
public class StringUtilityEngine {

    public static String convertMapToString(Map<String, Object> map) {
        if (!map.isEmpty()) {
            return map.keySet().stream()
                    .map(key -> key + ":" + map.get(key)).collect(Collectors.joining(", ", "{", "}"));
        }
        return null;
    }

    public static Map<String, Object> convertStringToMap(String mapAsString) {
        if (mapAsString != null) {
            mapAsString = mapAsString.replace("{", "").replace("}", "");
            return Arrays.stream(mapAsString.split(","))
                    .map(entry -> entry.split(":"))
                    .collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));
        }
        return new HashMap<>();
    }

}
