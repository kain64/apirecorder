package com.example.apirecorder.recorder.services;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import com.example.apirecorder.recorder.dto.ApiCall;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class ElasticServices {

    private final ElasticsearchAsyncClient elasticsearchAsyncClient;
    public void index(ApiCall apiCall){
        try {
            elasticsearchAsyncClient.index(i -> i.index("apicalls").id(apiCall.getId().toString()).document(apiCall));
        }catch (Exception e){
            log.error("index failed", e);
        }
    }
}
