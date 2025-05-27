package com.armin.database.address.address.entity;

import com.armin.database.address.record.entity.AddressRecordEntity;
import com.armin.database.user.entity.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Entity
@Table(name = "USER_ADDRESS", indexes = {
        @Index(columnList = "user_id_fk", name = "user_address_user_id_idx"),
        @Index(columnList = "address_record_id_fk", name = "user_address_record_id_idx")
})
@Getter
@Setter
@NoArgsConstructor
public class UserAddressEntity {
    @Id
    @Column(name = "ID_PK", scale = 0, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "User_Address_Sequence")
    @SequenceGenerator(name = "User_Address_Sequence", sequenceName = "USER_ADDRESS_SEQ", allocationSize = 50)
    private int id;

    @Column(name = "deleted")
    private LocalDateTime deleted;

    @Column(name = "default_address", nullable = false)
    private boolean defaultAddress;

    @Column(name = "USER_ID_FK", nullable = false)
    private int userId;

    @JoinColumn(name = "USER_ID_FK", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserEntity user;

    @Column(name = "ADDRESS_RECORD_ID_FK")
    private Integer addressRecordId;

    @JoinColumn(name = "ADDRESS_RECORD_ID_FK", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private AddressRecordEntity addressRecord;


    public UserAddressEntity(int userId, int addressRecordId) {
        this.id = id;
        this.deleted = deleted;
        this.defaultAddress = false;
        this.userId = userId;
        this.addressRecordId = addressRecordId;
    }
}
