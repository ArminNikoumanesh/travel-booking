package com.armin.database.user.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "SECURITY_REALM")
@Getter
@Setter
public class SecurityRealmEntity {
    @Id
    @Column(name = "ID_PK", scale = 0, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Realm_Sequence")
    @SequenceGenerator(name = "Realm_Sequence", sequenceName = "SECURITY_REALM_SEQ", allocationSize = 1)
    private int id;
    @Basic
    @Column(name = "NAME", length = 100)
    private String name;
    @Basic
    @Column(name = "DELETED", nullable = false)
    private boolean deleted;
}
