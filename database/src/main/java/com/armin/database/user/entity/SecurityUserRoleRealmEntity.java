package com.armin.database.user.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "SECURITY_USER_ROLE_REALM")
@Getter
@Setter
@NoArgsConstructor
public class SecurityUserRoleRealmEntity {
    @Id
    @Column(name = "ID_PK")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UserRoleRealm_Sequence")
    @SequenceGenerator(name = "UserRoleRealm_Sequence", sequenceName = "SECURITY_USER_ROLE_REALM_SEQ", allocationSize = 50)
    private int id;
    @Basic
    @Column(name = "ROLE_ID_FK", nullable = false)
    private int roleId;
    @Basic
    @Column(name = "REALM_ID_FK", nullable = false)
    private int realmId;
    @Basic
    @Column(name = "USER_ID_FK", nullable = false, insertable = false, updatable = false)
    private int userId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID_FK")
    private UserEntity userEntity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID_FK", referencedColumnName = "ID_PK", insertable = false, updatable = false)
    private SecurityRoleEntity role;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REALM_ID_FK", referencedColumnName = "ID_PK", insertable = false, updatable = false)
    private SecurityRealmEntity realmEntity;

    public SecurityUserRoleRealmEntity(UserEntity userEntity) {
        this.roleId = -3;
        this.realmId = -1;
        this.userEntity = userEntity;
    }
}
