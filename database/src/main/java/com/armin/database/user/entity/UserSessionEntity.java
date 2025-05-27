package com.armin.database.user.entity;

import com.armin.security.statics.constants.ClientType;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "USER_SESSION")
@Getter
@Setter
public class UserSessionEntity {
    @Id
    @Column(name = "ID_PK", scale = 0, precision = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Session_Sequence")
    @SequenceGenerator(name = "Session_Sequence", sequenceName = "USER_SESSION_SEQ", allocationSize = 50)
    private int id;
    @Basic
    @Column(name = "USER_ID_FK", scale = 0, precision = 10)
    private int userId;
    @JoinColumn(name = "USER_ID_FK", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;
    @Basic
    @Column(name = "UNIQUE_ID", length = 100)
    private String uniqueId;
    @Basic
    @Column(name = "CLIENT", length = 12, nullable = false)
    @Enumerated(EnumType.STRING)
    private ClientType clientType;
    @Basic
    @Column(name = "OS", length = 50, nullable = false)
    private String os;
    @Basic
    @Column(name = "AGENT", length = 400, nullable = false)
    private String agent;
    @Basic
    @Column(name = "FIREBASE_TOKEN", length = 400)
    private String firebaseToken;
    @Basic
    @Column(name = "CREATED", nullable = false)
    private LocalDateTime created;
    @Basic
    @Column(name = "IP", length = 39, nullable = false)
    private String ip;
}
