package com.armin.utility.repository.odm.logs.entity;

import com.armin.utility.object.ClientInfo;
import com.armin.utility.object.UserContextDto;
import com.armin.utility.repository.odm.model.entity.ElasticsearchEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.BeanPropertyBindingResult;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AuditLogElastic extends ElasticsearchEntity {
    private LocalDateTime created;
    private String client;
    private String ip;
    private String method;
    private String className;
    private String agent;
    private Object parameters;
    private String fullName;
    private Integer userId;


    public AuditLogElastic(String className, String method, HttpServletRequest request, Object[] inputParameters) {
        ClientInfo clientInfo = new ClientInfo(request);
        this.client = clientInfo.getRequestURI();
        this.ip = clientInfo.getMainIP();
        this.agent = clientInfo.getAgent();
        this.created = LocalDateTime.now();
        this.className = className;
        this.method = method;
        for (Object inputParameter : inputParameters) {
            if (!(inputParameter instanceof HttpServletRequest)
                    && !(inputParameter instanceof BeanPropertyBindingResult)
                    && !(inputParameter instanceof Integer)) {
                parameters = inputParameter;
                break;
            }
        }
    }

    public void setUserInfo(UserContextDto contextDto) {
        this.userId = contextDto.getId();
        this.fullName = contextDto.getFullName();
    }

    @Override
    public String toString() {
        return "AuditLogElastic{" +
                "created=" + created +
                ", client='" + client + '\'' +
                ", ip='" + ip + '\'' +
                ", method='" + method + '\'' +
                ", className='" + className + '\'' +
                ", agent='" + agent + '\'' +
                ", parameters=" + parameters +
                ", fullName='" + fullName + '\'' +
                ", userId=" + userId +
                '}';
    }
}
