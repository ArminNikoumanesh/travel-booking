package com.armin.database.user.entity;


import com.armin.database.user.statics.RoleType;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "SECURITY_ROLE")
@Getter
@Setter
public class SecurityRoleEntity {
    @Id
    @Column(name = "ID_PK", scale = 0, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Role_Sequence")
    @SequenceGenerator(name = "Role_Sequence", sequenceName = "SECURITY_ROLE_SEQ", allocationSize = 1)
    private int id;
    @Basic
    @Column(name = "CATEGORY", scale = 0, precision = 10)
    private Integer category;
    @Basic
    @Column(name = "NAME", length = 50)
    private String name;
    @Basic
    @Column(name = "DELETED", nullable = false)
    private boolean deleted;
    @Basic
    @Column(name = "SHOW", nullable = false, columnDefinition = "boolean default true")
    private boolean show;
    @Column(name = "type", nullable = false)
    private RoleType type;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "SECURITY_ROLE_PERMISSION", joinColumns = @JoinColumn(name = "ROLE_ID_FK", referencedColumnName = "ID_PK", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "PERMISSION_ID_FK", referencedColumnName = "ID_PK", nullable = false))
    private Set<SecurityPermissionEntity> permissions;
}
