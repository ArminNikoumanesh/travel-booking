package com.armin.security.bl;

import com.armin.security.statics.enums.RoleCategoryType;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import org.springframework.stereotype.Service;

/**
 * The Validation Service Class,
 * Containing Methods about String Patterns and Input Validation
 *
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */

/**
 * Exceptions error code range: 2101-2150
 */
@Service
public class SecurityValidationService {

    public String validateAuthHeaderToken(String value) throws SystemException {
        // 7 characters for "Bearer " and 2 characters for two dots
        if (value == null || value.length() < 10)
            throw new SystemException(SystemError.INVALID_TOKEN_HEADER, "value", 2101);
        return value.replaceAll("Bearer ", "");
    }

    public String validatePublicHeaderToken(String value) {
        // 7 characters for "Bearer " and 2 characters for two dots
        if (value == null || value.length() < 10)
            return null;
        return value.replaceAll("Bearer ", "");
    }

    public RoleCategoryType validateRoleCategoryType(String value) throws SystemException {
        if (value == null || value.equals(""))
            throw new SystemException(SystemError.INVALID_DATA_TYPE, "value", 2102);
        try {
            Integer x = Integer.valueOf(value);
            for (RoleCategoryType type : RoleCategoryType.values()) {
                if (x.equals(type.getCode()))
                    return type;
            }
        } catch (NumberFormatException e) {
            throw new SystemException(SystemError.INVALID_DATA_TYPE, "value", 2103);
        }
        throw new SystemException(SystemError.INVALID_DATA_TYPE, "value", 2104);
    }
}
