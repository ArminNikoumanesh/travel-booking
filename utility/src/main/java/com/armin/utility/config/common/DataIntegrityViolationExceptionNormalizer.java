package com.armin.utility.config.common;

import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataIntegrityViolationExceptionNormalizer {

    public static Map<String, Object> normalize(DataIntegrityViolationException exception) {
        Map<String, Object> response = new HashMap<>();
        String message = Objects.requireNonNull(exception.getRootCause()).getMessage();
        String[] as = message.split("Detail: Key");
        String detail = as[1];

        setResponseKeyAndValue(response, detail);
        setResponseCause(response, detail);
        return response;
    }

    private static void setResponseKeyAndValue(Map<String, Object> response, String exceptionDetail) {

        List<String> matchList = new ArrayList<>();
        Pattern regex = Pattern.compile("\\((.*?)\\)");
        Matcher regexMatcher = regex.matcher(exceptionDetail);

        while (regexMatcher.find()) {//Finds Matching Pattern in String
            matchList.add(regexMatcher.group(1));//Fetching Group from String
        }

        response.put("key", normalizeResponseKey(matchList.get(0)));
        response.put("value", matchList.get(1));
    }

    private static String normalizeResponseKey(String key) {
        key = key.replace("_id_fk", "");
        key = key.replace("_id_pk", "");

        return key;
    }

    private static void setResponseCause(Map<String, Object> response, String detail) {
        if (detail.contains("is not present")) {
            response.put("cause", DatabaseError.INVALID_FOREIGN_KEY);
        } else if (detail.contains("already exists")) {
            response.put("cause", DatabaseError.DUPLICATE_ERROR);
        } else {
            response.put("cause", DatabaseError.UNKNOWN);
        }
    }
}
