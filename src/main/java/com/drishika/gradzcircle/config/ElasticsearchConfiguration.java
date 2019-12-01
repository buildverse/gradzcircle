package com.drishika.gradzcircle.config;

import java.io.IOException;
import java.net.InetAddress;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableConfigurationProperties(ElasticsearchProperties.class)
@ConditionalOnProperty("spring.data.elasticsearch.cluster-nodes")
public class ElasticsearchConfiguration {

	
	private final Logger log = LoggerFactory.getLogger(ElasticsearchConfiguration.class);
	
	// USE in DEV
	@Bean
	@Profile({"default" , "dev"})
    public ElasticsearchTemplate elasticsearchTemplate(Client client, Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
        return new ElasticsearchTemplate(client, new CustomEntityMapper(jackson2ObjectMapperBuilder.createXmlMapper(false).build()));
    }
	
	/* USED for PROD */

	@Bean
	@Profile({"prod" , "uat"})
	public ElasticsearchTemplate elasticsearchTemplate(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
		return new ElasticsearchTemplate(client(),
				new CustomEntityMapper(jackson2ObjectMapperBuilder.createXmlMapper(false).build()));
	}

	@Bean
	@Profile({"prod" , "uat"})
	public TransportClient client() {
		TransportClient client = null;
		boolean enableSsl = true;
		try {
			log.info("Creating Elastic Client ");
			
			Settings settings = Settings.builder()
					.put("client.transport.nodes_sampler_interval", "5s")
					.put("client.transport.sniff", false)
					.put("transport.tcp.compress", true)
					.put("cluster.name", "b2aa8bc4c757b5f248a4a0d22b758c76")
					.put("xpack.security.transport.ssl.enabled", true)
					.put("request.headers.X-Found-Cluster", "b2aa8bc4c757b5f248a4a0d22b758c76")
					.put("xpack.security.user", "gradzcircle:rpqi9a786r5")
					//.put("xpack.security.transport.ssl.verification_mode", "false")
					.build();
			client = new PreBuiltXPackTransportClient(settings)
			        .addTransportAddress(new InetSocketTransportAddress(
			        		InetAddress.getByName("b2aa8bc4c757b5f248a4a0d22b758c76.us-east-1.aws.found.io"), 9343));
			
			log.info("Created Elastic Client ");
		
		} catch(Exception ex) {
			log.error("Error creating Elastic Client {},{}", ex.getMessage(),ex.getCause());
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
