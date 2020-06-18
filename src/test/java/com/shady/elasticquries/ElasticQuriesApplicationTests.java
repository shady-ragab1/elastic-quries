package com.shady.elasticquries;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shady.elasticquries.model.Memo;
import com.shady.elasticquries.repo.MemoRepo;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestExecution;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.pivovarit.function.ThrowingFunction.unchecked;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ElasticQuriesApplicationTests {

	@Autowired
	RestHighLevelClient elasticsearchClient;




	@Test
	void contextLoads() throws IOException, JsonProcessingException {

		SearchRequest searchRequest = new SearchRequest("memo");
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchRequest.source(searchSourceBuilder);

		SearchResponse response =  elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);

		ObjectMapper objectMapper = new ObjectMapper()
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); //to ignore

		List<Memo> myMemos = null;
		if (response.status().getStatus() == 200){

			myMemos = Arrays.stream(response.getHits().getHits())
					.map(unchecked(hit->objectMapper.readValue(hit.getSourceAsString(),Memo.class))) //lampda checked exception
					.collect(Collectors.toList());

		}

		assertEquals(myMemos.size(),2);


	}

}
