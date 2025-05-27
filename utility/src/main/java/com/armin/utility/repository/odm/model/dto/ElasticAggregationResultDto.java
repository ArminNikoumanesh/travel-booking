package com.armin.utility.repository.odm.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

@Getter
@Setter
@NoArgsConstructor
public class ElasticAggregationResultDto<T> {
    private Object field;
    private long count;
    private T source;

    public ElasticAggregationResultDto(Object field, long count) {
        this.field = field;
        this.count = count;
    }

    public ElasticAggregationResultDto(Terms.Bucket bucket, T source) {
        this.field = bucket.getKey();
        this.count = bucket.getDocCount();
        this.source = source;
    }

}