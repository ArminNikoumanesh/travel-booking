package com.armin.utility.repository.odm.dao;


import com.armin.utility.bl.StringService;
import com.armin.utility.object.SystemError;
import com.armin.utility.object.SystemException;
import com.armin.utility.repository.odm.model.entity.ElasticsearchEntity;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public class ElasticsearchLogDao {
    private static ActionListener listener = new ActionListener<IndexResponse>() {
        @Override
        public void onResponse(IndexResponse indexResponse) {

        }

        @Override
        public void onFailure(Exception e) {
        }
    };
    private final RestHighLevelClient client;
    private final BulkProcessor bulkProcessor;
    private final StringService stringService;

    @Autowired
    public ElasticsearchLogDao(@Qualifier("el-cl-logger") RestHighLevelClient client, @Qualifier("el-blk-logger") BulkProcessor bulkProcessor, StringService stringService) {
        this.client = client;
        this.bulkProcessor = bulkProcessor;
        this.stringService = stringService;
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


}
