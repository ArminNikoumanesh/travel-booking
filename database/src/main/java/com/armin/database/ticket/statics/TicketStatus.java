package com.armin.database.ticket.statics;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
public enum TicketStatus {
    PENDING(0),
    IN_PROGRESS(1),
    PENDING_BY_USER(2),
    CLOSED(3);

    private int value;

    private TicketStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
