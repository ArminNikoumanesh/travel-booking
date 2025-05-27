package com.armin.database.global.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Getter
@Setter
@Embeddable
public class GlobalPropertyId implements Serializable {
    private String name;
    private String profile;

    public GlobalPropertyId(String name, String profile) {
        this.name = name;
        this.profile = profile;
    }

    public GlobalPropertyId() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GlobalPropertyId profile = (GlobalPropertyId) o;
        return Objects.equals(name, profile.name) && Objects.equals(profile, profile.profile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, profile);
    }
}
