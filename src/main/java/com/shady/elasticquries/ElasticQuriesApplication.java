package com.shady.elasticquries;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories
public class ElasticQuriesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElasticQuriesApplication.class, args);
	}

}
