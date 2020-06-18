package com.shady.elasticquries;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;


@Configuration
public class ElsasticConfig extends AbstractElasticsearchConfiguration {

    @Value("${spring.data.elasticsearch.cluster-nodes}")
    private String hostAndPort;
    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {

        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(hostAndPort).withConnectTimeout(20000l).withSocketTimeout(20000)
                .build();

        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public Jackson2RepositoryPopulatorFactoryBean getRespositoryPopulator() {
        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
        factory.setResources(new Resource[]{new ClassPathResource("data.json")});
        return factory;
    }

}


