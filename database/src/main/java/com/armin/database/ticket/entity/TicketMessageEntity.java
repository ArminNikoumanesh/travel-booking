package com.armin.database.ticket.entity;

import com.armin.utility.file.model.object.Attachment;
import com.armin.database.user.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "TICKET_MESSAGE", indexes = {
        @Index(columnList = "TICKET_ID_FK", name = "ticket_message_ticket_idx"),
        @Index(columnList = "USER_ID_FK", name = "ticket_message_user_idx")
})
public class TicketMessageEntity implements Diffable<TicketMessageEntity>, Cloneable {
    @Id
    @Column(name = "ID_PK")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TicketMessageSeq")
    @SequenceGenerator(name = "TicketMessageSeq", sequenceName = "ticket_message_seq", allocationSize = 50)
    private int id;
    @Basic
    @Column(name = "BODY", length = 1000, nullable = false)
    private String body;
    @Basic
    @Column(name = "CREATED", nullable = false)
    private LocalDateTime created;
    @Basic
    @Column(name = "ATTACHMENT", length = 2000)
    @Attachment(container = "crm", bucket = "ticket")
    private String attachments;
    @Basic
    @Column(name = "IP", nullable = false, length = 30)
    private String ip;
    @Basic
    @Column(name = "SEEN")
    private LocalDateTime seen;
    @Basic
    @Column(name = "TICKET_ID_FK", nullable = false)
    private Integer ticketId;
    @JoinColumn(name = "TICKET_ID_FK", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TicketEntity ticket;
    @Basic
    @Column(name = "USER_ID_FK")
    private Integer userId;
    @JoinColumn(name = "USER_ID_FK", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserEntity user;
    @Basic
    @Column(name = "OPERATOR_ID_FK")
    private Integer operatorId;
    @JoinColumn(name = "OPERATOR_ID_FK", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private UserEntity operator;


    @PrePersist
    public void prePersist() {
        this.created = LocalDateTime.now();
    }

    public TicketMessageEntity cloneAttachments() {
        TicketMessageEntity entity = new TicketMessageEntity();
        entity.setAttachments(this.attachments);
        return entity;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public DiffResult<TicketMessageEntity> diff(TicketMessageEntity entity) {
        return new DiffBuilder(this, entity, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("attachments", this.attachments, entity.getAttachments())
                .append("body", this.body, entity.getBody())
                .build();
    }
}
