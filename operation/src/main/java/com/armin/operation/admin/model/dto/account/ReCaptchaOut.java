package com.armin.operation.admin.model.dto.account;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReCaptchaOut {
    private boolean success;
    @JsonAlias("challenge_ts")
    private String challengeTs;
    @JsonAlias("hostname")
    private String hostName;
    @JsonAlias("error-codes")
    private List<String> errorCodes;
    private String score;
    private String action;
}


