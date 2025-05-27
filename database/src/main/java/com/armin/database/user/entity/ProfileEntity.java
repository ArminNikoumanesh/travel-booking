package com.armin.database.user.entity;

import com.armin.utility.file.model.object.Attachment;
import com.armin.database.user.statics.Gender;
import com.armin.database.user.statics.LocaleType;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "USER_PROFILE")
@Getter
@Setter
public class ProfileEntity {
    @Id
    @Column(name = "ID_PK", scale = 0, precision = 10)
    private int id;
    @Basic
    @Column(name = "IMAGE", length = 100)
    @Attachment(container = "users", bucket = "image")
    private String image;
    @Basic
    @Column(name = "PHONE", length = 20)
    private String phone;
    @Basic
    @Column(name = "FAX", length = 20)
    private String fax;
    @Basic
    @Column(name = "GENDER", scale = 0, precision = 3)
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;
    @Basic
    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;
    @Basic
    @Column(name = "REGISTRATION_NO", length = 11)
    private String registrationNo;
    @Basic
    @Column(name = "ECONOMIC_CODE", length = 12)
    private String economicCode;

    @Basic
    @Column(name = "LOCALE", scale = 0, precision = 6)
    @Enumerated(EnumType.ORDINAL)
    private LocaleType locale;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "ID_PK")
    private UserEntity userEntity;

    public ProfileEntity cloneImages() {
        ProfileEntity result = new ProfileEntity();
        result.setImage(this.getImage());
        return result;
    }
}
