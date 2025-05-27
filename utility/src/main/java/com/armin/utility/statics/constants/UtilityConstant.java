package com.armin.utility.statics.constants;

public abstract class UtilityConstant {

    private UtilityConstant() {
    }

    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    public static final String ZIP_CODE_PATTERN = "[0-9]{10}";

    public static final String ENGLISH_STRING_PATTERN = "(?=.*[_A-Za-z0-9]).{1,}";
    public static final String PERSIAN_STRING_PATTERN = "(?=.*[\\u0600-\\u06FF\\uFB8A\\u067E\\u0686\\u06AF\\u200C\\u200F ]).{4,}";

    public static final Integer DEFAULT_REPORT_PAGE_SIZE = 10;
    public static final Integer DEFAULT_REPORT_PAGE_NUMBER = 0;
    public static final Integer MINIMUM_REPORT_PAGE_SIZE = 1;
    public static final Integer MAXIMUM_REPORT_PAGE_SIZE = 350;
    public static final Integer MINIMUM_REPORT_PAGE_NUMBER = 1;

    public static final String HASH_FUNCTION = "SHA-256";
    public static final String GOOGLE_API_KEY = "AIzaSyDh3yzsorXDyP-gAlxEGUlzmfJ6nVxizUw";
    public static final String PERSIAN_DATE_FORMAT = "yyyy/MM/dd";
    public static final String PERSIAN_DATE_TIME_FORMAT = "yyyy/MM/dd-HH:mm:ss";
    public static final String PERSIAN_LOCALE_ID = "fa_IR@calendar=persian";
    public static final String PERSIAN_ZONE_ID = "Asia/Tehran";


}
