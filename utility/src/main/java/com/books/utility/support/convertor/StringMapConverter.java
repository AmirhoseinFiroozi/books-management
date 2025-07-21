package com.books.utility.support.convertor;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @auther : Armin.Nik
 * @project : shared
 * @date : 03.10.21
 */
@Service
public class StringMapConverter {

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
