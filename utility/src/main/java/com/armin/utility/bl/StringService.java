package com.armin.utility.bl;

import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StringService {
    private final ObjectMapper objectMapper;

    @Autowired
    private StringService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static String toJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static <T> String convertObjectCollectionToString(Collection<T> objectList, int limitSize) throws SystemException {
        if (objectList != null && !objectList.isEmpty()) {
            String result = objectList.stream().filter(t -> StringUtils.isNotBlank(t.toString())).map(Object::toString).collect(Collectors.joining(",", ",", ","));
            if (result.length() > limitSize) {
                throw new SystemException(SystemError.ILLEGAL_ARGUMENT, "input", 3003);
            }
            return result;
        }
        return null;
    }

    public static List<Integer> convertStringToNumericArray(String list) {
        if (StringUtils.isBlank(list)) {
            return new ArrayList<>();
        } else {
            if (list.startsWith(","))
                list = list.substring(1);
            return Stream.of(list.split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        }
    }

    public static List<String> convertCommaSeparatedStringToList(String commaSeparated) {
        if (StringUtils.isBlank(commaSeparated)) {
            return new ArrayList<>();
        } else {
            return Stream.of(commaSeparated.split(","))
                    .collect(Collectors.toList());
        }
    }

    public static String getAlphaNumericString(int n) {

        // lower limit for LowerCase Letters
        int lowerLimit = 97;

        // lower limit for LowerCase Letters
        int upperLimit = 122;

        Random random = new Random();

        // Create a StringBuffer to store the result
        StringBuffer r = new StringBuffer(n);

        for (int i = 0; i < n; i++) {

            // take a random value between 97 and 122
            int nextRandomChar = lowerLimit
                    + (int) (random.nextFloat()
                    * (upperLimit - lowerLimit + 1));

            // append a character at the end of bs
            r.append((char) nextRandomChar);
        }

        // return the resultant string
        return r.toString();
    }

    public static String splitDigitsByComma(Number number) {
        DecimalFormat df = new DecimalFormat();
        df.setGroupingSize(3);
        df.setMaximumFractionDigits(2);
        return df.format(number);
    }

    public String toJsonStringAdvanced(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public String toPrettyJsonStringAdvanced(Object obj) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public <T> T convertJsonToObject(String json, Class<T> cls) {
        try {
            if (StringUtils.isBlank(json)) {
                return null;
            }
            return objectMapper.readValue(json, cls);
        } catch (IOException e) {
            return null;
        }
    }

    public <T> List<T> convertJsonToObjectList(String json, Class<T> tClass) {
        try {
            if (StringUtils.isBlank(json)) {
                return new ArrayList<>();
            }
            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass);
            return objectMapper.readValue(json, listType);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public Map<String, Object> objectToMap(Object obj) {
        return objectMapper.convertValue(obj, Map.class);
    }

    public Map<String, Object> stringToMap(String json) {
        try {
            if (StringUtils.isBlank(json)) {
                return null;
            } else {
                return objectMapper.readValue(json, Map.class);
            }
        } catch (IOException e) {
            return null;
        }
    }

    public <T> T mapToObject(Map<String, Object> map, Class<T> cls) {
        return objectMapper.convertValue(map, cls);
    }

    public String printExceptionStackTrace(Exception ex) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        ex.printStackTrace(printWriter);
        return stringWriter.toString();
    }

}
