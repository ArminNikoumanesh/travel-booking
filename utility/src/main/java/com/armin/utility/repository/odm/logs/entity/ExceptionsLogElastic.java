package com.armin.utility.repository.odm.logs.entity;

import com.armin.utility.repository.odm.model.entity.ElasticsearchEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ExceptionsLogElastic extends ElasticsearchEntity {
    private LocalDateTime created;
    private String stackTrace;
    private String cause;
    private String method;
    private String className;

    public ExceptionsLogElastic(String className, String method, String stackTrace, String cause) {
        this.stackTrace = stackTrace;
        this.method = method;
        this.className = className;
        this.cause = cause;
        this.created = LocalDateTime.now();
    }
}
