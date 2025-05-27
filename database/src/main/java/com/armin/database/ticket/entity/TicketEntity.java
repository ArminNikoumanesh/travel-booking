package com.armin.database.ticket.entity;


import com.armin.database.ticket.statics.TicketPriority;
import com.armin.database.ticket.statics.TicketStatus;
import com.armin.database.user.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TICKET", indexes = {
        @Index(columnList = "USER_ID_FK", name = "ticket_user_idx")
})
@Setter
@Getter
public class TicketEntity {
    @Id
    @Column(name = "ID_PK", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TicketSeq")
    @SequenceGenerator(name = "TicketSeq", sequenceName = "ticket_seq", allocationSize = 50)
    private int id;
    @Basic
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "STATUS", nullable = false)
    private TicketStatus status;
    @Enumerated(EnumType.STRING)
    @Column(name = "PRIORITY", nullable = false)
    private TicketPriority priority;
    @Basic
    @Column(name = "CREATED", nullable = false)
    private LocalDateTime created;
    @Basic
    @Column(name = "CLOSED")
    private LocalDateTime closed;
    @Basic
    @Column(name = "SUBJECT", nullable = false)
    private String subject;
    @Basic
    @Column(name = "USER_ID_FK")
    private Integer userId;
    @JoinColumn(name = "USER_ID_FK", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserEntity user;

    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ticket")
    private Set<TicketMessageEntity> messages = new HashSet<>();


    @PrePersist
    public void prePersist() {
        this.created = LocalDateTime.now();
    }
}
