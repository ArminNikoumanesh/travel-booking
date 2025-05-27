package com.armin.operation.security.admin.dto.realm;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.armin.utility.bl.NormalizeEngine.normalizePersianString;

/**
 * @author imax on 5/18/19
 */
public class RealmIn {
    @NotNull
    @Size(max = 50)
    private String name;
//    private RealmType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = normalizePersianString(name);
    }

//    public RealmType getType() {
//        return type;
//    }
//
//    public void setType(RealmType type) {
//        this.type = type;
//    }
}
