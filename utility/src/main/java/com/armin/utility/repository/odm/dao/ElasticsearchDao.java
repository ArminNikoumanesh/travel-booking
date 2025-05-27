package com.armin.utility.repository.odm.dao;

import com.google.gson.Gson;
import com.armin.utility.bl.StringService;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.utility.repository.odm.model.dto.ElasticAggregationEntranceDto;
import com.armin.utility.repository.odm.model.dto.ElasticAggregationResultDto;
import com.armin.utility.repository.odm.model.dto.ElasticResultDto;
import com.armin.utility.repository.odm.model.entity.ElasticsearchEntity;
import com.armin.utility.repository.odm.object.ElasticsearchFilter;
import com.armin.utility.repository.orm.object.ReportOption;
import com.armin.utility.statics.enums.SortType;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedTopHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@Repository
public class ElasticsearchDao {
    private static ActionListener listener = new ActionListener<IndexResponse>() {
        @Override
        public void onResponse(IndexResponse indexResponse) {

        }

        @Override
        public void onFailure(Exception e) {
        }
    };
    private final Gson gson;
    private final RestHighLevelClient client;
    private final BulkProcessor bulkProcessor;
    private final StringService stringService;

    @Autowired
    public ElasticsearchDao(Gson gson, @Qualifier("el-cl-search") RestHighLevelClient client, @Qualifier("el-blk-search") BulkProcessor bulkProcessor, StringService stringService) {
        this.gson = gson;
        this.client = client;
        this.bulkProcessor = bulkProcessor;
        this.stringService = stringService;
    }

