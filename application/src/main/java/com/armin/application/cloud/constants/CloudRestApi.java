package com.armin.application.cloud.constants;

import lombok.Getter;

@Getter
public abstract class CloudRestApi {
    public CloudRestApi() {
    }

    private static final String CLOUD_CONFIG = "/cloud/config-settings";
    public static final String ACCOUNT = CLOUD_CONFIG + "/account";
    public static final String REFRESH = CLOUD_CONFIG + "/refresh";
    public static final String ADDRESS = CLOUD_CONFIG + "/address";
    public static final String ORDER = CLOUD_CONFIG + "/order";
    public static final String ORDER_MESSAGE = CLOUD_CONFIG + "/order-message";
    public static final String SECURITY = CLOUD_CONFIG + "/security";
    public static final String FINANCIAL = CLOUD_CONFIG + "/financial";
    public static final String ALL_CONFIGS = CLOUD_CONFIG + "/all";
    public static final String PRODUCT = CLOUD_CONFIG + "/product";
    public static final String NOTIFICATION = CLOUD_CONFIG + "/notification";
    public static final String INVOICE = CLOUD_CONFIG + "/invoice";
    public static final String ROUTE_API = CLOUD_CONFIG + "/route-api";
}
