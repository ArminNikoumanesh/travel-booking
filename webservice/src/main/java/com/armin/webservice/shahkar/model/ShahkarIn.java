package com.armin.webservice.shahkar.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ShahkarIn {
    private RequestContext requestContext;
    private String nationalId;
    private String mobile;
}
