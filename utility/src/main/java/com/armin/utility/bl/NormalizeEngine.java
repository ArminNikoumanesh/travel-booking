package com.armin.utility.bl;

import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;

import java.util.Arrays;
import java.util.Random;

/**
 * The Normalize Service Class,
 * Containing Methods about Normalizing Input Data
 *
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */

/**
 * Exceptions error code range: 1081-1100
 */

public final class NormalizeEngine {

    private static final Random random = new Random();
    private static final String INT_ARRAY_STRING_REGEX = "\\[|]|\\s";
    private static final String IRAN_POSTAL_CODE_REGEX = "\\b(?!(\\d)\\1{3})[13-9]{4}[1346-9][013-9]{5}\\b";

    public static boolean mobileCheker(String mobile) {
        String mobileRegex = "(0|\\+98)?([ ]|,|-|[()]){0,2}9[1|2|3|4]([ ]|,|-|[()]){0,2}(?:[0-9]([ ]|,|-|[()]){0,2}){8}";
        return (mobile.matches(mobileRegex));
    }

    public static String getSlug(String value) {
        if (Strings.isNotBlank(value)) {
            value = value.trim().toLowerCase();
            String result = value.replaceAll("\\s+\\u200c*|\\u200c+\\s*|-+", "-");
            result = result.replace("/", ".");
            result = normalizeSpecialCharacter(result);
            return normalizePersianString(result);
        } else
            return null;
    }

    public static String normalizeSpecialCharacter(String input) {
        String pattern = "!*@*#*\\$*%*\\^*&*\\**\\(*\\)*\\{*}*_*";
        return input.replaceAll(pattern, "");
    }

    public static String normalizePercentInSlug(String slug) {
        return Strings.isBlank(slug) ? null : slug.replace("%", "درصد");
    }

    public static String trimAndNormalizeAndLowerCaseEnglishString(String input) {
        if (Strings.isNotBlank(input)) {
            return input.trim().toLowerCase();
        } else {
            return null;
        }
    }

    public static String trimAndNormalizePersianString(String input) {
        if (Strings.isNotBlank(input)) {
            return normalizePersianString(input.trim());
        } else {
            return null;
        }
    }

    public static String convertIntegerArrayToString(Integer[] array) {
        return Arrays.toString(array).replaceAll(INT_ARRAY_STRING_REGEX, "");
    }

