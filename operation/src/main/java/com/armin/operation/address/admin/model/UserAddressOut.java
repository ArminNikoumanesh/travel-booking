package com.armin.operation.address.admin.model;

import com.armin.database.address.address.entity.UserAddressEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;

/**
 * @auther : Armin.Nik
 * @date : 20.11.22
 */
@Getter
@Setter
public class UserAddressOut {
    private int id;
    private LocalDateTime created;
    private String address;
    private String number;
    private String unit;
    private Integer floor;
    private String zipCode;
    private double lat;
    private double lng;
    private String firstName;
    private String lastName;
    private String fullName;
    private String telephone;
    private String mobile;
    private Long countryId;
    private Long provinceId;
    private Long countyId;
    private Long cityId;
    private Long suburbId;
    private Long neighborhoodId;

    public UserAddressOut(UserAddressEntity entity) {
        this.id = entity.getId();
        if (Hibernate.isInitialized(entity.getAddressRecord()) && entity.getAddressRecord() != null) {

        }
        this.address = entity.getAddressRecord().getAddress();
        this.zipCode = entity.getAddressRecord().getZipCode();
        this.lat = entity.getAddressRecord().getLat();
        this.lng = entity.getAddressRecord().getLng();

    }
}
