package com.armin.operation.address.ident.model;

import com.armin.database.address.address.entity.UserAddressEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

/**
 * @auther : Armin.Nik
 * @date : 20.11.22
 */
@Getter
@Setter
public class UserAddressOut {
    private int id;
    private String address;
    private String zipCode;
    private double lat;
    private double lng;
    private int addressRecordId;
    private boolean defaultAddress;
    private String name;
    private String province;
    private String city;

    public UserAddressOut(UserAddressEntity entity) {
        this.id = entity.getId();
        this.defaultAddress = entity.isDefaultAddress();
        if (Hibernate.isInitialized(entity.getAddressRecord()) && entity.getAddressRecord() != null) {
            this.addressRecordId = entity.getAddressRecord().getId();
            this.address = entity.getAddressRecord().getAddress();
            this.zipCode = entity.getAddressRecord().getZipCode();
            this.lat = entity.getAddressRecord().getLat();
            this.lng = entity.getAddressRecord().getLng();
            this.name = entity.getAddressRecord().getName();
            this.province = entity.getAddressRecord().getProvince();
            this.city = entity.getAddressRecord().getCity();

        }
    }
}
