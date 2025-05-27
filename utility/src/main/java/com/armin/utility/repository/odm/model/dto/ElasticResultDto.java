package com.armin.utility.repository.odm.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ElasticResultDto<T> {
    private long count;
    private List<T> result;

    public ElasticResultDto() {
        this.result = new ArrayList<>();
    }
}
