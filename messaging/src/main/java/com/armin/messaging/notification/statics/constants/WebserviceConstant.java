package com.armin.messaging.notification.statics.constants;

public abstract class WebserviceConstant {

    public static final String GAMA_RECEIVE_SMS_URL = "http://panel.gamamn.ir:8080/url/receive.aspx?username=y.nasr&password=123qwe&LastRowID=0";
    public static final String GAMA_DELIVERY_SMS_URL = "http://panel.gamamn.ir:8080/url/delivery.aspx?username=y.nasr&ReturnIDs=";
    public static final String GAMA_INFO_SMS_URL = "http://panel.gamamn.ir:8080/url/GetInfoXML.aspx?username=y.nasr&password=123qwe";
    //public static final String GAMA_SEND_SMS_URL = "http://panel.gamamn.ir:8080/url/send.aspx?username=y.nasr&password=123qwe&farsi=true&from=10006315821732";
    public static final String GAMA_SEND_SMS_URL = "http://panel.gamamn.ir:8080/url/send.aspx";
    public static final String GAMA_FROM_NUMBER_1 = "10006315821732";


    public static final String MAGFA_SEND_SMS_URL = "https://sms.magfa.com/magfaHttpService?service=Enqueue&domain=magfa";
    public static final String MAGFA_FROM_NUMBER_1 = "98300071777";
    public static final String MAGFA_FROM_NUMBER_2 = "98300071778";
    public static final String MAGFA_FROM_NUMBER_3 = "98300071779";
    public static final Integer SMS_PROVIDER_ID_GAMA = 1;
    public static final Integer SMS_PROVIDER_ID_MAGFA = 2;
    public static final Integer MAGFA_BULK_SMS_MAX = 15;
}
