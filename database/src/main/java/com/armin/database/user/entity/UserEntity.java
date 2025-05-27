package com.armin.database.user.entity;


import com.armin.database.user.statics.UserMedium;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USERS")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {
    @Id
    @Column(name = "ID_PK", scale = 0, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "User_Sequence")
    @SequenceGenerator(name = "User_Sequence", sequenceName = "USER_SEQ", allocationSize = 50)
    private int id;
    @Basic
    @Column(name = "HASHED_PASSWORD", length = 100)
    private String hashedPassword;
    @Basic
    @Column(name = "MOBILE", length = 20)
    private String mobile;
    @Basic
    @Column(name = "MOBILE_CONFIRMED", nullable = false)
    private boolean mobileConfirmed;
    @Basic
    @Column(name = "EMAIL", length = 254)
    private String email;
    @Basic
    @Column(name = "DEVICE_ID")
    private String deviceId;
    @Basic
    @Column(name = "EMAIL_CONFIRMED", nullable = false)
    private boolean emailConfirmed;
    @Basic
    @Column(name = "LEGAL", nullable = false)
    private boolean legal;
    @Basic
    @Column(name = "FIRST_NAME", length = 100)
    private String firstName;
    @Basic
    @Column(name = "LAST_NAME", length = 100)
    private String lastName;
    @Basic
    @Column(name = "FULL_NAME", length = 201)
    private String fullName;
    @Basic
    @Column(name = "LEGAL_NAME", length = 100)
    private String legalName;
    @Basic
    @Column(name = "NATIONAL_ID", length = 12)
    private String nationalId;
    @Basic
    @Column(name = "LOCK_EXPIRED")
    private LocalDateTime lockExpired;
    @Basic
    @Column(name = "TWO_FACTOR_ENABLED", columnDefinition = "boolean default false")
    private boolean twoFactorEnabled;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "MEDIUM", nullable = false, length = 20)
    private UserMedium medium;
    @Basic
    @Column(name = "CREATED", nullable = false)
    private LocalDateTime created;
    @Basic
    @Column(name = "ACCESS_FAILED_COUNT", scale = 0, precision = 10, nullable = false)
    private int accessFailedCount;
    @Basic
    @Column(name = "SUSPENDED", nullable = false)
    private boolean suspended;
    @Basic
    @Column(name = "DELETED")
    private LocalDateTime deleted;
    @Basic
    @Column(name = "VERIFIED", nullable = false, columnDefinition = "boolean default false")
    private boolean verified;
    @Basic
    @Column(name = "REGISTERED", nullable = false, columnDefinition = "boolean default false")
    private boolean registered;

    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "userEntity")
    private ProfileEntity profile;

    @OneToMany(mappedBy = "userEntity", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<SecurityUserRoleRealmEntity> roleRealms = new HashSet<>();


    public void setRoleRealms(Set<SecurityUserRoleRealmEntity> roleRealms) {
        this.roleRealms.clear();
        if (roleRealms != null) {
            this.roleRealms.addAll(roleRealms);
        }
    }

    public void setFullName() {
        if ((firstName != null && !firstName.isEmpty()) || (lastName != null && !lastName.isEmpty())) {
            if (firstName != null) {
                this.fullName = firstName + " ";
            }
            if (lastName != null) {
                this.fullName += lastName;
            }
            return;
        } else if (mobile != null && !mobile.isEmpty()) {
            this.fullName = null;
        } else if (email != null && !email.isEmpty()) {
            this.fullName = null;
        } else {
            this.fullName = null;
        }
    }
}
