package com.armin.utility.bl;

import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.utility.statics.constants.UtilityConstant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The Validation Service Class,
 * Containing Methods about String Patterns and Input Validation
 *
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */

/**
 * Exceptions error code range: 1101-1110
 */

public final class ValidationEngine {

    public static <T> List<String> fieldNames(Class<T> cls) {
        List<String> names = new ArrayList<>();
        if (cls.getSuperclass() != null)
            names = ValidationEngine.fieldNames(cls.getSuperclass());
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields)
            names.add(field.getName());
        return names;
    }

    public static Integer validateInteger(String value) throws SystemException {
        if (value == null || value.equals(""))
            throw new SystemException(SystemError.INVALID_DATA_TYPE, "value:" + value, 1101);
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw new SystemException(SystemError.INVALID_DATA_TYPE, "value:" + value, 1102);
        }
    }

    /**
     * Validate Email of User
     *
     * @param value A {@link String} Instance Representing Email of User
     * @return A {@link String} Instance Representing Validated email
     * @throws SystemException A Customized {@link RuntimeException} with type of {@link SystemError#INVALID_DATA_TYPE} when email is not valid
     */
    public static void validateEmail(String value) throws SystemException {
        Pattern pattern = Pattern.compile(UtilityConstant.EMAIL_PATTERN);
        if (value == null || value.equals(""))
            throw new SystemException(SystemError.INVALID_DATA_TYPE, "value:" + value, 1103);
        if (!pattern.matcher(value).matches())
            throw new SystemException(SystemError.INVALID_DATA_TYPE, "value:" + value, 1104);
    }

}
