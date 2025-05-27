package com.armin.database.user.entity;

import com.armin.database.user.statics.PermissionType;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnTransformer;

import java.util.Set;

@Entity
@Table(name = "SECURITY_PERMISSION")
@Getter
@Setter
public class SecurityPermissionEntity {
    @Id
    @Column(name = "ID_PK", scale = 0, precision = 10)
    private int id;
    @Basic
    @Column(name = "PARENT_ID_FK", scale = 0, precision = 10)
    private Integer parentIdFk;
    @Basic
    @Column(name = "NODE_TYPE", nullable = false, scale = 0, precision = 10)
    private int nodeType;
    @Basic
    @Column(name = "TRAVERSAL", nullable = false)
    private boolean traversal;
    @Basic
    @Column(name = "NAME", length = 200)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PermissionType type;
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "SECURITY_PERMISSION_REST",
            joinColumns = @JoinColumn(name = "PERMISSION_ID_FK", referencedColumnName = "ID_PK", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "REST_ID_FK", referencedColumnName = "ID_PK", nullable = false))
    private Set<SecurityRestEntity> rests;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    private Set<SecurityRoleEntity> roleEntities;
}
