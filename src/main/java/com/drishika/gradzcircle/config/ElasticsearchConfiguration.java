package com.drishika.gradzcircle.config;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.shield.ShieldPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class ElasticsearchConfiguration {

    private final Logger log = LoggerFactory.getLogger(ElasticsearchConfiguration.class);

    //USE in DEV
    /*@Bean
    public ElasticsearchTemplate elasticsearchTemplate(Client client, Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
        return new ElasticsearchTemplate(client, new CustomEntityMapper(jackson2ObjectMapperBuilder.createXmlMapper(false).build()));
    }
*/
    /* USED for PROD */
    @Bean
    public ElasticsearchTemplate elasticsearchTemplate(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
        return new ElasticsearchTemplate(client(),
                new CustomEntityMapper(jackson2ObjectMapperBuilder.createXmlMapper(false).build()));
    }
    @Bean
    public TransportClient client() {
        TransportClient client = null;
        boolean enableSsl = true;
        try {
            Settings settings = Settings.builder().put("cluster.name", "d21850cf4f253174fc46a3c1bbce67d3")
                    .put("transport.ping_schedule", "5s")
                    //     .put("transport.sniff", false) 
                    .put("action.bulk.compress", false).put("shield.transport.ssl", enableSsl)
                    .put("request.headers.X-Found-Cluster", "d21850cf4f253174fc46a3c1bbce67d3")
                    .put("shield.user", "readwrite:rpqi9a786r5").build();
            client = TransportClient.builder().addPlugin(ShieldPlugin.class).settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(
                            InetAddress.getByName("d21850cf4f253174fc46a3c1bbce67d3.us-east-1.aws.found.io"), 9343));
        } catch (UnknownHostException e) {
            log.error("Error creating Elastic Client {}", e.getMessage());
        }
        return client;
    }

    public class CustomEntityMapper implements EntityMapper {

        private ObjectMapper objectMapper;

        public CustomEntityMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        }

        @Override
        public String mapToString(Object object) throws IOException {
            return objectMapper.writeValueAsString(object);
        }

        @Override
        public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
            return objectMapper.readValue(source, clazz);
        }
    }
}