    public static boolean nationalCodeChecker(String nationalCode) throws SystemException {
//        if (Strings.isBlank(nationalCode)) {
//            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "null", 1081);
//        }
//        int c, n, r;
//        char[] a = nationalCode.toCharArray();
//        if (nationalCode.length() == 10) {
//            boolean sameChars = nationalCode.chars().allMatch(ch -> ch == nationalCode.charAt(0));
//            if (sameChars) {
//                throw new SystemException(SystemError.VALIDATION_EXCEPTION, "nationalCode:" + nationalCode, 1082);
//            } else {
//                c = Integer.parseInt(String.valueOf(a[9]));
//                n = Integer.parseInt(String.valueOf(a[0])) * 10 + Integer.parseInt(String.valueOf(a[1])) * 9 + Integer.parseInt(String.valueOf(a[2])) * 8 + Integer.parseInt(String.valueOf(a[3])) * 7 + Integer.parseInt(String.valueOf(a[4])) * 6 + Integer.parseInt(String.valueOf(a[5])) * 5 + Integer.parseInt(String.valueOf(a[6])) * 4 + Integer.parseInt(String.valueOf(a[7])) * 3 + Integer.parseInt(String.valueOf(a[8])) * 2;
//                r = n - (n / 11) ;
//                if ((r == 0 && r == c) || (r == 1 && c == 1) || (r > 1 && c == 11 - r)) {
//                    return true;
//                } else {
//                    throw new SystemException(SystemError.VALIDATION_EXCEPTION, "nationalCode:" + nationalCode, 1083);
//                }
//            }
//        } else {
//            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "nationalCode:" + nationalCode, 1084);
//        }

        if (Strings.isBlank(nationalCode)) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "null", 1081);
        } else if (nationalCode.length() != 10) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "nationalCode:" + nationalCode, 1084);
        } else {
            int sum = 0;

            for (int i = 0; i < 9; i++) {
                sum += Character.getNumericValue(nationalCode.charAt(i)) * (10 - i);
            }
            int lastDigit;
            int divideRemaining = sum % 11;

            if (divideRemaining < 2) {
                lastDigit = divideRemaining;
            } else {
                lastDigit = 11 - (divideRemaining);
            }

            if (Character.getNumericValue(nationalCode.charAt(9)) == lastDigit) {
                return true;
            } else {
                throw new SystemException(SystemError.VALIDATION_EXCEPTION, "nationalCode:" + nationalCode, 1084);
            }
        }
    }

    public static String normalizePersianDigits(String number) {
        if (Strings.isNotBlank(number)) {
            number = number.trim();
            char[] chars = new char[number.length()];
            for (int i = 0; i < number.length(); i++) {
                char ch = number.charAt(i);
                if (ch >= 0x0660 && ch <= 0x0669)
                    ch -= 0x0660 - '0';
                else if (ch >= 0x06f0 && ch <= 0x06F9)
                    ch -= 0x06f0 - '0';
                chars[i] = ch;
            }
            return new String(chars);
        } else {
            return null;
        }
    }

    public static String getNormalizedPhoneNumber(String mobile) throws SystemException {
        try {
            if (Strings.isBlank(mobile)) {
                return null;
            }
            mobile = mobile.trim();
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber phoneProto = phoneUtil.parse(mobile, "IR");
            if (phoneUtil.isValidNumber(phoneProto)
                    && phoneUtil.isPossibleNumberForType(phoneProto, PhoneNumberUtil.PhoneNumberType.MOBILE)) {
                return phoneUtil.format(phoneProto, PhoneNumberUtil.PhoneNumberFormat.E164);
            }
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "mobile:" + mobile, 1085);
        } catch (NumberParseException e) {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "mobile:" + mobile, 1086);
        }
    }

    public static String normalizePersianString(String value) {
        if (Strings.isBlank(value)) {
            return value;
        }
        return value.trim().replace("۰", "0").replace("٠", "0")
                .replace("۱", "1").replace("١", "1")
                .replace("۲", "2").replace("٢", "2")
                .replace("۳", "3").replace("٣", "3")
                .replace("۴", "4").replace("٤", "4")
                .replace("۵", "5").replace("٥", "5")
                .replace("۶", "6").replace("٦", "6")
                .replace("۷", "7").replace("٧", "7")
                .replace("۸", "8").replace("٨", "8")
                .replace("۹", "9").replace("٩", "9")
                .replace("بِ", "ب")
                .replace("زِ", "ز")
                .replace("دِ", "د")
                .replace("ذِِ", "ذ")
                .replace("سِ", "س")
                .replace("شِ", "ش")
                .replace("ك", "ک")
                .replace("ى", "ی")
                .replace("ي", "ی");
    }

    public static String normalizePersianSpecialCharacter(String value) {
        if (Strings.isBlank(value)) {
            return value;
        }
        return value.trim().replace("آ", "ا");

    }

    public static String normalizeSpecialPersianString(String value) {
        if (Strings.isBlank(value))
            return value;
        return value.replace("آ", "ا");
    }


    public static boolean checkParsableKeyword(String keyword) {
        if (keyword.length() > 9) {
            return false;
        }
        String regex = "[0-9]+";
        return keyword.matches(regex);
    }

    public static int getRandomNumber() {
        return 100000 + random.nextInt(900000);
    }

    public static boolean validateIranZipCode(String zipCode) throws SystemException {
        if (Strings.isBlank(zipCode)) {
//            throw new SystemException(SystemError.VALIDATION_EXCEPTION, "null", 1095);
            return true;
        }
        if (zipCode.matches(IRAN_POSTAL_CODE_REGEX)) {
            return true;
        } else {
            throw new SystemException(SystemError.VALIDATION_EXCEPTION, zipCode, 1096);
        }
    }

    public static void validateTelephoneOrFax(String number, String prefix) throws SystemException {
        if ((StringUtils.isEmpty(number) && StringUtils.isNotEmpty(prefix)) || (StringUtils.isNotEmpty(number) && StringUtils.isEmpty(prefix))) {
            throw new SystemException(SystemError.ILLEGAL_REQUEST, "telephone or fax validation exception", 1087);
        } else {
            if ((number != null && prefix != null) && (number.length() != 8 || (prefix.length() != 3 || Integer.parseInt(prefix.substring(0, 1)) != 0))) {
                throw new SystemException(SystemError.ILLEGAL_REQUEST, "telephone or fax validation exception", 1087);
            }
        }
    }

    public static String trimAndReplaceWhiteSpaces(String s) {
        return s == null ? null : s.replaceAll("\\s+", "").replace("\u200c", "");
    }
}
