package com.armin.messaging.template.repository.dto;

import com.armin.infrastructure.common.HandlebarsTemplateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HandlebarsTemplateOut extends HandlebarsTemplateEdit {
    private int id;
    private String name;
    private List<String> parameters;

    public HandlebarsTemplateOut(HandlebarsTemplateEntity entity) {
        this.id = entity.getId();
        this.setName(entity.getName());
        this.setContent(entity.getContent());
        if (entity.getParameters() != null) {
            parameters = Arrays.asList(entity.getParameters().split(","));
        }
    }
}
