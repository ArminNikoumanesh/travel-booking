package com.armin.security.model.object;

import com.armin.security.statics.constants.SecurityConstant;
import com.armin.utility.bl.StringService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class SecurityAccessRule {
    private String url;
    private HttpMethod httpMethod;
    private List<String> access;

//    public SecurityAccessRule(SecurityRestEntity entity) {
//        this.url = entity.getUrl();
//        this.httpMethod = entity.getHttpMethod();
//        this.access = entity.getPermissions().stream()
//                .map(x -> SecurityConstant.ACCESS_RULE_PREFIX + x.getId())
//                .collect(Collectors.toList());
//    }

    public SecurityAccessRule(String url, HttpMethod httpMethod, String access) {
        this.url = url;
        this.httpMethod = httpMethod;
        this.access = StringService.convertCommaSeparatedStringToList(access)
                .stream()
                .map(x -> SecurityConstant.SIMPLE_GRANTED_AUTHORITY_PREFIX + x)
                .collect(Collectors.toList());
    }

    public void setAccess(String access) {
        this.access = StringService.convertCommaSeparatedStringToList(access)
                .stream()
                .map(x -> SecurityConstant.SIMPLE_GRANTED_AUTHORITY_PREFIX + x)
                .collect(Collectors.toList());
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = HttpMethod.valueOf(httpMethod);
    }
}
