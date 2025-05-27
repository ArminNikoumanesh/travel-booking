package com.armin.utility.config.common;

import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvalidDataAccessApiUsageExceptionNormalizer {

    public static Map<String, Object> normalize(InvalidDataAccessApiUsageException exception) {
        exception.printStackTrace();
        Map<String, Object> response = new HashMap<>();
        String exceptionMessage = exception.getMessage();

        List<String> matches = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\[.*?\\]");
        Matcher matcher = pattern.matcher(exceptionMessage);

        while (matcher.find()) {
            matches.add(matcher.group());
        }

        String input = matches.get(0).replaceAll("\\p{P}", "");
        String[] valueList = matches.get(1).split("[.]");
        String entity = valueList[valueList.length - 1].replaceAll("\\p{P}", "");
        response.put("entity", entity);
        response.put("input", input);
        response.put("cause", DatabaseError.INVALID_INCLUDE_OR_SORT);
        return response;
    }
}
