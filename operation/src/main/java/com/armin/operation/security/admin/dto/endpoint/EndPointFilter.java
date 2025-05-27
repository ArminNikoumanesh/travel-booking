package com.armin.operation.security.admin.dto.endpoint;

import com.armin.utility.repository.orm.service.FilterBase;
import org.springframework.http.HttpMethod;

/**
 * @author imax on 5/16/19
 * @author Mohammad Yasin Sadeghi
 */
public class EndPointFilter implements FilterBase {


    private Integer id;
    private String url;
    private HttpMethod httpMethod;
    private Integer permissionId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }
}
