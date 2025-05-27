package com.armin.database.address.record.entity;

import com.armin.database.address.address.entity.UserAddressEntity;
import com.armin.database.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Entity
@Table(name = "ADDRESS_RECORD", indexes = {
        @Index(columnList = "user_id_fk", name = "user_record_id_idx"),
        @Index(columnList = "user_address_id_fk", name = "user_record_address_id_idx")
})
@Getter
@Setter
public class AddressRecordEntity {
    @Id
    @Column(name = "id_pk", scale = 0, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AddressRecordSeq")
    @SequenceGenerator(name = "AddressRecordSeq", sequenceName = "address_record_seq", allocationSize = 50)
    private int id;

    @Basic
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Basic
    @Column(name = "ZIP_CODE", length = 16)
    private String zipCode;

    @Basic
    @Column(name = "PROVINCE", length = 30)
    private String province;

    @Basic
    @Column(name = "CITY", length = 30)
    private String city;

    @Basic
    @Column(name = "LATITUDE", nullable = false)
    private double lat;

    @Basic
    @Column(name = "LONGITUDE", nullable = false)
    private double lng;

    @Basic
    @Column(name = "ADDRESS", nullable = false, length = 500)
    private String address;

    @Basic
    @Column(name = "CREATED", nullable = false)
    private LocalDateTime created;

    @Column(name = "USER_ID_FK", nullable = false)
    private int userId;

    @JoinColumn(name = "USER_ID_FK", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserEntity user;

    @Column(name = "USER_ADDRESS_ID_FK")
    private Integer userAddressId;

    @JoinColumn(name = "USER_ADDRESS_ID_FK", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserAddressEntity userAddress;

    public void setFullName() {

    }
}