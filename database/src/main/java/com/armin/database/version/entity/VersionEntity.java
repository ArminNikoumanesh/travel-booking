package com.armin.database.version.entity;

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
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "VERSION")
public class VersionEntity {
    @Id
    @Column(name = "ID_PK")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Version_Sequence")
    @SequenceGenerator(name = "Version_Sequence", sequenceName = "VERSION_SEQ", allocationSize = 50)
    private int id;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "SERVICE_TYPE", nullable = false, length = 50)
    private ServiceType type;

    @Basic
    @Column(name = "LAST_MODIFIED", nullable = false)
    private LocalDateTime lastModified;

    public VersionEntity(ServiceType type) {
        this.type = type;
    }
}
