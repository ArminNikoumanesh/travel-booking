package com.armin.utility.repository.odm.logs.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Table(name = "logs")
@Entity
@Setter
@Getter
public class LogEntity {
    @Id
    @Column(name = "id_pk", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LogSeq")
    @SequenceGenerator(name = "LogSeq", sequenceName = "log_seq", allocationSize = 50)
    private int id;
    @Basic
    @Column(name = "created")
    private LocalDateTime created;
  /*  @Basic
    @Column(name = "ip", length = 25, nullable = false)
    private String ip;
    @Basic
    @Column(name = "created", nullable = false)
    private LocalDateTime created;
    @Basic
    @Column(name = "user_id_fk", nullable = false)
    private Integer adminId;
    @Basic
    @Column(name = "last_name", nullable = false)
    private String adminFullName;*/
    @Basic
    @Column(name = "log_data", nullable = false, columnDefinition = "text")
    private String logData;
    @Basic
    @Column(name = "cause", nullable = false, columnDefinition = "text")
    private String cause;
    /*@Basic
    @Column(name = "log_type", nullable = false)
    private String logType;*/
}
