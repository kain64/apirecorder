package com.example.apirecorder.config;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticConfig {
    @Value("${application.elastic.password:changeme}")
    private String password;
    @Value("${application.elastic.user:elastic}")
    private String user;
    @Value("${application.elastic.url:localhost:9200}")
    private String url;
    @Bean
    ElasticsearchAsyncClient getElasticsearchAsyncClient(){
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(user, password));

        var restClient = RestClient.builder(HttpHost.create(url))
                .setHttpClientConfigCallback(hcb -> hcb
                        .setDefaultCredentialsProvider(credentialsProvider)
                ).build();

        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new ElasticsearchAsyncClient(transport);
    }
}
