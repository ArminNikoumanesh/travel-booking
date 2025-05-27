package com.armin.utility.repository.odm.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.core.TimeValue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ElasticSearchLogConfig {
    @Value("${elasticsearch-log.host1}")
    private String host1;
    @Value("${elasticsearch-log.port}")
    private Integer port;
    @Value("${elasticsearch-log.schema}")
    private String schema;
    @Value("${elasticsearch-log.user}")
    private String username;
    @Value("${elasticsearch-log.pass}")
    private String pass;
    @Value("${elasticsearch-log.host2:#{null}}")
    private String host2;
    @Value("${elasticsearch-log.host3:#{null}}")
    private String host3;

    @Bean("el-cl-logger")
    public RestHighLevelClient loggerClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, pass));
        List<HttpHost> httpHosts = new ArrayList<>();
        if (host1 != null) {
            httpHosts.add(new HttpHost(host1, port, schema));
        }
        if (host2 != null) {
            httpHosts.add(new HttpHost(host2, port, schema));
        }
        if (host3 != null) {
            httpHosts.add(new HttpHost(host3, port, schema));
        }
        return new RestHighLevelClient(
                RestClient.builder(httpHosts.toArray(new HttpHost[0]))
                        .setNodeSelector(NodeSelector.ANY)
                        .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                                .setDefaultCredentialsProvider(credentialsProvider)));
    }


    @Bean("el-blk-logger")
    public BulkProcessor bulkProcessor(@Qualifier("el-cl-logger") RestHighLevelClient client) {
        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request,
                                  BulkResponse response) {
                if (response.hasFailures()) {
                    for (int i = 0; i < response.getItems().length; i++) {
                        if (response.getItems()[i].getFailure() != null && response.getItems()[i].getResponse() == null) {
                            String logData = ((IndexRequest) request.requests().get(i)).source().utf8ToString();
                            String cause = response.getItems()[i].getFailure().toString();
                        }
                    }
                }
            }

            @Override
            public void afterBulk(long executionId, BulkRequest request,
                                  Throwable failure) {
                ((IndexRequest) request.requests().get(0)).source().utf8ToString();
                for (DocWriteRequest<?> docWriteRequest : request.requests()) {
                    String logData = ((IndexRequest) docWriteRequest).source().utf8ToString();
                }
            }
        };

        return BulkProcessor.builder(
                        (request, bulkListener) ->
                                client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
                        listener)
                .setBulkActions(500)
                .setBulkSize(new ByteSizeValue(1L, ByteSizeUnit.MB))
                .setConcurrentRequests(1)
                .setFlushInterval(TimeValue.timeValueSeconds(5L))
                .setBackoffPolicy(BackoffPolicy.constantBackoff(TimeValue.timeValueSeconds(1L), 3))
                .build();

    }
}
