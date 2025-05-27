package com.armin.database.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import java.util.Set;

@Entity
@Table(name = "SECURITY_REST")
@Getter
@Setter
public class SecurityRestEntity {
    @Id
    @Column(name = "ID_PK", scale = 0, precision = 10)
    private int id;
    @Basic

    @Column(name = "HTTP_METHOD", length = 10, nullable = false)
    private String httpMethod;

    @Basic
    @Column(name = "URL", length = 200, nullable = false)
    private String url;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "rests")
    private Set<SecurityPermissionEntity> permissions;
}
