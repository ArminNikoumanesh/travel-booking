package com.armin.messaging.ticket.statics;

public abstract class TicketRestApi {
     //Ticket Controller
    public static final String TICKET = "/tickets";
    public static final String TICKET_COUNT = "/tickets/count";
    public static final String TICKET_ID = "/tickets/{id}";

    //Ticket Controller
    public static final String TICKET_ADMIN = "/tickets/user";
    public static final String TICKET_ADMIN_COUNT = "/tickets/user/count";
    public static final String TICKET_ADMIN_ID = "/tickets/user/{id}";
    public static final String TICKET_ADMIN_CLOSE = "/tickets/user/{id}/close";
    public static final String TICKET_ADMIN_OPEN = "/tickets/user/{id}/open";

    //Ticket Controller
    public static final String TICKET_MESSAGE = "/ticket-messages";
    public static final String TICKET_MESSAGE_COUNT = "/ticket-messages/count";
    public static final String TICKET_MESSAGE_ID = "/ticket-messages/{id}";
    public static final String TICKET_MESSAGE_TICKET_ID = "/ticket-messages/ticket/{id}";


    //Ticket Controller
    public static final String TICKET_MESSAGE_ADMIN = "/ticket-messages/user";
    public static final String TICKET_MESSAGE_ADMIN_COUNT = "/ticket-messages/user/count";
    public static final String TICKET_MESSAGE_ADMIN_ID = "/ticket-messages/user/{id}";
    public static final String TICKET_MESSAGE_ADMIN_TICKET_ID = "/ticket-messages/user/ticket/{id}";

}
