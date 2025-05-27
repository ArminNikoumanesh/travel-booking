package com.armin.application.cloud.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigRequest {
    private String application;
    private String profile;
    private String data;

    public ConfigRequest(String application, String profile, String data) {
        this.application = application;
        this.profile = profile;
        this.data = data;
    }
}