    public boolean exists(String indexName, Object id) throws SystemException {
        GetRequest getRequest = new GetRequest(indexName, String.valueOf(id));
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        try {
            return client.exists(getRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new SystemException(SystemError.ELASTIC_EXECUTION_EXCEPTION, "", 6353);
        }
    }

    public <T> List<T> getAll(String indexName, Class<T> cls) throws SystemException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery())
                .size(10000);
        searchRequest.source(searchSourceBuilder);
        try {
            List<T> result = new ArrayList<>();
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            for (SearchHit hit : hits.getHits()) {
                result.add(gson.fromJson(hit.getSourceAsString(), cls));
            }
            return result;
        } catch (IOException e) {
            throw new SystemException(SystemError.ELASTIC_EXECUTION_EXCEPTION, "", 6353);
        }
    }

    public <T> List<T> getAll(String indexName, ReportOption reportOption, Class<T> cls) throws SystemException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery())
                .from((reportOption.getPageNumber() - 1) * reportOption.getPageSize())
                .size(reportOption.getPageSize());
        searchRequest.source(searchSourceBuilder);
        try {
            List<T> result = new ArrayList<>();
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            for (SearchHit hit : hits.getHits()) {
                result.add(gson.fromJson(hit.getSourceAsString(), cls));
            }
            return result;
        } catch (IOException e) {
            throw new SystemException(SystemError.ELASTIC_EXECUTION_EXCEPTION, "", 6353);
        }
    }

    public <T> T getById(String indexName, Object id, Class<T> cls) throws SystemException {
        GetRequest request = new GetRequest(indexName, String.valueOf(id));
        request.refresh(true);
        try {
            GetResponse getResponse = client.get(request, RequestOptions.DEFAULT);
            return gson.fromJson(getResponse.getSourceAsString(), cls);
        } catch (IOException e) {
            throw new SystemException(SystemError.ELASTIC_EXECUTION_EXCEPTION, "", 6353);
        }
    }

    public <T, E extends Object> List<T> multiGet(String indexName, Collection<E> ids, Class<T> cls) throws SystemException {
        MultiGetRequest request = new MultiGetRequest();
        if (ids != null) {
            ids.forEach(id -> request.add(new MultiGetRequest.Item(indexName, String.valueOf(id))));
        }
        request.refresh(true);
        try {
            List<T> result = new ArrayList<>();
            MultiGetResponse response = client.mget(request, RequestOptions.DEFAULT);
            if (response.getResponses() == null) {
                return result;
            }
            for (MultiGetItemResponse item : response.getResponses()) {
//                result.add(gson.fromJson(item.getResponse().getSourceAsString(), cls));
            }
            return result;
        } catch (IOException e) {
            throw new SystemException(SystemError.ELASTIC_CONNECTION, "", 6353);
        }
    }

    public boolean index(String indexName, Object id, Object indexModel) throws SystemException {
        IndexRequest request = new IndexRequest(indexName);
        request.id(String.valueOf(id));
        request.source(stringService.objectToMap(indexModel));
        request.timeout(TimeValue.timeValueSeconds(1));
        try {
            IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                return true;
            } else return indexResponse.getResult() == DocWriteResponse.Result.UPDATED;
        } catch (IOException e) {
            throw new SystemException(SystemError.ELASTIC_EXECUTION_EXCEPTION, "", 6353);
        }
    }

    public <T> void bulkIndex(String indexName, Object id, T indexModel) {
        IndexRequest request = new IndexRequest(indexName);
        request.id(String.valueOf(id));
        request.source(stringService.objectToMap(indexModel));
        bulkProcessor.add(request);
    }

    public <T extends ElasticsearchEntity> void bulkIndex(String indexName, T indexModel) {
        IndexRequest request = new IndexRequest(indexName);
        request.id(String.valueOf(indexModel.getId()));
        request.source(stringService.objectToMap(indexModel));
        bulkProcessor.add(request);
    }

    public <T> void bulkIndexWithOutId(String indexName, T indexModel) {
        IndexRequest request = new IndexRequest(indexName);
        request.source(stringService.objectToMap(indexModel));
        bulkProcessor.add(request);
    }

    public <T extends ElasticsearchEntity> void bulkIndex(String indexName, List<T> indexModels) {
        for (T elasticsearchEntity : indexModels) {
            IndexRequest request = new IndexRequest(indexName);
            request.id(String.valueOf(elasticsearchEntity.getId()));
            request.source(stringService.objectToMap(elasticsearchEntity));
            bulkProcessor.add(request);
        }
        bulkProcessor.flush();
    }

    public <T> void update(String indexName, Object id, T updateModel) throws SystemException {
        UpdateRequest request = new UpdateRequest(indexName, String.valueOf(id));
        request.doc(stringService.objectToMap(updateModel));
        request.detectNoop(false);
        request.fetchSource(true);
        try {
            client.update(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new SystemException(SystemError.ELASTIC_EXECUTION_EXCEPTION, "", 6353);
        }
    }

    public <T> T update(String indexName, Object id, T updateModel, Class<T> cls) throws SystemException {
        UpdateRequest request = new UpdateRequest(indexName, String.valueOf(id));
        request.doc(stringService.objectToMap(updateModel));
        request.detectNoop(false);
        request.fetchSource(true);
        try {
            UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
            if (updateResponse.getResult() == DocWriteResponse.Result.CREATED || updateResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                return gson.fromJson(updateResponse.getGetResult().sourceAsString(), cls);
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new SystemException(SystemError.ELASTIC_EXECUTION_EXCEPTION, "", 6353);
        }
    }

    public <T extends ElasticsearchEntity> void bulkUpdate(String indexName, T updateModel) {
        UpdateRequest request = new UpdateRequest(indexName, String.valueOf(updateModel.getId()));
        request.doc(stringService.objectToMap(updateModel));
        request.detectNoop(false);
        request.fetchSource(false);
        bulkProcessor.add(request);
    }

    public <T> ElasticResultDto<T> search(String indexName, SearchSourceBuilder searchSourceBuilder, Class<T> cls) throws SystemException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(searchSourceBuilder);
        try {
            ElasticResultDto<T> resultDto = new ElasticResultDto<>();
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            for (SearchHit hit : hits.getHits()) {
                resultDto.getResult().add(gson.fromJson(hit.getSourceAsString(), cls));
            }
            resultDto.setCount(searchResponse.getHits().getTotalHits().value);
            return resultDto;
        } catch (IOException e) {
            throw new SystemException(SystemError.ELASTIC_EXECUTION_EXCEPTION, "", 6353);
        }
    }

    public <T> ElasticResultDto<T> search(String indexName, ElasticsearchFilter filter, Class<T> cls) throws SystemException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(generateSearchQuery(filter));
        try {
            ElasticResultDto<T> resultDto = new ElasticResultDto<>();
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            for (SearchHit hit : hits.getHits()) {
                resultDto.getResult().add(gson.fromJson(hit.getSourceAsString(), cls));
            }
            resultDto.setCount(searchResponse.getHits().getTotalHits().value);
            return resultDto;
        } catch (IOException e) {
            throw new SystemException(SystemError.ELASTIC_EXECUTION_EXCEPTION, "", 6353);
        }
    }

    public <T> ElasticResultDto<T> searchAndFeedElasticId(String indexName, ElasticsearchFilter filter, Class<T> t) throws SystemException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(generateSearchQuery(filter));
        try {
            ElasticResultDto<T> logs = new ElasticResultDto<>();
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            for (SearchHit hit : hits) {
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                sourceAsMap.put("id", hit.getId());
                T logEntity = stringService.mapToObject(sourceAsMap, t);
                logs.getResult().add(logEntity);
            }
            logs.setCount(searchResponse.getHits().getTotalHits().value);
            return logs;
        } catch (IOException e) {
            throw new SystemException(SystemError.ELASTIC_EXECUTION_EXCEPTION, "", 6353);
        }
    }

    private String getMostInnerHit(SearchHit hit, String[] innerKeys) {
        SearchHit searchHit = hit;
        for (String key : innerKeys) {
            searchHit = searchHit.getInnerHits().get(key).getAt(0);
        }
        return searchHit.getSourceAsString();
    }

    public long count(String indexName, QueryBuilder query) throws SystemException {
        CountRequest countRequest = new CountRequest(indexName);
        countRequest.query(query);
        try {
            CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);
            return countResponse.getCount();
        } catch (IOException e) {
            throw new SystemException(SystemError.ELASTIC_EXECUTION_EXCEPTION, "", 6353);
        }
    }

    public long countAll(String indexName) throws SystemException {
        CountRequest countRequest = new CountRequest(indexName);
        countRequest.query(QueryBuilders.matchAllQuery());
        try {
            CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);
            return countResponse.getCount();
        } catch (IOException e) {
            throw new SystemException(SystemError.ELASTIC_EXECUTION_EXCEPTION, "", 6353);
        }
    }

    public void deleteById(String indexName, Object id) throws SystemException {
        DeleteRequest request = new DeleteRequest(indexName, String.valueOf(id));
        request.timeout(TimeValue.timeValueMinutes(1));
        try {
            client.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new SystemException(SystemError.ELASTIC_CONNECTION, "", 6353);
        }
    }

    public void deleteAll(String indexName) throws SystemException {
        DeleteByQueryRequest request = new DeleteByQueryRequest(indexName);
        request.setQuery(QueryBuilders.matchAllQuery());
        try {
            client.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new SystemException(SystemError.ELASTIC_CONNECTION, "", 6353);
        }
    }

    public <T> void bulkDelete(String indexName, T id) {
        DeleteRequest request = new DeleteRequest(indexName, String.valueOf(id));
        bulkProcessor.add(request);
    }

    public <T> void bulkDelete(String indexName, List<T> ids) {
        for (Object id : ids) {
            DeleteRequest request = new DeleteRequest(indexName, String.valueOf(id));
            bulkProcessor.add(request);
        }
    }

    public void deleteByQuery(String indexName, QueryBuilder query) throws SystemException {
        DeleteByQueryRequest request = new DeleteByQueryRequest(indexName);
        request.setQuery(query);
        try {
            client.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new SystemException(SystemError.ELASTIC_CONNECTION, "", 6353);
        }
    }

    public List<ElasticAggregationResultDto> searchWithAggregate(String indexName, String aggregateName, String field, ElasticsearchFilter filter) throws SystemException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = generateSearchQuery(filter);
        searchSourceBuilder.size(0);
        TermsAggregationBuilder aggregationBuilders = AggregationBuilders.terms(aggregateName).field(field);
        searchSourceBuilder.aggregation(aggregationBuilders);
        searchRequest.source(searchSourceBuilder);
        try {
            SearchResponse response = this.client.search(searchRequest, RequestOptions.DEFAULT);
            Terms terms = response.getAggregations().get(aggregateName);
            List<? extends Terms.Bucket> buckets = terms.getBuckets();
            List<ElasticAggregationResultDto> results = new ArrayList<>();
            for (Terms.Bucket bucket : buckets) {
                ElasticAggregationResultDto resultDto = new ElasticAggregationResultDto(bucket.getKey(), bucket.getDocCount());
                results.add(resultDto);
            }
            return results;
        } catch (IOException ex) {
            throw new SystemException(SystemError.ELASTIC_EXECUTION_EXCEPTION, "", 6353);
        }


    }

    public <T> List<ElasticAggregationResultDto<T>> searchWithAggregateIncludeFields(ElasticAggregationEntranceDto entranceDto, Class<T> cls) throws SystemException {
        SearchRequest searchRequest = new SearchRequest(entranceDto.getIndexName());
        SearchSourceBuilder searchSourceBuilder = generateSearchQuery(entranceDto.getFilter());
        searchSourceBuilder.size(0);
        TermsAggregationBuilder aggregationBuilders = AggregationBuilders.terms(entranceDto.getAggregateName()).field(entranceDto.getField());
        if (entranceDto.getTopHitsAggregationBuilder() != null) {
            aggregationBuilders.subAggregation(entranceDto.getTopHitsAggregationBuilder());
        }
        searchSourceBuilder.aggregation(aggregationBuilders);
        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse response = this.client.search(searchRequest, RequestOptions.DEFAULT);
            Terms terms = response.getAggregations().get(entranceDto.getAggregateName());
            List<? extends Terms.Bucket> buckets = terms.getBuckets();
            List<ElasticAggregationResultDto<T>> results = new ArrayList<>();
            for (Terms.Bucket bucket : buckets) {
                for (Aggregation aggregation : bucket.getAggregations()) {
                    SearchHit[] hits = ((ParsedTopHits) aggregation).getHits().getHits();
                    for (SearchHit hit : hits) {
                        Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                        sourceAsMap.put("id", hit.getId());
                        T result = stringService.mapToObject(sourceAsMap, cls);
                        ElasticAggregationResultDto<T> resultDto = new ElasticAggregationResultDto<>(bucket, result);
                        results.add(resultDto);
                    }
                }
            }
            return results;
        } catch (IOException ex) {
            throw new SystemException(SystemError.ELASTIC_EXECUTION_EXCEPTION, "", 6353);
        }
    }

    private SearchSourceBuilder generateSearchQuery(ElasticsearchFilter filter) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //        add term conditions
        if (filter.getTermConditions() != null && !filter.getTermConditions().isEmpty()) {
            filter.getTermConditions()
                    .forEach(e -> {
                        if (e.getValue() != null)
                            boolQueryBuilder.filter().add(QueryBuilders.termQuery(e.getKey(), e.getValue()));
                    });
        }
        //        add match conditions
        if (filter.getMatchConditions() != null && !filter.getMatchConditions().isEmpty()) {
            filter.getMatchConditions()
                    .forEach(e -> {
                        if (e.getValue() != null)
                            boolQueryBuilder.filter().add(QueryBuilders.matchQuery(e.getKey(), e.getValue()));
                    });
        }
        //        add range conditions
        if (filter.getRangeConditions() != null && !filter.getRangeConditions().isEmpty()) {
            filter.getRangeConditions().forEach(e -> {
                if (e.getFrom() != null || e.getTo() != null) {
                    RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(e.getKey());
                    if (e.getFrom() != null) {
                        rangeQueryBuilder.from(e.getFrom());
                    }
                    if (e.getTo() != null) {
                        rangeQueryBuilder.to(e.getTo());
                    }
                    boolQueryBuilder.filter().add(rangeQueryBuilder);
                }
            });
        }
        //     add terms conditions
        if (filter.getTermsConditions() != null && !filter.getTermsConditions().isEmpty()) {
            filter.getTermsConditions()
                    .forEach(e -> {
                        if (e.getValue() != null)
                            boolQueryBuilder.filter().add(QueryBuilders.termsQuery(e.getKey(), (ArrayList) e.getValue()));
                    });
        }
        SearchSourceBuilder result = new SearchSourceBuilder();
        //        add sort
        if (filter.getSorts() != null && !filter.getSorts().isEmpty()) {
            filter.getSorts().forEach(s -> result.sort(s.getColumn(), s.getType() == SortType.ASCENDING ? SortOrder.ASC : SortOrder.DESC));
        }
        result.from((filter.getPage() - 1) * filter.getSize());
        result.size(filter.getSize());
        result.query(boolQueryBuilder);
        return result;
    }
}

