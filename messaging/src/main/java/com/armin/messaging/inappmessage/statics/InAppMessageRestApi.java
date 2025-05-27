package com.armin.messaging.inappmessage.statics;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 21.04.24
 */
public abstract class InAppMessageRestApi {
    public static final String IN_APP_MESSAGE = "/in_app_message";
    public static final String IN_APP_MESSAGE_ID = "/in_app_message/{id}";
    public static final String IN_APP_MESSAGE_COUNT = "/in_app_message/count";

    private InAppMessageRestApi() {
    }
}
