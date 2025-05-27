package com.armin.utility.repository.odm.model.dto;

import com.armin.utility.repository.odm.object.ElasticsearchFilter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.elasticsearch.search.aggregations.metrics.TopHitsAggregationBuilder;

/**
 * @author : Armin.Nik
 * @project : shared
 * @date : 09.06.24
 */
@Getter
@Setter
@Builder
public class ElasticAggregationEntranceDto {
    private String indexName;
    private String aggregateName;
    private String field;
    private ElasticsearchFilter filter;
    private TopHitsAggregationBuilder topHitsAggregationBuilder;
}
