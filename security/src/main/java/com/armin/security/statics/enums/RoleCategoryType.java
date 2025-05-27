package com.armin.security.statics.enums;

public enum RoleCategoryType {
    SUPER_ADMIN(-1, "SUP_ADM", "مدیر اصلی"),

    ADMIN_OWNER(1, "ADM_OWN", "مدیر به روز رسان"),
    ADMIN_COMPANY(2, "ADM_CMP", "مدیر تامین کننده"),

    OPERATOR_OWNER(10, "OPR_OWN", "اپراتور به روز رسان"),
    OPERATOR_COMPANY(50, "OPR_CMP", "اپراتور تامین کننده"),

    DRIVER_OWNER(100, "DRV_OWN", "رساننده به روز رسان");

//    CUSTOMER(1000, "CUS", "مشتری");

    private final Integer code;
    private final String name;
    private final String title;

    RoleCategoryType(Integer code, String name, String title) {
        this.code = code;
        this.name = name;
        this.title = title;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }
}
