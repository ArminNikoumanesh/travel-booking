package com.armin.messaging.template.repository.dto;

import com.armin.utility.repository.orm.service.FilterBase;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Size;

@Getter
@Setter
public class HandlebarsTemplateFilter implements FilterBase {
    private Integer id;
    @Size(max = 100)
    private String name;
    private String content;
}
