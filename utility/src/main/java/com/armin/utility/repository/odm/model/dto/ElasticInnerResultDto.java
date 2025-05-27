package com.armin.utility.repository.odm.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ElasticInnerResultDto<P, C> {
    private long count;
    private List<InnerDto<P, C>> result;

    public ElasticInnerResultDto() {
        result = new ArrayList<>();
    }
}
