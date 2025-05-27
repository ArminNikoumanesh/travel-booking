package com.armin.utility.repository.odm.config;

import com.armin.utility.repository.odm.logs.repository.LogService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ElasticsearchConfig {
    @Value("${elasticsearch.host1}")
    private String host1;
    @Value("${elasticsearch.port}")
    private Integer port;
    @Value("${elasticsearch.schema}")
    private String schema;
    @Value("${elasticsearch.user}")
    private String username;
    @Value("${elasticsearch.pass}")
    private String pass;
    @Value("${elasticsearch.host2:#{null}}")
    private String host2;
    @Value("${elasticsearch.host3:#{null}}")
    private String host3;

    @Autowired
    private LogService logService;
//    @Bean
//    public LogService getLogService() {
//        return new LogService();
//    }

    @Bean("el-cl-search")
    public RestHighLevelClient searchClient() {
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

    //    todo must fix this
    @Bean("el-blk-search")
    public BulkProcessor bulkProcessor(@Qualifier("el-cl-search") RestHighLevelClient client) {
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
//                            logService.create(logData, cause);
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
//                    logService.create(logData, failure.toString());

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

